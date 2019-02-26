package com.gpsworkers.gathr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author Alexander Larkin
 * This controller is used to handle requests for the /info page of the web app
 * 
 */
@Controller
public class InfoController {
	/**
	 * This method is called whenever a Get request is sent for the /info path
	 * @param model is a Spring construct that allows a program to pass info back to the web page
	 * @return the actual info.html file
	 */
	@GetMapping("/info")
	public String getLoginPage(Model model) {
		return "info";
	}
}
