package com.pfi.crm.controller;

import java.math.BigDecimal;
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
import com.pfi.crm.multitenant.tenant.model.Factura;
import com.pfi.crm.multitenant.tenant.model.FacturaItem;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.FacturaPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.FacturaNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.FacturaService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/factura")
public class FacturaController {
	
	@Autowired
	private FacturaService facturaService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
	public FacturaPayload getFacturaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.FACTURA, "Ver factura con id: '" + id + "'");
		return facturaService.getFacturaById(id);
	}
	
	@GetMapping({"/", "/all"})
	public List<FacturaPayload> getFactura(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.FACTURA, "Ver todas las facturas");
		return  facturaService.getFacturas();
	}
	
	@PostMapping({"/", "/alta"})
	public FacturaPayload altaFactura(@Valid @RequestBody FacturaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.FACTURA, "Cargar una nueva factura");
		return facturaService.altaFactura(payload);
	}
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
	public ResponseEntity<?> bajaFactura(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.FACTURA, "Eliminar una factura");
		String message = facturaService.bajaFactura(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}
	
	@PutMapping({"/", "/modificar"})
	public FacturaPayload modificarFactura(@Valid @RequestBody FacturaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.FACTURA, "Modificar una factura");
		return facturaService.modificarFactura(payload);
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.FACTURA, "Ver el nombre de cada columna de la tabla factura");
		return new FacturaNombreTablaPayload().getNombresFacturaTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Factura payload

	@GetMapping("/test")
	public FacturaPayload altaFacturaTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.FACTURA, "Testing ver un ejemplo de factura");

		Factura m = new Factura();

		// Contacto
		Contacto cliente = new Contacto();
		cliente.setEstadoActivoContacto(true);
		cliente.setNombreDescripcion("Cliente señor feliz");
		cliente.setCuit("20-1235678-9");
		cliente.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		cliente.setEmail("señorfeliz@gmail.com");
		cliente.setTelefono("1234-4567");
		m.setCliente(cliente);
		
		Contacto emisorFactura = new Contacto();
		emisorFactura.setEstadoActivoContacto(true);
		emisorFactura.setNombreDescripcion("Emisor factura señor feliz");
		emisorFactura.setCuit("20-1235678-9");
		emisorFactura.setDomicilio("Avenida siempre falsa 123, piso 5, depto B");
		emisorFactura.setEmail("emisorfactura@gmail.com");
		emisorFactura.setTelefono("1234-4567");
		m.setEmisorFactura(emisorFactura);

		// Factura
		m.setFecha(LocalDateTime.now());
		// Fin Factura
		
		//FacturaItem
		FacturaItem item = new FacturaItem();
		item.setDescripcion("Item 1");
		item.setId(null);
		item.setPrecio(BigDecimal.valueOf(250.00));
		item.setPrecioUnitario(BigDecimal.valueOf(25.00));
		item.setUnidades(10);
		m.agregarItemFactura(item);
		
		//Fin facturaItem

		return m.toPayload();
	}
}
