package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User geUserById(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
    }
	
	public List<User> getUsers() {
		return userRepository.findAll();
		//return userRepository.findAll().filter(a -> a.isActive()).collect(Collectors.toList());
    }
	
	public User altaUsuario (User user) {
		user.setId(null);
		return userRepository.save(user);
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
