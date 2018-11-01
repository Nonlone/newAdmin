package com.feitai.admin.system.service;

import com.feitai.admin.core.security.Digests;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.system.mapper.PermissionMapper;
import com.feitai.admin.system.mapper.RoleMapper;
import com.feitai.admin.system.mapper.UserRoleMapper;
import com.feitai.admin.system.model.*;
import com.feitai.admin.system.vo.Menu;
import com.feitai.base.mybatis.ManyAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.OneAnnotationFieldWalkProcessor;
import com.feitai.utils.ObjectUtils;
import com.feitai.utils.digest.SHAUtils;
import com.feitai.utils.encode.HexUtils;
import com.feitai.utils.identity.Encodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private ApplicationContext applicationContext;

    public void clearUserRole(Long userId) {
        Example example = Example.builder(UserRole.class).andWhere(Sqls.custom().andEqualTo("userId", userId)).build();
        userRoleMapper.deleteByExample(example);
    }

    public Integer saveUserRole(Long userId, Long roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        return userRoleMapper.insert(userRole);
    }

    public User findByLoginName(String loginName) {
        Example example = Example.builder(User.class).andWhere(Sqls.custom().andEqualTo("loginName", loginName)).build();
        User user = getMapper().selectOneByExample(example);
        if (user != null) {
            OneAnnotationFieldWalkProcessor oneAnnotationFieldWalkProcessor = new OneAnnotationFieldWalkProcessor(applicationContext);
            ObjectUtils.fieldWalkProcess(user, oneAnnotationFieldWalkProcessor);
            user.setRoles(getRoles(user.getId().toString()));
        }
        return user;
    }

    public List<Role> getRoles(String userId) {
        List<Role> list = new ArrayList<>();
        Example example = Example.builder(UserRole.class).andWhere(Sqls.custom().andEqualTo("userId", userId)).build();
        List<UserRole> userRoles = userRoleMapper.selectByExample(example);
        for (UserRole userRole :
                userRoles) {
            Role role = roleMapper.selectByPrimaryKey(userRole.getRoleId());
            list.add(walkProcess(role));
        }
        return list;
    }


    public List<Role> findRolesByUserId(Long userId) {
        List<Role> roles = getRoles(userId.toString());
        roles.size();//在shiro如果权限检查不是页面出发的就会导致无法异步加载所以这里用size强制加载。
        return roles;
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
        // 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
        }
        if (user.getCreateTime() == null) {
            user.setCreateTime(new Date());
        }
        return super.save(user);
    }

    public boolean checkPassword(Long id, String password) {
        User user = this.findOne(id);
        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
        if (user.getPassword().equals(Encodes.encodeHex(hashPassword))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    public Collection<Menu> loadMenu(Long userId, String ctx) {
        Iterator<Role> roles = getRoles(userId.toString()).iterator();
        Map<Long, Menu> levelOne = new HashMap<Long, Menu>();
        Map<Long, Menu> levelTwo = new HashMap<Long, Menu>();
        Set<Menu> levelThree = new HashSet<Menu>();
        while (roles.hasNext()) {
            Role role = roles.next();
            Iterator<RoleAuth> auths = role.getRoleAuths().iterator();
            while (auths.hasNext()) {
                RoleAuth ra = auths.next();
                try {
                    OneAnnotationFieldWalkProcessor oneAnnotationFieldWalkProcessor = new OneAnnotationFieldWalkProcessor(applicationContext);
                    ObjectUtils.fieldWalkProcess(ra, oneAnnotationFieldWalkProcessor);
                } catch (Exception e) {
                }
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

    public List<String> findUserPermissions(Long id) {
        List<Role> roles = getRoles(id.toString());
        List<String> permissionsStr = new ArrayList<String>();
        for (Role role : roles) {
            List<RoleAuth> ras = role.getRoleAuths();
            for (RoleAuth ra : ras) {
                try {
                    OneAnnotationFieldWalkProcessor oneAnnotationFieldWalkProcessor = new OneAnnotationFieldWalkProcessor(applicationContext);
                    ObjectUtils.fieldWalkProcess(ra, oneAnnotationFieldWalkProcessor);
                } catch (Exception e) {
                }
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

    class MenuComparator implements Comparator<Menu> {

        @Override
        public int compare(Menu o1, Menu o2) {
            if (o1.getOrder() < o2.getOrder())
                return -1;
            if (o1.getOrder() > o2.getOrder())
                return 1;
            return 0;
        }

    }
}
