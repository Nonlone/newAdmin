/**
 * @author 
 * @version 1.0
 * @since  2018-07-25 09:42:04
 * @desc 产品表
 */

package com.feitai.admin.backend.product.entity;

import com.feitai.jieya.server.dao.product.model.Product;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name = "t_product")
@Data
public class ProductMore extends Product {

    @Transient
    private String icon;
//    @Transient
//    private List<RatePlanMore> ratePlans;


}

