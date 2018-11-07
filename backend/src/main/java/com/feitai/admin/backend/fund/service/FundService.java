/**
 * @author 
 * @version 1.0
 * @since  2018-08-23 16:29:39
 * @desc 记录资金方主表信息
 */

package com.feitai.admin.backend.fund.service;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.fund.model.Fund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@Slf4j
public class FundService extends ClassPrefixDynamicSupportService<Fund> {

}
