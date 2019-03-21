package com.gpsworkers.gathr.mongo.communication;

import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.exceptions.ChannelAlreadyExistsException;
import com.gpsworkers.gathr.exceptions.ChannelDoesntExistException;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class contains prototype code for a flexible communication network.
 */
@Document(collection="commsnetworks")
public class CommunicationNetwork {
	private HashMap<String, Channel> channels;

	/**
	 * Default constructor
	 */
	public CommunicationNetwork() {
		this.channels = new HashMap<>();
	}
	
	/**
	 * This method allows for creation of an additional channel in this communication network instance
	 * @param name is the name of channel to be created
	 * @throws ChannelAlreadyExistsException if a channel with the given name already exists
	 * @throws Exception if the name given is null or an empty string
	 */
	public void addChannel(String name) throws ChannelAlreadyExistsException, Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot create a channel with a null or empty String");
		}
		
		if(channels.containsKey(name)) {
			throw new ChannelAlreadyExistsException("Channel already xists!");
		}
		
		channels.put(name, new Channel(name));
	}
	
	/**
	 * This method allows for the retrieval of a channel created on the this communication network instance
	 * @param name is the name of the channel to b retrieved
	 * @return the channel queried for
	 * @throws ChannelDoesntExistException if the queried channel doesn't exist.
	 * @throws Exception of the name of the channel to be retrieved is null or an empty string
	 */
	public Channel getChannel(String name) throws ChannelDoesntExistException, Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot retrieve a channel with a name of null or empty String");
		}
		
		if(channels.containsKey(name) == false) {
			throw new ChannelDoesntExistException("Channel doesn't exist!");
		}
		
		return channels.get(name);
	}
	
	public HashMap<String, Channel> getChannels() {
		return channels;
	}
	
	public void removeChannel(String name) {
		channels.remove(name);
	}

}
