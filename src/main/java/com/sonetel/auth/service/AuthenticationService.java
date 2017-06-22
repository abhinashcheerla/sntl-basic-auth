package com.sonetel.auth.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonetel.auth.beans.AuthResponse;
import com.sonetel.auth.beans.UserProfile;
import com.sonetel.auth.dao.AuthenticationDAO;

@Service
public class AuthenticationService {
	@Autowired
	private AuthenticationDAO authenticationDAO;
	
	@Transactional
	public AuthResponse authenticateForUsageRecordsAccess(UserProfile userProfile, AuthResponse authResponse) {
		UserProfile userDB = authenticationDAO.getUserByUserName(userProfile.getUsername());
		
		if(userDB == null) {
			authResponse.setResponse(AuthResponse.RESPONSE.INVALID_USER);
			authResponse.setDetails("User does not exist in the system");
			return authResponse;
		}
		
		if(!userProfile.getPassword().equals(userDB.getPassword())) {
			authResponse.setResponse(AuthResponse.RESPONSE.INVALID_CREDENTIALS);
			authResponse.setDetails("User entered password is incorrect");
			return authResponse;
		}
		
		authResponse.setResponse(AuthResponse.RESPONSE.AUTHORIZED);
		authResponse.setDetails("Authorization Successful");
		
		BigDecimal accountId = authenticationDAO.getAccountIdForUserName(userProfile.getUsername());
		if(accountId!=null && accountId.intValue()>0) {
			userDB.setEnterpriseId(String.valueOf(accountId));
		} else {
			userDB.setEnterpriseId("");
		}
		
		authResponse.setUserDetails(userDB);
		
		return authResponse;
	}
}
