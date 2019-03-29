package com.gpsworkers.gathr.mongo.groups;

public class GroupInvitation {
	public String sourceEmail;
	public String groupInvite;
	public String invitationMessage;
	
	public GroupInvitation(String sourceEmail, String groupInvite, String invitationMessage) {
		super();
		this.sourceEmail = sourceEmail;
		this.groupInvite = groupInvite;
		this.invitationMessage = invitationMessage;
	}
	
	
}
