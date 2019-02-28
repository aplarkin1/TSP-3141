package com.gpsworkers.gathr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.gpsworkers.gathr.controllers.APIController;
import com.gpsworkers.gathr.controllers.requestbodys.UpdateLocationAPIRequestBody;
import com.gpsworkers.gathr.controllers.responsebodys.ErrorResponseBody;
import com.gpsworkers.gathr.controllers.responsebodys.UpdateLocationAPIResponseBody;
import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;
import com.gpsworkers.gathr.mongo.users.User;
import com.gpsworkers.gathr.mongo.users.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	UserRepository userRepo;
	
	@Test
	public void getInfoRedirectsToLoginPageWithoutAuthTest() throws Exception {
		String url = "/info";
		String loginContents = this.restTemplate.getForObject("http://localhost:" + port + "/login", String.class).toString();
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Login</title>");
		
	}
	
	@Test
	public void getRootReturnsHomePageTest() throws Exception {
		String url = "/";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Home</title>");
	}
	
	@Test
	public void getHomeReturnsHomePageTest() throws Exception {
		String url = "/home";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Home</title>");
	}
	
	@Test
	public void getLoginSuccessRedirectsToLoginPageWithoutAuthTest() throws Exception {
		String url = "/loginSuccess";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Login</title>");
	}
	
	@Test
	public void getLoginSuccessfullyRespondsWithLoginPageTest() throws Exception {
		String url = "/login";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Login</title>");
	}
	
	@Test
	public void updateLocationWithInvalidTokenFailsTest() throws Exception {
		String url = "/api/updateLocation";
		assertThat(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody("0", 0, 0, 0), String.class).getBody())
		.isEqualTo(GathrJSONUtils.write(new ErrorResponseBody(APIController.ERR_INVALID_TOKEN, "Invalid token sent")));
	}
	
	@Test
	public void updateLocationWithFakeTokenFailsTest() throws Exception {
		String url = "/api/updateLocation";
		String hexString = "5c77056c2ed3cf1b10e06a91";
		assertThat(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody(hexString, 0, 0, 0), String.class).getBody())
		.isEqualTo(GathrJSONUtils.write(new ErrorResponseBody(APIController.ERR_EXP_OR_FAKE_TOKEN, "Expired or fake token sent")));
	}
	
	@Test
	public void updateLocationWithoutTokenFailsTest() throws Exception {
		String url = "/api/updateLocation";
		System.out.println(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody("", 0, 0, 0), String.class).getBody());
		assertThat(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody("", 0, 0, 0), String.class).getBody())
		.isEqualTo(GathrJSONUtils.write(new ErrorResponseBody(APIController.ERR_INVALID_TOKEN, "Invalid token sent")));
	}
	
	@Test
	public void updateLocationValidTest() throws Exception {
		String url = "/api/updateLocation";
		System.out.println(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody("", 0, 0, 0), String.class).getBody());
		
		User user = new User("Alex", "Larkin", "me@newemail.com");
		userRepo.delete(user);
		
		user.setApiToken(User.generateToken());
		System.out.println(user.getAPIToken());
		userRepo.save(user);
		
		
		
		assertThat(this.restTemplate.postForEntity("http://localhost:" + port + url, new UpdateLocationAPIRequestBody(user.getAPIToken(), 25.0, 43.0, 0), String.class).getBody())
		.isEqualTo("1");
		
		userRepo.delete(user);
	}
	
	
	
}
