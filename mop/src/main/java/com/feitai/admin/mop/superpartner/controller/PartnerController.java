package com.feitai.admin.mop.superpartner.controller;

import com.alibaba.fastjson.JSON;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.vo.AjaxResult;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.mop.superpartner.dao.entity.Partner;
import com.feitai.admin.mop.superpartner.dao.entity.PartnerChangeLog;
import com.feitai.admin.mop.superpartner.enums.PartnerType;
import com.feitai.admin.mop.superpartner.request.PartnerQueryRequest;
import com.feitai.admin.mop.superpartner.request.PartnerUpdateRequest;
import com.feitai.admin.mop.superpartner.service.PartnerChangeLogService;
import com.feitai.admin.mop.superpartner.service.PartnerService;
import com.feitai.admin.mop.superpartner.service.SettleRateHelper;
import com.feitai.admin.mop.superpartner.service.SettleRateHelper.RateInfo;
import com.feitai.admin.mop.superpartner.service.SettleRateHelper.RateRange;
import com.feitai.admin.mop.superpartner.vo.PartnerSettleResp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Controller
@RequestMapping("/mop/partner")
public class PartnerController extends BaseListableController<Partner> {

	@Autowired
	private PartnerService partnerService;
	@Autowired
	private PartnerChangeLogService partnerChangeLogService;

    @RequiresPermissions("/mop/partner:list")
    @RequestMapping("/list")
    @ResponseBody
    public Object listPartner(@ModelAttribute PartnerQueryRequest queryRequest){
		List<Partner> partners = partnerService.listPartner(
				queryRequest.getUserId(), queryRequest.getType(), queryRequest.getPhone());
		Page<Partner> partnerPage = buildPageByExemple(partners, queryRequest.getPageIndex(), queryRequest.getLimit());
		convertPartner(partnerPage);
        return partnerPage;
    }

	@RequiresPermissions("/mop/partner:list")
    @RequestMapping()
   	public String index() {
   		return "/mop/partner/index";
   	}

	private void convertPartner(Page pageInfo) {
		List<Partner> list = pageInfo.getContent();
		List<PartnerSettleResp> resultList = new ArrayList<>();
		for (Partner partner : list) {
			resultList.add(convertPartner(partner));
		}
		pageInfo.setContent(resultList);
	}

	private PartnerSettleResp convertPartner(Partner partner) {
		RateInfo rateInfo = SettleRateHelper.formJsonString(partner.getSettleInfo().getDetail());
		Double im = rateInfo.getImmediatelyRate() * 100;
		String periods = convertPartner(rateInfo.getPeriodRate());
		PartnerSettleResp psr = PartnerSettleResp.builder()
                .id(partner.getId())
                .userId(partner.getUserId())
                .type(partner.getType())
				.im(im + "%")
                .periods(periods)
                .originalPeriod(partner.getSettleInfo().getDetail())
                .build();
		return psr;
	}

	private String convertPartner(List<RateRange> rateRangeList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rateRangeList.size() - 1; i++) {
			RateRange rateRange = rateRangeList.get(i);
			sb.append(rateRange.getMin() / 10000)
                    .append("<")
                    .append(rateRange.getMax()/10000)
                    .append("万, ")
					.append(rateRange.getRate() * 100)
                    .append("%")
                    .append("; ");
		}
		RateRange lastRateRange = rateRangeList.get(rateRangeList.size() - 1);
		sb.append("≥")
                .append(lastRateRange.getMin() / 10000)
                .append("万，")
                .append(lastRateRange.getRate() * 100)
                .append("%");

		return sb.toString();
	}

    @RequiresPermissions("/mop/partner:update")
	@RequestMapping("/type/update")
	@ResponseBody
	public Object updatePartnerType(@ModelAttribute PartnerUpdateRequest updateRequest) {
		// 默认个人改企业
		partnerService.updatePartnerType(updateRequest.getUserId(), PartnerType.CORPORATE.getType(),
                SecurityUtils.getSubject().getPrincipal().toString());
		return new AjaxResult(true, "操作成功！");
	}

    @RequiresPermissions("/mop/partner:update")
    @RequestMapping("/rate/update")
    @ResponseBody
    public Object updatePartnerRate(@ModelAttribute PartnerUpdateRequest updateRequest){
    	try {
            JSON.parseObject(updateRequest.getSettle(), RateInfo.class).validateRange();
        } catch (Exception e) {
    	    return new AjaxResult(false, "配置信息有误，请仔细校对");
        }
        partnerService.updatePartnerRate(updateRequest.getUserId(), SecurityUtils.getSubject().getPrincipal().toString(), updateRequest.getSettle());
        return new AjaxResult(true, "操作成功！");
    }

	@RequestMapping("/changeLog/list/index")
	public String changeLogIndex() {
		return "/mop/partner/changeLogIndex";
	}

    @RequiresPermissions("/mop/partner:list")
    @RequestMapping("/changeLog/list")
    @ResponseBody
    public Object listPartnerChangeLog(@ModelAttribute PartnerQueryRequest queryRequest){
        List<PartnerChangeLog> pageInfo = partnerChangeLogService.listPartnerChangeLog(queryRequest.getUserId());
		Page<PartnerChangeLog> partnerChangeLogPage = buildPage(pageInfo, queryRequest.getPageIndex(), queryRequest.getLimit());
		return partnerChangeLogPage;
    }

	@Override
	protected DynamitSupportService<Partner> getService() {
		return null;
	}
}
