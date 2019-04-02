package com.gpsworkers.gathr.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.controllers.responsebodys.GetLocationResponse;
import com.gpsworkers.gathr.controllers.responsebodys.UserAccountInformationResponse;
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
import com.gpsworkers.gathr.mongo.communications.Message;
//import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;
//import com.gpsworkers.gathr.mongo.communications.Message;
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.GroupInvitation;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
import com.gpsworkers.gathr.mongo.users.Location;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

@Service
public class APIService {

	public static int ERR_EXP_OR_FAKE_TOKEN = -9;
	public static int ERR_INVALID_TOKEN = -2;
	public static int ERR_INVALID_REQUEST_SENT = -1;
	public static int ERR_MISSING_FIELD_IN_REQUEST = -3;

	public static final String GLOBAL_ADMIN_EMAIL = "globalgathradmin@gmail.com";
	
	@Autowired
	UserRepository users;

	@Autowired
	GroupRepository groups;

	public boolean updateLocation(String email, double lat, double lon, double elev) throws EmptyGeocodingResultException, GeoCodingConnectionFailedException {
		User validUser = users.findByEmail(email);
		Location currentLocation = getLocationGeoCodeInformation(lat, lon);
		validUser.updateLocation(lat, lon, elev, currentLocation.getCountry(), currentLocation.getState(), currentLocation.getCity());
		users.save(validUser);
		
		currentLocation = validUser.getCurrentLocation();
		updateUserCityBasedGroup(email, currentLocation.getCountry(), currentLocation.getState(), currentLocation.getCity());
		
		return true;
	}
	
	
	public UserAccountInformationResponse getAccountInformation(String email){
		/*
		ArrayList<String> accountInformation = new ArrayList<>();
		accountInformation.add(validUser.getEmail());
		accountInformation.add(validUser.getUsername());
		accountInformation.add(validUser.getFirstName());
		accountInformation.add(validUser.getLastName());
		accountInformation.add(validUser.getCurrentLocation().toString());
		*/
		User validUser = users.findByEmail(email);
		UserAccountInformationResponse userInfo = new UserAccountInformationResponse();
		userInfo.firstname = validUser.getFirstName();
		userInfo.lastname = validUser.getLastName();
		userInfo.email = validUser.getEmail();
		userInfo.username = validUser.getUsername();
		userInfo.country = validUser.getCurrentLocation().getCountry();
		userInfo.state = validUser.getCurrentLocation().getState();
		userInfo.city = validUser.getCurrentLocation().getCity();
		userInfo.lat = validUser.getCurrentLocation().getLatitude();
		userInfo.lon = validUser.getCurrentLocation().getLongitude();
		userInfo.elev = validUser.getCurrentLocation().getElevation();

		return userInfo;
	}

	public Location getLocationGeoCodeInformation(double lat, double lon) throws EmptyGeocodingResultException, GeoCodingConnectionFailedException {

		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyC2OKbwa0DhWHlA9cp8WxJP2TIRopz9daY").build();
		try {
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lon)).await();
			if(results.length == 0) {
				throw new EmptyGeocodingResultException();

			}
			String fullAddress = results[1].formattedAddress;
			String[] fullAddressSplit = fullAddress.split(",");
			System.out.println(fullAddress);
			String city = fullAddressSplit[1].split(" ")[1];
			String state = fullAddressSplit[2].split(" ")[1];
			String country = fullAddressSplit[3].split(" ")[1];

			System.out.println(country + "->" + state + "->" + city);

