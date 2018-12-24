package com.feitai.admin.channel.userChannel.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.feitai.admin.backend.config.entity.ChannelPrimary;
import com.feitai.admin.backend.config.service.ChannelPrimaryService;
import com.feitai.admin.channel.userChannel.service.UserChannelService;
import com.feitai.admin.core.annotation.LogAnnotation;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.system.model.Role;
import com.feitai.admin.system.model.User;
import com.feitai.admin.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * detail:
 * author:longhaoteng
 * date:2018/12/21
 */
@Controller
@Slf4j
@RequestMapping(value = "/channel/userChannel")
public class UserChannelController extends BaseListableController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private UserChannelService userChannelService;

    @Autowired
    private ChannelPrimaryService channelPrimaryService;

    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/channel/userChannel/index");
        return modelAndView;
    }

    @RequestMapping(value = "/primaryList")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)// 写日志但是不打印请求的params,但不打印ResponseBody的内容
    public Object primaryList(){
        List<ChannelPrimary> channelPrimarys = channelPrimaryService.findAll();
        List<ListItem> list = new ArrayList<ListItem>();
        for(ChannelPrimary channelPrimary:channelPrimarys){
            list.add(new ListItem(channelPrimary.getPrimaryChannelName(), channelPrimary.getChannelCode()));
        }
        return list;
    }

    /**
     * 写日志但是不打印请求的params,但不打印ResponseBody的内容
     *
     * @param request
     * @return
     */
    @RequiresPermissions("/channel/userChannelr:list")
    @RequestMapping(value = "list")
    @ResponseBody
    @LogAnnotation(value = true, writeRespBody = false)
    public Object listPage(ServletRequest request) {
        Page<User> listPage = super.list(request);
        String list = JSON.toJSONString(listPage);
        List newList = new ArrayList();
        Map mapList = JSON.parseObject(list, Map.class);
        List<JSONObject> content = (List<JSONObject>) mapList.get("content");
        //遍历page中内容，修改或添加非数据库的自定义字段
        for (JSONObject json :
                content) {
            try {
                Map<String, Object> map = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>() {
                });
                Integer userId = (Integer) map.get("id");
                List<Role> roles = userService.getRoles(userId.toString());
                map.put("roles", roles);
                newList.add(map);
            } catch (Exception e) {
                log.error("this json handle fail:[{}]! message:{}", json, e.getMessage());
                continue;
            }
            mapList.put("content", newList);
        }
        return mapList;
    }

    /**
     * 从数据库中获取user，如http://localhost/systemtem/update/1,则返回用户ID为1的用户
     *
     * @param id
     * @return
     */
    @RequiresPermissions("/channel/userChannel:update")
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object editFrom(@PathVariable("id") Long id) {
        User user = this.userService.findOne(id);
        return user;
    }

    @RequiresPermissions(value = "/channel/userChannel:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestParam(value = "loginName") String loginName,
                         @RequestParam(value = "channelIds") List<String> channelIds) {
        User user = this.userService.findByLoginName(loginName);
        userChannelService.saveAll(user.getId(),channelIds);
        return successResult;
    }

    @Override
    protected DynamitSupportService<User> getService() {
        return this.userService;
    }
}
