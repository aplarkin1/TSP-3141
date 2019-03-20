package com.gpsworkers.gathr;

public class SeleniumAPI {
	
	SeleniumConfig config;
	private String baseUri = "http://localhost:8080/";
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
	
}
