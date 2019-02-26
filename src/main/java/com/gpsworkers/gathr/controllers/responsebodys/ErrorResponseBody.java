package com.gpsworkers.gathr.controllers.responsebodys;

public class ErrorResponseBody {
	int error;
	String desc;
	public ErrorResponseBody(int error, String desc) {
		this.error = error;
		this.desc = desc;
	}
}
