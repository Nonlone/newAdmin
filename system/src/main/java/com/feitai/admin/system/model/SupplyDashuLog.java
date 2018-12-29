package com.feitai.admin.system.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * detail:补件发送大数记录
 * author:longhaoteng
 * date:2018/12/26
 */
@Data
@ToString
@Table(name = "sys_supply_dashu_log")
public class SupplyDashuLog {

    @Id
    private Long id;

    private Date createdTime;

    private Date updateTime;

}
