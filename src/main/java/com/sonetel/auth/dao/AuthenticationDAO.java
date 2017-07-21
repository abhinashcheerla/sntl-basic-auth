package com.sonetel.auth.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sonetel.auth.beans.UserProfile;

@Repository
public class AuthenticationDAO {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public AuthenticationDAO (@Qualifier("CS") DataSource dataSource) {
		jdbcTemplate=new JdbcTemplate(dataSource);
	}

	public UserProfile getUserByUserName(String userName) throws Exception {
		logger.info("getUserByUserName() [IN]");
		logger.info("Loading User datails for User Name: "+userName);
		StringBuffer strQuery = new StringBuffer();
		List<Map<String, Object>> resultList = null;
		UserProfile userDB = new UserProfile();
		try {
			strQuery.append("SELECT U.CUST_KEY AS USER_NAME, U.TOKEN AS PASSWORD, UR.ROLE_NAME AS ROLE");
			strQuery.append(" FROM USER_AUTH U JOIN USER_ROLES UR ON U.ROLE_ID = UR.ROLE_ID");
			strQuery.append(" WHERE U.CUST_KEY = '"+ userName+"'");
			
			resultList = jdbcTemplate.queryForList(strQuery.toString());
			if(resultList == null || resultList.isEmpty()) {
				return null;
			}
			Map<String, Object> result = resultList.get(0);
			userDB.setUsername((String) result.get("USER_NAME"));
			userDB.setPassword((String) result.get("PASSWORD"));
			userDB.setRole((String) result.get("ROLE"));
		} catch(Throwable t) {
			logger.error("Error occurred getUserByUserName()", t);
			throw new Exception(t);
		} finally {
			strQuery = null;
			resultList = null;
		}
		logger.info("getUserByUserName() [OUT]");
		return userDB;
	}

	public BigDecimal getAccountIdForUserName(String userName) {
		String sqlQuery = "SELECT DISTINCT EC.ENTERPRISEID AS ACCOUNT_ID FROM SNTL_MT_ENTERPRISECFG EC, USER_AUTH UAUTH"
								+ " WHERE EC.CUST_KEY = UAUTH.CUST_KEY AND UAUTH.CUST_KEY = '"+userName+"'";

		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sqlQuery);

		if(resultList == null || resultList.isEmpty()) {
			return null;
		}

		Map<String, Object> result = resultList.get(0);
		BigDecimal accountId = (BigDecimal) result.get("ACCOUNT_ID");
		return accountId;
	}
	
	
}
