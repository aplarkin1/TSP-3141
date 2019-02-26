package com.gpsworkers.gathr.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//Credit: https://www.baeldung.com/spring-security-5-oauth2-login.  This tutorial showed me how to setup OAuth2.0 correctly with a custom login page

/**
 * 
 * @author Alexander Larkin
 * This class is used to handle login requests
 */
@Controller
public class LoginController {
	
	//Authentication URLs google, facebook,...etc
	Map<String, String> oAuth2AuthenticationUrls = new HashMap<>();
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	/**
	 * This method is called whenever a GET request is sent for the page /login
	 * @param model is a Spring construct that allows a program to pass info back to the web page
	 * @return the login.html file
	 */
	@GetMapping("/login")
	public String getLoginPage(Model model) {
	    Iterable<ClientRegistration> clientRegistrations = null;
	    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
	    if (type != ResolvableType.NONE && 
	      ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
	        clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
	    }
	 
	    clientRegistrations.forEach(registration -> oAuth2AuthenticationUrls.put(registration.getClientName(), "oauth2/authorization/" + registration.getRegistrationId()));
	    model.addAttribute("urls", oAuth2AuthenticationUrls);
	    
		return "login";	
	}
}
