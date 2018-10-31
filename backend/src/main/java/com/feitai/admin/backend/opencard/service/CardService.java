/**
 * @author
 * @version 1.0
 * @desc 用户持有对应信用产品记录
 * @since 2018-08-06 09:28:35
 */

package com.feitai.admin.backend.opencard.service;

import com.feitai.admin.backend.opencard.entity.CardMore;
import com.feitai.admin.backend.opencard.mapper.CardMoreMapper;
import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.card.mapper.CardMapper;
import com.feitai.jieya.server.dao.card.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;


@Service
@Slf4j
public class CardService extends ClassPrefixDynamicSupportService<CardMore> {

    @Autowired
    private CardMoreMapper cardMoreMapper;

    public CardMore findByUserId(Long userId) {
        Example example = Example.builder(CardMore.class).andWhere(Sqls.custom().andEqualTo("userId", userId)).build();
        return this.cardMoreMapper.selectOneByExample(example);
    }

    public Card findSingleById(Long id) {
        return getMapper().selectByPrimaryKey(id);
    }

}
