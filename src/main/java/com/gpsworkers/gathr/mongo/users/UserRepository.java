package com.gpsworkers.gathr.mongo.users;

<<<<<<< HEAD
//import java.util.Date;
//import java.util.List;

//import org.bson.types.ObjectId;
=======
>>>>>>> 143bdc2707e5aaf3e3e08d06e930a344274ef26a
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
