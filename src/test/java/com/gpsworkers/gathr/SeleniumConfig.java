package com.gpsworkers.gathr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;

public class SeleniumConfig {
	private FirefoxDriver driver;

	public SeleniumConfig() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	static {
		System.setProperty("webdriver.gecko.driver", "C:/Users/Alexander Larkin/Desktop/geckodriver.exe");
	}
	
	public FirefoxDriver getDriver() {
		return driver;
	}
	
	
}
