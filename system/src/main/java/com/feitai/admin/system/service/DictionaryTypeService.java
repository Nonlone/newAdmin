package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.vo.ListItem;
import com.feitai.admin.system.model.DictionaryType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DictionaryTypeService extends DynamitSupportService<DictionaryType> {

    public List<ListItem> findAllItems() {
        List<DictionaryType> list = this.mapper.selectAll();
        List<ListItem> items = new ArrayList<ListItem>(list.size());
        for (DictionaryType dictionaryType : list) {
            ListItem item = new ListItem(dictionaryType.getName(),
                    String.valueOf(dictionaryType.getId()));
            items.add(item);
        }
        return items;
    }
}
