package com.gpsworkers.gathr;

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

@Controller
public class LoginController {
	
	Map<String, String> oAuth2AuthenticationUrls = new HashMap<>();
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	@GetMapping("/login")
	public String getLoginPage(Model model) {
	    Iterable<ClientRegistration> clientRegistrations = null;
	    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
	    if (type != ResolvableType.NONE && 
	      ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
	        clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
	    }
	 
	    clientRegistrations.forEach(registration -> oAuth2AuthenticationUrls.put(registration.getClientName(), "oauth2/authorization/" + registration.getRegistrationId()));
	    System.out.println("THIS IS THE AMOUNT OF OAUTHS BOI: " + oAuth2AuthenticationUrls.size());
	    model.addAttribute("urls", oAuth2AuthenticationUrls);
	    
		return "login";
		
	}
}
