package com.gpsworkers.gathr.mongo.users;

public class FriendInvitation {
	public String sourceEmail;
	public String friendInvite;
	public String invitationMessage;
	public String userId;

	public FriendInvitation(String sourceEmail, String friendInvite, String invitationMessage, String userId) {
		super();
		this.sourceEmail = sourceEmail;
		this.friendInvite = friendInvite;
		this.invitationMessage = invitationMessage;
		this.userId = userId;
	}
}
