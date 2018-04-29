package com.github.fridujo.sample;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class GraphiteApplication implements CommandLineRunner {

    private final Counter runCounter;

    public GraphiteApplication(MeterRegistry meterRegistry) {
        this.runCounter = meterRegistry.counter("run");
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GraphiteApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        Optional.of(args)
            .filter(a -> a.length > 0)
            .map(a -> a[0])
            .flatMap(this::tryParse)
            .ifPresent(incrementFactor -> runCounter.increment(incrementFactor));
    }

    private Optional<Double> tryParse(String string) {
        try {
            return Optional.of(Double.parseDouble(string));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
