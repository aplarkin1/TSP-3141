package com.gpsworkers.gathr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;

import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.web.WebAppConfiguration;

//import com.gpsworkers.gathr.controllers.APIController;
import com.gpsworkers.gathr.controllers.APIService;
import com.gpsworkers.gathr.controllers.responsebodys.GetLocationResponse;
import com.gpsworkers.gathr.exceptions.EmptyGeocodingResultException;
//import com.gpsworkers.gathr.controllers.responsebodys.ErrorResponseBody;
//import com.gpsworkers.gathr.controllers.responsebodys.UpdateLocationAPIResponseBody;
import com.gpsworkers.gathr.exceptions.EmptyMessageException;
import com.gpsworkers.gathr.exceptions.GeoCodingConnectionFailedException;
import com.gpsworkers.gathr.exceptions.GroupDoesntExistException;
import com.gpsworkers.gathr.exceptions.GroupIdAlreadyInUseException;
import com.gpsworkers.gathr.exceptions.MessageUserIdCannotBeEmptyException;
import com.gpsworkers.gathr.exceptions.TargetUserNotFoundException;
import com.gpsworkers.gathr.exceptions.UnauthorizedGroupManagementException;
import com.gpsworkers.gathr.exceptions.UnauthorizedUserInteractionException;
import com.gpsworkers.gathr.exceptions.UserNotFoundException;
//import com.gpsworkers.gathr.mongo.communications.CommunicationsNetwork;
//import com.gpsworkers.gathr.mongo.communications.CommunicationsNetworkRepository;
import com.gpsworkers.gathr.mongo.communications.Message;
import com.gpsworkers.gathr.mongo.groups.Group;
import com.gpsworkers.gathr.mongo.groups.GroupRepository;
import com.gpsworkers.gathr.mongo.users.Location;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebTests {

	private static final String TEST_GROUP_ADMIN_EMAIL = "testadmin@gmail.com";
	private static final String TEST_USER_1_EMAIL = "testuser1@gmail.com";
	private static final String TEST_USER_2_EMAIL = "testuser2@gmail.com";
	private static final String WEB_USER_GMAIL = "wwwest09@gmail.com";

	private static final String TEST_GROUP_ID = "THE TESTING GROUP!";
	private static final String TEST_WEB_GROUP_ID = "WEB TESTING GROUP!";
	
	

	@LocalServerPort
	private int port;

	//@Autowired
	//private TestRestTemplate restTemplate;

	@Autowired
	UserRepository userRepo;

	@Autowired
	GroupRepository groups;

	@Autowired
	private APIService api;

	private SeleniumAPI gathr;

	@Before
	public void initialize() throws UnauthorizedUserInteractionException, UserNotFoundException {
		api.systemDeleteGroup(TEST_WEB_GROUP_ID);
		api.systemDeleteGroup(TEST_GROUP_ID);
		api.systemDeleteUser(TEST_USER_1_EMAIL);
		api.systemDeleteUser(TEST_USER_2_EMAIL);
		api.systemDeleteUser(TEST_GROUP_ADMIN_EMAIL);
		
		
		userRepo.insert(new User("ADMIN", "Test", TEST_GROUP_ADMIN_EMAIL));
		userRepo.insert(new User("USER1", "Test", TEST_USER_1_EMAIL));
		userRepo.insert(new User("USER2", "Test", TEST_USER_2_EMAIL));
		
		if(userRepo.findById(APIService.GLOBAL_ADMIN_EMAIL).isPresent() == false) {
			userRepo.save(new User("Admin", "Global", APIService.GLOBAL_ADMIN_EMAIL));
		}
		
		api.systemDeleteGroup(TEST_WEB_GROUP_ID);
		api.systemDeleteGroup("USA->MI->Houghton");
		
		
	}

	@After
	public void clean() {

	}

	@Test
	public void getInfoRedirectsToLoginPageWithoutAuthTest() throws Exception {
		gathr = new SeleniumAPI();
		gathr.getInfo();
		String title = gathr.getTitle();
		gathr.closeBrowser();
		assertThat(title).isEqualTo("Login");
	}

	@Test
	public void getRootWhenUnauthenticatedReturnsLoginPageTest() throws Exception {
		gathr = new SeleniumAPI();
		gathr.getRoot();
		String title = gathr.getTitle();
		gathr.closeBrowser();
		assertThat(title).isEqualTo("Login");
	}

	@Test
	public void getLoginSuccessRedirectsToLoginPageWithoutAuthTest() throws Exception {
		gathr = new SeleniumAPI();
		gathr.getLoginSuccess();
		String title = gathr.getTitle();
		gathr.closeBrowser();
		assertThat(title).isEqualTo("Login");
	}

	@Test
	public void getLoginSuccessfullyRespondsWithLoginPageTest() throws Exception {
		gathr = new SeleniumAPI();
		gathr.getLogin();
		String title = gathr.getTitle();
		gathr.closeBrowser();
		assertThat(title).isEqualTo("Login");
	}

	public boolean updateLocationValidTest() throws Exception {
		gathr.getHome();
		Thread.sleep(20000);
		Location loc = userRepo.findByEmail(TEST_USER_1_EMAIL).getCurrentLocation();
		return loc != null;
	}

	/*
	@Test
	public void userBackEndTest() throws Exception {
		User retrievedUser = userRepo.findByEmail(TEST_USER_1_EMAIL);
		String firstName = retrievedUser.getFirstName();
		String lastName = retrievedUser.getLastName();
		String email = retrievedUser.getEmail();
		if(firstName == null || lastName == null || email == null) {
			throw new RuntimeException("User Backend Test Failure");
		}
	}
	*/
	@Test
	public void apiGroupFirstTestCreate() throws GroupIdAlreadyInUseException, UserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		assertThat(groups.findById(TEST_GROUP_ID).isPresent()).isTrue();
	}

	@Test
	public void apiGroupSecondTestInvite() throws GroupIdAlreadyInUseException, UserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		User targetUser = userRepo.findById(TEST_USER_1_EMAIL).get();
		int preInviteInvitationsSize = targetUser.getGroupInvites().size();
		api.inviteUserToGroup(TEST_GROUP_ID, TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, "you should join...or else!!!!");
		targetUser = userRepo.findById(TEST_USER_1_EMAIL).get();
		int postInviteInvitationsSize = targetUser.getGroupInvites().size();
		assertThat(postInviteInvitationsSize).isGreaterThan(preInviteInvitationsSize);
	}

	@Test
	public void apiGroupThirdTestDelete() throws GroupIdAlreadyInUseException, UserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.deleteGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		assertThat(groups.findById(TEST_GROUP_ID).isPresent()).isFalse();
	}

	public boolean groupCreationWebRequestTest() throws Exception {
		String url = "/api/createGroup";
		HashMap<String, Object> keyValuePairs = new HashMap<String, Object>();
		keyValuePairs.put("groupId", TEST_WEB_GROUP_ID);
		String response = gathr.executePost(url, keyValuePairs);
		Thread.sleep(5000);
		boolean groupFound = groups.existsById((String)keyValuePairs.get("groupId"));
		if(groupFound == true) {
			System.out.println("Group " + keyValuePairs.get("groupId") + " found");
		} else {
			System.out.println("Group " + keyValuePairs.get("groupId") + " not found");
		}
		Thread.sleep(3000);
		return groupFound;
	}
	/*
	public boolean groupInvitationGenerationTest() throws Exception {
		String url = "/api/createGroup";
		HashMap<String, Object> keyValuePairs = new HashMap<String, Object>();
		String response = "";

		url = "/api/inviteUserToGroup";
		keyValuePairs = new HashMap<String, Object>();
		keyValuePairs.put("groupId", "TESTING123");
		keyValuePairs.put("userEmail", "lolme@gmail.com");
		keyValuePairs.put("invitationMessage", "You want to join our group?");
		Optional<User> invitedUserPreInvitationState = userRepo.findById((String)keyValuePairs.get("userEmail"));
		boolean wasInvited = false;

		response = gathr.executePost(url, keyValuePairs);
		Thread.sleep(10000);

		Optional<User> invitedUserPostInvitationState = userRepo.findById((String)keyValuePairs.get("userEmail"));

		if(invitedUserPostInvitationState.get().getGroupInvites().size() == invitedUserPreInvitationState.get().getGroupInvites().size() + 1) {
			System.out.println("User " + invitedUserPreInvitationState.get().getEmail() + "was invited");
			invitedUserPostInvitationState.get().getGroupInvites().remove(invitedUserPostInvitationState.get().getGroupInvites().size() - 1);
			userRepo.save(invitedUserPostInvitationState.get());
			wasInvited = true;
		} else {
			System.out.println("User " + invitedUserPreInvitationState.get().getEmail() + "was not invited");
		}

		return wasInvited;
	}
	*/
	//
	/*
	@Test
	public void userInteractionTest() throws Exception {

		if(groups.existsById("TESTING123")) {
			groups.delete(groups.findById("TESTING123").get());
		}

		if(userRepo.findById(TEST_USER_2_EMAIL).isPresent()) {
			userRepo.delete(userRepo.findById(TEST_USER_2_EMAIL).get());
		}

		User targetUser = new User("NEW", "TESTER", "lolme@gmail.com");
		userRepo.save(targetUser);


		gathr = new SeleniumAPI();
		login(gathr);

		boolean results = updateLocationValidTest();

		results = groupCreationWebRequestTest();

		//results = groupInvitationGenerationTest();

		gathr.closeBrowser();

		assertThat(results).isEqualTo(true);
	}
	*/
	//
	public void login(SeleniumAPI gathr) throws InterruptedException {
		gathr.getRoot();
		FirefoxDriver driver = gathr.getConfig().getDriver();
		WebElement loginBtn = driver.findElementById("Google");
		loginBtn.click();

		Thread.sleep(6000);
		WebElement emailTextField = driver.findElementById("identifierId");
		WebElement nextBtn = driver.findElementById("identifierNext");
		emailTextField.sendKeys(WEB_USER_GMAIL);
		nextBtn.click();
		Thread.sleep(7000);

		WebElement passwordField = driver.findElementByName("password");
		passwordField.sendKeys("Th3P@ssw0rd");
		WebElement passwordSubmitBtn = driver.findElementById("passwordNext");
		passwordSubmitBtn.click();
		Thread.sleep(30000);
	}

	@Test
	public void groupCommunicationNetworkFirstTestCreation() throws GroupIdAlreadyInUseException, UserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		assertThat(groups.findById(TEST_GROUP_ID).isPresent()).isTrue();
	}

	@Test
	public void groupCommunicationNetworkSecondTestPostingMessage() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {

		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		Group group = groups.findById(TEST_GROUP_ID).get();

		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);


		User poster = userRepo.findById(TEST_USER_1_EMAIL).get();

		api.postMessageInGroup("This is my first message to you all!", TEST_GROUP_ID, TEST_USER_1_EMAIL);

		group = groups.findById(TEST_GROUP_ID).get();

		Message message = group.getGroupCommsNetwork().getLastMessage();
		assertThat(message.getMessageContent()).isEqualTo("This is my first message to you all!");
	}

	@Test
	public void groupCommunicationNetworkSecondTestReadingPostedMessage() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		Group group = groups.findById(TEST_GROUP_ID).get();

		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_2_EMAIL, TEST_GROUP_ID);
		
		api.postMessageInGroup("This is my first message to you all!", TEST_GROUP_ID, TEST_USER_1_EMAIL);
		User readingUser = userRepo.findById(TEST_USER_2_EMAIL).get();

		for(String groupName : readingUser.getGroupNames()) {
			//System.out.println("Group!!!!!");
			if(groupName.equals(TEST_GROUP_ID)) {
				Group alternateGroupReference = groups.findById(groupName).get();
				//System.out.println("HELLO WORLD !!!!!!!");
				Message message = alternateGroupReference.getGroupCommsNetwork().getLastMessage();
				assertThat(message.getMessageContent()).isEqualTo("This is my first message to you all!");
				return;
			}
		}
		throw new RuntimeException("Alternate Group Reference Test Failed!");
	}
	
	@Test
	public void groupCommunicationNetworkAdminDeletePostedMessage() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_2_EMAIL, TEST_GROUP_ID);
		api.postMessageInGroup("This is my first message to you all!", TEST_GROUP_ID, TEST_USER_1_EMAIL);

		if(groups.findById(TEST_GROUP_ID).get().getGroupCommsNetwork().getAllMessages().size() > 0) {
			Group group = groups.findById(TEST_GROUP_ID).get();
			group = groups.findById(TEST_GROUP_ID).get();
			api.deleteMessageInGroup(TEST_GROUP_ID, 0, TEST_GROUP_ADMIN_EMAIL);
			assertThat(api.getAllGroupMessages(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID).size()).isEqualTo(0);
		} else {
			throw new RuntimeException("Message was never posted!");
		}
	}
	
	@Test
	public void groupCommunicationNetworkDeletePostedMessageBySelfTest() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_2_EMAIL, TEST_GROUP_ID);
		api.postMessageInGroup("This is my first message to you all!", TEST_GROUP_ID, TEST_USER_1_EMAIL);

		if(groups.findById(TEST_GROUP_ID).get().getGroupCommsNetwork().getAllMessages().size() > 0) {
			Group group = groups.findById(TEST_GROUP_ID).get();
			group = groups.findById(TEST_GROUP_ID).get();
			api.deleteMessageInGroup(TEST_GROUP_ID, 0, TEST_USER_1_EMAIL);
			assertThat(api.getAllGroupMessages(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID).size()).isEqualTo(0);
		} else {
			throw new RuntimeException("Message was never posted!");
		}
	}

	@Test
	public void groupDeletionTest() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);

		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_2_EMAIL, TEST_GROUP_ID);
		
		api.deleteGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		assertThat(groups.findById(TEST_GROUP_ID).isPresent()).isEqualTo(false);
	}
	
	@Test
	public void groupSummaryTest() throws EmptyMessageException, MessageUserIdCannotBeEmptyException, Exception {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		Group group = groups.findById(TEST_GROUP_ID).get();

		assertThat(api.getGroupSummary(TEST_GROUP_ID).size()).isGreaterThan(0);
	}
	
	@Test
	public void getUserAccountInfoTest() throws EmptyGeocodingResultException, GeoCodingConnectionFailedException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException, UserNotFoundException {
		api.updateLocation(TEST_USER_1_EMAIL, 47.11625, -88.54010, 0.0);
		assertThat(api.getAccountInformation(TEST_USER_1_EMAIL)).isNotNull();
	}
	
	@Test
	public void updateUserLocationAndAgetUserLocationTest() throws EmptyGeocodingResultException, GeoCodingConnectionFailedException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException, UserNotFoundException {
		api.updateLocation(TEST_USER_1_EMAIL, 47.11625, -88.54010, 0.0);
		GetLocationResponse location = api.getUserLocation(TEST_USER_1_EMAIL);
		String locationString = "" + location.lat + "" + location.lon + "" + location.city + "" + location.state + "" + location.country;
		//System.out.println(location);
		assertThat(locationString).isNotEmpty();
	}

	@Test
	public void updateUserLocationAndCheckCityBasedGroup() throws EmptyGeocodingResultException, GeoCodingConnectionFailedException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException, UserNotFoundException {
		api.updateLocation(TEST_USER_1_EMAIL, 47.11625, -88.54010, 0.0);
		User user = userRepo.findById(TEST_USER_1_EMAIL).get();
		assertThat(user.getGroupNames().size()).isGreaterThan(0);
	}
	
	@Test
	public void updateMultipleUsersAndRetrieveTheirLocationsFromGroupTest() throws InterruptedException, GroupIdAlreadyInUseException, UserNotFoundException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_2_EMAIL, TEST_GROUP_ID);
		
		int i = 0;
		while(i < 2) {
			try {
				//System.out.println("Geo Coding Connection: Round 1");
				api.updateLocation(TEST_GROUP_ADMIN_EMAIL, 47.11625, -88.54010, 0.0);
				//System.out.println("Geo Code Admin!");
				api.updateLocation(TEST_USER_1_EMAIL, 47.11625, -88.54010, 0.0);
				//System.out.println("Geo Code User 1!");
				api.updateLocation(TEST_USER_2_EMAIL, 47.11625, -88.54010, 0.0);
				//System.out.println("Geo Code User 2!");
			} catch(GeoCodingConnectionFailedException e) {
				System.out.println("Geo Coding Failed!");
			}
			i++;
			Thread.sleep(2000);
		}
		
		HashMap<String, GetLocationResponse> locationResponses = api.getLocationsOfGroupMembers(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		//System.out.print("THE RESPONSE SIZE IS: " + locationResponses.size());
		if(locationResponses.size() == 3) {
			assertThat(true).isEqualTo(true);
		} else {
			throw new RuntimeException("Location responses size is not 3");
		}
	}
	
	@Test
	public void addUserToGroupTest() throws InterruptedException, GroupIdAlreadyInUseException, UserNotFoundException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		assertThat(api.getGroupNamesOfUser(TEST_USER_1_EMAIL));
	}
	
	@Test
	public void removeUserFromGroupTest() throws InterruptedException, GroupIdAlreadyInUseException, UserNotFoundException, GroupDoesntExistException, UnauthorizedGroupManagementException, TargetUserNotFoundException, UnauthorizedUserInteractionException {
		api.createGroup(TEST_GROUP_ADMIN_EMAIL, TEST_GROUP_ID);
		api.addUserToGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
		if(api.getGroupNamesOfUser(TEST_USER_1_EMAIL).contains(TEST_GROUP_ID)) {
			api.removeUserFromGroup(TEST_GROUP_ADMIN_EMAIL, TEST_USER_1_EMAIL, TEST_GROUP_ID);
			assertThat(api.getGroupNamesOfUser(TEST_USER_1_EMAIL).contains(TEST_GROUP_ID));	
		}
	}
	
}
