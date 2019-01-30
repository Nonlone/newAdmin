package com.feitai.admin.mop.appversion.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;

import com.feitai.admin.mop.appversion.enums.AppVersionStatusEnum;
import com.feitai.admin.mop.appversion.enums.OSTypeEnum;
import com.feitai.admin.mop.appversion.enums.UpdateTypeEnum;

import lombok.Data;
import lombok.ToString;

/**
 * VerAppVersion数据实体类
 */
@Data
@ToString(callSuper = true)
@Table(name = "t_ver_app_version")
public class VerAppVersion implements Serializable {
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
	 * 应用编码
	 */
	private String appCode;
	/**
	 * 系统类型{@link OSTypeEnum}
	 */
	private String osType;
	/**
	 * APP外部版本号
	 */
	private String appVersion;
	/**
	 * APP外部版本号值(可用于最新版本排序)
	 */
	private Long appVersionValue;
	/**
	 * 内部版本号
	 */
	private String insideVersion;
	/**
	 * 更新规则 {@link VerAppUpdateRule}
	 */
	private String updateRule;
	/**
	 * 更新说明
	 */
	private String updateNote;
	/**
	 * 状态{@link AppVersionStatusEnum}
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	@Data
	@ToString(callSuper = true)
	public static class VerAppUpdateRule implements Serializable {
		protected static final long serialVersionUID = 1L;

		/**
		 * 通用的更新规则
		 */
		private UpdateRule commonRule;

		/**
		 * 高级的更新规则
		 */
		private List<UpdateRule> customRules;

		@Data
		public static class UpdateRule {

			/**
			 * 渠道
			 */
			private List<String> channels;

			/**
			 * 默认更新方式 {@link UpdateTypeEnum}
			 */
			private Integer defaultUpdateType;

			/**
			 * 额外更新方式(覆盖默认更新方式)
			 */
			private List<AdditionUpdateRule> additions;
		}

		@Data
		public static class AdditionUpdateRule {

			/**
			 * 开始版本号
			 */
			private String beginVersion;

			/**
			 * 结束版本号
			 */
			private String endVersion;

			/**
			 * 通用更新方式 {@link UpdateTypeEnum}
			 */
			private Integer updateType;
		}
	}
}
