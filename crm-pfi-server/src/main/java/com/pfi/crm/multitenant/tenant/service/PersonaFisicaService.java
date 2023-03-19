package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaFisicaRepository;

@Service
public class PersonaFisicaService {
	
	@Autowired
	private PersonaFisicaRepository personaFisicaRepository;
	
	@Autowired
	private ContactoRepository contactoRepository;
	
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
        return personaFisicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaFisica", "id", id)).toPayload();
    }
	
	public List<PersonaFisicaPayload> getPersonasFisicas() {
		//return personaFisicaRepository.findAll();
		return personaFisicaRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public PersonaFisicaPayload altaPersonaFisica (PersonaFisicaPayload payload) {
		if(payload.getId() != null) {
			/**
			 * 3 pasos:
			 * 1) Verificar si existe Persona. Si existe Persona, se vuelve sin dar de alta
			 * 2) Verificar si existe Contacto a asociar. De "no" encontrar Contacto, se vuelve sin dar de alta.
			 * 3) Buscar Contacto y devolver Contacto. 
			 */
			Long id = payload.getId();
			boolean existePersona = personaFisicaRepository.existsByContacto_Id(id);
			if(existePersona)
				throw new BadRequestException("Ya existe Persona con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
			boolean existeContacto = contactoRepository.existsById(id);
			if(!existeContacto)
				throw new BadRequestException("Ha ingresado un ID '" + id.toString() + "' para asociar un contacto, y no existe. "
						+ "Es posible que sea otro número o haya sido dado de baja.");
			//Existe contacto
			//Lo comentado de abajo, era para que el usuario no pueda editar contacto
			//Contacto contacto = contactoRepository.findById(id).orElseThrow(
	        //        () -> new ResourceNotFoundException("Contacto", "id", id));
			PersonaFisica newModel = new PersonaFisica(payload);
			//newModel.setContacto(contacto);//No permito que se edite el contacto
			return personaFisicaRepository.save(newModel).toPayload();
		}
		else {
			return personaFisicaRepository.save(new PersonaFisica(payload)).toPayload();
		}
	}
	
	public void bajaPersonaFisica(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		PersonaFisica m = personaFisicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaFisica", "id", id));
		m.setEstadoActivoPersonaFisica(false);

		if(beneficiarioService.existeBeneficiarioPorIdContacto(id))
			beneficiarioService.bajaBeneficiario(id);
		
		if(colaboradorService.existeColaboradorPorIdContacto(id))
			colaboradorService.bajaColaborador(id);
		
		if(consejoAdHonoremService.existeConsejoAdHonoremPorIdContacto(id))
			consejoAdHonoremService.bajaConsejoAdHonorem(id);
		
		if(empleadoService.existeEmpleadoPorIdContacto(id))
			empleadoService.bajaEmpleado(id);
		
		if(profesionalService.existeProfesionalPorIdContacto(id))
			profesionalService.bajaProfesional(id);
		
		if(voluntarioService.existeVoluntarioPorIdContacto(id))
			voluntarioService.bajaVoluntario(id);
		
		personaFisicaRepository.save(m);
		personaFisicaRepository.delete(m);	//Temporalmente se elimina de la BD
		
		
	}
	
	public PersonaFisicaPayload modificarPersonaFisica(PersonaFisicaPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			PersonaFisica model = personaFisicaRepository.findByContacto_Id(payload.getId()).orElseThrow(
	                () -> new ResourceNotFoundException("PersonaFisica", "id", payload.getId()));
			model.modificar(payload);
			return personaFisicaRepository.save(model).toPayload();
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
		return ResponseEntity.ok(contactoService.getContactoById(id));//Devuelve no ok si no hay contacto cargado
		
		
			//return ResponseEntity.ok(getPersonaFisicaByIdContacto(id));
		//boolean existeContacto = contactoRepository.existsById(id);
		//if(existeContacto)
			//return ResponseEntity.ok(contactoService.getContactoById(id));
		//return ResponseEntity.notFound().build();
	}
	
	
	
	//Conversiones Payload Model
	/*public PersonaFisica toModel (PersonaFisicaPayload p) {
		
		PersonaFisica m = new PersonaFisica();
		
		//Contacto		
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());
		//Fin Contacto
		
		// Persona Juridica
		m.setIdPersonaFisica(null);
		m.setDni(p.getDni());
		m.setNombre(p.getNombre());
		m.setApellido(p.getApellido());
		m.setFechaNacimiento(p.getFechaNacimiento());
		// Fin Persona Juridica
		
		return m;
	}
	
	public PersonaFisicaPayload toPayload(PersonaFisica m) {
		
		PersonaFisicaPayload p = new PersonaFisicaPayload();
		
		//Contacto		
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		//Fin Contacto
		
		// Persona Juridica
			// p.setIdPersonaFisica(null);
		p.setDni(m.getDni());
		p.setNombre(m.getNombre());
		p.setApellido(m.getApellido());
		p.setFechaNacimiento(m.getFechaNacimiento());
		// Fin Persona Juridica
		
		return p;
	}*/
}
