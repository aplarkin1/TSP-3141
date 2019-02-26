package com.gpsworkers.gathr.mongo.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.data.annotation.Id;

import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;

/**
 * 
 * @author Alexander Larkin
 * 
 * This prototype class contains functionality and information that can assist in routing information through a communication channel
 * A channel can be established on a @see {@link CommunicationNetwork}
 */
public class Channel {
	
	@Id
	private String name;
	
	// This buffer contains all messages currently in the channel
	private ArrayList<Message> messages;
	
	/**
	 * This constructor allows for the creation of channel with the name specified by the name parameters
	 * @param name of the channel
	 * @throws Exception 
	 */
	public Channel(String name) throws Exception {
		setName(name);
		messages = new ArrayList<>();
	}
	
	/**
	 * This method allows for the creation of a message on the channel
	 * @param messageContent is a String that contains the content of the message
	 * @param userId is the String version of a users UUID
	 * @throws MessageUserIdCannotBeEmptyException when an empty user id has been passed to the posting method
	 * @throws Exception when an general error is thrown
	 */
	public void postMessage(String messageContent, String userId) throws MessageUserIdCannotBeEmptyException, Exception {
		Message newMessage = new Message(messageContent, userId);
		messages.add(newMessage);
	}
	
	/**
	 * This method is used to delete a message via a given index
	 * @param index is the index of the message to be deleted
	 * @throws Exception if the given index is negative
	 */
	public void deleteMessage(int index) throws Exception {
		if(index < 0) {
			throw new Exception("Cannot remove a message with a negative index!");
		}
		messages.remove(index);
	}
	
	/**
	 * This method is used to set the name of the channel
	 * @param name is String name to be given to the channel
	 * @throws Exception if the channel name is null or empty
	 */
	private void setName(String name) throws Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot create a channel with a name of null or empty!");
		}
	}
	
	/**
	 * This method retrieves a single @see {@link DisplayableMessage} object from the channel
	 * @param index is the index of the message to retrieve 
	 * @param uuidToUsernameMapping allows for the substitution of the UUID for the current username of the user that posted
	 * @return a single @see {@link DisplayableMessage} that corresponds with the index given
	 * @throws Exception when the index given is less than 0 or an index is given that is greater than or qual to the size of the message buffer
	 */
	public DisplayableMessage getDisplayableMessage(int index, HashMap<String, String> uuidToUsernameMapping) throws Exception {
		if(index < 0) {
			throw new Exception("Cannot retrieve a displayable message of an index less than 0");
		}
		
		if(index >= messages.size()) {
			throw new Exception("Cannnot retrieve a message with an index greater than or equal to the size of the message buffer");
		}
		Message message = messages.get(index);
		return new DisplayableMessage(message.getMessageContent(), uuidToUsernameMapping.getOrDefault(message.getUserId(), "Unknown or Missing User"), message.getPostDate());
	}
	
	/**
	 * This method builds on the getDisplayable message method by displaying all messages in the channel.
	 * @param uuidToUsernameMapping is the user id username mapping table
	 * @return all {@link DisplayableMessage} 's currently in the channel
	 * @throws Exception @see {@link Channel#getDisplayableMessage}
	 */
	public ArrayList<DisplayableMessage> getAllDisplayableMessagesInChannel(HashMap<String, String> uuidToUsernameMapping) throws Exception {
		ArrayList<DisplayableMessage> displayableMessages = new ArrayList<>();
		for(int i = 0; i < messages.size(); i++) {
			displayableMessages.add(getDisplayableMessage(i, uuidToUsernameMapping));
		}
		return displayableMessages;
	}
	
		/**
		 * This method further builds on the previous ones by only retrieving messages after a certain date.
		 * This will be useful because it will enable us to only send messages that the user hasn't seen before
		 * @param date is the start date for displaying messages
		 * @param uuidToUsernameMapping is the user id username mapping table
		 * @return all displayable messages that were created after the given date.
		 * @throws Exception @see {@link Channel#getDisplayableMessage}
		 */
	public ArrayList<DisplayableMessage> getAllDisplayableMessagesAfterDate(Date date, HashMap<String, String> uuidToUsernameMapping) throws Exception{
		int designatedRangeStartIndex = 0;
		
		//Search for first message after designated date..set start index with it found message index as soon as it is found
		ArrayList<DisplayableMessage> displayableMessages = new ArrayList<>();
		for(int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			if(message.getPostDate().after(date)) {
				designatedRangeStartIndex = i;
				break;
			}
		}
		for(int j = designatedRangeStartIndex; j < messages.size(); j++) {
			displayableMessages.add(getDisplayableMessage(j, uuidToUsernameMapping));
		}
		return displayableMessages;
	}
}
