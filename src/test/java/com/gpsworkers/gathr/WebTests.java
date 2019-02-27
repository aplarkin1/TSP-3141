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
	public void testInfoRedirectToLoginPageWithoutAuth() throws Exception {
		String url = "/info";
		String loginContents = this.restTemplate.getForObject("http://localhost:" + port + "/login", String.class).toString();
		System.out.println(loginContents);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Login</title>");
		
	}
	
	@Test
	public void getRootReturnsSuccessfullyToHomePage() throws Exception {
		String url = "/";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + url, String.class)).contains("<title>Home</title>");
		
	}
	
}
