package com.pfi.crm.payload.response;

public class JwtAuthenticationResponse {
	
	private String userName;
	private String token;
	private String tokenType = "Bearer";

	public JwtAuthenticationResponse(String userName, String token) {
		this.userName = userName;
		this.token = token;
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
	
	
}
