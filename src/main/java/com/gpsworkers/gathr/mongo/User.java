package com.gpsworkers.gathr.mongo;

import org.springframework.data.annotation.Id;


public class User {

    @Id
    public String email;
    
    public String firstName, lastName;
    
    public double x,y,z;
    
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    
}