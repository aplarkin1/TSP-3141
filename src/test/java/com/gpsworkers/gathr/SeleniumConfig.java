package com.gpsworkers.gathr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;

public class SeleniumConfig {
	private FirefoxDriver driver;
	private static String OS = System.getProperty("os.name").toLowerCase();

	public SeleniumConfig() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	static {
		if (OS.indexOf("win") >= 0) {
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver/geckodriver-win.exe");
		} else if(OS.indexOf("mac") >= 0) {
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver/geckodriver-mac");
		} else {
			System.setProperty("webdriver.gecko.driver", "/src/main/resources/geckodriver/geckodriver-linux");
		}
	}
	
	public FirefoxDriver getDriver() {
		return driver;
	}


}
