package com.gpsworkers.gathr.mongo.communication;

import java.util.Date;
/**
 * 
 * @author Alexander Larkin
 * This class is used to store a Message in a more user-friendly format.
 */
public class DisplayableMessage {
	
	private String messageContent;
	private String username;
	private Date postDate;
	
	/**
	 * This constructor allows for the generation of Displayable Message
	 * @param messageContent is the String content of the message...for now
	 * @param username is the current String username of the person that posted the message in the channel.
	 * @param postDate is the Data that this message was posted
	 */
	public DisplayableMessage(String messageContent, String username, Date postDate) {
		this.messageContent = messageContent;
		this.username = username;
		this.postDate = postDate;
	}
	
	/**
	 * This method retrieves the String contents of the message
	 * @return String format of the contents
	 */
	public String getMessageContent() {
		return messageContent;
	}
	
	/**
	 * This method returns the username of the poster of this message
	 * @return String format of the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This returns the post date of this message.
	 * @return Date of this message's posting
	 */
	public Date getPostDate() {
		return postDate;
	}
	
	
}
