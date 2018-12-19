package com.feitai.admin.mop.advert.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
* AdvertEditCopyRelation数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_advert_edit_copy_relation")
public class AdvertEditCopyRelation implements Serializable{
	private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 编辑副本ID
     */
    private Long editCopyId;
    /**
     * 关联类型
     * AdvertEditCopyRelTypeEnum
     */
    private Integer relType;
    /**
     * 关联ID
     */
    private Long relId;
    /**
     * 目标关联类型
     * AdvertEditCopyRelTypeEnum
     */
    private Integer targetRelType;
    /**
     * 目标关联ID
     */
    private Long targetRelId;
    /**
     * 创建时间
     */
    private Date createdTime;
}


