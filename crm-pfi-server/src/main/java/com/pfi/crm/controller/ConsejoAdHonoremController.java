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
import com.pfi.crm.multitenant.tenant.model.ConsejoAdHonorem;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ConsejoAdHonoremNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ConsejoAdHonoremService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/consejoadhonorem")
public class ConsejoAdHonoremController {
	
	@Autowired
	private ConsejoAdHonoremService consejoAdHonoremService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public ConsejoAdHonoremPayload getConsejoAdHonoremById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONSEJOADHONOREM, "Buscar ConsejoAdHonorem por id: '" + id + "'");
        return consejoAdHonoremService.getConsejoAdHonoremByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ConsejoAdHonoremPayload> getConsejoAdHonorem(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONSEJOADHONOREM, "Buscar la lista completa de ConsejoAdHonorem");
    	return  consejoAdHonoremService.getConsejoAdHonorems();
	}
	
	@PostMapping({"/", "/alta"})
    public ConsejoAdHonoremPayload altaConsejoAdHonorem(@Valid @RequestBody ConsejoAdHonoremPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONSEJOADHONOREM, "Dar de alta un nuevo ConsejoAdHonorem");
    	return consejoAdHonoremService.altaConsejoAdHonorem(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaConsejoAdHonorem(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONSEJOADHONOREM, "Eliminar un ConsejoAdHonorem");
		String message = consejoAdHonoremService.bajaConsejoAdHonorem(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public ConsejoAdHonoremPayload modificarConsejoAdHonorem(@Valid @RequestBody ConsejoAdHonoremPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONSEJOADHONOREM, "Modificar un ConsejoAdHonorem");
    	return consejoAdHonoremService.modificarConsejoAdHonorem(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONSEJOADHONOREM, "Ver nombre de cada columna de tabla ConsejoAdHonorem");
		return new ConsejoAdHonoremNombreTablaPayload().getNombresConsejoAdHonoremTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchConsejoAdHonoremById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONSEJOADHONOREM, "Buscar un consejoAdHonorem/persona/contacto por id: '" + id + "'");
        return consejoAdHonoremService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONSEJOADHONOREM, "Ver ConsejoAdHonorem creados en los ultimos 12 meses por mes");
		return consejoAdHonoremService.countContactosCreadosUltimos12meses();
	}
	
	@GetMapping("/grafico/contar_categoria_edad")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONSEJOADHONOREM, "Contar cuántos consejeros ad honorem hay, por categoría de edad");
		return consejoAdHonoremService.obtenerConteoPorEtapasEdad();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de ConsejoAdHonorem payload

	@GetMapping("/test")
	public ConsejoAdHonoremPayload getConsejoAdHonoremTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONSEJOADHONOREM, "Ver un ejemplo de ConsejoAdHonorem");

		ConsejoAdHonorem m = new ConsejoAdHonorem();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("ConsejoAdHonorem Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// ConsejoAdHonorem
		m.setFuncion("Super consejero de la ONG");

		return m.toPayload();
	}
}
