package com.feitai.admin.web.configuration;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.feitai.base.mybatis.MultipleDataSource;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceInterceptor;
import com.feitai.base.mybatis.interceptor.ClassPrefixMultiDataSourceSelector;
import com.feitai.base.mybatis.interceptor.ConnectionSignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class DataSourceConfiguration implements EnvironmentAware {

    private static final String BEAN_CLASS_PREFIX_MAP = "classPrefixMap";

    private static final String ADMIN_DATASOURCE = "admin";

    private static final String DEFAULT_ADMIN_PACKAGE = "com.feitai.admin";

    private static final String BACKEND_DATASOURCE = "backend";

    private static final String ORDER_CENTER_DATASOURCE = "orderCenter";

    private static final String DEFAULT_ADMIN_PACKAGE_PREFIX = DEFAULT_ADMIN_PACKAGE + ".";

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
        Map<String, String> classPrefixMap = new HashMap<String, String>();
        classPrefixMap.put(DEFAULT_ADMIN_PACKAGE, ADMIN_DATASOURCE);
        classPrefixMap.putAll(getClassPrefixMap(
                //  服务端数据源
                BACKEND_DATASOURCE
        ));
        classPrefixMap.put("com.feitai.jieya.server.dao", BACKEND_DATASOURCE);
        classPrefixMap.put("com.feitai.admin.backend.creditdata", ORDER_CENTER_DATASOURCE);
        return classPrefixMap;
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
            classPrefixMap.put(DEFAULT_ADMIN_PACKAGE_PREFIX + key, key);
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
                if (multipleDataSourceMap.containsKey(ADMIN_DATASOURCE)) {
                    return new MultipleDataSource(multipleDataSourceMap, (DataSource) multipleDataSourceMap.get(ADMIN_DATASOURCE));
                } else {
                    Collection<Object> dataSourceSets = multipleDataSourceMap.values();
                    if (!CollectionUtils.isEmpty(dataSourceSets)) {
                        return new MultipleDataSource(multipleDataSourceMap, (DataSource) dataSourceSets.toArray()[0]);
                    }
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
    public ClassPrefixMultiDataSourceInterceptor classPrefixMultiDataSourceInterceptor(@Autowired MultipleDataSource multipleDataSource, @Qualifier(BEAN_CLASS_PREFIX_MAP) Map<String, String> classPrefixMap) throws SQLException {
        ConcurrentHashMap<String, ConnectionSignature> connectionSignatureMap = new ConcurrentHashMap<>();
        Set<String> dataSourceKeySet = multipleDataSource.getDataSourceKeySet();
        for (String key : dataSourceKeySet) {
            connectionSignatureMap.put(key, new ConnectionSignature(multipleDataSource.getDataSouce(key).getConnection()));
        }
        return new ClassPrefixMultiDataSourceInterceptor(multipleDataSource, connectionSignatureMap, classPrefixMap);
    }

    /**
     * 类前缀数据源修改拦截器
     *
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
