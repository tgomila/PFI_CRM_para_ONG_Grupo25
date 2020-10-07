package com.pfi.crm.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.model.Contacto;
import com.pfi.crm.payload.ContactoPayload;
import com.pfi.crm.repository.ContactoRepository;

@Service
public class ContactoService {
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	public Contacto getContactoById(@PathVariable Long id) {
		return contactoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contacto", "id", id));
    }
	
	public List<Contacto> getContactos() {
		return contactoRepository.findAll();
		//return contactoRepository.findAll().filter(a -> a.getEstadoActivoContacto()).collect(Collectors.toList());
    }
	
	public Contacto altaContacto (Contacto contacto) {
		contacto.setId(null);
		return contactoRepository.save(contacto);
	}
	
	public void bajaContacto(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Contacto> optionalModel = contactoRepository.findById(id);
		if(optionalModel.isPresent()) {
			Contacto m = optionalModel.get();
			m.setEstadoActivoContacto(false);
			contactoRepository.save(m);
			//contactoRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe contacto
		}
		
	}
	
	public Contacto modificarContacto(Contacto contacto) {
		if (contacto != null && contacto.getId() != null)
			return contactoRepository.save(contacto);
		else
			return new Contacto();
	}
	
	
	
	
	
	
	
	
	// Payloads
	public Contacto toModel(ContactoPayload p) {

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
	}
}
