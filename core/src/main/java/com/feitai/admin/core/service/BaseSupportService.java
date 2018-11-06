package com.feitai.admin.core.service;

import com.feitai.utils.ObjectUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxy;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Fn;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
public abstract class BaseSupportService<T> {

    @Autowired
    protected Mapper<T> mapper;


    /**
     * 擦除获取类
     */
    protected Class<T> classOfT;


    protected BaseSupportService() {
        this.classOfT = ObjectUtils.getGenericClass(getClass());
    }


    /***
     * 单表查询所有
     * @return
     */
    public List<T> findAll() {
        return mapper.selectAll();
    }


    /***
     * 根据id获取单个实体
     * @param id
     * @return
     */
    public T findOne(Object id) {
        T t = mapper.selectByPrimaryKey(id);
        return t;
    }

    /***
     * 根据id数组批量删除实体
     * @param ids
     */
    public void delete(Object[] ids) {
        for (Object id : ids) {
            deleteByPrimaryKey(id);
        }
    }


    /**
     * 插入单个实体
     *
     * @param entity 实体
     * @return 返回保存的实体
     */
    public T save(T entity) {
        try{
            if (mapper.insert(entity) > 0) {
                return entity;
            }
        }catch (Exception e){
            log.warn("save fail,try to update because:{}",e.getMessage());
            mapper.updateByPrimaryKey(entity);
            return entity;
        }
        return null;
    }

    /**
     * 插入多个实体
     *
     * @param entities
     * @return
     */
    public Iterable<T> insert(Iterable<T> entities) {
        List<T> result = new ArrayList<T>();
        for (T t : entities) {
            T r = save(t);
            if (r != null) {
                result.add(r);
            }
        }
        return result;
    }


    /**
     * 插入单个实体
     *
     * @param entity 实体
     * @return 返回保存的实体
     */
    public T updateByPrimaryKey(T entity) {
        if (mapper.updateByPrimaryKey(entity) > 0) {
            return entity;
        }
        return null;
    }

    /**
     * 插入多个实体
     *
     * @param entities
     * @return
     */
    public Iterable<T> updateByPrimaryKey(Iterable<T> entities) {
        List<T> result = new ArrayList<T>();
        for (T t : entities) {
            T r = updateByPrimaryKey(t);
            if (r != null) {
                result.add(r);
            }
        }
        return result;
    }


    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public int deleteByPrimaryKey(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID删除
     *
     * @param ids 实体
     */
    public int deleteByPrimaryKey(Object[] ids) {
        int count = 0;
        for (Object id : ids) {
            count += mapper.deleteByPrimaryKey(id);
        }
        return count;
    }


    /**
     * 删除实体
     *
     * @param entity 实体
     */
    public int delete(T entity) {
        return mapper.delete(entity);
    }


    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    public long count() {
        return mapper.selectCountByExample(Example.builder(classOfT));
    }


    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    public boolean exists(Fn<T, Object> fn, Object id) {
        return mapper.selectOneByExample(Example.builder(classOfT)
                .where(WeekendSqls.<T>custom().andEqualTo(fn, id)).build()) != null;
    }


    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    public boolean exists(Object id) {
        return mapper.selectByPrimaryKey(id) != null;
    }


    /**
     * 获取MapperClass类
     * @param mapper
     * @return
     */
    protected Class<?> getMapperClass(Mapper<?> mapper) {
        MapperProxy mapperProxy = (MapperProxy) ObjectUtils.getFieldValue(mapper, "h");
        if(!Objects.isNull(mapperProxy)){
            return (Class<?>)ObjectUtils.getFieldValue(mapperProxy,"mapperInterface");
        }
        return mapper.getClass();
    }

}
