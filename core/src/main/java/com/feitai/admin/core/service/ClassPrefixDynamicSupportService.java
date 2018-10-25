package com.feitai.admin.core.service;

import com.feitai.base.mybatis.SqlMapper;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceSelector;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 前缀动态数据源服务类
 *
 * @param <T>
 */
public abstract class ClassPrefixDynamicSupportService<T> extends DynamitSupportService<T> {

    @Autowired
    private ClassPrefixMultiDataSourceSelector classPrefixMultiDataSourceSelector;

    private DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();


    @Override
    public void init() {
        DataSource dataSource = classPrefixMultiDataSourceSelector.getDataSource(getMapperClass(getMapper()));
        this.sqlMapper = getSqlMapper(dataSource);
    }

    @Override
    public SqlMapper doGetMapper() {
        DataSource dataSource = classPrefixMultiDataSourceSelector.getDataSource(getMapper().getClass());
        return getSqlMapper(dataSource);
    }


    private SqlMapper getSqlMapper(DataSource dataSource) {
        try {
            return new SqlMapper(sqlSession, dataSource, databaseIdProvider);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
