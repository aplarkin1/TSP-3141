package com.gpsworkers.gathr.mongo.users;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 * @author Alexander Larkin
 * This is the MongoRepository where users can be saved and retrieved from.
 */
public interface UserRepository extends MongoRepository<User, String> {
	/**
	 * This method specifies that all Users that have had no interaction before the given date should be returned as a List
	 * @param dateOfLastInteraction is the threshold date
	 * @return
	 */
	public List<User> findAllByDateOfLastInteractionLessThanEqual(Date dateOfLastInteraction);
	
	/**
	 * This method allows for the retrieval of a user given an API Token
	 * @param token is the ObjectID that can be used to retrieve User 
	 * @return User that the token belongs to.
	 */
	public User findByApiToken(ObjectId token);
}
