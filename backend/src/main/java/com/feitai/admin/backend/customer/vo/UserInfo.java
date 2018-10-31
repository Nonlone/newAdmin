package com.feitai.admin.backend.customer.vo;

import lombok.Data;

import java.util.Date;

/**
 * detail:
 * author:longhaoteng
 * date:2018/7/24
 */

@Data
public class UserInfo {

    //userid
    private Long userId;

    //名字
    private String name;

    //性别
    private String sex;

    //民族
    private String nation;

    //出生日期
    private Date birthday;

    //身份证住址
    private String address;

    //身份证有效开始时间
    private Date startTime;

    //身份证失效时间
    private Date endTime;

    //签发机关
    private String orgination;

    //是否实名
    private Boolean certified;

    //手机号码
    private String phone;

    //用户实名状态
    private Byte authStatus;

    //是否封禁
    private Boolean blocked;

    //封禁原因
    private String blockReason;

    //封禁id
    private Long blockCauseI;

    //封禁时间
    private Date blockTime;

    //省份编码
    private String provinceCode;

    //省份名称
    private String provinceName;

    //城市编码
    private String cityCode;

    //城市名称
    private String cityName;

    //地区编码
    private String districtCode;

    //地区名称
    private String districtName;

    //居住地址
    private String addressNow;

    //居住类型
    private String residentialType;

    //学历类型
    private String educationLevel;

    //婚育状况
    private String maritalStatus;

    //生育状况
    private String fertilityStatus;

    //配偶姓名
    private String spouseName;

    //配偶电话
    private String spouseContact;


}