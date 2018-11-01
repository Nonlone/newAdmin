package com.feitai.admin.system.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.vo.TreeItem;
import com.feitai.admin.system.model.Resource;
import com.feitai.base.mybatis.ManyAnnotationFieldWalkProcessor;
import com.feitai.base.mybatis.OneAnnotationFieldWalkProcessor;
import com.feitai.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ResourceService extends DynamitSupportService<Resource> {


    public List<TreeItem> getRsourceList(){
        Example example = Example.builder(Resource.class).andWhere(Sqls.custom().andEqualTo("level",1)).build();
        List<Resource> resources = getMapper().selectByExample(example);
        List<Resource> modelList = new ArrayList<>();
       walkProcessCollection(modelList);

       //TODO 迁移到Controller
        List<TreeItem> trees = new ArrayList<TreeItem>(resources.size());
        for(Resource resource : modelList){
            trees.add(this.resourceToTree(resource));
        }
        return trees;
    }


    private TreeItem resourceToTree(Resource resource){
        TreeItem tree = new TreeItem();
        tree.setId(resource.getId());
        if(resource.getParent()!=null){
            tree.setParentId(resource.getParent().getId());
        }
        tree.setText(resource.getName());
        tree.setLevel(resource.getLevel());
        tree.setValue(resource.getPermissionIds());
        List<Resource> list = resource.getResources();
        if(list!=null && list.size()>0){
            Iterator<Resource> it = resource.getResources().iterator();
            List<TreeItem> children = new ArrayList<TreeItem>(list.size());
            while(it.hasNext()){
                children.add(this.resourceToTree(it.next()));
            }
            tree.setChildren(children);
        }
        return tree;
    }
}
