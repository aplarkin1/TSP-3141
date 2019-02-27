package com.gpsworkers.gathr.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.controllers.requestbodys.BasicRequestBody;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.controllers.responsebodys.ErrorResponseBody;
import com.gpsworkers.gathr.exceptions.WebApiErrorResponseException;
import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

/**
 * 
 * @author Alexander Larkin
 * This class is used to control API access
 */
@RestController
public class APIController {
	
	private static int ERR_EXP_OR_FAKE_TOKEN = -9;
	private static int ERR_INVALID_TOKEN = -2;
	private static int ERR_INVALID_REQUEST_SENT = -1;
	private static int ERR_MISSING_FIELD_IN_REQUEST = -3;
	
	@Autowired
	UserRepository users;
	
	/**
	 * This method is called a POST web request is sent to /api/updateLocation.
	 * @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * to see the parameters for this request
	 * @param request @see com.gpsworkers.gathr.controllers.requestbodys.CreateUserApiRequestBody
	 * @return a "1" if successful or a JSON string containing an error message
	 */
	@PostMapping("/api/updateLocation")
	public String updateLocation(@RequestBody UpdateLocationAPIRequestBody request) {
		System.out.println("HELLO WORLD!!!");
		try {
			validateAPIRequest(request);
			User validUser = users.findByApiToken(new ObjectId(request.apiToken));
			GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyC2OKbwa0DhWHlA9cp8WxJP2TIRopz9daY").build();
			try {
				GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(request.lat, request.lon)).await();
				String cityName = results[0].addressComponents[1].longName;
				String stateName = results[0].addressComponents[2].longName;
				String countryName = results[0].addressComponents[3].longName;
				validUser.updateLocation(request.lon, request.lon, request.elev, countryName, stateName, cityName);
			} catch (Exception e) {
				System.out.println("Geocoding Connection Failed!");
				e.printStackTrace();
			}
			return "1";
		} catch(WebApiErrorResponseException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * This method is a helper method that validates requests that contain an apiToken.
	 * All requests that contain tokens are derived from the interface @see BasicRequestBody
	 * This way we don't have to worry about creating validation code for each API request.
	 * @param request @see BasicRequestBody
	 * @throws WebApiErrorResponseException if the token is found to be forged or has a bad format
	 */
	private void validateAPIRequest(BasicRequestBody request) throws WebApiErrorResponseException{
		try {
			if(request.getApiToken() == null || request.getApiToken().isEmpty()) {
				System.out.println("Bad Token Format for Update Location");
				throw new WebApiErrorResponseException(GathrJSONUtils.write(new ErrorResponseBody(ERR_INVALID_TOKEN, "Invalid token sent")));
			}
			
			//Check user API Token validity
			ObjectId apiToken = new ObjectId(request.getApiToken());
			User user = users.findByApiToken(apiToken);
			
			// If user not found in user repo, based on token.  Then send back bad token error message
			if(user == null) {
				throw new WebApiErrorResponseException(GathrJSONUtils.write(new ErrorResponseBody(ERR_EXP_OR_FAKE_TOKEN, "Forged or fake token sent"))); 
			}
		} catch(JsonProcessingException jsErr) {
			throw new WebApiErrorResponseException("{ error: " + ERR_INVALID_REQUEST_SENT + ", desc: 'Invalid Request Sent'}");
		}
	}
	
}
