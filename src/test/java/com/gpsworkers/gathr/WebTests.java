package com.gpsworkers.gathr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
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
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
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
	
	@Autowired
	GroupRepository groups;
	
	private SeleniumAPI gathrApi;
	
	@After
	public void clean() {
		User user = userRepo.findByEmail("wwwest09@gmail.com");
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
	
	public boolean updateLocationValidTest() throws Exception {
		User user = userRepo.findByEmail("wwwest09@gmail.com");
		user.setLocation(null);
		gathrApi.getHome();
		Thread.sleep(20000);
		Location loc = userRepo.findByEmail("wwwest09@gmail.com").getCurrentLocation();
		return loc != null;
	}
	
	@Test
	public void userBackEndTest() throws Exception {
		User newUser = new User("Gathr", "Tester", "wwwest09@gmail.com");
		userRepo.insert(newUser);
		User retrievedUser = userRepo.findByEmail("wwwest09@gmail.com");
		String firstName = retrievedUser.getFirstName();
		String lastName = retrievedUser.getLastName();
		String email = retrievedUser.getEmail();
		
		String originalUserString = "Firstname: Gathr, Lastname: Tester, Email: wwwest09@gmail.com";
		String retrievedUserString = "Firstname: " + firstName + ", Lastname: " + lastName + ", Email: " + email;
		assertThat(originalUserString).isEqualTo(retrievedUserString);
		userRepo.delete(newUser);
	}
	
	
	public boolean groupCreationWebRequestTest() throws Exception {
		String url = "/api/createGroup";
		HashMap<String, Object> keyValuePairs = new HashMap<String, Object>();
		keyValuePairs.put("groupId", "TESTING123");
		String response = gathrApi.executePost(url, keyValuePairs);
		Thread.sleep(30000);
		
		boolean groupFound = false;
		Group group = groups.findById((String)keyValuePairs.get("groupId")).get();
		
		if(group != null) {
			groupFound = true;
			groups.delete(group);
		}
		
		return groupFound;
	}
	
	@Test
	public void UserInteractionTest() throws Exception {
		gathrApi = new SeleniumAPI();
		login(gathrApi);
		
		boolean results = updateLocationValidTest();
		
		//results = groupCreationWebRequestTest();
		
		assertThat(results).isEqualTo(true);
	}
	
	public void login(SeleniumAPI gathrApi) throws InterruptedException {
		gathrApi.getRoot();
		FirefoxDriver driver = gathrApi.getConfig().getDriver();
		WebElement loginBtn = driver.findElementById("Google");
		loginBtn.click();
		
		WebElement emailTextField = driver.findElementById("identifierId");
		WebElement nextBtn = driver.findElementById("identifierNext");
		emailTextField.sendKeys("wwwest09@gmail.com");
		nextBtn.click();
		Thread.sleep(7000);
		
		WebElement passwordField = driver.findElementByName("password");
		passwordField.sendKeys("Th3P@ssw0rd");
		WebElement passwordSubmitBtn = driver.findElementById("passwordNext");
		passwordSubmitBtn.click();
		Thread.sleep(7000);
	}
	
	
	
	
}