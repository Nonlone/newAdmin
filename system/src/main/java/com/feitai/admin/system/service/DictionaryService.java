package com.feitai.admin.system.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.SearchParams;
import com.feitai.admin.core.service.SelectMultiTable;
import com.feitai.admin.core.service.Sort;
import com.feitai.admin.system.model.Dictionary;
import com.feitai.base.mybatis.SqlMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class DictionaryService extends ClassPrefixDynamicSupportService<Dictionary> {

    @Autowired
    protected SqlSession sqlSession;


    public Map<String, String> searchEnum(List<SearchParams> searchParams) {
        SqlMapper sqlMapper = new SqlMapper(sqlSession);
        String sql = SelectMultiTable.builder(Dictionary.class).buildSqlString() + buildSqlWhereCondition(searchParams);
        List<Dictionary> list = sqlMapper.selectList(sql, Dictionary.class);
        Map<String, String> map = new HashMap<String, String>(list.size());
        for (Dictionary dic : list) {
            map.put(dic.getValue(), dic.getName());
        }
        return map;
    }

    public List<Dictionary> searchListItem(List<SearchParams> searchParamsList, Sort sort, String headText, String headValue) {
        String sql = SelectMultiTable.builder(Dictionary.class).buildSqlString() + buildSqlWhereCondition(searchParamsList);
        sql += (" " + sort.toString());
        SqlMapper sqlMapper = getSqlMapper();
        List<Dictionary> list = sqlMapper.selectList(sql, Dictionary.class);
        return list;
    }


}
