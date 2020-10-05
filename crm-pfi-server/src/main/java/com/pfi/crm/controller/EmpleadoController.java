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

import com.pfi.crm.model.Empleado;
import com.pfi.crm.repository.EmpleadoRepository;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@PostMapping("/")
    public Empleado altaEmpleado(@Valid @RequestBody Empleado empleado) {
    	System.out.println("Entre aca");
    	
    	return empleadoRepository.save(empleado);
    }
	
	@GetMapping("/{id}")
    public Optional<Empleado> getEmpleadoById(@PathVariable Long id) {
        return empleadoRepository.findById(id);
    }
	
	@GetMapping("/empleados")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<Empleado> empleados() {
    	return empleadoRepository.findAll();
    }
}
