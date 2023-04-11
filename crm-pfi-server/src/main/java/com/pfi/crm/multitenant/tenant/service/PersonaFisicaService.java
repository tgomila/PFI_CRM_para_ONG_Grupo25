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
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaFisicaRepository;

@Service
public class PersonaFisicaService {
	
	@Autowired
	private PersonaFisicaRepository personaFisicaRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	//Services utilizados solo para cuando se da de baja 1 persona, se le da de baja en todo lo relacionado (excepto Contacto)
	//	Cualquier duda ver el diagrama de BD o de clases para comprender.
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Autowired
	private ConsejoAdHonoremService consejoAdHonoremService;
	
	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private ProfesionalService profesionalService;
	
	@Autowired
	private VoluntarioService voluntarioService;
	
	public PersonaFisicaPayload getPersonaFisicaByIdContacto(@PathVariable Long id) {
        return this.getPersonaFisicaModelByIdContacto(id).toPayload();
    }
	
	public PersonaFisica getPersonaFisicaModelByIdContacto(Long id) {
        return personaFisicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaFisica", "id", id));
    }
	
	public List<PersonaFisicaPayload> getPersonasFisicas() {
		//return personaFisicaRepository.findAll();
		return personaFisicaRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public PersonaFisicaPayload altaPersonaFisica (PersonaFisicaPayload payload) {
		return altaPersonaFisicaModel(payload).toPayload();
	}
	
	/**
	 * Si ingresa un ID y "Contacto" no existe en la BD, no se dará de alta.
	 * @param payload
	 * @return model
	 */
	public PersonaFisica altaPersonaFisicaModel (PersonaFisicaAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como persona a dar de alta, por favor ingrese datos de una persona.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existePersona = this.existePersonaFisicaPorIdContacto(id);
			if(existePersona)
				throw new BadRequestException("Ya existe Persona con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		// 2) Buscar/Crear Contacto y asociarlo. Si hay ID Contacto y no existe en BD, se vuelve sin dar de alta.
		Contacto contacto = contactoService.altaModificarContactoModel(payload);
		// 3) Alta Persona
		PersonaFisica persona = new PersonaFisica(payload);
		persona.setContacto(contacto);
		return personaFisicaRepository.save(persona);
	}
	
	//Es reciclado de alta y modificar
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public PersonaFisica altaModificarPersonaFisicaModel (PersonaFisicaAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como persona a dar de alta/modificar, por favor ingrese datos de una persona.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existePersona = personaFisicaRepository.existsByContacto_Id(payload.getId());
			if(existePersona)
				return this.modificarPersonaFisicaModel(payload);
		}
		//Existe o no persona
		return altaPersonaFisicaModel(payload);
	}
	
	/**
	 * Baja de Persona Física y sus models asociados a la persona.
	 * @param id de contacto
	 * @return mensaje de qué se dió de baja para un ResponseEntity.ok
	 */
	public String bajaPersonaFisica(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		PersonaFisica m = this.getPersonaFisicaModelByIdContacto(id);
		m.setEstadoActivoPersonaFisica(false);
		m.setContacto(null);
		
		String message = "Se ha dado de baja a Persona";
		message += beneficiarioService.bajaBeneficiarioSiExiste(id) ? ", Beneficiario" : "";
		message += colaboradorService.bajaColaboradorSiExiste(id) ? ", Colaborador" : "";
		message += consejoAdHonoremService.bajaConsejoAdHonoremSiExiste(id) ? ", Consejo Ad Honorem" : "";
		message += empleadoService.bajaEmpleadoSiExiste(id) ? ", Empleado" : "";
		message += profesionalService.bajaProfesionalSiExiste(id) ? ", Profesional" : "";
		message += voluntarioService.bajaVoluntarioSiExiste(id) ? ", Voluntario" : "";
		message += " de id: " + id.toString() + "";
		
		m = personaFisicaRepository.save(m);
		personaFisicaRepository.delete(m);	//Temporalmente se elimina de la BD
		
		return message;
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public String bajaPersonaFisicaSiExiste(Long id) {
		if(existePersonaFisicaPorIdContacto(id))
			return bajaPersonaFisica(id);
		return "";
	}
	
	public PersonaFisicaPayload modificarPersonaFisica(PersonaFisicaPayload payload) {
		return modificarPersonaFisicaModel(payload).toPayload();
	}
	
	public PersonaFisica modificarPersonaFisicaModel(PersonaFisicaAbstractPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			PersonaFisica model = this.getPersonaFisicaModelByIdContacto(payload.getId());
			model.modificar(payload);
			return personaFisicaRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Persona Fisica sin ID");
	}
	
	public boolean existePersonaFisicaPorIdContacto(Long id) {
		return personaFisicaRepository.existsByContacto_Id(id);
	}
	
	public ResponseEntity<?> buscarContactoSiExiste(Long id) {
		boolean existePersona = personaFisicaRepository.existsByContacto_Id(id);
		if(existePersona)
			throw new BadRequestException("Ya existe Persona con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		boolean existeContacto = contactoService.existeContacto(id);
		if(!existeContacto)
			throw new BadRequestException("No existe Contacto ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o no exista.");
			//return ResponseEntity.notFound().build();
		//Existe contacto
		return ResponseEntity.ok(contactoService.getContactoById(id));//Devuelve no ok si no hay contacto cargado
	}
	
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param id a buscar.
	 * @return Dto encontrado, o nada.
	 */
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id){
		boolean existePersona = personaFisicaRepository.existsByContacto_Id(id);
		if(existePersona)
			return ResponseEntity.ok(this.getPersonaFisicaByIdContacto(id));//Devuelve no ok si no hay contacto cargado
		boolean existeContacto = contactoService.existeContacto(id);
		if(existeContacto)
			return ResponseEntity.ok(contactoService.getContactoById(id));//Devuelve no ok si no hay contacto cargado
		//No existen
		throw new BadRequestException("No existe Contacto ID '" + id.toString() + "' cargado. "
				+ "Es posible que sea otro número o no exista.");
	}
}
