/**
 * @author
 * @version 1.0
 * @desc 产品费率方案表
 * @since 2018-08-10 13:12:44
 */

package com.feitai.admin.backend.product.web;


import com.feitai.admin.backend.product.entity.RatePlanMore;
import com.feitai.admin.backend.product.entity.RatePlanTermMore;
import com.feitai.admin.backend.product.entity.SnapshotRatePlan;
import com.feitai.admin.backend.product.entity.SnapshotRatePlanTerm;
import com.feitai.admin.backend.product.service.*;
import com.feitai.admin.backend.product.vo.FeePlan;
import com.feitai.admin.backend.product.vo.FeePlanDetail;
import com.feitai.admin.backend.product.vo.RatePlanRequest;
import com.feitai.admin.backend.product.vo.Weight;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetailSnapshot;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/product/ratePlan")
public class RatePlanController extends BaseListableController<RatePlanMore> {

    /**
     * 默认计费基数
     */
    private static final byte DEFAULT_FEE_BASE_TYPE = 0;

    /**
     * 默认还款方式
     */
    private static final byte DEFAULT_PAYMENT_TYPE = 0;

    /**
     * 科目映射
     */
    private static final Map<Byte, String> subjectMap = new HashMap<Byte, String>() {{
        this.put((byte) 2, "利息");
        this.put((byte) 10, "审批费");
        this.put((byte) 11, "担保费");
        this.put((byte) 12, "居间人服务费");
    }};

    @Autowired
    private RatePlanService ratePlanService;

    @Autowired
    private RatePlanTermService ratePlanTermService;

    @Autowired
    private RatePlanDetailService ratePlanDetailService;

    @Autowired
    private SnapshotRatePlanService snapshotRatePlanService;

    @Autowired
    private SnapshotRatePlanTermService snapshotRatePlanTermService;

    @Autowired
    private SnapshotRatePlanDetailService snapshotRatePlanDetailService;

    @RequestMapping(value = "")
    public String index() {
        return "/admin/product/ratePlan/index";
    }

    @RequiresPermissions("/admin/product/ratePlan:list")
    @RequestMapping(value = "list")
    @ResponseBody
    public Object listPage(ServletRequest request) {
        return super.list(request);
    }

    @RequestMapping(value = "getRatePlan", method = RequestMethod.POST)
    @ResponseBody
    public Object getRatePaln(@RequestParam(value = "id") Long id) {
        RatePlanMore ratePlan = ratePlanService.findOne(id);
        if (ratePlan != null) {
            if (!CollectionUtils.isEmpty(ratePlan.getRatePlanTerms())) {
                for (RatePlanTermMore ratePlanTerm : ratePlan.getRatePlanTerms()) {
                    if (!CollectionUtils.isEmpty(ratePlanTerm.getRatePlanDetails())) {
                        for (RatePlanDetail ratePlanDetail : ratePlanTerm.getRatePlanDetails()) {
                            // 利率展示乘以100
                            if (ratePlanDetail.getCalculationMode() != 1) {
                                ratePlanDetail.setFee(ratePlanDetail.getFee().multiply(new BigDecimal("100")));
                            }
                        }
                    }
                }
            }
            return ratePlan;
        }
        return null;
    }

