package com.gpsworkers.gathr.mongo.groups;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.mongo.message.CommunicationNetwork;
import com.gpsworkers.gathr.mongo.users.User;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class specifies the format of a Group entity in MongoDB
 * This will someday be put into the server when we build a MongoDB repo to store users.
 */
@Document(collection="group")
public class Group {
	
	@Id
	private String name;
	
	@DBRef
	private ArrayList<User> users;
	
	//@DBRef
	//private CommunicationNetwork commsNetwork;
	
	/**
	 * This constructor allows for the construction of a group with a specific name.
	 * @param name of the group
	 */
	public Group(String name) {
		this.name = name;
		users = new ArrayList<User>();
		//commsNetwork = new CommunicationNetwork();
	}

}
