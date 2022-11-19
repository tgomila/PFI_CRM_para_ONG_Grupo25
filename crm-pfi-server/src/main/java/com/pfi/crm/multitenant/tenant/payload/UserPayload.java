package com.pfi.crm.multitenant.tenant.payload;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.pfi.crm.multitenant.tenant.model.Role;

public class UserPayload {
	
	@SuppressWarnings("unused")
	private Long id;
	@Size(max = 40)
	private String name;
	
	@Size(max = 40)
    @Email
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
