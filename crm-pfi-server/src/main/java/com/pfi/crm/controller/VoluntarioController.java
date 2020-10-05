package com.pfi.crm.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.model.Voluntario;
import com.pfi.crm.repository.VoluntarioRepository;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/voluntario")
public class VoluntarioController {
	
	@Autowired
	private VoluntarioRepository voluntarioRepository;
	
	@PostMapping("/")
    public Voluntario altaVoluntario(@Valid @RequestBody Voluntario voluntario) {
    	return voluntarioRepository.save(voluntario);
    }
	
	@GetMapping("/{id}")
    public Optional<Voluntario> getEmpleadoById(@PathVariable Long id) {
        return voluntarioRepository.findById(id);
    }
	
	@GetMapping("/voluntarios")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<Voluntario> voluntarios() {
    	return voluntarioRepository.findAll();
    }
}
