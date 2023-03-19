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
import com.pfi.crm.multitenant.tenant.model.Beneficiario;
import com.pfi.crm.multitenant.tenant.payload.BeneficiarioPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.BeneficiarioRepository;

@Service
public class BeneficiarioService {
	
	@Autowired
	private BeneficiarioRepository beneficiarioRepository;
	
	@Autowired
	private ActividadService actividadService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BeneficiarioService.class);
	
	public BeneficiarioPayload getBeneficiarioByIdContacto(@PathVariable Long id) {
		return beneficiarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
				() -> new ResourceNotFoundException("Beneficiario contacto", "id", id)).toPayload();
    }
	
	public List<BeneficiarioPayload> getBeneficiarios() {
		//return beneficiarioRepository.findAll();
		return beneficiarioRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public BeneficiarioPayload altaBeneficiario (BeneficiarioPayload payload) {
		payload.setId(null);
		return beneficiarioRepository.save(new Beneficiario(payload)).toPayload();
	}
	
	public void bajaBeneficiario(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Beneficiario m = beneficiarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
				() -> new ResourceNotFoundException("Beneficiario", "id", id));
		m.setEstadoActivoBeneficiario(false);
		m.setContacto(null);
		m.setPersonaFisica(null);
		beneficiarioRepository.save(m);
		
		//Eliminar objeto en todo lo que esta asociado Beneficiario
		actividadService.bajaBeneficiarioEnActividades(m.getId());
		
		beneficiarioRepository.delete(m);	//Temporalmente se elimina de la BD			
		
	}
	
	public BeneficiarioPayload modificarBeneficiario(BeneficiarioPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Beneficiario model = beneficiarioRepository.findByPersonaFisica_Contacto_Id(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Beneficiario", "id", payload.getId()));
			model.modificar(payload);
			return beneficiarioRepository.save(model).toPayload();
		}
		throw new BadRequestException("No se puede modificar Beneficiario sin ID");
	}
	
	public boolean existeBeneficiarioPorIdContacto(Long id) {
		return beneficiarioRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	
	
	// Conversiones Payload Model
	/*public Beneficiario toModel(BeneficiarioPayload p) {

		Beneficiario m = new Beneficiario();

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
		
		// Beneficiario
		m.setIdONG(p.getIdONG());
		m.setLegajo(p.getLegajo());
		m.setLugarDeNacimiento(p.getLugarDeNacimiento());
		m.setSeRetiraSolo(p.getSeRetiraSolo());
		m.setCuidadosEspeciales(p.getCuidadosEspeciales());
		m.setEscuela(p.getEscuela());
		m.setGrado(p.getGrado());
		m.setTurno(p.getTurno());
		//m.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario

		return m;
	}

	public BeneficiarioPayload toPayload(Beneficiario m) {

		BeneficiarioPayload p = new BeneficiarioPayload();

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
		
		// Beneficiario
		p.setIdONG(m.getIdONG());
		p.setLegajo(m.getLegajo());
		p.setLugarDeNacimiento(m.getLugarDeNacimiento());
		p.setSeRetiraSolo(m.getSeRetiraSolo());
		p.setCuidadosEspeciales(m.getCuidadosEspeciales());
		p.setEscuela(m.getEscuela());
		p.setGrado(m.getGrado());
		p.setTurno(m.getTurno());
		//p.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario

		return p;
	}*/
}