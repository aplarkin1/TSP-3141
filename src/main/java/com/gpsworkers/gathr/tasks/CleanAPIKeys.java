package com.gpsworkers.gathr.tasks;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

/**
 * 
 * @author Alexander Larkin
 * This class/Mongo Component is used to clean up expired API keys
 * 
 */
@Component
public class CleanAPIKeys {
	
	@Autowired
	UserRepository userRepo;
	
	//Run every ten minutes...clean up all expired tokens
	/**
	 * This method runs every ten minutes and cleans up all expired API tokens.  It checks all user's for expired tokens.
	 * If it finds some user's that have expired tokens, it deletes them
	 */
	@Scheduled(fixedDelay=600000)
	public void run() {
		System.out.println("Token cleaning commencing");
		List<User> usersWithExpiredTokens = userRepo.findAllByDateOfLastInteractionLessThanEqual(new DateTime().minusMinutes(1).toDate());
		for(User user : usersWithExpiredTokens) {
			user.removeToken();
			userRepo.save(user);
			System.out.println("Cleaning expired token for: " + user.getEmail());
		}
	}
}
