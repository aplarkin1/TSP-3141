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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.controllers.responsebodys.GetLocationResponse;
import com.gpsworkers.gathr.controllers.responsebodys.UserAccountInformationResponse;
import com.gpsworkers.gathr.exceptions.ChannelDoesntExistException;
import com.gpsworkers.gathr.exceptions.EmptyGeocodingResultException;
import com.gpsworkers.gathr.exceptions.EmptyMessageException;
import com.gpsworkers.gathr.exceptions.GeoCodingConnectionFailedException;
import com.gpsworkers.gathr.exceptions.GroupDoesntExistException;
import com.gpsworkers.gathr.exceptions.GroupIdAlreadyInUseException;
import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;
import com.gpsworkers.gathr.exceptions.NotAdminException;
import com.gpsworkers.gathr.exceptions.TargetUserNotFoundException;
import com.gpsworkers.gathr.exceptions.UnauthorizedGroupManagementException;
import com.gpsworkers.gathr.exceptions.UnauthorizedUserInteractionException;
import com.gpsworkers.gathr.exceptions.UserNotFoundException;
import com.gpsworkers.gathr.mongo.communications.DisplayableMessage;
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.GroupInvitation;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
import com.gpsworkers.gathr.mongo.users.Location;
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
	
	@Autowired
	private APIService api;
	
	/**
	 * This method is called a POST web request is sent to /api/updateLocation.
	 * @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * to see the parameters for this request
	 * @param request @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * @return a "1" if successful or a JSON string containing an error message
	 */
	@PostMapping("/api/updateLocation")
	@ResponseBody
	public ResponseEntity<String> handleUpdateLocationRequest(UpdateLocationAPIRequestBody request) {
		System.out.println("HELLO WORLD!!!");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = extractEmailFromAuth(auth);
		try {
			api.updateLocation(email, request.lat, request.lon, request.elev);
			
		} catch (GroupDoesntExistException e) {
			return new ResponseEntity<>("-3", HttpStatus.NOT_FOUND);
		} catch (UnauthorizedGroupManagementException e) {
			return new ResponseEntity<>("-1", HttpStatus.UNAUTHORIZED);
		} catch (TargetUserNotFoundException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>("1", HttpStatus.OK);
	}
	
	
	@PostMapping("/api/postMessageInGroup")
	@ResponseBody
	public ResponseEntity<String> handlePostMessageInGroupRequest(String message, String groupId) {
		String posterEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		try {
			api.postMessageInGroup(message, groupId, posterEmail);
			return new ResponseEntity<>("1", HttpStatus.OK);
		} catch (EmptyMessageException e) {
			return new ResponseEntity<>("-1", HttpStatus.BAD_REQUEST);
		} catch (MessageUserIdCannotBeEmptyException e) {
			return new ResponseEntity<>("-2", HttpStatus.BAD_REQUEST);
		} catch (UnauthorizedUserInteractionException e) {
			return new ResponseEntity<>("-1", HttpStatus.UNAUTHORIZED);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		} catch (GroupDoesntExistException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		}  catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/api/deleteMessageInGroup")
	@ResponseBody
	public ResponseEntity<String> handleDeleteMessageInGroupRequest(String groupId, int index) {
		String deleterEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		try {
			api.deleteMessageInGroup(groupId, index, deleterEmail);
			return new ResponseEntity<>("1", HttpStatus.OK);
		} catch (MessageUserIdCannotBeEmptyException e) {
			return new ResponseEntity<>("-2", HttpStatus.BAD_REQUEST);
		} catch (UnauthorizedUserInteractionException e) {
			return new ResponseEntity<>("-1", HttpStatus.UNAUTHORIZED);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		} catch (GroupDoesntExistException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		}  catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
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
	*/
	public static final String extractEmailFromAuth(Authentication auth) {
		String authString = "" + auth.getPrincipal();
	    authString = authString.replace("[", "");
		authString = authString.replace("]", "");
		return authString.split("email=")[1];
	}
	
	@PostMapping("/api/createGroup")
	@ResponseBody
	public ResponseEntity<String> handleCreateGroupRequest(String groupId) throws JsonProcessingException {
		String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		try {
			api.createGroup(sourceEmail, groupId);
			return new ResponseEntity<>("1", HttpStatus.OK);
		} catch (GroupIdAlreadyInUseException e1) {
			return new ResponseEntity<>("-1", HttpStatus.CONFLICT);
		} catch (UserNotFoundException e1) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	@PostMapping("/api/deleteGroup")
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
	public ResponseEntity<String> handleInviteUserToGroupRequest(String groupId, String targetUserEmail, String invitationMessage) throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		try {
			String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
			api.inviteUserToGroup(groupId, sourceEmail, targetUserEmail, invitationMessage);
			return new ResponseEntity<>("1", HttpStatus.OK);
		} catch (UserNotFoundException e){
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		} catch(GroupDoesntExistException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/api/getGroupInvites")
	@ResponseBody
	public ResponseEntity<String> handleGetGroupInvites() throws JsonProcessingException {
		String userEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		ArrayList<GroupInvitation> invites;
		try {
			invites = api.getGroupInvites(userEmail);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(GathrJSONUtils.write(invites), HttpStatus.OK);
	}
	
	@GetMapping("/api/getGroupSummary")
	@ResponseBody
	public ResponseEntity<String> handleGetGroupSummary(String groupId) throws JsonProcessingException {
		String userEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		try {
			api.getGroupSummary(groupId);
		} catch (GroupDoesntExistException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(groupId, HttpStatus.OK);
	}
	
	
	/*
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
	*/
	
	
	@GetMapping("/api/getAccountInformation")
	@ResponseBody
	public ResponseEntity<String> handleGetAccountInformation() throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		try {
			String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
			UserAccountInformationResponse accountInformation = api.getAccountInformation(sourceEmail);
			return new ResponseEntity<>(GathrJSONUtils.write(accountInformation), HttpStatus.OK);
		} catch(GroupDoesntExistException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/api/getUserLocation")
	@ResponseBody
	public ResponseEntity<String> handleGetUserLocation() throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		try {
			String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
			GetLocationResponse userLocation = api.getUserLocation(email);
			return new ResponseEntity<>(GathrJSONUtils.write(userLocation), HttpStatus.OK);
		} catch (UserNotFoundException e){
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/api/getAllGroupMessages")
	@ResponseBody
	public ResponseEntity<String> handleGetAllGroupMessages(String groupId) throws JsonProcessingException {
		try {
			String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
			ArrayList<DisplayableMessage> displayableMessage = api.getAllGroupMessages(email, groupId);
			return new ResponseEntity<>(GathrJSONUtils.write(displayableMessage), HttpStatus.OK);
		} catch (GroupDoesntExistException e){
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		} catch (UnauthorizedUserInteractionException e) {
			return new ResponseEntity<>("-1", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/api/getAllGroupNames")
	@ResponseBody
	public ResponseEntity<String> handleGetAllGroupMessages() throws JsonProcessingException {
		String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		Collection<String> groupNames;
		try {
			groupNames = api.getGroupNamesOfUser(email);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(GathrJSONUtils.write(groupNames), HttpStatus.OK);
	}
	
	@PostMapping("/api/addUserToGroup")
	@ResponseBody
	public ResponseEntity<String> handlePostAddUser(String groupId, String targetUserEmail) throws MessageUserIdCannotBeEmptyException, ChannelDoesntExistException, Exception {
		try {
			String sourceEmail = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
			api.addUserToGroup(sourceEmail, targetUserEmail, groupId);
			return new ResponseEntity<>("1", HttpStatus.OK);
		} catch (UserNotFoundException e){
			return new ResponseEntity<>("-1", HttpStatus.NOT_FOUND);
		} catch(GroupDoesntExistException e) {
			return new ResponseEntity<>("-2", HttpStatus.NOT_FOUND);
		}
	}
}