/**
 * @author
 * @version 1.0
 * @desc 产品费率方案表
 * @since 2018-08-10 13:12:44
 */

package com.feitai.admin.backend.product.service;

import com.feitai.admin.backend.product.entity.RatePlanMore;
import com.feitai.admin.backend.product.entity.RatePlanTermMore;
import com.feitai.admin.backend.product.entity.SnapshotRatePlan;
import com.feitai.admin.backend.product.entity.SnapshotRatePlanTerm;
import com.feitai.admin.backend.product.vo.FeePlan;
import com.feitai.admin.backend.product.vo.FeePlanDetail;
import com.feitai.admin.backend.product.vo.RatePlanRequest;
import com.feitai.admin.backend.product.vo.Weight;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.jieya.server.dao.base.constant.CalculationMode;
import com.feitai.jieya.server.dao.base.constant.FeeBaseType;
import com.feitai.jieya.server.dao.base.constant.PaymentType;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetail;
import com.feitai.jieya.server.dao.rateplan.model.RatePlanDetailSnapshot;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class RatePlanService extends ClassPrefixDynamicSupportService<RatePlanMore> {
	
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
    private RatePlanTermService ratePlanTermService;

    @Autowired
    private RatePlanDetailService ratePlanDetailService;

    @Autowired
    private SnapshotRatePlanService snapshotRatePlanService;

    @Autowired
    private SnapshotRatePlanTermService snapshotRatePlanTermService;

    @Autowired
    private SnapshotRatePlanDetailService snapshotRatePlanDetailService;
    
    public void addRatePlan(RatePlanRequest ratePlanRequest){
    	 Date date = new Date();
         ratePlanRequest.setId(null);
         RatePlanMore ratePlan = new RatePlanMore();
         BeanUtils.copyProperties(ratePlanRequest, ratePlan);        
         ratePlan.setCurrentVersion(0);
         ratePlan = save(ratePlan);
         //复制到快照表
         SnapshotRatePlan snapshotRatePlan = new SnapshotRatePlan();
         BeanUtils.copyProperties(ratePlan, snapshotRatePlan);
         snapshotRatePlan.setId(null);
         snapshotRatePlan.setFundId(Long.valueOf(ratePlan.getFundId()));
         snapshotRatePlan = snapshotRatePlanService.save(snapshotRatePlan);
         for (Weight weight : ratePlanRequest.getWeight()) {
             RatePlanTermMore ratePlanTerm = new RatePlanTermMore();
             ratePlanTerm.setRatePlanId(ratePlan.getId());
             ratePlanTerm.setTerm(weight.getTerm());
             ratePlanTerm.setWeight(weight.getWeight());
             ratePlanTerm.setVersion("0");
             ratePlanTerm = ratePlanTermService.save(ratePlanTerm);
             // 复制到快照表
             SnapshotRatePlanTerm snapshotRatePlanTerm = new SnapshotRatePlanTerm();
             BeanUtils.copyProperties(ratePlanTerm, snapshotRatePlanTerm);
             snapshotRatePlanTerm.setId(null);
             snapshotRatePlanTerm.setRatePlanId(ratePlan.getId());
             snapshotRatePlanTerm.setVersion(0);
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
                         if (!CalculationMode.FIXED_AMOUNT.equals(ratePlanDetail.getCalculationMode())) {
                             ratePlanDetail.setFee(feePlanDetail.getFee().divide(new BigDecimal("100")));
                         }
                         // 默认回写
                         if (ratePlanDetail.getFeeBaseType() == null) {
                             ratePlanDetail.setFeeBaseType(FeeBaseType.DEFAULT);
                         }
                         if (ratePlanDetail.getPaymentType() == null) {
                             ratePlanDetail.setPaymentType(PaymentType.DEFAULT);
                         }
                         ratePlanDetailService.save(ratePlanDetail);
                         // 复制到快照表
                         RatePlanDetailSnapshot snapshotRatePlanDetail = new RatePlanDetailSnapshot();
                         BeanUtils.copyProperties(ratePlanDetail, snapshotRatePlanDetail);
                         snapshotRatePlanDetail.setId(null);
                         snapshotRatePlanDetail.setRatePlanTermId(ratePlanTerm.getId());
                         snapshotRatePlanDetailService.save(snapshotRatePlanDetail);
                     }
                 }
             }
         }
    }
     
	public void updateRatePlan(RatePlanRequest ratePlanRequest){
		  RatePlanMore ratePlan = findOne(ratePlanRequest.getId());
	        if (ratePlan != null) {
	            //保留code和版本信息，删除原来记录
	            Integer currentVersion = ratePlan.getCurrentVersion() + 1;
	            for (RatePlanTermMore ratePlanTerm : ratePlan.getRatePlanTerms()) {
	            	if(ratePlanTerm.getRatePlanDetails()!=null){
		                for (RatePlanDetail ratePlanDetail : ratePlanTerm.getRatePlanDetails()) {
		                    ratePlanDetailService.delete(ratePlanDetail);
		                }
	            	}
	                ratePlanTermService.delete(ratePlanTerm);
	            }
	            delete(ratePlan);
	            // 重新插入
	            Date date = new Date();
	            BeanUtils.copyProperties(ratePlanRequest, ratePlan);
	            ratePlan.setCurrentVersion(currentVersion);
	            ratePlan.setCreatedTime(date);
	            ratePlan.setUpdateTime(date);
	            ratePlan = save(ratePlan);
	            SnapshotRatePlan snapshotRatePlan = new SnapshotRatePlan();
	            BeanUtils.copyProperties(ratePlan, snapshotRatePlan);
	            snapshotRatePlan.setId(null);
	            snapshotRatePlan.setFundId(Long.valueOf(ratePlan.getFundId()));
	            snapshotRatePlan = snapshotRatePlanService.save(snapshotRatePlan);
	            for (Weight weight : ratePlanRequest.getWeight()) {
	                RatePlanTermMore ratePlanTerm = new RatePlanTermMore();
	                ratePlanTerm.setRatePlanId(ratePlan.getId());
	                ratePlanTerm.setTerm(weight.getTerm());
	                ratePlanTerm.setWeight(weight.getWeight());
	                ratePlanTerm.setVersion(currentVersion+"");
	                ratePlanTerm.setCreatedTime(date);
	                ratePlanTerm.setUpdateTime(date);
	                ratePlanTerm = ratePlanTermService.save(ratePlanTerm);
	                SnapshotRatePlanTerm snapshotRatePlanTerm = new SnapshotRatePlanTerm();
	                BeanUtils.copyProperties(ratePlanTerm, snapshotRatePlanTerm);
	                snapshotRatePlanTerm.setVersion(currentVersion);
	                snapshotRatePlanTerm.setRatePlanId(ratePlan.getId());
	                snapshotRatePlanTerm.setId(null);
	                snapshotRatePlanTermService.save(snapshotRatePlanTerm);
	                for (FeePlan feePlan : ratePlanRequest.getFeePlan()) {
	                    if (feePlan.getTerm().shortValue() == weight.getTerm()) {
	                        List<FeePlanDetail> feePlanDetailList = feePlan.getSubject();
	                        for (FeePlanDetail feePlanDetail : feePlanDetailList) {
	                            RatePlanDetail ratePlanDetail = new RatePlanDetail();
	                            BeanUtils.copyProperties(feePlanDetail, ratePlanDetail);
	                            ratePlanDetail.setRatePlanTermId(ratePlanTerm.getId());
	                            ratePlanDetail.setName(subjectMap.get(ratePlanDetail.getSubjectId()));
	                           ratePlanDetail.setVersion(currentVersion);
	                            // 利率除以100
	                            if (!CalculationMode.FIXED_AMOUNT.equals(ratePlanDetail.getCalculationMode())) {
	                                ratePlanDetail.setFee(ratePlanDetail.getFee().divide(new BigDecimal("100")));
	                            }
	                            // 默认回写
	                            if (ratePlanDetail.getFeeBaseType() == null) {
	                                ratePlanDetail.setFeeBaseType(FeeBaseType.DEFAULT);
	                            }
	                            if (ratePlanDetail.getPaymentType() == null) {
	                                ratePlanDetail.setPaymentType(PaymentType.DEFAULT);
	                            }
	                            ratePlanDetail.setCreatedTime(date);
	                            ratePlanDetail.setUpdateTime(date);
	                            ratePlanDetailService.save(ratePlanDetail);
	                            RatePlanDetailSnapshot snapshotRatePlanDetail = new RatePlanDetailSnapshot();
	                            BeanUtils.copyProperties(ratePlanDetail, snapshotRatePlanDetail);
	                            snapshotRatePlanDetail.setRatePlanTermId(ratePlanTerm.getId());
	                            snapshotRatePlanDetail.setId(null);
	                            snapshotRatePlanDetailService.save(snapshotRatePlanDetail);
	                        }
	                    }
	                }
	            }
	        }
	}
  public void deleteRatePlan(Long[] ids){
	  for (Long id : ids) {
          RatePlanMore ratePlan = findOne(id);
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
              delete(ratePlan);
          }
      }
  }
}
