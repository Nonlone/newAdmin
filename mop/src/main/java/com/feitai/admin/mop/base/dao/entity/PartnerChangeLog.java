package com.feitai.admin.mop.base.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
* PartnerChangeLog数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_partner_change_log")
public class PartnerChangeLog {
    /**
	 *
	 */
	protected static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 用户ID(partner)
     */
    private Long userId;
    /**
     * 操作人账号
     */
    private String operator;
    /**
     * 更改记录描述
     */
    private String changeDesc;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}
