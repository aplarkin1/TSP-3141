package com.gpsworkers.gathr.mongo.groups;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.mongo.message.CommunicationNetwork;
import com.gpsworkers.gathr.mongo.users.User;

@Document(collection="group")
public class Group {
	
	@Id
	private String name;
	
	@DBRef
	private ArrayList<User> users;
	
	@DBRef
	private CommunicationNetwork commsNetwork;

	public Group(String name) {
		this.name = name;
		users = new ArrayList<User>();
		commsNetwork = new CommunicationNetwork();
	}

}
