package com.terra.framework.strata.config.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.google.common.collect.Lists;
import com.terra.framework.strata.exception.ValhallaDataVersionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

import static com.baomidou.mybatisplus.annotation.DbType.MYSQL;

/**
 * @author ywt
 * @description
 * @date 2022年12月24日 22:05
 */
@ConditionalOnClass(DataSourceProperties.class)
@ConditionalOnProperty(prefix = "spring.datasource.druid", name = "url")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class TerraDruidAutoConfiguration {

    @Bean("terra-datasource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean("terra-transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("terra-datasource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    @Bean("terra-jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("terra-datasource") DataSource datasource) {
        return new JdbcTemplate(datasource);
    }

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(MYSQL);
        paginationInnerInterceptor.setMaxLimit(-1L);
        paginationInnerInterceptor.setOptimizeJoin(true);
        return paginationInnerInterceptor;
    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        optimisticLockerInnerInterceptor.setException(new ValhallaDataVersionException());
        return optimisticLockerInnerInterceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(Lists.newArrayList(paginationInnerInterceptor(), optimisticLockerInnerInterceptor()));
        return mybatisPlusInterceptor;
    }
}
