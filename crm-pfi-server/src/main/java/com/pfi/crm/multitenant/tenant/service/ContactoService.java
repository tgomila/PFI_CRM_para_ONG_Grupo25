package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.payload.ContactoAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;

@Service
public class ContactoService {
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FacturaService facturaService;
	
	public ContactoPayload getContactoById(@PathVariable Long id) {
		return this.getContactoModelById(id).toPayload();
    }
	
	public Contacto getContactoModelById(@PathVariable Long id) {
		return contactoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contacto", "id", id));
    }
	
	public List<ContactoPayload> getContactos() {
		//return contactoRepository.findAllByEstadoActivoContactoTrue();
		return contactoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
		//return contactoRepository.findAll().stream().filter(a -> a.getEstadoActivoContacto()).map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ContactoPayload altaContacto (ContactoAbstractPayload payload) {
		return this.altaContactoModel(payload).toPayload();
	}
	
	public Contacto altaContactoModel (ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a dar de alta, por favor ingrese un contacto.");
		if(payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de contacto: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		payload.setId(null);
		return contactoRepository.save(new Contacto(payload));
	}
	
	/**
	 * Si ingresa un ID y no existe en la BD, no se dará de alta.
	 * @param payload contacto
	 * @return Model contacto
	 */
	public Contacto altaModificarContactoModel (ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a dar de alta/modificar, por favor ingrese un contacto.");
		if(payload.getId() != null) //Si existe ID contacto asociado de alta:
			return this.modificarContactoModel(payload);
		else
			return this.altaContactoModel(payload);
	}
	
	public String bajaContacto(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Contacto m = this.getContactoModelById(id);
		m.setEstadoActivoContacto(false);
		m.setFechaBajaContacto(LocalDate.now());
		
		String message = "Se ha dado de baja: Contacto";
		String aux = "";
		
		//Dar de baja a personaFisica y todo su alrededor
		aux = personaFisicaService.bajaPersonaFisicaSiExiste(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Dar de baja a personaJuridica
		aux = personaJuridicaService.bajaPersonaJuridicaSiExiste(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Dar de baja su user
		//Por las dudas no lo hago, a ver si doy de baja un admin o alguien importante.
		//Mejor que lo hagan aparte.
		//aux = userService.bajaUsuariosPorContacto(id);
		//message += !aux.isEmpty() ? (". "+aux) : "";
		aux = userService.desasociarContactoDeUsers(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Mantener facturas pero quitar su contacto
		facturaService.quitarContactoDeSusFacturas(id);
		
		m = contactoRepository.save(m);
		contactoRepository.delete(m);		//Temporalmente se elimina de la BD
		//return ResponseEntity.ok().body(new ApiResponse(true, message));
		
		message += " de id: " + id.toString() + "";
		return message;
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaContactoSiExiste(Long id) {
		if(existeContacto(id)) {
			bajaContacto(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ContactoPayload modificarContacto(ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a modificar. Por favor ingrese un contacto que no sea null.");
		if(payload.getId() == null) //Necesito el id de contacto o se crearia uno nuevo
			throw new BadRequestException("No se puede modificar contacto sin ID");//return null;
		
		Contacto model = this.getContactoModelById(payload.getId());
		model.modificar(payload);
		return contactoRepository.save(model).toPayload();
	}
	
	//Sirve para services superiores, como personaFisicaService
	public Contacto modificarContactoModel(ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a modificar. Por favor ingrese un contacto que no sea null.");
		if(payload.getId() == null) //Necesito el id de contacto o se crearia uno nuevo
			throw new BadRequestException("No se puede modificar contacto sin ID");
		if(!existeContacto(payload.getId()))
			throw new BadRequestException("Ha ingresado un ID '" + payload.getId().toString() + "' a modificar, y no existe. "
					+ "Es posible que sea otro número o haya sido dado de baja.");
		
		Contacto model = this.getContactoModelById(payload.getId());
		model.modificar(payload);
		return contactoRepository.save(model);
	}
	
	/**
	 * Método hecho para otros services, por si ingresan un payload y desean buscarlo o darlo de alta.
	 * @param payload (ContactoPayload)
	 * @return Contacto (model) dado de alta, o encontrado en la BD si fue dado de alta, o null si payload == null.
	 */
	public Contacto buscarOAlta(ContactoAbstractPayload payload) {
		//if(payload == null)
		//	throw new BadRequestException("Ha introducido un null como contacto a dar de alta, por favor ingrese un contacto.");
		if(payload != null) {
			if(payload.getId() != null) {//Lo busco
				return this.getContactoModelById(payload.getId());
			}
			else {//Doy de alta su prestamista (Contacto)
				return this.altaContactoModel(payload);
			}
		}
		return null;
		
	}
	
	public boolean existeContacto(Long id) {
		return contactoRepository.existsById(id);
	}
}
