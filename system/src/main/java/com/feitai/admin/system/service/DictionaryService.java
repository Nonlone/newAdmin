package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.SearchParams;
import com.feitai.admin.core.service.SelectMultiTable;
import com.feitai.admin.core.service.Sort;
import com.feitai.admin.system.model.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DictionaryService extends DynamitSupportService<Dictionary> {

    public Map<String, String> searchEnum(List<SearchParams> searchParamsList) {
        String sql = SelectMultiTable.builder(Dictionary.class).buildSqlString() + buildSqlWhereCondition(searchParamsList);
        List<Dictionary> list = sqlMapper.selectList(sql, Dictionary.class);
        Map<String, String> map = new HashMap<String, String>(list.size());
        for (Dictionary dic : list) {
            map.put(dic.getValue(), dic.getName());
        }
        return map;
    }

    public List<Dictionary> searchListItem(List<SearchParams> searchParamsList , Sort sort) {
        String sql = SelectMultiTable.builder(Dictionary.class).buildSqlString() + buildSqlWhereCondition(searchParamsList);
        sql += (" ORDER BY " + sort.toString());
        return sqlMapper.selectList(sql, Dictionary.class);
    }

}
