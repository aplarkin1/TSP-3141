package com.gpsworkers.gathr.mongo;

import org.springframework.data.annotation.Id;


public class User {

    @Id
    public String email;

    public String firstName, lastName;

    public double location;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    @Override
    public String toString() {
        return String.format(
                "User[ email='%s', firstName='%s', lastName='%s', location='%s']",
                email, firstName, lastName, location);
    }


}
