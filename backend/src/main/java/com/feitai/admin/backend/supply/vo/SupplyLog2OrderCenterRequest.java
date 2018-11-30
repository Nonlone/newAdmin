package com.feitai.admin.backend.supply.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * detail:提交补件至orderCenter
 * author:longhaoteng
 * date:2018/11/30
 */
@Data
@ToString
public class SupplyLog2OrderCenterRequest {

    private Long loanId;

    private List<PicturesInfo> picList;

}
