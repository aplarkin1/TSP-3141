package com.gpsworkers.gathr.mongo.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmpty;

public class Channel {
	
	@Id
	private String name;
	
	private ArrayList<Message> messages;
	private long nextMessageIndex;
	
	public Channel(String name) throws Exception {
		setName(name);
		messages = new ArrayList<>();
		nextMessageIndex = 0;
		
	}
	
	public void postMessage(String messageContent, String userId) throws MessageUserIdCannotBeEmpty, Exception {
		Message newMessage = new Message(nextMessageIndex, messageContent, userId);
		messages.add(newMessage);
		nextMessageIndex += 1;
	}
	
	public void deleteMessage(int index) throws Exception {
		if(index < 0) {
			throw new Exception("Cannot remove a message with a negative index!");
		}
		messages.remove(index);
	}
	
	private void setName(String name) throws Exception {
		if(name == null || name.isEmpty()) {
			throw new Exception("Cannot create a channel with a name of null or empty!");
		}
	}
	
	private DisplayableMessage getDisplayableMessage(int index, HashMap<String, String> uuidToUsernameMapping) throws Exception {
		if(index < 0) {
			throw new Exception("Cannot retrieve a displayable message of an index less than 0");
		}
		
		if(index >= messages.size()) {
			throw new Exception("Cannnot retrieve a message with an index greater than or equal to the size of the message buffer");
		}
		Message message = messages.get(index);
		return new DisplayableMessage(message.getMessageContent(), uuidToUsernameMapping.getOrDefault(message.getUserId(), "Unknown or Missing User"), message.getPostDate());
	}
	
	private ArrayList<DisplayableMessage> getAllDisplayableMessagesInChannel(HashMap<String, String> uuidToUsernameMapping) {
		ArrayList<DisplayableMessage> displayableMessages = new ArrayList<>();
		for(int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			displayableMessages.add(new DisplayableMessage(message.getMessageContent(), uuidToUsernameMapping.getOrDefault(message.getUserId(), "Unknown User"), message.getPostDate()));
		}
		return displayableMessages;
	}
	
	private ArrayList<DisplayableMessage> getAllDisplayableMessagesAfterDate(Date date, HashMap<String, String> uuidToUsernameMapping){
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
			Message message = messages.get(j);
			displayableMessages.add(new DisplayableMessage(message.getMessageContent(), uuidToUsernameMapping.getOrDefault(message.getUserId(), "Unknown User"), message.getPostDate()));
		}
		return displayableMessages;
	}
}
