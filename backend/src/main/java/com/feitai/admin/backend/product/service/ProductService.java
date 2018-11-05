/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 09:42:04
 * @desc 产品表
 */

package com.feitai.admin.backend.product.service;

import com.feitai.admin.backend.product.entity.ProductMore;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.product.mapper.ProductMapper;
import com.feitai.jieya.server.dao.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  ProductService extends DynamitSupportService<ProductMore> {

    public ProductMore getProduct (Object id){
        return getMapper().selectByPrimaryKey(id);
    }

}
