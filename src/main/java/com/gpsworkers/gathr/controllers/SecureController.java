package com.gpsworkers.gathr.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class SecureController {
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	 
	@GetMapping("/secured")
	public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
	    OAuth2AuthorizedClient client = authorizedClientService
	      .loadAuthorizedClient(
	        authentication.getAuthorizedClientRegistrationId(), 
	          authentication.getName());
	    
	    String userCredsUrl = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
	    
	    
	    RestTemplate restTemp = new  RestTemplate();
	    HttpHeaders webRequestHeaders = new HttpHeaders();
	    webRequestHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
	    
	    HttpEntity<String> webRequest = new HttpEntity<String>("", );
	    return "loginSuccess";
	}
}
