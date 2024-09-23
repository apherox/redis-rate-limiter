package com.ratelimiter.util;

import com.ratelimiter.config.ConfigLoader;
import com.ratelimiter.dao.RequestLogMapper;
import com.ratelimiter.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

@Slf4j
public class MyBatisUtil {

    private MyBatisUtil() {
    }

    private static final SqlSessionFactory sqlSessionFactory = initializeSessionFactory();
    private static SqlSession sqlSession;

    private static SqlSessionFactory initializeSessionFactory() {
        try {

            var configuration = getConfiguration();

            // Register the mappers
            configuration.addMapper(UserMapper.class);
            configuration.addMapper(RequestLogMapper.class);

            return new SqlSessionFactoryBuilder().build(configuration);

        } catch (Exception e) {
            log.error("Error initializing SqlSessionFactory: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize SqlSessionFactory", e);
        }
    }

    private static Configuration getConfiguration() {
        var dataSource = new PooledDataSource();
        dataSource.setDriver(ConfigLoader.getDatabaseDriver());
        dataSource.setUrl(ConfigLoader.getDatabaseUrl());
        dataSource.setPoolMaximumActiveConnections(10);
        dataSource.setPoolMaximumIdleConnections(5);

        var transactionFactory = new JdbcTransactionFactory();

        var environment = new Environment("development", transactionFactory, dataSource);

        return new Configuration(environment);
    }

    public static synchronized SqlSession getSession() {
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession();
        }
        return sqlSession;
    }
}