			Location newLocation = new Location();
			newLocation.update(lon, lat, 0, country, state, city);
			return newLocation;
		} catch (Exception e) {
			System.out.println("Geocoding Connection Failed!");
			e.printStackTrace();
			throw new GeoCodingConnectionFailedException();
		}

	}

	public boolean createGroup(String sourceEmail, String groupId) throws GroupIdAlreadyInUseException {
		Optional<User> sourceUser = users.findById(sourceEmail);
		if(!sourceUser.isPresent()) {
			throw new UserNotFoundException();
		}
		System.out.println(groupId);
		if(!groups.findById(groupId).isPresent()) {
			Group newGroup = new Group(groupId, sourceUser.get());
			sourceUser.get().addGroup(newGroup.getGroupName());
			users.save(sourceUser.get());
			groups.save(newGroup);
			return true;
		} else {
			throw new GroupIdAlreadyInUseException();
		}
	}

	public boolean deleteGroup(String sourceEmail, String groupId) throws GroupIdAlreadyInUseException {
		Optional<User> sourceUser = users.findById(sourceEmail);
		if(!sourceUser.isPresent()) {
			throw new UserNotFoundException();
		}
		System.out.println(groupId);
		if(groups.findById(groupId).isPresent()) {

			//MAYBE ADD ADMIN VOTE LOGIC....NOT ESSENTIAL
			if(true) {
				Group group = groups.findById(groupId).get();
				for(User user : group.getUsers()) {
					user.removeGroup(groupId);
				}
				groups.deleteById(groupId);
			}
			return true;
		} else {
			throw new GroupDoesntExistException();
		}
	}

	public boolean inviteUserToGroup(String groupId, String sourceEmail, String targetEmail, String invitationMessage) throws UserNotFoundException, GroupDoesntExistException{
		User sourceUser = users.findByEmail(sourceEmail);
		Optional<Group> group = groups.findById(groupId);

		if(group.isPresent()) {
			User targetUser = users.findByEmail(targetEmail);
			if(targetUser != null) {
				GroupInvitation invitation = new GroupInvitation(sourceEmail, group.get().getGroupInvite(), invitationMessage);
				targetUser.postGroupInvite(invitation);
				users.save(targetUser);
				return true;
			} else {
				throw new UserNotFoundException();
			}
		} else {
			throw new GroupDoesntExistException();
		}
	}

	public boolean postMessageInGroup(String message, String groupId, String posterEmail) throws EmptyMessageException, MessageUserIdCannotBeEmptyException, UnauthorizedUserInteractionException, GroupDoesntExistException, UserNotFoundException, Exception {
		Optional<User> poster = users.findById(posterEmail);
		if(poster.isPresent()) {
			Optional<Group> group = groups.findById(groupId);
			if(group.isPresent()) {
				Collection<User> users = group.get().getUsers();
				for(User user : users) {
					if(user.getEmail().equals(posterEmail)) {
						group.get().getGroupCommsNetwork().postMesage(poster.get(), message);
						groups.save(group.get());
						return true;
					}
				}
				throw new UnauthorizedUserInteractionException();

			} else {
				throw new GroupDoesntExistException();
			}
		} else {
			throw new UserNotFoundException();
		}
	}

	public boolean deleteMessageInGroup(String groupId, int index, String deleterEmail) throws EmptyMessageException, MessageUserIdCannotBeEmptyException, UnauthorizedUserInteractionException, GroupDoesntExistException, UserNotFoundException, Exception {
		Optional<User> deleter = users.findById(deleterEmail);
		if(deleter.isPresent()) {
			Optional<Group> group = groups.findById(groupId);
			if(group.isPresent()) {
				if(group.get().isAdmin(deleterEmail)) {
					group.get().getGroupCommsNetwork().deleteMesage(deleterEmail, index, true);
					groups.save(group.get());
					return true;
				}
				Collection<User> users = group.get().getUsers();
				for(User user : users) {
					if(user.getEmail().equals(deleterEmail)) {
						group.get().getGroupCommsNetwork().deleteMesage(deleterEmail, index, false);
						groups.save(group.get());
						return true;
					}
				}
				throw new UnauthorizedUserInteractionException();
			} else {
				throw new GroupDoesntExistException();
			}
		} else {
			throw new UserNotFoundException();
		}
	}

	public void addUserToGroup(String adderUserEmail, String targetUserEmail, String groupId) throws UnauthorizedGroupManagementException, TargetUserNotFoundException, GroupDoesntExistException, UserNotFoundException {
		System.out.println("1");
		Optional<User> adderUser = users.findById(adderUserEmail);
		if(adderUser.isPresent()) {
			System.out.println("2");
			Optional<Group> group = groups.findById(groupId);
			if(group.isPresent()) {
				System.out.println("3");
				Optional<User> targetUser = users.findById(targetUserEmail);
				if(targetUser.isPresent()) {
					System.out.println("4");
					if(group.get().isAdmin(adderUserEmail)) {
						System.out.println("5");
						group.get().addUser(targetUser.get());
						groups.save(group.get());
						targetUser.get().addGroup(groupId);
						users.save(targetUser.get());
						System.out.println("HELLO FRIENDS: " + users.findByEmail(targetUserEmail).getGroupNames().size());
					} else {
						throw new UnauthorizedGroupManagementException();
					}
				} else {
					throw new TargetUserNotFoundException();
				}
			} else {
				throw new GroupDoesntExistException();
			}
		} else {
			throw new UserNotFoundException();
		}
	}

	public ArrayList<GroupInvitation> getGroupInvites(String userEmail) {
		Optional<User> user = users.findById(userEmail);
		if(user.isPresent()) {
			return user.get().getGroupInvites();
		} else {
			throw new UserNotFoundException();
		}
	}

	public HashMap<String, Object> getGroupSummary(String groupId) throws JsonProcessingException {
		Optional<Group> group = groups.findById(groupId);
		if(groups.findById(groupId).isPresent()) {
			return group.get().getGroupSummary();
		} else {
			throw new GroupDoesntExistException();
		}
	}
	
	public GetLocationResponse getUserLocation(String email) throws UserNotFoundException{
		Optional<User> optUser = users.findById(email);
		if(optUser.isPresent()) {
			User user = optUser.get();
			GetLocationResponse response = new GetLocationResponse();
			response.country = user.getCurrentLocation().getCountry();
			response.state = user.getCurrentLocation().getState();
			response.city = user.getCurrentLocation().getCity();
			response.lat = user.getCurrentLocation().getLatitude();
			response.lon = user.getCurrentLocation().getLongitude();
			response.elev = user.getCurrentLocation().getElevation();
			return response;
		} else {
			throw new UserNotFoundException();
		}
	}
	
	public ArrayList<DisplayableMessage> getAllGroupMessages(String email, String groupId) throws UnauthorizedUserInteractionException, GroupDoesntExistException{
		Optional<Group> group = groups.findById(groupId);
		if(group.isPresent()) {
			
			if(group.get().isUserInGroup(email)) {
				ArrayList<DisplayableMessage> displayableMessages = new ArrayList<DisplayableMessage>();
				for(Message message : group.get().getGroupCommsNetwork().getAllMessages()) {
					displayableMessages.add(new DisplayableMessage(message.getMessageContent(), users.findById(message.getUserId()).get().getUsername(), message.getPostDate()));
				}
				return displayableMessages;
			} else {
				throw new UnauthorizedUserInteractionException();
			}
		} else {
			throw new GroupDoesntExistException();
		}
	}
	
	public void updateUserCityBasedGroup(String emailOfUserToAdd, String country, String state, String city) {
		String groupId = country + "->" + state + "->" + city;
		if(groups.findById(groupId).isPresent()) {
			if(groups.findById(groupId).get().isUserInGroup(emailOfUserToAdd)) {
				return;
			} else {
				addUserToGroup(GLOBAL_ADMIN_EMAIL, emailOfUserToAdd, groupId);
			}
		} else {
			Group group = new Group(groupId, users.findById(GLOBAL_ADMIN_EMAIL).get());
			groups.save(group);
			addUserToGroup(GLOBAL_ADMIN_EMAIL, emailOfUserToAdd, groupId);
		}
	}
	
	public Collection<String> getGroupNamesOfUser(String email) {
		Optional<User> optUser = users.findById(email);
		if(optUser.isPresent()) {
			User user = optUser.get();
			return user.getGroupNames();
		} else {
			throw new UserNotFoundException();
		}
	}
	
	public void removeUserFromGroup(String authorityEmail, String targetUserEmail, String groupId) {
		Optional<User> targetUser = users.findById(targetUserEmail);
		if(targetUser.isPresent()) {
			Optional<Group> group = groups.findById(groupId);
			if(group.isPresent()) {
				try {
					group.get().deleteUser(targetUserEmail);
					System.out.println("THE USER IS GETTING REMOVED : " + targetUser.get().getGroupNames().size());
					targetUser.get().removeGroup(groupId);
					System.out.println("THE USER IS GETTING REMOVED");
					users.save(targetUser.get());
					System.out.println("THE USER IS GETTING REMOVED : " + targetUser.get().getGroupNames().size());
					groups.save(group.get());
					if(group.get().getUsers().size() == 0) {
						systemDeleteGroup(groupId);
					}
				} catch (NotAdminException e) {
					throw new UnauthorizedUserInteractionException();
				}
			} else {
				throw new GroupDoesntExistException();
			}
		} else {
			throw new UserNotFoundException();
		}
	}

	public void systemDeleteGroup(String groupId) {
		System.out.println(groupId);
		if(groups.findById(groupId).isPresent()) {

			//MAYBE ADD ADMIN VOTE LOGIC....NOT ESSENTIAL
			if(true) {
				Group group = groups.findById(groupId).get();
				for(User user : group.getUsers()) {
					if(user != null) {
						user.removeGroup(groupId);
					}
					
				}
				groups.deleteById(groupId);
			}
		}
	}
	
	public void systemDeleteUser(String targetUserEmail) {
		Optional<User> targetUser = users.findById(targetUserEmail);
		if(targetUser.isPresent()) {
			for(String groupName : targetUser.get().getGroupNames()) {
				Optional<Group> group = groups.findById(groupName);
				if(group.isPresent()) {
					removeUserFromGroup(group.get().getAdmin().iterator().next().getEmail(), targetUserEmail, groupName);
				}
			}
			users.deleteById(targetUserEmail);
		}
	}
	
}
