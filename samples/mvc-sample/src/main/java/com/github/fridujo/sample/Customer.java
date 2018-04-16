package com.github.fridujo.sample;

public class Customer {

    private final int id;
    private final String firstName;
    private final String lastName;

    public Customer(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format("Customer[firstName='%s', lastName='%s']", firstName, lastName);
    }
}
