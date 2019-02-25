package com.gpsworkers.gathr.mongo.message;


import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.exceptions.ChannelAlreadyExistsException;
import com.gpsworkers.gathr.exceptions.ChannelDoesntExistException;

@Document(collection="commsnetworks")
public class CommunicationNetwork {
	private HashMap<String, Channel> channels;

	public CommunicationNetwork() {
		this.channels = new HashMap<>();
	}
	
	public void addChannel(String name) throws Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot create a channel with a null or empty String");
		}
		
		if(channels.containsKey(name)) {
			throw new ChannelAlreadyExistsException("Channel already xists!");
		}
		
		channels.put(name, new Channel(name));
	}
	
	public Channel getChannel(String name) throws Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot retrieve a channel with a name of null or empty String");
		}
		
		if(channels.containsKey(name) == false) {
			throw new ChannelDoesntExistException("Channel doesn't exist!");
		}
		
		return channels.get(name);
	}
}
