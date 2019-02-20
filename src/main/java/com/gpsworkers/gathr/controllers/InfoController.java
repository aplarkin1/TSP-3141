package com.gpsworkers.gathr.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {
	@GetMapping("/info")
	public String getLoginPage(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
        Map<String, String> details = new LinkedHashMap<>();
        details = (Map<String, String>) auth.getDetails();
		
		System.out.println(details.get("email") + "-USERNAME");
		return "info";
	}
}
