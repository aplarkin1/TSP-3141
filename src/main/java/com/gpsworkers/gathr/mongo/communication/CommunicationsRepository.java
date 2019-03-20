package com.gpsworkers.gathr.mongo.communication;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Amanda Erdmann
 * This is the MongoRepository where groups can be saved and retrieved from.
 */
public interface CommunicationsRepository extends MongoRepository<CommunicationNetwork, String> {
	
}
