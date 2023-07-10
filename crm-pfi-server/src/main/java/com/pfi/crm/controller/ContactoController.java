package com.pfi.crm.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ContactoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ContactoService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {
	
	@Autowired
	private ContactoService contactoService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public ContactoPayload getContactoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver un id");
        return contactoService.getContactoById(id);
    }
	
	@GetMapping({"/", "/all"})
	public List<ContactoPayload> getContactos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver todos los contactos");
		System.out.println("\n\n\n----------------------------Entre acá /all-----------------------------------\n\n\n");
    	//return  contactoService.getContactos().stream().map(e -> contactoService.toPayload(e)).collect(Collectors.toList());
		return  contactoService.getContactos();
	}
	
	@PostMapping({"/", "/alta"})
    public ContactoPayload altaContacto(@Valid @RequestBody ContactoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Dar de alta un contacto");
		System.out.println("\n\nEntre acáaaaaaaaaaaaaaaaaa\n\n");
    	return contactoService.altaContacto(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaContacto(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Dar de baja un contacto");
    	String message = contactoService.bajaContacto(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }	
	
	@PutMapping({"/", "/modificar"})
    public ContactoPayload modificarContacto(@Valid @RequestBody ContactoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Modificar un contacto");
    	return contactoService.modificarContacto(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "ver nombres para la tabla");
		return  new ContactoNombreTablaPayload().getNombresContactoTabla();
	}
	
	// TEST
	// Devuelve un ejemplo de contacto payload

	@GetMapping("/test")
	@PreAuthorize("hasRole('ADMIN')")
	public ContactoPayload getContactoTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "test");
		
		ContactoPayload p = new ContactoPayload();

		// Contacto
		//m.setEstadoActivoContacto(true);
		p.setNombreDescripcion("Contacto Don Roque");
		p.setCuit("20-1235678-9");
		p.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		p.setEmail("felipe@gmail.com");
		p.setTelefono("1234-4567");

		return p;
	}
	
	@GetMapping("/test/fecha")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Instant getFecha(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "test get fecha");
		Instant now = Instant.now();
		Instant firstDayOfMonth = now.atZone(ZoneId.systemDefault())
			    .toLocalDate()
			    .withDayOfMonth(1)
			    .atStartOfDay()
			    .atZone(ZoneId.systemDefault())
			    .toInstant();
		
		return firstDayOfMonth;
	}
	
	@GetMapping("/grafico/contactos_este_mes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public int getContactosEsteMes(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Ver número de contactos creados este mes");
		return contactoService.getContactosCreadosEsteMes().size();
	}
	
	@GetMapping("/grafico/contar_contactos/este_mes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEsteMes(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Ver contactos creados este año por mes");
		return contactoService.countContactosCreadosEsteAnioPorMes();
	}
	
	//Utilizado en front
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Ver contactos creados en los ultimos 12 meses por mes");
		return contactoService.countContactosCreadosUltimos12meses();
	}
	
	@GetMapping("/grafico/contar_creados/este_anio/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEsteAnio(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Ver contactos creados este año por mes");
		int esteAnio = LocalDate.now().getYear();
		return contactoService.countContactosByAnioMes(esteAnio);
	}
	
	@GetMapping("/grafico/contar_contactos/este_anio/{anioInput}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEnAnioInput(@PathVariable int anioInput, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Ver contactos creados por mes en el año "+anioInput);
		return contactoService.countContactosByAnioMes(anioInput);
	}
}