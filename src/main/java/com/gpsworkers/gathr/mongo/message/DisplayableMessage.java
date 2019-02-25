package com.gpsworkers.gathr.mongo.message;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class DisplayableMessage {
	
	private String messageContent;
	private String username;
	private Date postDate;
	
	public DisplayableMessage(String messageContent, String username, Date postDate) {
		this.messageContent = messageContent;
		this.username = username;
		this.postDate = postDate;
	}
	
	
}
