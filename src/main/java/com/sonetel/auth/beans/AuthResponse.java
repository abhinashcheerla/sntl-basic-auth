package com.sonetel.auth.beans;

public class AuthResponse {
	public enum RESPONSE {
		AUTHORIZED, INVALID_USER, INVALID_CREDENTIALS, FAILURE
	}
	
	private RESPONSE response;
	private String details;
	private UserProfile userDetails;
	
	public RESPONSE getResponse() {
		return response;
	}
	public void setResponse(RESPONSE response) {
		this.response = response;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public UserProfile getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserProfile userDetails) {
		this.userDetails = userDetails;
	}
}
