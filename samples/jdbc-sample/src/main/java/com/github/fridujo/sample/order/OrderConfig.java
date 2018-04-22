package com.github.fridujo.sample.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "orderEntityManagerFactory", transactionManagerRef = "orderTransactionManager")
class OrderConfig {

    @Bean
    PlatformTransactionManager orderTransactionManager(@Qualifier("order") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    @Qualifier("order")
    LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(@Qualifier("order") DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan(OrderConfig.class.getPackage().getName());

        return factoryBean;
    }

    @Bean
    @ConfigurationProperties("order.datasource")
    @Qualifier("order")
    DataSourceProperties orderDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("order")
    DataSource orderDataSource(@Qualifier("order") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }
}
