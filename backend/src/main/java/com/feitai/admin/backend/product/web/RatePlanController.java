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
import com.feitai.jieya.server.dao.base.constant.CalculationMode;
import com.feitai.jieya.server.dao.base.constant.FeeBaseType;
import com.feitai.jieya.server.dao.base.constant.PaymentType;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetailSnapshot;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/backend/ratePlan")
public class RatePlanController extends BaseListableController<RatePlanMore> {


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

    @RequiresPermissions("/backend/ratePlan:list")
    @RequestMapping(value = "index")
    public String index() {
        return "/backend/ratePlan/index";
    }

    @RequiresPermissions("/backend/ratePlan:list")
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
                            if (!CalculationMode.FIXED_AMOUNT.equals(ratePlanDetail.getCalculationMode())) {
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

    @RequiresPermissions("/backend/ratePlan:update")
    @RequestMapping(value = "addOrUpdate", method = RequestMethod.GET)
    public String addOrUpdate(
            @RequestParam(value = "id", defaultValue = "-1") Long id,
            Model model
    ) {
        model.addAttribute("id", id);
        return "/backend/ratePlan/addOrUpdate";
    }


    @RequiresPermissions("/backend/ratePlan:add")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Valid @RequestBody RatePlanRequest ratePlanRequest) {
    	ratePlanService.addRatePlan(ratePlanRequest);
        return successResult;
    }

    @RequiresPermissions("/backend/ratePlan:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestBody RatePlanRequest ratePlanRequest) {
    	ratePlanService.updateRatePlan(ratePlanRequest);
        return successResult;
    }

    @RequiresPermissions("/backend/ratePlan:del")
    @RequestMapping(value = "del")
    @ResponseBody
    public Object del(@RequestParam(value = "ids[]") Long[] ids) {
        ratePlanService.delete(ids);
        return successResult;
    }



    @Override
    protected DynamitSupportService<RatePlanMore> getService() {
        return this.ratePlanService;
    }

}
