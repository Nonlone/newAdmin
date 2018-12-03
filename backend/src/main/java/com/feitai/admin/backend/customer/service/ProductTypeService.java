/**
 * @author 
 * @version 1.0
 * @since  2018-08-06 15:02:35
 * @desc 产品类型表
 */

package com.feitai.admin.backend.customer.service;

import com.feitai.admin.backend.customer.entity.ProductType;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class ProductTypeService extends ClassPrefixDynamicSupportService<ProductType> {

}
