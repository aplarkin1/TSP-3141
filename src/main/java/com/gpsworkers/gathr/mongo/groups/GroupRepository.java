package com.gpsworkers.gathr.mongo.groups;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author Amanda Erdmann
 * This is the MongoRepository where groups can be saved and retrieved from.
 */
public interface GroupRepository extends MongoRepository<Group, String> {

  public Group findByGroupName(String groupName);
}
