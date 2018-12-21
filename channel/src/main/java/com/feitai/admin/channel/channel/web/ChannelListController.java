package com.feitai.admin.channel.channel.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.config.service.ChannelService;
import com.feitai.admin.channel.channel.entity.ChannelList;
import com.feitai.admin.channel.channel.service.ChannelListService;
import com.feitai.admin.channel.userChannel.service.UserChannelService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.core.web.PageBulider;
import com.feitai.jieya.server.dao.card.model.Card;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import com.feitai.jieya.server.dao.product.model.Product;
import com.feitai.jieya.server.dao.user.model.User;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * detail:渠道方的列表
 * author:longhaoteng
 * date:2018/12/20
 */
@Controller
@RequestMapping(value = "/channel/channelList")
@Slf4j
public class ChannelListController extends BaseListableController<ChannelList> {

    @Autowired
    private UserChannelService userChannelService;

    @Autowired
    private ChannelListService channelListService;

    @Autowired
    private ChannelService channelService;

    @GetMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/channel/channelList/index");
        return modelAndView;
    }

    @RequiresPermissions("/channel/channelList:list")
    @PostMapping("/list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        //根据request获取page
        List<String> channelCodes = userChannelService.findChannelCodeByUserId();
        List<String> channelIdByMainPackageCode = channelService.findChannelIdByMainPackageCode(channelCodes);
        String sqlInChannel = Joiner.on(",").join(channelIdByMainPackageCode);

        int pageNo = PageBulider.getPageNo(request);
        int pageSize = PageBulider.getPageSize(request);
        String sql = getSql(request, getSelectMultiTable()) + " ORDER BY " + SelectMultiTable.MAIN_ALAIS + ".created_time DESC";
        Page<ChannelList> loanOrderMorePage = list(sql, pageNo, pageSize, getCountSqls(request), SelectMultiTable.COUNT_ALIAS);
        List<ChannelList> content = loanOrderMorePage.getContent();
        List<JSONObject> resultList = new ArrayList<>();

        //遍历page中内容，修改或添加非数据库的自定义字段
        for (ChannelList channelList :
                content) {
            JSONObject json = (JSONObject) JSON.toJSON(channelList);
            try {
                JSONObject jsonObject = handleSingleData(json);
                resultList.add(jsonObject);
            } catch (Exception e) {
                log.error("this json handle fail:[{}]! message:{}", json, e.getMessage());
                continue;
            }
        }
        return switchContent(loanOrderMorePage, resultList);
    }

    /***
     * 处理单条数据
     * @return
     */
    private JSONObject handleSingleData(JSONObject json) {



        return json;
    }


    private String getCountSqls(ServletRequest request) {
        StringBuffer sbSql = new StringBuffer();
        String searchSql = getService().buildSqlWhereCondition(bulidSearchParamsList(request), SelectMultiTable.MAIN_ALAIS);
        if (searchSql.equals(DynamitSupportService.WHERE_COMMON)) {
            sbSql.append(SelectMultiTable.builder(ChannelList.class).buildCountSqlString());
        } else {
            sbSql.append(getSelectMultiTable().buildCountSqlString());
        }
        sbSql.append(searchSql);
        return sbSql.toString();
    }

    private SelectMultiTable getSelectMultiTable() {
        return SelectMultiTable.builder(ChannelList.class)
                .leftJoin(PersonData.class, "person", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId"),
                }).leftJoin(IdCardData.class, "idcard", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                }).leftJoin(Product.class, "product", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "productId", Operator.EQ, "id")
                }).leftJoin(User.class, "user", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id")
                }).leftJoin(Card.class, "card", new OnCondition[]{
                        new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "userId")
                });
    }
    @Override
    protected DynamitSupportService<ChannelList> getService() {
        return this.channelListService;
    }
}
