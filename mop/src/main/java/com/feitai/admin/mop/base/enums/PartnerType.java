package com.feitai.admin.mop.base.enums;
/**
 * 合伙人账号类型
 * @author lingc
 *  
 */
public enum PartnerType {
	   /**
	    * 个人
	    */
	   PERSONAL(1),
	   /**
	    * 企业
	    */
	   CORPORATE(2);
       int type;
	   private PartnerType(int type) {
		this.type=type;
	}
	   
	   public int getType(){
		   return type;
	   }
}
