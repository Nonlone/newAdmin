package com.feitai.admin.mop.advert.dao.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* AdvertItem数据实体类
*/
@Data
@ToString(callSuper = true)
@Table(name = "t_advert_item")
public class AdvertItem implements Serializable{
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
     * 编辑副本ID
     */
    private Long editCopyId;
    
    /**
     * 标题
     */
    private String title;
    /**
     * 子标题
     */
    private String subTitle;
    /**
     * 内容
     */
    private String content;
    /**
     * 备注
     */
    private String remark;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 播放时间(默认)
     */
    private Integer playTime;
    /**
     * 总共需要播放的次数
     */
    private Integer totalPlayCount;
    /**
     * 每周期播放次数
     */
	private Integer perPlayCount;
	/**
     * 播放周期(单位:分钟)
     */
	private Integer perPlayUnit;
	/**
     * 开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    /**
     * 结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * @link AdvertItemMatchConfig
     * 匹配配置(JSON)
     */
    private String matchConfig;
    /**
     * 展示配置(JSON)
     * @link List<AdvertItemShowConfigItem>
     */
    private String showConfig;
    /**
     * 自定义样式(JSON)
     * @link CommonJSONConfig
     */
    private String style;
    /**
     * 事件(JSON)
     * @link CommonJSONConfig
     * @link AdvertItemEvent
     */
    private String event;
    /**
     * 拓展参数(JSON)
     * @link CommonJSONConfig
     */
    private String ext;
    /**
     * 状态
     * @link AdvertItemStatusEnum
     */
    private Integer status;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 激活版本号
     */
    private Long activeVersion;
    /**
     * 发布时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    
    @Data
    @ToString(callSuper = true)
    public static class CommonJSONConfig{
    	public static final String H5 = "h5";
    	public static final String APP = "app";
    	
    	/**
         * H5配置
         */
        private Object h5;
        
        /**
         * APP配置
         */
        private Object app;
    }
    
    
    @Data
    @ToString(callSuper = true)
    public static class AdvertItemEvent{
    	/**
         * 类型
         */
        private String type;
        /**
         * 链接
         */
        private String link;
        /**
         * 参数
         */
        private JSONObject params;
    }


    @Data
    @ToString(callSuper = true)
    public static class AdvertItemMatchConfig{

        /**
         * 默认
         */
        private AdvertItemMatchConfigItem defaultConfig;
    }


    @Data
    @ToString(callSuper = true)
    public static class AdvertItemMatchConfigItem{

        /**
         * APP版本
         */
        private String appVersions;

        /**
         * 系统类型
         */
        private String osTypes;

        /**
         * 行政区城代码
         */
        private String adcodes;
    }
    
    
    
    @Data
    @ToString(callSuper = true)
    public static class AdvertItemShowConfigItem{
    	
    	/**
         * 类型
         * AdvertItemTypeEnum
         */
        private String type;
        /**
         * 链接
         */
        private String url;
        /**
         * 播放时间(自定义)
         */
        private Integer playTime;
        /**
         * 总共需要播放的次数
         */
        private Integer totalPlayCount;
        /**
         * 每周期播放次数
         */
    	private Integer perPlayCount;
    	/**
         * 播放周期(单位:小时)
         */
    	private Integer perPlayUnit;
    	/**
         * 开始时间
         */
        private Date beginTime;
        /**
         * 结束时间
         */
        private Date endTime;
        /**
         * 比例(图片)
         */
        private String ratio;
        /**
         *
         */
        private BigDecimal ratioValue;
        /**
         * 分辨率
         */
        private String dpi;
        /**
         * 否是默认项
         */
        private Boolean defaultItem;
    }
}
