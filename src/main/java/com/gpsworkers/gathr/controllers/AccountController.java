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
		
		
		
		return modelAndView;
	}
	
}
