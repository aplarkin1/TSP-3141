package com.gpsworkers.gathr.mongo.message;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmpty;

public class Message {
	@Id
	private long messageIndex;
	
	private String messageContent;
	private String userId;
	private Date postDate;
	
	public Message(long messageIndex, String messageContent, String userId) throws MessageUserIdCannotBeEmpty, Exception {
		setMessageIndex(messageIndex);
		setMessageContent(messageContent);
		setUserId(userId);
		postDate = new Date();
	}

	public long getMessageIndex() {
		return messageIndex;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public String getUserId() {
		return userId;
	}

	public Date getPostDate() {
		return postDate;
	}
	
	public void setMessageContent(String messageContent) throws Exception {
		if(messageContent == null || messageContent.isEmpty()) {
			throw new Exception("Message content cannot be null or and empty string");
		}
		this.messageContent = messageContent;
	}

	private void setMessageIndex(long messageIndex) throws Exception {
		if(messageIndex < 0) {
			throw new Exception("Message with messageIndex of less than 0 cannot be created");
		}
		this.messageIndex = messageIndex;
	}

	private void setUserId(String userId) throws MessageUserIdCannotBeEmpty {
		if(userId == null || userId.isEmpty()) {
			throw new MessageUserIdCannotBeEmpty("Message with empty user ID cannot be created");
		}
		this.userId = userId;
	}

}
