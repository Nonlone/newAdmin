package com.feitai.admin.mop.advert.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class AdvertEditCopyObject implements Serializable{
	
	/**
	 *
	 */
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
}
