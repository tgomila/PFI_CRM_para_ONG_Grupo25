package com.pfi.crm.multitenant.tenant.model;

import org.hibernate.annotations.NaturalId;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.UserPayload;
import com.pfi.crm.payload.request.SignUpRequest;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {
			"username"
		}),
		@UniqueConstraint(columnNames = {
			"email"
		})
})
public class User extends UserDateAudit{

	private static final long serialVersionUID = 8495957082037464071L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
	//@SequenceGenerator(name = "users_seq", sequenceName = "users_sequence", allocationSize = 1)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	@NotBlank
	@Size(max = 15)
	private String username;

	@NaturalId
	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String password;
	
	@ManyToOne(cascade = {CascadeType.MERGE} )
	@OrderBy("nombreDescripcion ASC")
	private Contacto contacto;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_usuario",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	public User() {

	}
	
	public UserPayload toPayload() {

		UserPayload p = new UserPayload();

		// User
		p.setId(this.getId());
		p.setName(this.getName());
		p.setUsername(this.username);
		p.setEmail(this.getEmail());
		p.setRoleMasValuado((this.getRoleMasValuado().getName()));
		if(contacto!=null)
			p.setContacto(this.getContacto().toPayload());
		p.setRoles(roles.stream().map(r -> r.getRoleName()).collect(Collectors.toSet()));
		return p;
	}

	public User(String nombre, String username, String email, String password) {
		this.name = nombre;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(SignUpRequest payload, Contacto contacto, Set<Role> roles) {
		this.name = payload.getName();
		this.username = payload.getUsername();
		this.email = payload.getEmail();
		this.password = payload.getPassword();
		this.contacto = contacto;
		this.roles = roles;
	}
	
	/**
	 * Pendiente modificar roles y/o password en service
	 * @param userPayload
	 */
	public void modificar(UserPayload p) {
		//this.id = p.getId();//No permito modificar id
		this.name = p.getName();
		//this.username = p.getUsername();//No permito modificar username
		//this.email = p.getEmail();
		//this.password
		//this.roles
	}
	
	/**
	 * Es aux para el service, si tiene nuevos roles hay que modificar y save.
	 * @return
	 */
	public boolean hayQueModificarSusRoles (UserPayload p) {
		Set<RoleName> rolesPayload = p.getRoles();
		Set<RoleName> rolesUser = this.getRoles().stream()
				.map(Role::getRoleName)
				.collect(Collectors.toSet());
		return !rolesPayload.equals(rolesUser);
	}
	
	/**
	 * Es aux para el service, si tiene distinto contacto hay que modificar y save.
	 * @return
	 */
	public boolean hayQueModificarSuContacto (UserPayload p) {
		Long idContactoPayload = (p.getContacto() != null) ? p.getContacto().getId() : null;
		Long idContactoUser = (this.getContacto() != null) ? this.getContacto().getId() : null;
		
		if (idContactoPayload == null && idContactoUser == null) {
			return false;
		}
		
		if (idContactoPayload != null && idContactoUser != null) {
			return !idContactoPayload.equals(idContactoUser);
		}
		
		return true;
	}
	
	public void agregarRol(Role rol) {
		roles.add(rol);
	}
	
	public void quitarRol(Role rol) {
		roles.remove(rol);
	}
	
	public RoleName getRoleMasValuado() {
		Set<RoleName> roles = new HashSet<RoleName>();
		getRoles().forEach((rol) -> roles.add(rol.getRoleName()));
		
		//Por prioridad
		RoleName rolSuperior;
		if(roles.contains(RoleName.ROLE_ADMIN))
			rolSuperior = RoleName.ROLE_ADMIN;
		else if(roles.contains(RoleName.ROLE_EMPLOYEE))
			rolSuperior = RoleName.ROLE_EMPLOYEE;
		else if(roles.contains(RoleName.ROLE_PROFESIONAL))
			rolSuperior = RoleName.ROLE_PROFESIONAL;
		else if(roles.contains(RoleName.ROLE_USER))
			rolSuperior = RoleName.ROLE_USER;
		else {
			new ResourceNotFoundException("roles", "currentUser", this);
			rolSuperior = RoleName.ROLE_DEFAULT;
		}
		
		/*if(roles.size() == 0) {
			new ResourceNotFoundException("roles", "currentUser", this);
			return null;
		}
		rolSuperior = roles.stream().max(Comparator.comparing(r -> r.getPriority())).get();
		*/
		
		return rolSuperior;
	}
	
	public Set<RoleName> getRoleNames() {
		return roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		//Sin acentos
		this.username = Normalizer.normalize(username, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "");;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Contacto getContacto() {
		return contacto;
	}

	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
	public void isActive() {
		contacto.getEstadoActivoContacto();
	}
}