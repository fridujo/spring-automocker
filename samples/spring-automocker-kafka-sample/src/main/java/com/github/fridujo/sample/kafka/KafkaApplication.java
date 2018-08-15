package com.github.fridujo.sample.kafka;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaApplication {

    static final String APP_TOPIC = "test-topic";

    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(KafkaApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
