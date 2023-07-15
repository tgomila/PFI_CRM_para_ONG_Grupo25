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
import com.pfi.crm.multitenant.tenant.model.Beneficiario;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.BeneficiarioPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.BeneficiarioNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.BeneficiarioService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/beneficiario")
public class BeneficiarioController {
	
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public BeneficiarioPayload getBeneficiarioById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.BENEFICIARIO, "Buscar un beneficiario por su ID");
        return beneficiarioService.getBeneficiarioByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
    public List<BeneficiarioPayload> getBeneficiario(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.BENEFICIARIO, "Ver todos los beneficiarios");
    	return  beneficiarioService.getBeneficiarios();
	}
	
	@PostMapping({"/", "/alta"})
    public BeneficiarioPayload altaBeneficiario(@Valid @RequestBody BeneficiarioPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.BENEFICIARIO, "Cargar un nuevo beneficiario");
    	return beneficiarioService.altaBeneficiario(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaBeneficiario(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.BENEFICIARIO, "Eliminar un beneficiario");
		String message = beneficiarioService.bajaBeneficiario(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public BeneficiarioPayload modificarBeneficiario(@Valid @RequestBody BeneficiarioPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.BENEFICIARIO, "Modificar un beneficiario");
    	return beneficiarioService.modificarBeneficiario(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.BENEFICIARIO, "Ver nombres de la tabla beneficiario");
		return new BeneficiarioNombreTablaPayload().getNombresBeneficiarioTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchBeneficiarioById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.BENEFICIARIO, "Buscar un beneficiario por su verdadero ID en DB");
        return beneficiarioService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.BENEFICIARIO, "Ver beneficiarios creados en los ultimos 12 meses por mes");
		return beneficiarioService.countCreadosUltimos12meses();
	}
	
	@GetMapping("/grafico/contar_categoria_edad")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.BENEFICIARIO, "Contar cuántos beneficiarios hay, por categoría de edad");
		return beneficiarioService.obtenerConteoPorEtapasEdad();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de beneficiario payload

	@GetMapping("/test")
	public BeneficiarioPayload getBeneficiarioTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.BENEFICIARIO, "Obtener un ejemplo de beneficiario");

		Beneficiario m = new Beneficiario();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Beneficiario Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("001234"));
		m.setLegajo(Long.parseLong("1090555"));
		m.setLugarDeNacimiento("Lanús");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Necesita asistencia psicologica");
		m.setEscuela("Colegio Nº123");
		m.setGrado("5º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario

		return m.toPayload();
	}
}
