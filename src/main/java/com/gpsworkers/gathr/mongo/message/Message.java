package com.gpsworkers.gathr.mongo.message;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Message {
	@Id
	private long messageIndex;
	
	private String messageContent;
	private String userWhoSentMessage;
	
	private Date postDate;
	
	public Message(long messageIndex, String messageContent, String userWhoSentMessage) {
		this.messageIndex = messageIndex;
		this.messageContent = messageContent;
		this.userWhoSentMessage = userWhoSentMessage;
		postDate = new Date();
	}
	
	
	
}
