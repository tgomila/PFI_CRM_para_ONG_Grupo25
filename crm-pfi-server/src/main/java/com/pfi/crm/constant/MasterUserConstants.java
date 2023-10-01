package com.pfi.crm.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.security.UserPrincipal;

//Será auxiliar hasta que funcione
public class MasterUserConstants {

	public static final String MASTER_TENANT_USERNAME = "admin";
	public static final String MASTER_TENANT_USER_PASSWORD = "MasterSecretPassword";
	
	private static final List<RoleName> allRoles = Arrays.asList(RoleName.values());
	private static final List<GrantedAuthority> authorities = allRoles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
	private static final UserPrincipal userPrincipal = new UserPrincipal(Long.valueOf(0), "Master Tenant Admin", MasterUserConstants.MASTER_TENANT_USERNAME, "adminOfAll@admin.com",
			MasterUserConstants.MASTER_TENANT_USER_PASSWORD, null, authorities);
	public static final UserDetails USER_DETAILS = userPrincipal;
}
