package com.pfi.crm.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.model.Voluntario;
import com.pfi.crm.repository.VoluntarioRepository;

@Service
public class VoluntarioService {
	
	@Autowired
	private VoluntarioRepository voluntarioRepository;
	
	 private static final Logger logger = LoggerFactory.getLogger(EjemploService.class);
	
	public Optional<Voluntario> getEmpleadoById(@PathVariable Long id) {
        return voluntarioRepository.findByContacto_Id(id);
    }
}
