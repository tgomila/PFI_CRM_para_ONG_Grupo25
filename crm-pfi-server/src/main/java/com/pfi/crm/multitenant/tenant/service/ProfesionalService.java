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
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.model.Profesional;
import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProfesionalRepository;

@Service
public class ProfesionalService {
	
	@Autowired
	private ProfesionalRepository profesionalRepository;
	
	@Autowired
	private ActividadService actividadService;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProfesionalService.class);
	
	public ProfesionalPayload getProfesionalByIdContacto(@PathVariable Long id) {
        return this.getProfesionalModelByIdContacto(id).toPayload();
    }
	
	public Profesional getProfesionalModelByIdContacto(Long id) {
        return profesionalRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Profesional", "id", id));
    }
	
	public List<ProfesionalPayload> getProfesionales() {
		//return profesionalRepository.findAll();
		return profesionalRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ProfesionalPayload altaProfesional (ProfesionalPayload payload) {
		return this.altaProfesionalModel(payload).toPayload();
	}
	
	public Profesional altaProfesionalModel (ProfesionalPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como profesional a dar de alta, por favor ingrese datos de un profesional.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) Verificar si existe profesional. Si existe, se retorna sin dar de alta.
			Long id = payload.getId();
			boolean existeProfesional = this.existeProfesionalPorIdContacto(id);
			if(existeProfesional)
				throw new BadRequestException("Ya existe Profesional con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		//3) Alta Profesional
		Profesional profesional = new Profesional(payload);
		profesional.setPersonaFisica(persona);
		return profesionalRepository.save(profesional);
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public Profesional altaModificarProfesionalModel(ProfesionalPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como profesional a dar de alta/modificar, por favor ingrese datos de un profesional.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeProfesional = profesionalRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeProfesional)
				return this.modificarProfesionalModel(payload);
		}
		//Existe o no persona
		return altaProfesionalModel(payload);
	}
	
	public String bajaProfesional(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Profesional m = this.getProfesionalModelByIdContacto(id);
		String message = "Se ha dado de baja a profesional";
		
		//Eliminar objeto en todo lo que esta asociado Profesional
		String aux = actividadService.bajaProfesionalEnActividades(m.getId());
		if(aux != null && !aux.isEmpty())
			message += " " + aux;
		
		m.setEstadoActivoProfesional(false);
		m.setPersonaFisica(null);
		profesionalRepository.save(m);
		profesionalRepository.delete(m);	//Temporalmente se elimina de la BD		
		
		aux = personaFisicaService.bajaPersonaFisicaSiNoTieneAsociados(id);
		if(aux != null && !aux.isEmpty())
			message += " " + aux;
		return message;
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaProfesionalSiExiste(Long id) {
		if(existeProfesionalPorIdContacto(id)) {
			bajaProfesional(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ProfesionalPayload modificarProfesional(ProfesionalPayload payload) {
		return this.modificarProfesionalModel(payload).toPayload();
	}
	
	public Profesional modificarProfesionalModel(ProfesionalPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Profesional model = this.getProfesionalModelByIdContacto(payload.getId());
			model.modificar(payload);
			return profesionalRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Profesional sin ID");
	}
	
	public boolean existeProfesionalPorIdContacto(Long id) {
		return profesionalRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeProfesional = profesionalRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeProfesional)
			throw new BadRequestException("Ya existe Profesional con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		return personaFisicaService.buscarPersonaFisicaSiExiste(id);
	}
}
