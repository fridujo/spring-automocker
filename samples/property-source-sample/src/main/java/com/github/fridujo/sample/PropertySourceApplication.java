package com.github.fridujo.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan
@PropertySource("classpath:appli.properties")
public class PropertySourceApplication {

    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        // Necessary to properly inject java.util.Optional values
        configurer.setNullValue("");
        return configurer;
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(PropertySourceApplication.class, args)) {
            TextService service = context.getBean(TextService.class);

            System.out.println(service.getLiteralText());
        }
    }
}
