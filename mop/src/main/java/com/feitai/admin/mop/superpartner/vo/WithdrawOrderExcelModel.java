package com.feitai.admin.mop.superpartner.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.feitai.admin.mop.superpartner.dao.entity.WithdrawOrder;
import com.feitai.admin.mop.superpartner.enums.SuperPartnerOrderStatusEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author qiuyunlong
 */
@Data
public class WithdrawOrderExcelModel extends BaseRowModel {

    @ExcelProperty(value = "申请提现时间", index = 0, format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ExcelProperty(value = "提现单号", index = 1)
    private String id;

    @ExcelProperty(value = "用户ID", index = 2)
    private String partnerUserId;

    @ExcelProperty(value = "提现金额/元", index = 3)
    private BigDecimal amount;

    @ExcelProperty(value = "税后金额/元", index = 4)
    private BigDecimal afterTaxAmount;

    @ExcelProperty(value = "状态", index = 5)
    private String status;

    @ExcelProperty(value = "收款人姓名", index = 6)
    private String name;

    @ExcelProperty(value = "身份证号", index = 7)
    private String idCardNo;

    @ExcelProperty(value = "银行卡号", index = 8)
    private String bankCardNo;

    @ExcelProperty(value = "开户银行", index = 9)
    private String bankName;

    @ExcelProperty(value = "分支行", index = 10)
    private String bankFullName;

    public static WithdrawOrderExcelModel convertFrom(WithdrawOrder withdrawOrder) {
        WithdrawOrderExcelModel result = new WithdrawOrderExcelModel();
        BeanUtils.copyProperties(withdrawOrder, result);
        BeanUtils.copyProperties(withdrawOrder.getOrderReceiverInfo(), result);
        result.setStatus(SuperPartnerOrderStatusEnum.getByValue(withdrawOrder.getStatus()).getDesc());
        result.setPartnerUserId(String.valueOf(withdrawOrder.getPartnerUserId()));
        result.setId(String.valueOf(withdrawOrder.getId()));
        return result;
    }

}
