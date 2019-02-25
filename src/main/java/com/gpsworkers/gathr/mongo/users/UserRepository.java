package com.gpsworkers.gathr.mongo.users;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	public List<User> findAllByDateOfLastInteractionLessThanEqual(Date dateOfLastInteraction);
}
