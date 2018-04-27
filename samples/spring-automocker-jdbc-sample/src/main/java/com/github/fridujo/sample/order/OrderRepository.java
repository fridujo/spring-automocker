package com.github.fridujo.sample.order;

import com.github.fridujo.sample.customer.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByCustomer(Customer.CustomerId id);
}
