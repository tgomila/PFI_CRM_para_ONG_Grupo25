package com.pfi.crm.payload.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class LoginRequest implements Serializable {
	@NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
    
    private Integer tenantOrClientId;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public LoginRequest setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

	public Integer getTenantOrClientId() {
		return tenantOrClientId;
	}

	public LoginRequest setTenantOrClientId(Integer tenantOrClientId) {
		this.tenantOrClientId = tenantOrClientId;
		return this;
	}
}
