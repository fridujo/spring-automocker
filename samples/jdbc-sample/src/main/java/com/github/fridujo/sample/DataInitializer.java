package com.github.fridujo.sample;

import com.github.fridujo.sample.customer.Customer;
import com.github.fridujo.sample.customer.CustomerRepository;
import com.github.fridujo.sample.order.LineItem;
import com.github.fridujo.sample.order.Order;
import com.github.fridujo.sample.order.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
public class DataInitializer {

    private final OrderRepository orders;
    private final CustomerRepository customers;

    public DataInitializer(OrderRepository orders, CustomerRepository customers) {
        this.orders = orders;
        this.customers = customers;
    }

    @Transactional("customerTransactionManager")
    public Customer.CustomerId initializeCustomer() {
        return customers.save(new Customer("Dave", "Matthews"))
            .getId();
    }

    @Transactional("orderTransactionManager")
    public void initializeOrder(Customer.CustomerId customer) {
        Assert.notNull(customer, "Customer identifier must not be null!");

        Order order1 = new Order(customer);
        order1.add(new LineItem("Fender Jag-Stang Guitar"));
        orders.save(order1);

        Order order2 = new Order(customer);
        order2.add(new LineItem("Gene Simmons Axe Bass"));
        orders.save(order2);

    }
}
