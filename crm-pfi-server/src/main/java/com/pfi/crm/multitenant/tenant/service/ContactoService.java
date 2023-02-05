package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;

@Service
public class ContactoService {
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FacturaService facturaService;
	
	public ContactoPayload getContactoById(@PathVariable Long id) {
		return contactoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contacto", "id", id)).toPayload();
    }
	
	public List<ContactoPayload> getContactos() {
		//return contactoRepository.findAllByEstadoActivoContactoTrue();
		//return contactoRepository.findAll().filter(a -> a.getEstadoActivoContacto()).collect(Collectors.toList());
		return contactoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ContactoPayload altaContacto (ContactoPayload contacto) {
		contacto.setId(null);
		//contacto.setFechaAltaContacto(LocalDate.now());
		//return contactoRepository.save(contacto);
		return contactoRepository.save(new Contacto(contacto)).toPayload();
	}
	
	public void bajaContacto(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Contacto m = contactoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contacto", "id", id));
		m.setEstadoActivoContacto(false);
		m.setFechaBajaContacto(LocalDate.now());
		
		//Dar de baja a personaFisica y todo su alrededor
		personaFisicaService.bajaPersonaFisica(id);
		
		//Dar de baja su user
		userService.bajaUsuariosPorContacto(id);
		
		//Mantener facturas pero quitar su contacto
		facturaService.quitarContactoDeSusFacturas(id);
		
		contactoRepository.save(m);
		contactoRepository.delete(m);		//Temporalmente se elimina de la BD
	}
	
	public ContactoPayload modificarContacto(ContactoPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de contacto o se crearia uno nuevo
			Optional<Contacto> optional = contactoRepository.findById(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Contacto model = optional.get();
				model.modificar(payload);
				return contactoRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo exception
			throw new BadRequestException("No se ha encontrado el contacto con ID: " + payload.getId());//return null;
		}
		throw new BadRequestException("No se puede modificar contacto sin ID");//return null;
		
		
		//if (contacto != null && contacto.getId() != null)
		//	return contactoRepository.save(contacto);
		//else
		//	return new Contacto();
	}
	
	
	
	
	
	
	
	
	// Payloads
	/*public Contacto toModel(ContactoPayload p) {

		// Contacto
		Contacto m = new Contacto();
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());

		return m;
	}

	public ContactoPayload toPayload(Contacto m) {

		ContactoPayload p = new ContactoPayload();

		// Contacto
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		// Fin Contacto

		return p;
	}*/
}
