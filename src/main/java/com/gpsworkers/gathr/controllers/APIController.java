package com.gpsworkers.gathr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

/**
 * 
 * @author Alexander Larkin
 * This class is used to control API access
 */
@RestController
public class APIController {
	
	public static int ERR_EXP_OR_FAKE_TOKEN = -9;
	public static int ERR_INVALID_TOKEN = -2;
	public static int ERR_INVALID_REQUEST_SENT = -1;
	public static int ERR_MISSING_FIELD_IN_REQUEST = -3;
	
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
	@ResponseBody
	public ResponseEntity<String> updateLocation(UpdateLocationAPIRequestBody request) {
		System.out.println("HELLO WORLD!!!");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			
		System.out.println("NAME: " + auth.getPrincipal());
		    
		String authString = "" + auth.getPrincipal();
	    authString = authString.replace("[", "");
		authString = authString.replace("]", "");
		System.out.println(authString);
		String userEmail = authString.split("email=")[1];
		System.out.println("EMAIL: " + userEmail);
		    
		User validUser = users.findByEmail(userEmail);
		    
		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyC2OKbwa0DhWHlA9cp8WxJP2TIRopz9daY").build();
		try {
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(request.lat, request.lon)).await();
			if(results.length == 0) {
				return new ResponseEntity<>("-1", HttpStatus.FAILED_DEPENDENCY);
			}
			String fullAddress = results[1].formattedAddress;
			String[] fullAddressSplit = fullAddress.split(",");
			System.out.println(fullAddress);
			String city = fullAddressSplit[1].split(" ")[1];
			String state = fullAddressSplit[2].split(" ")[1];
			String country = fullAddressSplit[3].split(" ")[1];

			System.out.println(country + "->" + state + "->" + city);

			validUser.updateLocation(request.lat, request.lon, request.elev, country, state, city);
			users.save(validUser);
		} catch (Exception e) {
			System.out.println("Geocoding Connection Failed!");
			e.printStackTrace();
			return new ResponseEntity<>("-1", HttpStatus.FAILED_DEPENDENCY);
		}
		return new ResponseEntity<>("1", HttpStatus.OK);

	}
	
}