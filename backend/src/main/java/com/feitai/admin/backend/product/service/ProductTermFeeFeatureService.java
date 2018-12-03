/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 09:44:15
 * @desc 产品期限费用特性
 */

package com.feitai.admin.backend.product.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.product.model.ProductTermFeeFeature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
public class ProductTermFeeFeatureService extends ClassPrefixDynamicSupportService<ProductTermFeeFeature> {

	public List<ProductTermFeeFeature> findByProductIdAndTerm(Long productId, Short term){
		Example example = Example.builder(ProductTermFeeFeature.class).andWhere(Sqls.custom().andEqualTo("productId",productId).andEqualTo("term",term)).build();
		return getMapper().selectByExample(example);
	}

}
