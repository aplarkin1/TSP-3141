package com.gpsworkers.gathr.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
=======
import org.springframework.stereotype.Service;
>>>>>>> 143bdc2707e5aaf3e3e08d06e930a344274ef26a

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.exceptions.EmptyGeocodingResultException;
import com.gpsworkers.gathr.exceptions.EmptyMessageException;
import com.gpsworkers.gathr.exceptions.GeoCodingConnectionFailedException;
import com.gpsworkers.gathr.exceptions.GroupDoesntExistException;
import com.gpsworkers.gathr.exceptions.GroupIdAlreadyInUseException;
import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;
import com.gpsworkers.gathr.exceptions.TargetUserNotFoundException;
import com.gpsworkers.gathr.exceptions.UnauthorizedGroupManagementException;
import com.gpsworkers.gathr.exceptions.UnauthorizedUserInteractionException;
import com.gpsworkers.gathr.exceptions.UserNotFoundException;
<<<<<<< HEAD
//import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;
//import com.gpsworkers.gathr.mongo.communications.Message;
=======
>>>>>>> 143bdc2707e5aaf3e3e08d06e930a344274ef26a
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

	@Autowired
	UserRepository users;

	@Autowired
	GroupRepository groups;

	public boolean updateLocation(String email, double lat, double lon, double elev) throws EmptyGeocodingResultException, GeoCodingConnectionFailedException {
		User validUser = users.findByEmail(email);
		Location currentLocation = getLocationGeoCodeInformation(lat, lon);
		validUser.updateLocation(lat, lon, elev, currentLocation.getCountry(), currentLocation.getState(), currentLocation.getCity());
		users.save(validUser);
		return true;
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
			System.out.println("SAVED GROUP HASH: " + newGroup.hashCode());
			System.out.println(newGroup.getGroupInvite());
			groups.save(newGroup);
			sourceUser.get().addGroup(newGroup.getGroupName());
			users.save(sourceUser.get());
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
			throw new GroupIdAlreadyInUseException();
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
				}
				Collection<User> users = group.get().getUsers();
				for(User user : users) {
					if(user.getEmail().equals(deleterEmail)) {
						group.get().getGroupCommsNetwork().deleteMesage(deleterEmail, index, false);
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
		Optional<User> adderUser = users.findById(adderUserEmail);
		if(adderUser.isPresent()) {
			Optional<Group> group = groups.findById(groupId);
			if(group.isPresent()) {
				Optional<User> targetUser = users.findById(targetUserEmail);
				if(targetUser.isPresent()) {
					if(group.get().isAdmin(adderUserEmail)) {
						group.get().addUser(targetUser.get());
						groups.save(group.get());
						targetUser.get().addGroup(groupId);
						users.save(targetUser.get());
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

	public String getGroupSummary(String groupId) throws JsonProcessingException {
		Optional<Group> group = groups.findById(groupId);
		if(groups.findById(groupId).isPresent()) {
			return group.get().getGroupSummary();
		} else {
			throw new GroupDoesntExistException();
		}
	}

}
