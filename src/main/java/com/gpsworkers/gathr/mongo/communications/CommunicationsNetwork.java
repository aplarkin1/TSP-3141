package com.gpsworkers.gathr.mongo.communications;

import java.util.ArrayList;
import java.util.HashMap;import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gpsworkers.gathr.exceptions.EmptyMessageException;
import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;
import com.gpsworkers.gathr.exceptions.UnauthorizedUserInteractionException;
import com.gpsworkers.gathr.exceptions.UserMutedInCommsNetworkException;
import com.gpsworkers.gathr.mongo.users.User;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class contains prototype code for a flexible communication network.
 */
@Document(collection="commsnetworks")
public class CommunicationsNetwork {
	
	private ArrayList<Message> messages = new ArrayList<Message>();
	private ArrayList<String> mutedUsers = new ArrayList<String>();

	/**
	 * Default constructor
	 */
	public CommunicationsNetwork() {
	}
	
	public void postMesage(User poster, String message) throws MessageUserIdCannotBeEmptyException, EmptyMessageException, Exception {
		if(message.isEmpty() == false) {
			if(mutedUsers.indexOf(poster.getEmail()) == -1) {
				/*if(messages.size() > 0) {
					int indexOfLastMessage = messages.size() - 1;
					Message mostRecentMessage = messages.get(indexOfLastMessage);
					if(mostRecentMessage.getUserId().equals(poster.getEmail()) && mostRecentMessage.getPostDate().isSe(new DateTime())){
						mostRecentMessage.appendMessageContent(message);
						return;
					}
				}
				*/
				//
				Message messageObject = new Message(message, poster.getEmail());
				messages.add(messageObject);
			} else {
				throw new UserMutedInCommsNetworkException();
			}
		} else {
			throw new EmptyMessageException();
		}
	}
	
	
	public void deleteMesage(String deleterEmail, int index, boolean isAdmin) throws UnauthorizedUserInteractionException {
		if(messages.size() > index && index >= 0) {
			if(isAdmin) {
				messages.remove(index);
			} else if(messages.get(index).getUserId().equals(deleterEmail)) {
				messages.remove(index);
			} else {
				throw new UnauthorizedUserInteractionException();
			}
		} else {
			throw new RuntimeException("Tried to delete message ");
		}

	}
	
	public ArrayList<Message> getMessagesAfterDatetime(DateTime time) {
		if(messages.size() > 0) {
			ArrayList<Message> messagesFound = new ArrayList<Message>(messages.size()/2 + messages.size()/4);
			for(Message message : messages) {
				if(message.getPostDate().isAfter(time.toInstant())) {
					messagesFound.add(message);
				}
			}
			return messagesFound;
		} else {
			return new ArrayList<Message>();
		}
	}
	
	public ArrayList<Message> getAllMessages() {
		return messages;
	}
	
	public Message getLastMessage() {
		if(messages.size() >= 1) {
			return messages.get(messages.size() - 1);
		} else {
			return null;
		}
	}
	
	public Message getMessage(int index) {
		return messages.get(index);
	}
	
}
