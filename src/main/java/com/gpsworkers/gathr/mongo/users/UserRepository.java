package com.gpsworkers.gathr.mongo.users;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Alexander Larkin
 * This is the MongoRepository where users can be saved and retrieved from.
 */
public interface UserRepository extends MongoRepository<User, String> {

	/**
		* This method allows for the retrieval of a user given an email address
		* @param email string used to retieve user
		* @return user the email belongs to
	*/
	public User findByEmail( String email );

}
