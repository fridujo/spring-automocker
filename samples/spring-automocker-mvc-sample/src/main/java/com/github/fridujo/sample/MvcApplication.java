package com.github.fridujo.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sample web application.<br>
 * Run {@link #main(String[])} to launch.
 */
@SpringBootApplication
@RestController
public class MvcApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MvcApplication.class);

    private final Map<Integer, Customer> database = new LinkedHashMap<>();
    private final AtomicInteger sequenceGenerator = new AtomicInteger();

    public MvcApplication() {
        LOGGER.info("Initiating web server");
    }

    public static void main(String[] args) {
        SpringApplication.run(MvcApplication.class);
    }

    @RequestMapping("/create_user")
    Customer createUser(@RequestParam("firstName") String firstName,
                        @RequestParam("lastName") String lastName) {
        int id = sequenceGenerator.incrementAndGet();
        Customer newCustomer = new Customer(id, firstName, lastName);
        database.put(id, newCustomer);
        return newCustomer;
    }

    @RequestMapping("/list_users")
    Collection<Customer> listUsers() {
        return database.values();
    }
}
