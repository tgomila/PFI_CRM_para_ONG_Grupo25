package com.pfi.crm.multitenant.tenant.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.AppException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	public User getUserById(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
    }
	
	public User getUserByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }
	
	public List<User> getUsers() {
		return userRepository.findAll();
		//return userRepository.findAll().filter(a -> a.isActive()).collect(Collectors.toList());
    }
	
	public User altaUsuario (User user) {
		user.setId(null);//Para no modificar nada
		//Si hay contacto precargado, lo asocio sin modificar contacto. Si no hay id, lo creo
		if(user.getContacto() != null && user.getContacto().getId() != null) {
			Contacto contactoAsociar = contactoRepository.findById(user.getContacto().getId()).orElseThrow(
	                () -> new ResourceNotFoundException("Contacto", "id", user.getContacto().getId()));
			user.setContacto(contactoAsociar);
		}
		//Me aseguro su ID bien asociado. 
		Set<Role> rolesDBAgregar = new HashSet<Role>();
		user.getRoles().forEach((role) -> rolesDBAgregar.add(buscarRole(role.getRoleName())));
		user.setRoles(rolesDBAgregar);
		return userRepository.save(user);
	}
	
	public User agregarRol(String username, RoleName roleName) {
		User user = getUserByUsername(username);
		Role newUserRole = buscarRole(roleName);
		user.agregarRol(newUserRole);
		return userRepository.save(user);
	}
	
	private Role buscarRole(RoleName roleName) {
		return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", roleName.toString()));
	}
	
	public User quitarRol(String username, RoleName roleName) {
		User user = getUserByUsername(username);
		Role newUserRole = buscarRole(roleName);
		user.quitarRol(newUserRole);
		return userRepository.save(user);
	}
	
	public void bajaUsuariosPorContacto(Long id) {
		List<User> usuariosAModificar = userRepository.findByContacto_Id(id);
		if(!usuariosAModificar.isEmpty()) {
			usuariosAModificar.forEach((user) -> user.setContacto(null));
			usuariosAModificar = userRepository.saveAll(usuariosAModificar);
			userRepository.deleteAll(usuariosAModificar);
		}
		else {
			throw new AppException("No existen usuarios con Contacto ID: " + id + " para dar de baja.");
		}
		
	}
	
	
	
	/*public void bajaUser(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<User> optionalModel = userRepository.findById(id);
		if(optionalModel.isPresent()) {
			User m = optionalModel.get();
			m.setEstadoActivoUser(false);
			m.setFechaBajaUser(LocalDate.now());
			userRepository.save(m);
			//userRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe user
		}
		
	}
	
	public User modificarUser(User user) {
		if (user != null && user.getId() != null)
			return userRepository.save(user);
		else
			return new User();
	}
	
	
	
	
	
	
	
	
	// Payloads
	public User toModel(UserPayload p) {

		// User
		User m = new User();
		m.setId(p.getId());
		m.setEstadoActivoUser(true);
		m.setFechaAltaUser(LocalDate.now());
		m.setFechaBajaUser(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());

		return m;
	}

	public UserPayload toPayload(User m) {

		UserPayload p = new UserPayload();

		// User
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		// Fin User

		return p;
	}*/
}
