package com.gpsworkers.gathr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TspGathRApplication {

	public static void main(String[] args) {
		SpringApplication.run(TspGathRApplication.class, args);
	}

}

