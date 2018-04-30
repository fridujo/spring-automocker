package com.github.fridujo.sample.jdbc.customer;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Customer {
    private final String firstname;
    private final String lastname;
    @Id
    @GeneratedValue
    private Long id;

    public Customer() {
        firstname = null;
        lastname = null;
    }

    public Customer(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public CustomerId getId() {
        return new CustomerId(id);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Embeddable
    public static class CustomerId implements Serializable {
        private final Long customerId;

        CustomerId() {
            this.customerId = null;
        }

        CustomerId(Long id) {
            this.customerId = id;
        }

        public Long getCustomerId() {
            return customerId;
        }
    }
}
