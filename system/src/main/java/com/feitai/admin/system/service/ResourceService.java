package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.system.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class ResourceService extends DynamitSupportService<Resource> {

    public List<Resource> getRsourceList() {
        Example example = Example.builder(Resource.class).andWhere(Sqls.custom().andEqualTo("level", 1)).build();
        List<Resource> resources = this.mapper.selectByExample(example);
        return walkProcessCollection(resources);
    }


}
