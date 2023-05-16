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
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.Voluntario;
import com.pfi.crm.multitenant.tenant.payload.VoluntarioPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.VoluntarioNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.VoluntarioService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/voluntario")
public class VoluntarioController {
	
	@Autowired
	private VoluntarioService voluntarioService;
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public VoluntarioPayload getVoluntarioById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.VOLUNTARIO, "Ver voluntario con id: '" + id + "'");
        return voluntarioService.getVoluntarioByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<VoluntarioPayload> getVoluntario(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.VOLUNTARIO, "Ver lista de voluntarios");
    	return  voluntarioService.getVoluntarios();
	}
	
	@PostMapping({"/", "/alta"})
    public VoluntarioPayload altaVoluntario(@Valid @RequestBody VoluntarioPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.VOLUNTARIO, "Cargar nuevo voluntario");
    	return voluntarioService.altaVoluntario(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaVoluntario(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.VOLUNTARIO, "Eliminar un voluntario");
		String message = voluntarioService.bajaVoluntario(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public VoluntarioPayload modificarVoluntario(@Valid @RequestBody VoluntarioPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.VOLUNTARIO, "Modificar voluntario");
    	return voluntarioService.modificarVoluntario(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.VOLUNTARIO, "Ver nombre de cada columna de tabla voluntario");
		return new VoluntarioNombreTablaPayload().getNombresVoluntarioTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchVoluntarioById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.VOLUNTARIO, "Buscar voluntario/persona/contacto con id: '" + id + "'");
        return voluntarioService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload

	@GetMapping("/test")
	public VoluntarioPayload getVoluntarioTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.VOLUNTARIO, "Ver un ejemplo de voluntario");

		Voluntario m = new Voluntario();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Voluntario Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		//Voluntario
		//No tiene otros atributos

		return m.toPayload();
	}
}
