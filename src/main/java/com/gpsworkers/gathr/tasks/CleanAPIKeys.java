package com.gpsworkers.gathr.tasks;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

@Component
public class CleanAPIKeys {
	
	@Autowired
	UserRepository userRepo;
	
	//Run every ten minutes...clean up all expired tokens
	@Scheduled(fixedDelay=600000)
	public void run() {
		List<User> usersWithExpiredTokens = userRepo.findAllByDateOfLastInteractionLessThanEqual(new DateTime().minusMinutes(1).toDate());
		
		for(User user : usersWithExpiredTokens) {
			user.removeToken();
		}
		
		System.out.println("Cleaning all expired Tokens...");
	}
	
}
