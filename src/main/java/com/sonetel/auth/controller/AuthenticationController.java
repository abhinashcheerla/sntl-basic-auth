package com.sonetel.auth.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sonetel.auth.beans.AuthResponse;
import com.sonetel.auth.beans.UserProfile;
import com.sonetel.auth.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	
	@Autowired
	private AuthenticationService authentiationService;
	
	@RequestMapping(value="/usagerecord/basic", method=RequestMethod.POST, produces="application/json")
	public Object authenticateUsageRecordsAccess(
			@RequestBody UserProfile userProfile) {
		logger.info("authenticateUsageRecordsAccess() [IN]");
		AuthResponse response = new AuthResponse();
		
		if(userProfile == null) {
			response.setResponse(AuthResponse.RESPONSE.FAILURE);
			response.setDetails("Bad Request");
			return response;
		}
		
		String userName = userProfile.getUsername();
		String password = userProfile.getPassword();
		
		if(userName==null || userName.trim().isEmpty()) {
			response.setResponse(AuthResponse.RESPONSE.FAILURE);
			response.setDetails("User Name is Empty");
			return response;
		}
		
		if(password==null || password.isEmpty()) {
			response.setResponse(AuthResponse.RESPONSE.FAILURE);
			response.setDetails("Password id Empty");
			return response;
		}
		
		response = authentiationService.authenticateForUsageRecordsAccess(userProfile, response);
		logger.info("authenticateUsageRecordsAccess() [OUT]");
		return response;
	}
}
