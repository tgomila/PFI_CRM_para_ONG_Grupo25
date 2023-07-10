package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.Colaborador;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ColaboradorPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ColaboradorNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ColaboradorService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController  {
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public ColaboradorPayload getColaboradorById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.COLABORADOR, "Buscar un colaborador por id: '" + id + "'");
        return colaboradorService.getColaboradorByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ColaboradorPayload> getColaborador(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.COLABORADOR, "Buscar una lista de colaboradores");
    	return  colaboradorService.getColaboradores();
	}
	
	@PostMapping({"/", "/alta"})
    public ColaboradorPayload altaColaborador(@Valid @RequestBody ColaboradorPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.COLABORADOR, "Cargar un nuevo colaborador");
    	return colaboradorService.altaColaborador(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaColaborador(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.COLABORADOR, "Eliminar un colaborador con id: '" + id + "'");
		String message = colaboradorService.bajaColaborador(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public ColaboradorPayload modificarColaborador(@Valid @RequestBody ColaboradorPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.COLABORADOR, "Modificar un colaborador");
    	return colaboradorService.modificarColaborador(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.COLABORADOR, "Ver el nombre de la tabla Colaborador");
		return new ColaboradorNombreTablaPayload().getNombresColaboradorTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchColaboradorById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.COLABORADOR, "Buscar un colaborador/persona/contacto");
        return colaboradorService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.COLABORADOR, "Ver colaboradores creados en los ultimos 12 meses por mes");
		return colaboradorService.countCreadosUltimos12meses();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de colaborador payload

	@GetMapping("/test")
	public ColaboradorPayload altaColaboradorTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.COLABORADOR, "Ver un ejemplo de colaborador");

		Colaborador m = new Colaborador();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Colaborador Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Colaborador
		m.setArea("Area administrativa");
		// Fin Colaborador

		return m.toPayload();
	}
}
