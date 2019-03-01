package com.gpsworkers.gathr;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gpsworkers.gathr.controllers.APIController;

@RunWith(SpringRunner.class)
@WebMvcTest(APIController.class)
public class APIControllerTests {
	  @Autowired
	  private MockMvc mvc;
	  
}
