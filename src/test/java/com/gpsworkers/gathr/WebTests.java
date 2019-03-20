package com.gpsworkers.gathr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gpsworkers.gathr.controllers.APIController;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.controllers.responsebodys.ErrorResponseBody;
import com.gpsworkers.gathr.controllers.responsebodys.UpdateLocationAPIResponseBody;
import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;
import com.gpsworkers.gathr.mongo.users.Location;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WebTests {
	
	@LocalServerPort
	private int port;
	
	//@Autowired
	//private TestRestTemplate restTemplate;
	
	@Autowired
	UserRepository userRepo;
	
	private SeleniumAPI gathrApi;
	
	@After
	public void clean() {
		User user = userRepo.findByEmail("gathrtester1@gmail.com");
		if(user != null) {
			userRepo.deleteById(user.getEmail());
		}
		
		try {
			gathrApi.closeBrowser();
		} catch(RuntimeException e){
			
		}
	}
	
	@Test
	public void getInfoRedirectsToLoginPageWithoutAuthTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getInfo();
		String title = gathrApi.getTitle();
		assertThat(title).isEqualTo("Login");
	}
	
	@Test
	public void getRootWhenUnauthenticatedReturnsLoginPageTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getRoot();
		String title = gathrApi.getTitle();
		assertThat(title).isEqualTo("Login");
	}
	
	@Test
	public void getLoginSuccessRedirectsToLoginPageWithoutAuthTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getLoginSuccess();
		String title = gathrApi.getTitle();
		assertThat(title).isEqualTo("Login");
	}
	
	@Test
	public void getLoginSuccessfullyRespondsWithLoginPageTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getLogin();
		String title = gathrApi.getTitle();
		assertThat(title).isEqualTo("Login");
	}

	@Test
	public void userCreationTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getRoot();
		FirefoxDriver driver = gathrApi.getConfig().getDriver();
		WebElement loginBtn = driver.findElementById("Google");
		loginBtn.click();
		
		WebElement emailTextField = driver.findElementById("identifierId");
		WebElement nextBtn = driver.findElementById("identifierNext");
		emailTextField.sendKeys("gathrtester1@gmail.com");
		nextBtn.click();
		Thread.sleep(3000);
		
		WebElement passwordField = driver.findElementByName("password");
		passwordField.sendKeys("Th3P@ssw0rd");
		WebElement passwordSubmitBtn = driver.findElementById("passwordNext");
		passwordSubmitBtn.click();
		Thread.sleep(3000);
		
		User user = userRepo.findByEmail("gathrtester1@gmail.com");
		
		userRepo.deleteById(user.getEmail());
		assertThat(user).isNotNull();
	}
	
	@Test
	public void userLoginTest() throws Exception {
		
		User newUser = new User("Gathr", "Tester", "gathrtester1@gmail.com");
		userRepo.insert(newUser);
		gathrApi = new SeleniumAPI();
		FirefoxDriver driver = gathrApi.getConfig().getDriver();
		gathrApi.getRoot();
		Thread.sleep(2000);
		WebElement loginBtn = driver.findElementById("Google");
		loginBtn.click();
		
		WebElement emailTextField = driver.findElementById("identifierId");
		WebElement nextBtn = driver.findElementById("identifierNext");
		emailTextField.sendKeys("gathrtester1@gmail.com");
		nextBtn.click();
		Thread.sleep(3000);
		
		WebElement passwordField = driver.findElementByName("password");
		passwordField.sendKeys("Th3P@ssw0rd");
		WebElement passwordSubmitBtn = driver.findElementById("passwordNext");
		passwordSubmitBtn.click();
		Thread.sleep(3000);
		
		String title = driver.getTitle();
		
		User user = userRepo.findByEmail("gathrtester1@gmail.com");
		userRepo.deleteById(user.getEmail());
		assertThat(title).isEqualTo("Home");
	}
	
	@Test
	public void updateLocationValidTest() throws Exception {
		gathrApi = new SeleniumAPI();
		gathrApi.getRoot();
		FirefoxDriver driver = gathrApi.getConfig().getDriver();
		WebElement loginBtn = driver.findElementById("Google");
		loginBtn.click();
		
		WebElement emailTextField = driver.findElementById("identifierId");
		WebElement nextBtn = driver.findElementById("identifierNext");
		emailTextField.sendKeys("gathrtester1@gmail.com");
		nextBtn.click();
		Thread.sleep(3000);
		
		WebElement passwordField = driver.findElementByName("password");
		passwordField.sendKeys("Th3P@ssw0rd");
		WebElement passwordSubmitBtn = driver.findElementById("passwordNext");
		passwordSubmitBtn.click();
		
		Thread.sleep(20000);
		Location loc = userRepo.findByEmail("gathrtester1@gmail.com").getCurrentLocation();
		User user = userRepo.findByEmail("gathrtester1@gmail.com");
		userRepo.deleteById(user.getEmail());
		assertThat(loc).isNotEqualTo(null);
	}
	
	@Test
	public void userBackEndTest() throws Exception {
		User newUser = new User("Gathr", "Tester", "gathrtester1@gmail.com");
		userRepo.insert(newUser);
		
		User retrievedUser = userRepo.findByEmail("gathrtester1@gmail.com");
		String firstName = retrievedUser.getFirstName();
		String lastName = retrievedUser.getLastName();
		String email = retrievedUser.getEmail();
		
		String originalUserString = "Firstname: Gathr, Lastname: Tester, Email: gathrtester1@gmail.com";
		String retrievedUserString = "Firstname: " + firstName + ", Lastname: " + lastName + ", Email: " + email;
		assertThat(originalUserString).isEqualTo(retrievedUserString);
		userRepo.delete(newUser);
	}
	
	
}