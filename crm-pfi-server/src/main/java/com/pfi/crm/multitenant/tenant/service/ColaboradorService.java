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
import com.pfi.crm.multitenant.tenant.model.Colaborador;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.ColaboradorPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ColaboradorRepository;

@Service
public class ColaboradorService {
	
	@Autowired
	private ColaboradorRepository colaboradorRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ColaboradorService.class);
	
	public ColaboradorPayload getColaboradorByIdContacto(@PathVariable Long id) {
        return this.getColaboradorModelByIdContacto(id).toPayload();
    }
	
	public Colaborador getColaboradorModelByIdContacto(@PathVariable Long id) {
        return colaboradorRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Colaborador", "id", id));
    }
	
	public List<ColaboradorPayload> getColaboradores() {
		//return colaboradorRepository.findAll();
		return colaboradorRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ColaboradorPayload altaColaborador (ColaboradorPayload payload) {
		return this.altaColaboradorModel(payload).toPayload();
	}
	
	/**
	 * Si ingresa un ID y "Contacto" no existe en la BD, no se dará de alta.
	 * @param payload
	 * @return model
	 */
	public Colaborador altaColaboradorModel (ColaboradorPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como colaborador a dar de alta, por favor ingrese datos de un colaborador.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) Verificar si existe colaborador. Si existe, se retorna sin dar de alta.
			Long id = payload.getId();
			boolean existeColaborador = this.existeColaboradorPorIdContacto(id);
			if(existeColaborador)
				throw new BadRequestException("Ya existe Colaborador con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		//3) Alta Colaborador
		Colaborador colaborador = new Colaborador(payload);
		colaborador.setPersonaFisica(persona);
		return colaboradorRepository.save(colaborador);
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public Colaborador altaModificarColaboradorModel(ColaboradorPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como colaborador a dar de alta/modificar, por favor ingrese datos de un colaborador.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeColaborador = colaboradorRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeColaborador)
				return this.modificarColaboradorModel(payload);
		}
		//Existe o no persona
		return altaColaboradorModel(payload);
	}
	
	public String bajaColaborador(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Colaborador m = this.getColaboradorModelByIdContacto(id);
		m.setEstadoActivoColaborador(false);
		m.setPersonaFisica(null);
		colaboradorRepository.save(m);
		colaboradorRepository.delete(m);	//Temporalmente se elimina de la BD	
		
		String message = "Se ha dado de baja a colaborador";
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
	public boolean bajaColaboradorSiExiste(Long id) {
		if(existeColaboradorPorIdContacto(id)) {
			bajaColaborador(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ColaboradorPayload modificarColaborador(ColaboradorPayload payload) {
		return this.modificarColaboradorModel(payload).toPayload();
	}
	
	public Colaborador modificarColaboradorModel(ColaboradorPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Colaborador model = this.getColaboradorModelByIdContacto(payload.getId());
			model.modificar(payload);
			return colaboradorRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Colaborador sin ID");
	}
	
	public boolean existeColaboradorPorIdContacto(Long id) {
		return colaboradorRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeColaborador = colaboradorRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeColaborador)
			throw new BadRequestException("Ya existe Colaborador con ID '" + id.toString() + "' cargado. "
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
		List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth = colaboradorRepository.countCreatedLast12MonthsByMonth(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
		return countContactosCreatedLast12MonthsByMonth;
	}
	
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad() {
        List<LocalDate> fechasNacimiento = colaboradorRepository.findAllFechaNacimiento();
        return personaFisicaService.clasificarPorEtapasEdad(fechasNacimiento);
    }
}
