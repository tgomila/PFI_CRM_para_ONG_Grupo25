package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.PersonaJuridica;
import com.pfi.crm.multitenant.tenant.payload.PersonaJuridicaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaJuridicaRepository;

@Service
public class PersonaJuridicaService {
	
	@Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	//@Autowired
	//private PersonaJuridicaService personaJuridicaService;
	
	public PersonaJuridicaPayload getPersonaJuridicaByIdContacto(@PathVariable Long id) {
        return this.getPersonaJuridicaModelByIdContacto(id).toPayload();
    }
	
	public PersonaJuridica getPersonaJuridicaModelByIdContacto(Long id) {
        return personaJuridicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaJuridica", "id", id));
    }
	
	public List<PersonaJuridicaPayload> getPersonasJuridicas() {
		//return personaJuridicaRepository.findAll();
		return personaJuridicaRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public PersonaJuridicaPayload altaPersonaJuridica (PersonaJuridicaPayload payload) {
		return this.altaPersonaJuridicaModel(payload).toPayload();
	}
	
	public PersonaJuridica altaPersonaJuridicaModel (PersonaJuridicaPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como personaJuridica a dar de alta, por favor ingrese datos de una personaJuridica.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existePersonaJuridica = this.existePersonaJuridicaPorIdContacto(id);
			if(existePersonaJuridica)
				throw new BadRequestException("Ya existe PersonaJuridica con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Buscar/Crear Contacto y asociarlo. Si hay ID Contacto y no existe en BD, se vuelve sin dar de alta.
		Contacto contacto = contactoService.altaModificarContactoModel(payload);
		// 3) Alta PersonaJuridica
		PersonaJuridica personaJuridica = new PersonaJuridica(payload);
		personaJuridica.setContacto(contacto);
		// 4) Alta PersonaJuridica
		return personaJuridicaRepository.save(personaJuridica);
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public PersonaJuridica altaModificarPersonaJuridicaModel(PersonaJuridicaPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como personaJuridica a dar de alta/modificar, por favor ingrese datos de una personaJuridica.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existePersonaJuridica = personaJuridicaRepository.existsByContacto_Id(payload.getId());
			if(existePersonaJuridica)
				return this.modificarPersonaJuridicaModel(payload);
		}
		//Existe o no persona
		return altaPersonaJuridicaModel(payload);
	}
	
	public String bajaPersonaJuridica(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		PersonaJuridica model = this.getPersonaJuridicaModelByIdContacto(id);
		model.setEstadoActivoPersonaJuridica(false);
		model.setContacto(null);
		model = personaJuridicaRepository.save(model);
		personaJuridicaRepository.delete(model);	//Temporalmente se elimina de la BD
		
		String message = "Se ha dado de baja a persona jurídica";
		String aux = contactoService.bajaContactoSiNoTieneAsociados(id);
		if(aux != null)
			message += ". " + aux;
		return message;
	}
	
	/**
	 * 
	 * @param id
	 * @return String con mensaje de si se dió de baja. O string vacío si no existe y no se dió de baja.
	 */
	public String bajaPersonaJuridicaSiExiste(Long id) {
		if(existePersonaJuridicaPorIdContacto(id))
			return bajaPersonaJuridica(id);
		return "";
	}
	
	public PersonaJuridicaPayload modificarPersonaJuridica(PersonaJuridicaPayload payload) {
		return this.modificarPersonaJuridicaModel(payload).toPayload();
	}
	
	public PersonaJuridica modificarPersonaJuridicaModel(PersonaJuridicaPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona juridica o se crearia uno nuevo
			PersonaJuridica model = this.getPersonaJuridicaModelByIdContacto(payload.getId());
			model.modificar(payload);
			return personaJuridicaRepository.save(model);	
		}
		throw new BadRequestException("No se puede modificar Persona Jurídica sin ID");
	}
	
	public boolean existePersonaJuridicaPorIdContacto(Long id) {
		return personaJuridicaRepository.existsByContacto_Id(id);
	}
	
	public ResponseEntity<?> buscarContactoSiExiste(Long id) {
		boolean existePersonaJuridica = personaJuridicaRepository.existsByContacto_Id(id);
		if(existePersonaJuridica)
			throw new BadRequestException("Ya existe Persona Jurídica con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		boolean existeContacto = contactoService.existeContacto(id);
		if(!existeContacto)
			throw new BadRequestException("No existe Contacto ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o no exista.");
		return ResponseEntity.ok(contactoService.getContactoById(id));//Devuelve no ok si no hay contacto cargado
	}
}
