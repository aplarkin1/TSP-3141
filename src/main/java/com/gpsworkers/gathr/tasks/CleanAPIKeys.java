package com.gpsworkers.gathr.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanAPIKeys {
	
	@Scheduled(fixedDelay=600000)
	public void run() {
		
	}
	
}
