package com.feitai.admin.mop.advert.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* AdvertGroupBlock数据实体类
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "t_advert_group_block")
public class AdvertGroupBlock implements Serializable{
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
     * 广告组ID
     */
    private Long groupId;
    /**
     * 广告块ID
     */
    private Long blockId;
    /**
     * 广告组对单个广告块的显示数量限制
     */
    private Integer perLimit;
    /**
     * 创建时间
     */
    private Date createdTime;
}
