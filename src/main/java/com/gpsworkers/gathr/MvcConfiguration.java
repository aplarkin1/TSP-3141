package com.gpsworkers.gathr;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer{

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/about").setViewName("info");
		registry.addViewController("/api").setViewName("API");
		registry.addViewController("/loginSucess").setViewName("loginSuccess");
		registry.addViewController("/contactus").setViewName("contactus");
		registry.addViewController("/account").setViewName("account");
		registry.addViewController("/chat").setViewName("chat");
		registry.addViewController("/groups").setViewName("groups");
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**", "/css/**", "/js/**")
                .addResourceLocations("classpath:/static/images/", "classpath:/static/css/", "classpath:/static/js/");
	}

}
