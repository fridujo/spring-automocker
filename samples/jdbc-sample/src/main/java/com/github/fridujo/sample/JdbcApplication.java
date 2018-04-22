package com.github.fridujo.sample;

import com.github.fridujo.sample.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class})
@EnableTransactionManagement
@EnableConfigurationProperties
public class JdbcApplication {

    @Autowired
    DataInitializer initializer;

    public static void main(String[] args) {
        SpringApplication.run(JdbcApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Customer.CustomerId customerId = initializer.initializeCustomer();
        initializer.initializeOrder(customerId);
    }
}
