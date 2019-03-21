package com.gpsworkers.gathr.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.exceptions.ChannelDoesntExistException;
import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;
import com.gpsworkers.gathr.exceptions.NotAdminException;
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.GroupInvitation;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;
import com.gpsworkers.gathr.gathrutils.*;

/**
 * 
 * @author Alexander Larkin
 * This class is used to control API access
 */
@RestController
public class APIController {
	
	public static int ERR_EXP_OR_FAKE_TOKEN = -9;
	public static int ERR_INVALID_TOKEN = -2;
	public static int ERR_INVALID_REQUEST_SENT = -1;
	public static int ERR_MISSING_FIELD_IN_REQUEST = -3;
	
	@Autowired
	UserRepository users;
	
	@Autowired
	GroupRepository groups;
	
	/**
	 * This method is called a POST web request is sent to /api/updateLocation.
	 * @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * to see the parameters for this request
	 * @param request @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * @return a "1" if successful or a JSON string containing an error message
	 */
	@PostMapping("/api/updateLocation")
	@ResponseBody
	public ResponseEntity<String> updateLocation(UpdateLocationAPIRequestBody request) {
		System.out.println("HELLO WORLD!!!");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			
		System.out.println("NAME: " + auth.getPrincipal());
		    
		String authString = "" + auth.getPrincipal();
	    authString = authString.replace("[", "");
		authString = authString.replace("]", "");
		System.out.println(authString);
		String userEmail = authString.split("email=")[1];
		System.out.println("EMAIL: " + userEmail);
		    
		User validUser = users.findByEmail(userEmail);
		    
		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyC2OKbwa0DhWHlA9cp8WxJP2TIRopz9daY").build();
		try {
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(request.lat, request.lon)).await();
			if(results.length == 0) {
				return new ResponseEntity<>("-1", HttpStatus.FAILED_DEPENDENCY);
			}
			String fullAddress = results[1].formattedAddress;
			String[] fullAddressSplit = fullAddress.split(",");
			System.out.println(fullAddress);
			String city = fullAddressSplit[1].split(" ")[1];
			String state = fullAddressSplit[2].split(" ")[1];
			String country = fullAddressSplit[3].split(" ")[1];

			System.out.println(country + "->" + state + "->" + city);

			validUser.updateLocation(request.lat, request.lon, request.elev, country, state, city);
			users.save(validUser);
		} catch (Exception e) {
			System.out.println("Geocoding Connection Failed!");
			e.printStackTrace();
			return new ResponseEntity<>("-1", HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<>("1", HttpStatus.OK);

	}
	
	@PostMapping("/api/sendGroupChannelMessage")
	@ResponseBody
	public ResponseEntity<String> sendGroupChannelMessage(String message, String groupId, String channelName) {
		String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User user = users.findByEmail(email);
		
		boolean status = user.sendMessage(message, groupId, channelName);
		if(status == false) {
			return new ResponseEntity<>("-1", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("1", HttpStatus.OK);
		}
		
	}
	
	@PostMapping("/api/openCommsWithUser")
	@ResponseBody
	public ResponseEntity<String> openCommsWithUser(String message, String targetUserEmail) throws JsonProcessingException, NotAdminException {
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		
		User sourceUser = users.findByEmail(sourceEmail);
		User targetUser = users.findByEmail(targetUserEmail);
		if(targetUser == null) {
			HashMap<String, String> errMsg = new HashMap<>();
			errMsg.put("error", "-1");
			errMsg.put("desc", "Target user doesn't exist!");
			return new ResponseEntity<>(GathrJSONUtils.write(errMsg), HttpStatus.NOT_FOUND);
		} else if(targetUser.hasBlacklistedUser(sourceUser.getEmail())) {
			System.out.println( sourceUser.getEmail() + " is blacklisted by " + targetUser.getEmail());
			return new ResponseEntity<>("1", HttpStatus.OK);
		} else if(targetUser != null && targetUser.hasBlacklistedUser(sourceUser.getEmail())){
			//MAKE SURE TO SWITCH TO GLOBALLY UNIQUE ID SCHEMA!!!!!!!!!!!!!
			Group personalGroup = new Group(sourceUser.getUsername() + ":" + targetUser.getUsername(), sourceUser);
			personalGroup.addUser(targetUser, personalGroup.getGroupInvite());
			personalGroup.makeAdmin(sourceUser, targetUser);
			sourceUser.addGroup(personalGroup);
			targetUser.addGroup(personalGroup);
			groups.insert(personalGroup);
			users.save(sourceUser);
			users.save(targetUser);
			return new ResponseEntity<>("1", HttpStatus.OK);
		}
		return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/api/sendPersonalGroupMessage")
	@ResponseBody
	public ResponseEntity<String> sendPersonalGroupMessage(String message, String groupId) throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);
		Group group = sourceUser.getGroup(groupId);

		if(group != null) {
			//Set up banning code here!
			if(1 == 1) {
				group.getGroupCommsNetwork().getChannel("MAIN").postMessage(message, sourceUser.getEmail());
			}
		}
		
		return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
	}

	public static final String extractEmailFromAuth(Authentication auth) {
		String authString = "" + auth.getPrincipal();
	    authString = authString.replace("[", "");
		authString = authString.replace("]", "");
		return authString.split("email=")[1];
	}
	
	@PostMapping("/api/createGroup")
	@ResponseBody
	public ResponseEntity<String> createGroup(String groupId) throws JsonProcessingException {
		
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);
		
		if(groups.findById(groupId).isEmpty()) {
			Group newGroup = new Group(groupId, sourceUser);
			groups.save(newGroup);
			return new ResponseEntity<>("1", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
	}
	
	/*
	@PostMapping("/api/deleteGroup")
	@ResponseBody
	public ResponseEntity<String> deleteGroup(String groupId, String groupInvite) throws JsonProcessingException {
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);
		
		if(groups.findById(groupId).isEmpty()) {
			Group newGroup = new Group(groupId, sourceUser);
			groups.save(newGroup);
		}
		return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
	}
	*/
	
	@PostMapping("/api/inviteUserToGroup")
	@ResponseBody
	public ResponseEntity<String> inviteUserToGroup(String groupId, String userEmail, String invitationMessage) throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		
		HashMap<String, String> errMsg = new HashMap<>();
		
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);
		
		Group group = sourceUser.getGroup(groupId);

		if(group != null) {
			User targetUser = users.findByEmail(userEmail);
			if(targetUser != null) {
				GroupInvitation invitation = new GroupInvitation(sourceEmail, group.getGroupInvite(), invitationMessage, group.getGroupName());
				targetUser.postGroupInvite(invitation);
				users.save(targetUser);
				return new ResponseEntity<>("1", HttpStatus.OK);
			} else {
				errMsg.put("error", "-1");
				errMsg.put("desc", "Target user doesn't exist!");
				return new ResponseEntity<>(GathrJSONUtils.write(errMsg), HttpStatus.NOT_FOUND);
			}
		} else {
			System.out.println("Group doesn't exist!");
			return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/api/getGroupInvites")
	@ResponseBody
	public ResponseEntity<String> getGroupInvites() throws JsonProcessingException {
		HashMap<String, String> errMsg = new HashMap<>();
		
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);
		
		if(sourceUser.getGroupInvites().size() > 0) {
			return new ResponseEntity<>(GathrJSONUtils.write(sourceUser.getGroupInvites()), HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>("1", HttpStatus.OK);
		}
	}
	
	@PostMapping("/api/getGroupInvitationDetails")
	@ResponseBody
	public ResponseEntity<String> getGroupInvitationDetails(String groupId, String groupInvite) throws JsonProcessingException {
		HashMap<String, String> errMsg = new HashMap<>();
		
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		User sourceUser = users.findByEmail(sourceEmail);

		for(GroupInvitation invite : sourceUser.getGroupInvites()) {
			if(invite.groupId.equals(groupId) && invite.groupInvite.equals(groupInvite)) {
				
				Optional<Group> group = groups.findById(groupId);
				if(group.isPresent()) {
					return new ResponseEntity<>(group.get().getGroupSummary(), HttpStatus.FOUND);
				} else {
					errMsg.put("error", "-1");
					errMsg.put("description", "The group " + groupId + " no longer exists!");
					return new ResponseEntity<>(GathrJSONUtils.write(errMsg), HttpStatus.NOT_FOUND);
				}
			}
		}
		return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
	}
	
	
}