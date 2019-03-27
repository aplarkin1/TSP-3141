package com.gpsworkers.gathr.mongo.groups;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Notification {
	
	public enum Type 
    { 
        GROUP_INVITE, FRIEND_INVITE, PRIVATE_MESSAGE, GROUP_MESSAGE
    } 
	
	String title;
	Type type;
	HashMap<String, String> content;
	
	public Notification(String title, Type type) {
		super();
		this.title = title;
		this.type = type;
		this.content = new HashMap<String, String>();
	}
	
	public void addContentPair(String key, String value) {
		content.putIfAbsent(key, value);
	}
	
	public Set<String> getKeySet() {
		return content.keySet();
	}
	
	public Collection<String> getValues(){
		return content.values();
	}
	
	
}
