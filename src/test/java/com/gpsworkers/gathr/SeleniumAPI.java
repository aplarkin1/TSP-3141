package com.gpsworkers.gathr;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpsworkers.gathr.gathrutils.GathrJSONUtils;

public class SeleniumAPI {
	
	SeleniumConfig config;
	private String baseUri = "https://localhost:8443/";
	private String rootUri = "";
	private String loginUri = "login";
	private String infoUri = "info";
	private String loginSuccessUri = "loginSuccess";
	
	public SeleniumAPI() {
		config = new SeleniumConfig();
	}
	
	public void getRoot() {
		System.out.println(config.getDriver());
		config.getDriver().get(baseUri + rootUri);
	}

	public void getLogin() {
		config.getDriver().get(baseUri + loginUri);
	}
	
	public void getInfo() {
		config.getDriver().get(baseUri + infoUri);
	}
	
	public void getHome() {
		config.getDriver().get(baseUri + "home");
	}
	
	public void getLoginSuccess() {
		config.getDriver().get(baseUri + loginSuccessUri);
	}
	
	public void closeBrowser() {
		this.config.getDriver().close();
	}
	
	public String getTitle() {
		return this.config.getDriver().getTitle();
	}
	
	public SeleniumConfig getConfig() {
		return config;
	}
	
	public String executePost(String path, HashMap<String, Object> keyValuePairs) throws JsonProcessingException {
		Object[] empty = new Object[0];
		String keyValuePairJSON = GathrJSONUtils.write(keyValuePairs);
		StringBuilder builder = new StringBuilder();
		builder.append("$.post('");
		builder.append(path);
		builder.append("', ");
		String request = String.format("$.post('%s', %s)", path, keyValuePairJSON);
		System.out.println(request);
		
		return (String)config.getDriver().executeScript(request, empty);
	}
	
}
