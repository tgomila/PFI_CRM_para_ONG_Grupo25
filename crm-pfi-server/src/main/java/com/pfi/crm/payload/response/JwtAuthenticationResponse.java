package com.pfi.crm.payload.response;

import java.util.List;

public class JwtAuthenticationResponse {
	
	private String userName;
	private String token;
	private String tokenType = "Bearer";
	private List<String> roles;

	public JwtAuthenticationResponse(String userName, String token, List<String> roles) {
		this.userName = userName;
		this.token = token;
		this.roles = roles;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
