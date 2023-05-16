package com.pfi.crm.controller;

import java.time.LocalDateTime;
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
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Donacion;
import com.pfi.crm.multitenant.tenant.model.DonacionTipo;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.DonacionPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.DonacionNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.DonacionService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/donacion")
public class DonacionController {
	
	@Autowired
	private DonacionService donacionService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
    public DonacionPayload getDonacionById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.DONACION, "Ver Donacion id: '" + id + "'");
        return donacionService.getDonacionById(id);
    }
	
	@GetMapping({"/", "/all"})
    public List<DonacionPayload> getDonacion(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.DONACION, "Ver todas las donaciones");
    	return  donacionService.getDonaciones();
	}
	
	@PostMapping({"/", "/alta"})
    public DonacionPayload altaDonacion(@Valid @RequestBody DonacionPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.DONACION, "Dar de alta una donación");
    	return donacionService.altaDonacion(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaDonacion(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.DONACION, "Dar de baja una donación");
		String message = donacionService.bajaDonacion(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
    }
	
	@PutMapping({"/", "/modificar"})
    public DonacionPayload modificarDonacion(@Valid @RequestBody DonacionPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.DONACION, "Modificar una donación");
    	return donacionService.modificarDonacion(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.DONACION, "Ver nombre de columnas de tabla Donacion");
		return new DonacionNombreTablaPayload().getNombresDonacionTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Donacion payload

	@GetMapping("/test")
	public DonacionPayload altaDonacionTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.DONACION, "Ver un ejemplo de Donación");

		Donacion m = new Donacion();

		// Contacto
		Contacto donante = new Contacto();
		donante.setEstadoActivoContacto(true);
		donante.setNombreDescripcion("Donante señor feliz");
		donante.setCuit("20-1235678-9");
		donante.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		donante.setEmail("señorfeliz@gmail.com");
		donante.setTelefono("1234-4567");
		m.setDonante(donante);

		// Donacion
		m.setFecha(LocalDateTime.now());
		m.setTipoDonacion(DonacionTipo.INSUMO);
		m.setDescripcion("Biromes para los chicos");
		// Fin Donacion

		return m.toPayload();
	}
}
