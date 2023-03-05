package com.pfi.crm.payload.response;

import java.util.List;

public class JwtAuthenticationResponse {
	
	private String userName;
	private String token;
	private String tokenType = "Bearer";
	private List<String> roles;
	private Integer tenantClientId;
	private String dbName;
	private String tenantName;

	public JwtAuthenticationResponse(String userName, String token, List<String> roles, Integer tenantClientId, String dbName, String tenantName) {
		this.userName = userName;
		this.token = token;
		this.roles = roles;
		this.tenantClientId = tenantClientId;
		this.dbName = dbName;
		this.tenantName = tenantName;
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

	public Integer getTenantClientId() {
		return tenantClientId;
	}

	public void setTenantClientId(Integer tenantClientId) {
		this.tenantClientId = tenantClientId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
	
}
