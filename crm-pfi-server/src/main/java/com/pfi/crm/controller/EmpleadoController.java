package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.Empleado;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.EmpleadoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.EmpleadoService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/empleado")
public class EmpleadoController {
	
	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public EmpleadoPayload getEmpleadoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.EMPLEADO, "Buscar un empleado por su ID: '" + id + "'");
        return empleadoService.getEmpleadoByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
    public List<EmpleadoPayload> getEmpleado(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.EMPLEADO, "Ver lista de todos los empleados");
    	return  empleadoService.getEmpleados();
	}
	
	@PostMapping({"/", "/alta"})
    public EmpleadoPayload altaEmpleado(@Valid @RequestBody EmpleadoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.EMPLEADO, "Dar de alta un empleado");
    	return empleadoService.altaEmpleado(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaEmpleado(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.EMPLEADO, "Eliminar un empleado");
		String message = empleadoService.bajaEmpleado(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public EmpleadoPayload modificarEmpleado(@Valid @RequestBody EmpleadoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.EMPLEADO, "Modificar un empleado");
    	return empleadoService.modificarEmpleado(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.EMPLEADO, "Ver nombre de cada columna de tabla empleado");
		return new EmpleadoNombreTablaPayload().getNombresEmpleadoTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchEmpleadoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.EMPLEADO, "Ver un ejemplo de empleado");
        return empleadoService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Empleado payload

	@GetMapping("/test")
	public EmpleadoPayload getEmpleadoTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.EMPLEADO, "");

		Empleado m = new Empleado();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Empleado Don psicologo");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("estebanquito@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Esteban");
		m.setApellido("Quito");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Empleado
		m.setFuncion("Desktop Helper");
		m.setDescripcion("Da las altas y bajas de beneficiarios");
		// Fin Empleado

		return m.toPayload();
	}
}
