package com.gpsworkers.gathr.mongo.groups;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.mongo.users.User;

@Document(collection="group")
public class Group {
	
	@DBRef
	private ArrayList<User> users;
	
	
	
	@Id
	private String name;
	
	
	
	
	
	
}
