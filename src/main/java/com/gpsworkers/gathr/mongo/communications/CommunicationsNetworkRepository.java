package com.gpsworkers.gathr.mongo.communications;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gpsworkers.gathr.mongo.groups.Group;

public interface CommunicationsNetworkRepository extends MongoRepository<CommunicationsNetwork, String> {

}
