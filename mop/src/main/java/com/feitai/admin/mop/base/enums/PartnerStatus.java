package com.feitai.admin.mop.base.enums;
/**
 * 账号状态
 * @author lingc
 *
 */
public enum PartnerStatus {
	   /**
	    * 激活
	    */
	   ACTIVE(1),
	   /**
	    * 冻结
	    */
	   FROZEN(2);
	   int status;
	   PartnerStatus(int status){
		   this.status=status;
	   }
       public int getStatus(){
    	   return status;
       }
}
