package com.feitai.admin.mop.superpartner.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.feitai.admin.mop.base.BusinessException;
import com.google.common.collect.Range;
import lombok.Data;

import java.util.List;

/**
 * @Author qiuyunlong
 */
public class SettleRateHelper {

    private SettleRateHelper(){}

    //无穷
    private static final double INFINITE = -1;

    @Data
    public static class RateInfo {
        @JSONField(name = "im")
        private double immediatelyRate;

        @JSONField(name = "period")
        private List<RateRange> periodRate;

        /**
         * 校验金额区间是否有空隙
         */
        public void validateRange() {
            if (periodRate.get(0).getMin() != 0) {
                throw new BusinessException("range must start with 0");
            }

            if (periodRate.get(0).getMin() >= periodRate.get(0).getMax()) {
                throw new BusinessException("range error in start");
            }

            RateRange last = null;
            for (RateRange rateRange : periodRate) {
                if (last == null) {
                    last = rateRange;
                    continue;
                }
                if (rateRange.getMin() != last.getMax()) {
                    throw new BusinessException("gap between " + rateRange.getMin() + " and " + last.getMax());
                }
                last = rateRange;
            }
        }

        /**
         * 根据金额获取比率
         */
        public Double getRate(double amount) {
            for (RateRange rateRange : periodRate) {
                if (rateRange.getRange().contains(amount)) {
                    return rateRange.getRate();
                }
            }
            return null;
        }
    }

    @Data
    public  static class RateRange {
        private double rate;

        private double min;

        private double max;

        @JSONField(serialize = false)
        private Range range;
        
        public RateRange(){
        	
        }
        public RateRange(double min,double max,double rate){
        	this.min=min;
        	this.max=max;
        	this.rate=rate;
        }

        public Range<Double> getRange() {
            if (range == null) {
                //先写死，不作区间判断
                range = buildRange(min, max, false, true);
            }
            return range;
        }
    }

    private static Range<Double> buildRange(double min, double max, boolean minOpen, boolean maxOpen) {
        // x >= min
        if (INFINITE == max) {
            return Range.atLeast(min);
        }
        // min <= x < max
        if (!minOpen && maxOpen ) {
            return Range.closedOpen(min, max);
        }
        throw new BusinessException("check range :" + " min:" + min + "; max:" + max + "; minOpen:" + minOpen + "; maxOpen:" + maxOpen);
    }

    public static RateInfo formJsonString(String json) {
        return JSON.parseObject(json, RateInfo.class);
    }

}
