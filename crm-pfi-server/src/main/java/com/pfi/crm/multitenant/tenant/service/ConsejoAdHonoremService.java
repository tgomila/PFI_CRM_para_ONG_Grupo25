package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.ConsejoAdHonorem;
import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ConsejoAdHonoremRepository;

@Service
public class ConsejoAdHonoremService {
	
	@Autowired
	private ConsejoAdHonoremRepository consejoAdHonoremRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ConsejoAdHonoremService.class);
	
	public ConsejoAdHonoremPayload getConsejoAdHonoremByIdContacto(@PathVariable Long id) {
        return consejoAdHonoremRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("ConsejoAdHonorem", "id", id)).toPayload();
    }
	
	public List<ConsejoAdHonoremPayload> getConsejoAdHonorems() {
		//return ConsejoAdHonoremRepository.findAll();
		return consejoAdHonoremRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ConsejoAdHonoremPayload altaConsejoAdHonorem (ConsejoAdHonoremPayload payload) {
		payload.setId(null);
		return consejoAdHonoremRepository.save(new ConsejoAdHonorem(payload)).toPayload();
	}
	
	public void bajaConsejoAdHonorem(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		ConsejoAdHonorem m = consejoAdHonoremRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("ConsejoAdHonorem", "id", id));
		m.setEstadoActivoConsejoAdHonorem(false);
		m.setContacto(null);
		m.setPersonaFisica(null);
		consejoAdHonoremRepository.save(m);
		consejoAdHonoremRepository.delete(m);	//Temporalmente se elimina de la BD
		
	}
	
	public ConsejoAdHonoremPayload modificarConsejoAdHonorem(ConsejoAdHonoremPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			ConsejoAdHonorem model = consejoAdHonoremRepository.findByPersonaFisica_Contacto_Id(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Consejo Ad Honorem", "id", payload.getId()));
			model.modificar(payload);
			return consejoAdHonoremRepository.save(model).toPayload();	
		}
		throw new BadRequestException("No se puede modificar Consejo Ad Honorem sin ID");
	}
	
	public boolean existeConsejoAdHonoremPorIdContacto(Long id) {
		return consejoAdHonoremRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	
	
	// Conversiones Payload Model
	/*public ConsejoAdHonorem toModel(ConsejoAdHonoremPayload p) {

		ConsejoAdHonorem m = new ConsejoAdHonorem();

		// Contacto
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());
		// Fin Contacto

		// Persona Fisica
		m.setIdPersonaFisica(null);
		m.setDni(p.getDni());
		m.setNombre(p.getNombre());
		m.setApellido(p.getApellido());
		m.setFechaNacimiento(p.getFechaNacimiento());
		// Fin Persona Fisica
		
		// ConsejoAdHonorem
		m.setFuncion(p.getFuncion());
		//p.setEstadoActivoConsejoAdHonorem(this.getEstadoActivoConsejoAdHonorem());
		// Fin ConsejoAdHonorem

		return m;
	}

	public ConsejoAdHonoremPayload toPayload(ConsejoAdHonorem m) {

		ConsejoAdHonoremPayload p = new ConsejoAdHonoremPayload();

		// Contacto
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		// Fin Contacto

		// Persona Fisica
		// p.setIdPersonaFisica(null);
		p.setDni(m.getDni());
		p.setNombre(m.getNombre());
		p.setApellido(m.getApellido());
		p.setFechaNacimiento(m.getFechaNacimiento());
		// Fin Persona Fisica
		
		// ConsejoAdHonorem
		p.setFuncion(m.getFuncion());
		//p.setEstadoActivoConsejoAdHonorem(this.getEstadoActivoConsejoAdHonorem());
		// Fin ConsejoAdHonorem

		return p;
	}*/
}
