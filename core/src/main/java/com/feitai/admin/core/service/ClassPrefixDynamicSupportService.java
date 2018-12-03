package com.feitai.admin.core.service;

import com.feitai.base.mybatis.MultipleDataSource;
import com.feitai.base.mybatis.SqlMapper;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceSelector;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;

/**
 * 前缀动态数据源服务类
 *
 * @param <T>
 */
public abstract class ClassPrefixDynamicSupportService<T> extends DynamitSupportService<T> {

    @Autowired
    private ClassPrefixMultiDataSourceSelector classPrefixMultiDataSourceSelector;

    @Autowired
    private MultipleDataSource multipleDataSource;

    @Autowired
    private MybatisAutoConfiguration mybatisAutoConfiguration;

    private DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();


    @Override
    public void init() {
        this.sqlMapper = doGetSqlMapper();
    }

    @Override
    public SqlMapper doGetSqlMapper() {
            try {
                DataSource dataSource = classPrefixMultiDataSourceSelector.getDataSource(getMapperClass(getMapper()));
                MybatisProperties properties = (MybatisProperties) com.feitai.utils.ObjectUtils.getFieldValue(mybatisAutoConfiguration, "properties");
                ResourceLoader resourceLoader = (ResourceLoader) com.feitai.utils.ObjectUtils.getFieldValue(mybatisAutoConfiguration, "resourceLoader");
                List<ConfigurationCustomizer> configurationCustomizers = (List) com.feitai.utils.ObjectUtils.getFieldValue(mybatisAutoConfiguration, "configurationCustomizers");
                Interceptor[] interceptors = (Interceptor[]) com.feitai.utils.ObjectUtils.getFieldValue(mybatisAutoConfiguration, "interceptors");
                SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(dataSource, properties, resourceLoader, configurationCustomizers, interceptors);
                return new SqlMapper(new SqlSessionTemplate(sqlSessionFactory));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


        public SqlSessionFactory buildSqlSessionFactory(DataSource dataSource,
                                                    MybatisProperties properties,
                                                    ResourceLoader resourceLoader,
                                                    List<ConfigurationCustomizer> configurationCustomizers,
                                                    Interceptor[] interceptors) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
            factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        }

        org.apache.ibatis.session.Configuration configuration = properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }

        if (configuration != null && !CollectionUtils.isEmpty(configurationCustomizers)) {
            Iterator var4 = configurationCustomizers.iterator();

            while (var4.hasNext()) {
                ConfigurationCustomizer customizer = (ConfigurationCustomizer) var4.next();
                customizer.customize(configuration);
            }
        }
        if (properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(properties.getConfigurationProperties());
        }

        if (!ObjectUtils.isEmpty(interceptors)) {
            factory.setPlugins(interceptors);
        }

        if (this.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(this.databaseIdProvider);
        }

        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }

        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }

        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }

        return factory.getObject();
    }


}
