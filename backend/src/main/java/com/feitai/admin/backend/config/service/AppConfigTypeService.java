/**
 * @author
 * @version 1.0
 * @desc AppConfigType
 * @since 2018-08-02 11:40:20
 */

package com.feitai.admin.backend.config.service;

import com.feitai.admin.backend.config.entity.AppConfigType;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.vo.ListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AppConfigTypeService extends DynamitSupportService<AppConfigType> {

    public List<ListItem> findAllItems(String sql) {
        List<AppConfigType> list = this.findAll(sql);
        List<ListItem> items = new ArrayList<ListItem>(list.size());
        for (AppConfigType appConfigType : list) {
            ListItem item = new ListItem(appConfigType.getName(),
                    String.valueOf(appConfigType.getTypeCode()));
            items.add(item);
        }
        return items;
    }

}
