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
* AdvertBlockItem数据实体类
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "t_advert_block_item")
public class AdvertBlockItem implements Serializable{
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
     * 广告块ID
     */
    private Long blockId;
    /**
     * 广告项ID
     */
    private Long itemId;
    /**
     * 创建时间
     */
    private Date createdTime;
}
