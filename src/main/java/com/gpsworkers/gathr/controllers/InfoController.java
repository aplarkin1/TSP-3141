package com.gpsworkers.gathr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {
	@GetMapping("/info")
	public String getLoginPage(Model model) {
		return "info";
	}
}
