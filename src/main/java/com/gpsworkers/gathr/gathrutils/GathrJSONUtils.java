package com.gpsworkers.gathr.gathrutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Alexander Larkin
 * 
 * This class is a convenience class used for JSON parsing and serializing.
 */
public class GathrJSONUtils {
	
	/**
	 * This method takes an arbitrary object and serializes it into JSON.
	 * @param obj is an arbitrary object
	 * @return String that is the serialized JSON String of the passed object
	 * @throws JsonProcessingException if JSON can not parse the Object.
	 */
	public static final String write(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
}
