package com.feitai.admin.web.configuration;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.feitai.base.mybatis.MultipleDataSource;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceInterceptor;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceSelector;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class DataSourceConfiguration implements EnvironmentAware {

    private static final String BEAN_CLASS_PREFIX_MAP = "classPrefixMap";

    private static final String DEFAULT_CLASS_PREFIX = "com.feitai.";

    private static final String DEFAULT_PROPERTIES_PREFIX = "mysql";

    private static final String DEFAULT_CONNECTION_PROPERTIES = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 构造类前缀映射Map
     *
     * @return
     */
    @Bean(name = BEAN_CLASS_PREFIX_MAP)
    public Map<String, String> classPrefixMap() {
        return getClassPrefixMap(
                //  服务端数据源
                "backend",
                // Admin 数据源
                "admin"
        );
    }

    /**
     * 构造Map工具方法
     *
     * @param keys
     * @return
     */
    private Map<String, String> getClassPrefixMap(String... keys) {
        Map<String, String> classPrefixMap = new HashMap<>();
        for (String key : keys) {
            classPrefixMap.put(DEFAULT_CLASS_PREFIX + key, key);
        }
        return classPrefixMap;
    }


    /**
     * 构造多数据源
     *
     * @param classPrefixMap
     * @return
     */
    @Bean
    public MultipleDataSource multipleDataSource(@Qualifier(BEAN_CLASS_PREFIX_MAP) Map<String, String> classPrefixMap) {
        Map<Object, Object> multipleDataSourceMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(classPrefixMap)) {
            try {
                for (Map.Entry<String, String> entry : classPrefixMap.entrySet()) {
                    DataSource dataSource = getDataSource(entry.getValue(), DEFAULT_PROPERTIES_PREFIX, DEFAULT_CONNECTION_PROPERTIES);
                    multipleDataSourceMap.put(entry.getValue(), dataSource);
                }
                Collection<Object> dataSourceSets = multipleDataSourceMap.values();
                if (!CollectionUtils.isEmpty(dataSourceSets)) {
                    return new MultipleDataSource(multipleDataSourceMap, (DataSource) dataSourceSets.toArray()[0]);
                }
            } catch (Exception e) {
                log.error(String.format("multipleDataSource error %s", e.getMessage()), e);
            }
        }
        return null;
    }

    /**
     * 类前缀数据源修改拦截器
     *
     * @param multipleDataSource
     * @param classPrefixMap
     * @return
     */
    @Bean
    public ClassPrefixMultiDataSourceInterceptor classPrefixMultiDataSourceInterceptor(@Autowired MultipleDataSource multipleDataSource, @Qualifier(BEAN_CLASS_PREFIX_MAP) Map<String, String> classPrefixMap) {
        return new ClassPrefixMultiDataSourceInterceptor(multipleDataSource, classPrefixMap);
    }

    /**
     * 类前缀数据源修改拦截器
     * @param classPrefixMultiDataSourceInterceptor
     * @param multipleDataSource
     * @return
     */
    @Bean
    public ClassPrefixMultiDataSourceSelector classPrefixMultiDataSourceSelector(@Autowired ClassPrefixMultiDataSourceInterceptor classPrefixMultiDataSourceInterceptor, @Autowired MultipleDataSource multipleDataSource) {
        return new ClassPrefixMultiDataSourceSelector(classPrefixMultiDataSourceInterceptor, multipleDataSource);
    }

    /**
     * MyBatis 拦截器
     *
     * @param classPrefixMultiDataSourceInterceptor
     * @return
     */
    @Bean
    public Interceptor[] mybatisInterceptors(@Autowired ClassPrefixMultiDataSourceInterceptor classPrefixMultiDataSourceInterceptor) {
        return new Interceptor[]{
                classPrefixMultiDataSourceInterceptor
        };
    }


    /**
     * 获取数据源
     *
     * @param propertiesPrefix
     * @param connectionProperties
     * @return
     * @throws Exception
     */
    private DataSource getDataSource(String propertiesPrefix, String connectionProperties) throws Exception {
        return getDataSource(propertiesPrefix, propertiesPrefix, connectionProperties);
    }

    /**
     * 获取数据源
     *
     * @param propertiesPrefix
     * @param defaultPropertiesPrefix
     * @param connectionProperties
     * @return
     * @throws Exception
     */
    private DataSource getDataSource(String propertiesPrefix, String defaultPropertiesPrefix, String connectionProperties) throws Exception {
        Properties props = new Properties();
        // 构建基础信息
        props.put(DruidDataSourceFactory.PROP_URL, environment.getProperty(propertiesPrefix + ".url"));
        props.put(DruidDataSourceFactory.PROP_USERNAME, environment.getProperty(propertiesPrefix + ".username"));
        props.put(DruidDataSourceFactory.PROP_PASSWORD, environment.getProperty(propertiesPrefix + ".password"));

        props.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, environment.getProperty(defaultPropertiesPrefix + ".driver"));
        // Config optimize
        props.put(DruidDataSourceFactory.PROP_VALIDATIONQUERY, environment.getProperty(defaultPropertiesPrefix + ".validationQuery"));
        props.put(DruidDataSourceFactory.PROP_MAXACTIVE, environment.getProperty(defaultPropertiesPrefix + ".poolMaximumActiveConnections"));
        props.put(DruidDataSourceFactory.PROP_MAXIDLE, environment.getProperty(defaultPropertiesPrefix + ".poolMaximumIdleConnections"));
        props.put(DruidDataSourceFactory.PROP_INITIALSIZE, environment.getProperty(defaultPropertiesPrefix + ".initialSize"));
        props.put(DruidDataSourceFactory.PROP_FILTERS, environment.getProperty(defaultPropertiesPrefix + ".filters"));
        props.put(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS, environment.getProperty(defaultPropertiesPrefix + ".timeBetweenEvictionRunsMillis"));
        props.put(DruidDataSourceFactory.PROP_MINEVICTABLEIDLETIMEMILLIS, environment.getProperty(defaultPropertiesPrefix + ".minEvictableIdleTimeMillis"));
        props.put(DruidDataSourceFactory.PROP_TESTONBORROW, environment.getProperty(defaultPropertiesPrefix + ".testOnBorrow"));
        props.put(DruidDataSourceFactory.PROP_TESTONRETURN, environment.getProperty(defaultPropertiesPrefix + ".testOnReturn"));
        props.put(DruidDataSourceFactory.PROP_POOLPREPAREDSTATEMENTS, environment.getProperty(defaultPropertiesPrefix + ".poolPreparedStatements"));
        props.put(DruidDataSourceFactory.PROP_MAXOPENPREPAREDSTATEMENTS, environment.getProperty(defaultPropertiesPrefix + ".maxPoolPreparedStatementPerConnectionSize"));
        props.put(DruidDataSourceFactory.PROP_CONNECTIONPROPERTIES, connectionProperties);
        return DruidDataSourceFactory.createDataSource(props);
    }
}
