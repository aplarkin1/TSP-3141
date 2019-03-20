package com.gpsworkers.gathr.mongo.communication;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;

/**
 * 
 * @author Alexander Larkin
 * This class stores a user posted message
 */
public class Message {
	@Id
	private String messageId;
	
	private String messageContent;
	private String userId;
	private Date postDate;
	
	/**
	 * This constructor allows for the creation of a new instance of Message with the message content and UserId specified
	 * @param messageContent is the String content of the message
	 * @param userId is the String userId
	 * @throws MessageUserIdCannotBeEmptyException when the UserId of the message is null or empty. 
	 * @throws Exception When the message content is null or empty
	 */
	public Message(String messageContent, String userId) throws MessageUserIdCannotBeEmptyException, Exception {
		setMessageContent(messageContent);
		setUserId(userId);
		postDate = new Date();
		messageId = userId + "-" + Date.from(postDate.toInstant());
	}

	/**
	 * This method returns the messageContent String stored by this class
	 * @return
	 */
	public String getMessageContent() {
		return messageContent;
	}
	
	/**
	 * This method returns the userId of the person that posted the message
	 * @return the userId string of stored UserId
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * This method returns the post date of the message
	 * @return the Date that this message was posted on.
	 */
	public Date getPostDate() {
		return postDate;
	}
	
	/**
	 * This method sets the message content of this message with the given String of messageContent
	 * @param messageContent is what the current message content will be changed to.
	 * @throws Exception
	 */
	public void setMessageContent(String messageContent) throws Exception {
		if(messageContent == null || messageContent.isEmpty()) {
			throw new Exception("Message content cannot be null or and empty string");
		}
		this.messageContent = messageContent;
	}
	
	/**
	 * This method sets the user id of the poster
	 * @param userId is the UserID of the user that posted this message
	 * @throws MessageUserIdCannotBeEmptyException when message id is null or empty.
	 */
	private void setUserId(String userId) throws MessageUserIdCannotBeEmptyException {
		if(userId == null || userId.isEmpty()) {
			throw new MessageUserIdCannotBeEmptyException("Message with empty user ID cannot be created");
		}
		this.userId = userId;
	}

}
