package com.gpsworkers.gathr.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.gpsworkers.gathr.mongo.users.UserRepository;

/**
 * 
 * @author Alexander Larkin
 * This class is used for the OAuth2.0 callback if the login through OAuth2.0 was successful.
 */
@Controller
public class LoginSuccessController {
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	//User storage repo of our own...not OAuth related...is our own personal MongoDB user storage
	@Autowired
	UserRepository userRepo;
	
	/**
	 * This method is called whenever a GET request is sent for the url of /loginSuccess
	 * This method is called if a web client successfully authenticates with OAuth2.0
	 * @param model is a Spring construct that allows a program to pass info back to the web page
	 * @param authentication contains a token for the server to retrieve further information about the user
	 * @return the login.html page with the email and name posted on the page.
	 */
	@GetMapping("/loginSuccess")
	public ModelAndView loginSuccess(Model model, OAuth2AuthenticationToken authentication) {
	    OAuth2AuthorizedClient client = authorizedClientService
	      .loadAuthorizedClient(
	        authentication.getAuthorizedClientRegistrationId(), 
	          authentication.getName());
	    
	    String userCredsUrl = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
	    
	    RestTemplate restTemp = new  RestTemplate();
	    HttpHeaders webRequestHeaders = new HttpHeaders();
	    webRequestHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
	    
	    HttpEntity<String> webRequest = new HttpEntity<String>("", webRequestHeaders);
	    
	    ResponseEntity<Map> webResponse = restTemp.exchange(userCredsUrl, HttpMethod.GET, webRequest, Map.class);
	    
	    Map userAttributes = webResponse.getBody();
	    //System.out.println(userAttributes.get("email"));
	    //System.out.println(userAttributes.get("name"));
	    
	    //Code for to retrieve firstname, lastname, and email address from Google.....
	    String firstName = userAttributes.get("name").toString().split(" ")[0];
	    String lastName = userAttributes.get("name").toString().split(" ")[1];
	    String email = userAttributes.get("email").toString();
	    
	    
	    // CODE TO CHECK IF USER IS ALREADY IN DATABASE OR NOT
	    // IF USER IS IN DATABASE, SEND THEM TO HOME OR SOMETHING
	    // IF A USER IS NOT IN THE DATABASE, TAKE THEM THROUGH A USER CREATION TOOL.....?
	    
        ModelAndView mav = new ModelAndView("loginSuccess");
        mav.addObject("email", email);
        mav.addObject("fname", firstName);
        mav.addObject("lname", lastName);

        //Check if user exists, if not then create a new user and generate a new token for the user User.generateToken() will return an ObjectID()...token
        
        //If user exists, then check to see if the user has a valid token
        
        //Generate new token if user is valid and has no token
        
        //Insert new user by typing userRepo.insert(new User()) or if the user does exist, then save the new user by typing userRepo.save(new User())
        
	    
	    return mav;
	}
}
