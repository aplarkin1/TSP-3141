package com.gpsworkers.gathr.mongo.groups;

import java.util.ArrayList;

public class GroupInvitation {
	public String sourceEmail;
	public String groupInvite;
	public String invitationMessage;
	public String groupId;
	
	public GroupInvitation(String sourceEmail, String groupInvite, String invitationMessage, String groupId) {
		super();
		this.sourceEmail = sourceEmail;
		this.groupInvite = groupInvite;
		this.invitationMessage = invitationMessage;
		this.groupId = groupId;
	}
	
	
}
