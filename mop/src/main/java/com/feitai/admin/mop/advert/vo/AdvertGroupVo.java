package com.feitai.admin.mop.advert.vo;

import com.feitai.admin.mop.advert.dao.entity.AdvertGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class AdvertGroupVo extends AdvertGroup {

    private String groupIds;

    public static AdvertGroupVo build(AdvertGroup advertGroup, List<Long> groupIdsList) {
        AdvertGroupVo advertGroupVo = new AdvertGroupVo();
        BeanUtils.copyProperties(advertGroup, advertGroupVo);
        if (!groupIdsList.isEmpty()) {
            advertGroupVo.setGroupIds(StringUtils.join(groupIdsList, ","));
        }
        return advertGroupVo;
    }
}
