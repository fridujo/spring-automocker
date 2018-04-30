package com.github.fridujo.sample.jdbc.order;

import com.github.fridujo.sample.jdbc.customer.Customer;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// Needs to be explicitly named as Order is a reserved keyword
@Table(name = "SampleOrder")
public class Order {

    private final Customer.CustomerId customer;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<LineItem> lineItems = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;

    public Order() {
        customer = null;
    }

    public Order(Customer.CustomerId customer) {
        this.customer = customer;
    }

    public void add(LineItem lineItem) {
        Assert.notNull(lineItem, "Line item must not be null!");

        this.lineItems.add(lineItem);
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
}
