package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.ConsejoAdHonorem;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ConsejoAdHonoremRepository;

@Service
public class ConsejoAdHonoremService {
	
	@Autowired
	private ConsejoAdHonoremRepository consejoAdHonoremRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ConsejoAdHonoremService.class);
	
	public ConsejoAdHonoremPayload getConsejoAdHonoremByIdContacto(@PathVariable Long id) {
        return this.getConsejoAdHonoremModelByIdContacto(id).toPayload();
    }
	
	public ConsejoAdHonorem getConsejoAdHonoremModelByIdContacto(@PathVariable Long id) {
        return consejoAdHonoremRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("ConsejoAdHonorem", "id", id));
    }
	
	public List<ConsejoAdHonoremPayload> getConsejoAdHonorems() {
		//return ConsejoAdHonoremRepository.findAll();
		return consejoAdHonoremRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ConsejoAdHonoremPayload altaConsejoAdHonorem (ConsejoAdHonoremPayload payload) {
		return this.altaConsejoAdHonoremModel(payload).toPayload();
	}
	
	public ConsejoAdHonorem altaConsejoAdHonoremModel (ConsejoAdHonoremPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como ConsejoAdHonorem a dar de alta, por favor ingrese datos de un ConsejoAdHonorem.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeConsejoAdHonorem = this.existeConsejoAdHonoremPorIdContacto(id);
			if(existeConsejoAdHonorem)
				throw new BadRequestException("Ya existe ConsejoAdHonorem con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		//3) Alta ConsejoAdHonorem
		ConsejoAdHonorem consejoAdHonorem = new ConsejoAdHonorem(payload);
		consejoAdHonorem.setPersonaFisica(persona);
		return consejoAdHonoremRepository.save(consejoAdHonorem);
		
	}
	
	//Es reciclado de alta y modificar
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public ConsejoAdHonorem altaModificarConsejoAdHonoremModel (ConsejoAdHonoremPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como consejoAdHonorem a dar de alta/modificar, por favor ingrese datos de un consejoAdHonorem.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeConsejoAdHonorem = consejoAdHonoremRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeConsejoAdHonorem)
				return this.modificarConsejoAdHonoremModel(payload);
		}
		//Existe o no persona
		return altaConsejoAdHonoremModel(payload);
	}
	
	public void bajaConsejoAdHonorem(Long id) {
		
		ConsejoAdHonorem m = this.getConsejoAdHonoremModelByIdContacto(id);
		m.setEstadoActivoConsejoAdHonorem(false);
		m.setPersonaFisica(null);
		consejoAdHonoremRepository.save(m);
		consejoAdHonoremRepository.delete(m);	//Temporalmente se elimina de la BD
		
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaConsejoAdHonoremSiExiste(Long id) {
		if(existeConsejoAdHonoremPorIdContacto(id)) {
			bajaConsejoAdHonorem(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ConsejoAdHonoremPayload modificarConsejoAdHonorem(ConsejoAdHonoremPayload payload) {
		return this.modificarConsejoAdHonoremModel(payload).toPayload();
	}
	
	public ConsejoAdHonorem modificarConsejoAdHonoremModel(ConsejoAdHonoremPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			ConsejoAdHonorem model = this.getConsejoAdHonoremModelByIdContacto(payload.getId());
			model.modificar(payload);
			return consejoAdHonoremRepository.save(model);	
		}
		throw new BadRequestException("No se puede modificar Consejo Ad Honorem sin ID");
	}
	
	public boolean existeConsejoAdHonoremPorIdContacto(Long id) {
		return consejoAdHonoremRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeConsejoAdHonorem = consejoAdHonoremRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeConsejoAdHonorem)
			throw new BadRequestException("Ya existe Consejo Ad Honorem con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		return personaFisicaService.buscarPersonaFisicaSiExiste(id);
	}
}
