package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
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
import com.pfi.crm.multitenant.tenant.model.Voluntario;
import com.pfi.crm.multitenant.tenant.payload.VoluntarioPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.VoluntarioRepository;

@Service
public class VoluntarioService {
	
	@Autowired
	private VoluntarioRepository voluntarioRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(VoluntarioService.class);
	
	public VoluntarioPayload getVoluntarioByIdContacto(@PathVariable Long id) {
        return this.getVoluntarioModelByIdContacto(id).toPayload();
    }
	
	public Voluntario getVoluntarioModelByIdContacto(@PathVariable Long id) {
        return voluntarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Voluntario", "id", id));
    }
	
	public List<VoluntarioPayload> getVoluntarios() {
		//return voluntarioRepository.findAll();
		return voluntarioRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public VoluntarioPayload altaVoluntario (VoluntarioPayload payload) {
		return this.altaVoluntarioModel(payload).toPayload();
	}
	
	public Voluntario altaVoluntarioModel (VoluntarioPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como voluntario a dar de alta, por favor ingrese datos de un voluntario.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeVoluntario = this.existeVoluntarioPorIdContacto(id);
			if(existeVoluntario)
				throw new BadRequestException("Ya existe Voluntario con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		// 3) Alta Voluntario
		Voluntario voluntario = new Voluntario(payload);
		voluntario.setPersonaFisica(persona);
		return voluntarioRepository.save(voluntario);
		
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public Voluntario altaModificarVoluntarioModel(VoluntarioPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como voluntario a dar de alta/modificar, por favor ingrese datos de un voluntario.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeVoluntario = voluntarioRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeVoluntario)
				return this.modificarVoluntarioModel(payload);
		}
		//Existe o no persona
		return altaVoluntarioModel(payload);
	}
	
	public String bajaVoluntario(Long id) {
		
		Voluntario m = voluntarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Voluntario", "id", id));
		m.setEstadoActivoVoluntario(false);
		m.setPersonaFisica(null);
		voluntarioRepository.save(m);
		voluntarioRepository.delete(m);	//Temporalmente se elimina de la BD	
		
		String message = "Se ha dado de baja a voluntario";
		String aux = personaFisicaService.bajaPersonaFisicaSiNoTieneAsociados(id);
		if(aux != null)
			message += ". " + aux;
		return message;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaVoluntarioSiExiste(Long id) {
		if(existeVoluntarioPorIdContacto(id)) {
			bajaVoluntario(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public VoluntarioPayload modificarVoluntario(VoluntarioPayload payload) {
		return this.modificarVoluntarioModel(payload).toPayload();
	}
	
	public Voluntario modificarVoluntarioModel(VoluntarioPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Voluntario model = this.getVoluntarioModelByIdContacto(payload.getId());
			model.modificar(payload);
			return voluntarioRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Voluntario sin ID");
	}
	
	public boolean existeVoluntarioPorIdContacto(Long id) {
		return voluntarioRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeVoluntario = voluntarioRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeVoluntario)
			throw new BadRequestException("Ya existe Voluntario con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		return personaFisicaService.buscarPersonaFisicaSiExiste(id);
	}
	
	

	/**
	 * Info para gráficos de front 
	 * @return
	 */
	public List<Map<String, Object>> countCreadosUltimos12meses() {
		LocalDateTime start = LocalDateTime.now().minusMonths(11).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999);
		List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth = voluntarioRepository.countCreatedLast12MonthsByMonth(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
		return countContactosCreatedLast12MonthsByMonth;
	}
	
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad() {
        List<LocalDate> fechasNacimiento = voluntarioRepository.findAllFechaNacimiento();
        return personaFisicaService.clasificarPorEtapasEdad(fechasNacimiento);
    }
}
