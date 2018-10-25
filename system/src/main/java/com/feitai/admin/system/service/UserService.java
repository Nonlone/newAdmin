package com.feitai.admin.system.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.system.mapper.PermissionMapper;
import com.feitai.admin.system.mapper.RoleMapper;
import com.feitai.admin.system.mapper.UserRoleMapper;
import com.feitai.admin.system.model.*;
import com.feitai.admin.system.vo.Menu;
import com.feitai.utils.digest.SHAUtils;
import com.feitai.utils.encode.HexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.security.GeneralSecurityException;
import java.util.*;

@Service
@Slf4j
public class UserService extends ClassPrefixDynamicSupportService<User> {

    public static final String HASH_ALGORITHM = "SHA-1";

    public static final int HASH_INTERATIONS = 1024;

    private static final int SALT_SIZE = 8;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    private class MenuComparator implements Comparator<Menu> {

        @Override
        public int compare(Menu o1, Menu o2) {
            if (o1.getOrder() < o2.getOrder())
                return -1;
            if (o1.getOrder() > o2.getOrder())
                return 1;
            return 0;
        }
    }


    public User findByLoginName(String loginName) {
        Example example = Example.builder(User.class).andWhere(Sqls.custom().andEqualTo("loginName", loginName)).build();
        User user = this.mapper.selectOneByExample(example);
        user.setRoles(getRoles(user.getId()));
        walkProcess(user);
        return user;
    }

    public List<Role> getRoles(Long userId) {
        List<Role> list = new ArrayList<>();
        List<UserRole> userRoles = userRoleMapper.selectByExample(Example.builder(UserRole.class).andWhere(Sqls.custom().andEqualTo("userId", userId)).build());
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectByPrimaryKey(userRole.getRoleId());
            walkProcess(role);
            list.add(role);
        }
        return list;
    }


    public List<Role> findRolesByUserId(Long userId) {
        return getRoles(userId);
    }

    /**
     * 在保存用户时,发送用户修改通知消息, 由消息接收者异步进行较为耗时的通知邮件发送.
     * <p>
     * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
     *
     * @return
     */
    @Override
    public User save(User user) {

        // if (isSupervisor(user)) {
        // logger.warn("操作员{}尝试修改超级管理员用户", getCurrentUserName());
        // throw new ServiceException("不能修改超级管理员用户");
        // }

        // 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
        }
        if (user.getCreateTime() == null) {
            user.setCreateTime(new Date());
        }
        return this.save(user);
    }

    public boolean checkPassword(Long id, String password) {
        User user = findOne(id);
        byte[] salt = HexUtils.decode(user.getSalt());
        try {
            byte[] hashPassword = SHAUtils.sha1(password.getBytes(), salt, HASH_INTERATIONS);
            if (user.getPassword().equals(HexUtils.encode(hashPassword))) {
                return true;
            }
        } catch (GeneralSecurityException e) {
            log.error(String.format("checkPassword error %s password<%s>", e.getMessage(), password), e);
        }
        return false;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = SHAUtils.generateSalt(SALT_SIZE);
        user.setSalt(HexUtils.encode(salt));
        try {
            byte[] hashPassword = SHAUtils.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
            user.setPassword(HexUtils.encode(hashPassword));
        } catch (GeneralSecurityException e) {
            log.error(String.format("entryptPassword error %s salt<%s>", salt), e);
        }
    }

    public Collection<Menu> loadMenu(Long userId, String ctx) {
        Iterator<Role> roles = getRoles(userId).iterator();
        Map<Long, Menu> levelOne = new HashMap<Long, Menu>();
        Map<Long, Menu> levelTwo = new HashMap<Long, Menu>();
        Set<Menu> levelThree = new HashSet<Menu>();
        while (roles.hasNext()) {
            Role role = roles.next();
            Iterator<RoleAuth> auths = role.getRoleAuths().iterator();
            while (auths.hasNext()) {
                RoleAuth ra = auths.next();
                walkProcess(ra);
                Resource resource = ra.getResource();
                if (resource.getLevel() == 1) {
                    levelOne.put(resource.getId(), Menu.build(resource.getLevel(), resource, ctx));
                } else if (resource.getLevel() == 2) {
                    levelTwo.put(resource.getId(), Menu.build(resource.getLevel(), resource, ctx));
                } else if (resource.getLevel() == 3) {
                    levelThree.add(Menu.build(resource.getLevel(), resource, ctx));
                }
            }
        }

        Iterator<Menu> three = levelThree.iterator();
        while (three.hasNext()) {
            Menu menu = three.next();
            Menu parentMenu = levelTwo.get(menu.getParentId());
            if (parentMenu != null) {
                parentMenu.getItems().add(menu);
            }
        }

        MenuComparator comparator = new MenuComparator();// 给所有菜单排序

        Collection<Menu> menus = levelTwo.values();
        for (Menu menu : menus) {
            Collections.sort(menu.getItems(), comparator);// 排序二级菜单下的菜单
            Menu parentMenu = levelOne.get(menu.getParentId());
            if (parentMenu != null) {
                parentMenu.getMenu().add(menu);
            }
        }

        List<Menu> menuOne = new ArrayList<Menu>(levelOne.values());

        for (Menu menu : menuOne) {
            Collections.sort(menu.getMenu(), comparator);
        }

        Collections.sort(menuOne, comparator);// 排序二级菜单下的菜单
        return menuOne;
    }

    public List<String> findUserPermissions(Long userId) {
        List<Role> roles = getRoles(userId);
        List<String> permissionsStr = new ArrayList<String>();
        for (Role role : roles) {
            List<RoleAuth> ras = role.getRoleAuths();
            for (RoleAuth ra : ras) {
                walkProcess(ra);
                Resource resource = ra.getResource();
                String permissionsIds = ra.getPermissionIds();
                if (permissionsIds != null && !"".equals(permissionsIds)) {
                    String[] idsStr = permissionsIds.split(",");
                    List<Long> ids = new ArrayList<Long>(idsStr.length);
                    List<Permission> permissions = new ArrayList<>();
                    for (String idStr : idsStr) {
                        Permission permission = permissionMapper.selectByPrimaryKey(Long.parseLong(idStr));
                        permissions.add(permission);
                    }
                    for (Permission permission : permissions) {
                        permissionsStr.add(resource.getCode() + ":" + permission.getPermission());
                    }
                }

            }
        }
        return permissionsStr;
    }

}
