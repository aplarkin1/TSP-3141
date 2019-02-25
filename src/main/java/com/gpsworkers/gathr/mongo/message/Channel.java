package com.gpsworkers.gathr.mongo.message;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

public class Channel {
	
	@Id
	private String name;
	
	private ArrayList<Message> messages;

	public Channel(String name) {
		this.name = name;
		this.messages = new ArrayList<>();
	}
	
	public void postMessage(Message message) {
		messages.add(message);
	}
	
	public void deleteMessage(int index) {
		messages.remove(index);
	}

}
