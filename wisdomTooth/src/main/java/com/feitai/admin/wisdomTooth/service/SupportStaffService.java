/**
 * @author 
 * @version 1.0
 * @since  2018-08-10 15:17:49
 * @desc SupportStaff
 */

package com.feitai.admin.wisdomTooth.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.wisdomTooth.model.SupportStaff;
import com.feitai.utils.digest.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Component
@Transactional
@Slf4j
public class SupportStaffService extends ClassPrefixDynamicSupportService<SupportStaff> {

	@Value("${sysNum}")
	private String sysNum;


	public boolean checkHaveSupport(String email, String sign) {
		Example example = Example.builder(SupportStaff.class).andWhere(Sqls.custom().andEqualTo("email",email)).build();
		SupportStaff supportStaff = this.mapper.selectOneByExample(example);
		if(supportStaff!=null&&supportStaff.getEnable()==1){
			final String md5 = MD5Utils.md5((sysNum + supportStaff.getEmail()));
			if(md5.equals(sign)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
}
