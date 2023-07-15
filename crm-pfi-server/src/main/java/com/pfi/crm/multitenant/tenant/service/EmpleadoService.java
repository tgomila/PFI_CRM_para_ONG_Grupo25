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
import com.pfi.crm.multitenant.tenant.model.Empleado;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.EmpleadoRepository;

@Service
public class EmpleadoService {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);
	
	public EmpleadoPayload getEmpleadoByIdContacto(@PathVariable Long id) {
		return this.getEmpleadoModelByIdContacto(id).toPayload();
    }
	
	public Empleado getEmpleadoModelByIdContacto(@PathVariable Long id) {
		return empleadoRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
        		() -> new ResourceNotFoundException("Empleado contacto", "id", id));
    }
	
	public List<EmpleadoPayload> getEmpleados() {
		return empleadoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public EmpleadoPayload altaEmpleado (EmpleadoPayload payload) {
		return this.altaEmpleadoModel(payload).toPayload();
	}
	
	public Empleado altaEmpleadoModel (EmpleadoPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como empleado a dar de alta, por favor ingrese datos de un empleado.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeEmpleado = this.existeEmpleadoPorIdContacto(id);
			if(existeEmpleado)
				throw new BadRequestException("Ya existe Empleado con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		//3) Alta Empleado
		Empleado empleado = new Empleado(payload);
		empleado.setPersonaFisica(persona);
		return empleadoRepository.save(empleado);
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public Empleado altaModificarEmpleadoModel(EmpleadoPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como empleado a dar de alta/modificar, por favor ingrese datos de un empleado.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeEmpleado = empleadoRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeEmpleado)
				return this.modificarEmpleadoModel(payload);
		}
		//Existe o no persona
		return altaEmpleadoModel(payload);
	}
	
	public String bajaEmpleado(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Empleado m = this.getEmpleadoModelByIdContacto(id);
		m.setEstadoActivoEmpleado(false);
		m.setPersonaFisica(null);
		empleadoRepository.save(m);
		empleadoRepository.delete(m);	
		
		String message = "Se ha dado de baja a empleado";
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
	public boolean bajaEmpleadoSiExiste(Long id) {
		if(existeEmpleadoPorIdContacto(id)) {
			bajaEmpleado(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public EmpleadoPayload modificarEmpleado(EmpleadoPayload payload) {
		return this.modificarEmpleadoModel(payload).toPayload();
	}
	
	public Empleado modificarEmpleadoModel(EmpleadoPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Empleado model = this.getEmpleadoModelByIdContacto(payload.getId());
			model.modificar(payload);
			return empleadoRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Empleado sin ID");
	}
	
	public boolean existeEmpleadoPorIdContacto(Long id) {
		return empleadoRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeEmpleado = empleadoRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeEmpleado)
			throw new BadRequestException("Ya existe Empleado con ID '" + id.toString() + "' cargado. "
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
		List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth = empleadoRepository.countCreatedLast12MonthsByMonth(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
		return countContactosCreatedLast12MonthsByMonth;
	}
	
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad() {
        List<LocalDate> fechasNacimiento = empleadoRepository.findAllFechaNacimiento();
        return personaFisicaService.clasificarPorEtapasEdad(fechasNacimiento);
    }
}
