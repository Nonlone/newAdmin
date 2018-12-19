package com.feitai.admin.mop.advert.dao.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* AdvertEditCopy数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_advert_edit_copy")
public class AdvertEditCopy implements Serializable{
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
     * 关联类型
     */
    private Integer relType;
    /**
     * 关联ID
     */
    private Long relId;
    /**
     * 内容
     * AdvertEditCopyContent
     */
    private String content;
    /**
     * 最后操作人
     */
    private String operator;
    /**
     * 发布人
     */
    private String publisher;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    
    @Data
    public static class AdvertEditCopyContent{
    	
    	private String editInfo;
    	
    	private List<Long> relations;
    	
    	public <T> T getEditInfoObj(Class<T> clazz) {
    		
    		if (StringUtils.isBlank(editInfo)) {
    			return null;
    		}
    		
    		return JSON.parseObject(editInfo, clazz);
    	}

		public void setEditInfoObj(Object editInfoObj) {
			if (null == editInfoObj) {
				return;
			}
			this.editInfo = JSON.toJSONString(editInfoObj);
		}
    }
}