    @RequiresPermissions("/admin/product/ratePlan:update")
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.GET)
    public String addOrUpdate(
            @RequestParam(value = "id", defaultValue = "-1") Long id,
            Model model
    ) {
        model.addAttribute("id", id);
        return "/admin/product/ratePlan/addOrUpdate";
    }


    @RequiresPermissions("/admin/product/ratePlan:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid @RequestBody RatePlanRequest ratePlanRequest) {
        Date date = new Date();
        RatePlanMore ratePlan = new RatePlanMore();
        BeanUtils.copyProperties(ratePlanRequest, ratePlan);
        //ratePlan.setCurrentVersion(1);
        ratePlan.setCreatedTime(date);
        ratePlan.setUpdateTime(date);
        ratePlan = ratePlanService.save(ratePlan);
        //复制到快照表
        SnapshotRatePlan snapshotRatePlan = new SnapshotRatePlan();
        BeanUtils.copyProperties(ratePlan, snapshotRatePlan);
        snapshotRatePlan = snapshotRatePlanService.save(snapshotRatePlan);
        for (Weight weight : ratePlanRequest.getWeight()) {
            RatePlanTermMore ratePlanTerm = new RatePlanTermMore();
            ratePlanTerm.setRatePlanId(ratePlan.getId());
            ratePlanTerm.setTerm(weight.getTerm());
            ratePlanTerm.setWeight(weight.getWeight());
            ratePlanTerm.setVersion("0");
            ratePlanTerm.setCreatedTime(date);
            ratePlanTerm.setUpdateTime(date);
            ratePlanTerm = ratePlanTermService.save(ratePlanTerm);
            // 复制到快照表
            SnapshotRatePlanTerm snapshotRatePlanTerm = new SnapshotRatePlanTerm();
            BeanUtils.copyProperties(ratePlanTerm, snapshotRatePlanTerm);
            snapshotRatePlanTerm.setRatePlanId(snapshotRatePlan.getId());
            snapshotRatePlanTerm = snapshotRatePlanTermService.save(snapshotRatePlanTerm);
            for (FeePlan feePlan : ratePlanRequest.getFeePlan()) {
                if (feePlan.getTerm().shortValue() == weight.getTerm()) {
                    List<FeePlanDetail> feePlanDetailList = feePlan.getSubject();
                    for (FeePlanDetail feePlanDetail : feePlanDetailList) {
                        RatePlanDetail ratePlanDetail = new RatePlanDetail();
                        BeanUtils.copyProperties(feePlanDetail, ratePlanDetail);
                        ratePlanDetail.setRatePlanTermId(ratePlanTerm.getId());
                        ratePlanDetail.setName(subjectMap.get(ratePlanDetail.getSubjectId()));
                        ratePlanDetail.setVersion(0);
                        // 利率除以100
                        if (ratePlanDetail.getCalculationMode() != 1) {
                            ratePlanDetail.setFee(ratePlanDetail.getFee().multiply(new BigDecimal("100")));
                        }
                        // 默认回写
                        if (ratePlanDetail.getFeeBaseType() == null) {
                            ratePlanDetail.setFeeBaseType(DEFAULT_FEE_BASE_TYPE);
                        }
                        if (ratePlanDetail.getPaymentType() == null) {
                            ratePlanDetail.setPaymentType(DEFAULT_PAYMENT_TYPE);
                        }
                        ratePlanDetail.setUpdateTime(date);
                        ratePlanDetail.setCreatedTime(date);
                        ratePlanDetailService.save(ratePlanDetail);
                        // 复制到快照表
                        RatePlanDetailSnapshot snapshotRatePlanDetail = new RatePlanDetailSnapshot();
                        BeanUtils.copyProperties(ratePlanDetail, snapshotRatePlanDetail);
                        snapshotRatePlanDetail.setRatePlanTermId(snapshotRatePlanTerm.getRatePlanId());
                        snapshotRatePlanDetailService.save(snapshotRatePlanDetail);
                    }
                }
            }
        }
        return successResult;
    }

    @RequiresPermissions("/admin/product/ratePlan:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestBody RatePlanRequest ratePlanRequest) {
        RatePlanMore ratePlan = ratePlanService.findOne(ratePlanRequest.getId());
        if (ratePlan != null) {
            //保留code和版本信息，删除原来记录
            //Integer currentVersion = ratePlan.getCurrentVersion() + 1;
            for (RatePlanTermMore ratePlanTerm : ratePlan.getRatePlanTerms()) {
                for (RatePlanDetail ratePlanDetail : ratePlanTerm.getRatePlanDetails()) {
                    ratePlanDetailService.delete(ratePlanDetail);
                }
                ratePlanTermService.delete(ratePlanTerm);
            }
            ratePlanService.delete(ratePlan);
            // 重新插入
            Date date = new Date();
            BeanUtils.copyProperties(ratePlanRequest, ratePlan);
            ratePlan.setId(null);
            //ratePlan.setCurrentVersion(currentVersion);
            ratePlan.setCreatedTime(date);
            ratePlan.setUpdateTime(date);
            ratePlan = ratePlanService.save(ratePlan);
            SnapshotRatePlan snapshotRatePlan = new SnapshotRatePlan();
            BeanUtils.copyProperties(ratePlan, snapshotRatePlan);
            snapshotRatePlan = snapshotRatePlanService.save(snapshotRatePlan);
            for (Weight weight : ratePlanRequest.getWeight()) {
                RatePlanTermMore ratePlanTerm = new RatePlanTermMore();
                ratePlanTerm.setRatePlanId(ratePlan.getId());
                ratePlanTerm.setTerm(weight.getTerm());
                ratePlanTerm.setWeight(weight.getWeight());
                //ratePlanTerm.setVersion(currentVersion);
                ratePlanTerm.setCreatedTime(date);
                ratePlanTerm.setUpdateTime(date);
                ratePlanTerm = ratePlanTermService.save(ratePlanTerm);
                SnapshotRatePlanTerm snapshotRatePlanTerm = new SnapshotRatePlanTerm();
                BeanUtils.copyProperties(ratePlanTerm, snapshotRatePlanTerm);
                snapshotRatePlanTerm.setRatePlanId(snapshotRatePlan.getId());
                snapshotRatePlanTermService.save(snapshotRatePlanTerm);
                for (FeePlan feePlan : ratePlanRequest.getFeePlan()) {
                    if (feePlan.getTerm().shortValue() == weight.getTerm()) {
                        List<FeePlanDetail> feePlanDetailList = feePlan.getSubject();
                        for (FeePlanDetail feePlanDetail : feePlanDetailList) {
                            RatePlanDetail ratePlanDetail = new RatePlanDetail();
                            BeanUtils.copyProperties(feePlanDetail, ratePlanDetail);
                            ratePlanDetail.setRatePlanTermId(ratePlanTerm.getId());
                            ratePlanDetail.setName(subjectMap.get(ratePlanDetail.getSubjectId()));
                           //ratePlanDetail.setVersion(currentVersion);
                            // 利率除以100
                            if (ratePlanDetail.getCalculationMode() != 1) {
                                ratePlanDetail.setFee(ratePlanDetail.getFee().multiply(new BigDecimal("100")));
                            }
                            // 默认回写
                            if (ratePlanDetail.getFeeBaseType() == null) {
                                ratePlanDetail.setFeeBaseType(DEFAULT_FEE_BASE_TYPE);
                            }
                            if (ratePlanDetail.getPaymentType() == null) {
                                ratePlanDetail.setPaymentType(DEFAULT_PAYMENT_TYPE);
                            }
                            ratePlanDetail.setCreatedTime(date);
                            ratePlanDetail.setUpdateTime(date);
                            ratePlanDetailService.save(ratePlanDetail);
                            RatePlanDetailSnapshot snapshotRatePlanDetail = new RatePlanDetailSnapshot();
                            BeanUtils.copyProperties(ratePlanDetail, snapshotRatePlanDetail);
                            snapshotRatePlanDetail.setRatePlanTermId(snapshotRatePlanTerm.getId());
                            snapshotRatePlanDetailService.save(snapshotRatePlanDetail);
                        }
                    }
                }
            }
        }
        return successResult;
    }

    @RequiresPermissions("/admin/product/ratePlan:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        for (Long id : ids) {
            RatePlanMore ratePlan = ratePlanService.findOne(id);
            if (ratePlan != null ) {
                if(!CollectionUtils.isEmpty(ratePlan.getRatePlanTerms())){
                    // 删除期数
                    for (RatePlanTermMore ratePlanTerm : ratePlan.getRatePlanTerms()) {
                        if (!CollectionUtils.isEmpty(ratePlanTerm.getRatePlanDetails())) {
                            for (RatePlanDetail ratePlanDetail : ratePlanTerm.getRatePlanDetails()) {
                                // 删除明细
                                ratePlanDetailService.deleteByPrimaryKey(ratePlanDetail.getId());
                            }
                        }
                        ratePlanTermService.deleteByPrimaryKey(ratePlanTerm.getId());
                    }
                }
                ratePlanService.delete(ratePlan);
            }
        }
        return successResult;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getratePlan(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("ratePlan", this.ratePlanService.findOne(id));
        }
    }

    @Override
    protected DynamitSupportService<RatePlanMore> getService() {
        return this.ratePlanService;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder){
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }
}