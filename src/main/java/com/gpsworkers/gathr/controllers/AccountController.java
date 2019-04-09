package com.gpsworkers.gathr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gpsworkers.gathr.controllers.responsebodys.UserAccountInformationResponse;

@Controller
public class AccountController {
	
	@Autowired
	private APIService api;
	
	@GetMapping("/account")
	public ModelAndView getLoginPage(Model model) {
		String email = APIController.extractEmailFromAuth(SecurityContextHolder.getContext().getAuthentication());
		UserAccountInformationResponse userInfo = api.getAccountInformation(email);
		ModelAndView modelAndView = new ModelAndView("account");
		modelAndView.addObject("username", userInfo.username);
		modelAndView.addObject("email", userInfo.email);
		modelAndView.addObject("firstname", userInfo.firstname);
		modelAndView.addObject("lastname", userInfo.lastname);
		modelAndView.addObject("country", userInfo.country);
		modelAndView.addObject("state", userInfo.state);
		modelAndView.addObject("city", userInfo.city);
		modelAndView.addObject("lon", userInfo.lon);
		modelAndView.addObject("lat", userInfo.lat);
		modelAndView.addObject("elev", userInfo.elev);
		modelAndView.addObject("groupnames", userInfo.groupNames);
		modelAndView.addObject("location", userInfo.location);
		modelAndView.addObject("coords", userInfo.lat + "/" + userInfo.lon);
		return modelAndView;
	}
	
}
