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
import com.pfi.crm.multitenant.tenant.model.Beneficiario;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.BeneficiarioPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.BeneficiarioRepository;

@Service
public class BeneficiarioService {
	
	@Autowired
	private BeneficiarioRepository beneficiarioRepository;
	
	@Autowired
	private ActividadService actividadService;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BeneficiarioService.class);
	
	public BeneficiarioPayload getBeneficiarioByIdContacto(@PathVariable Long id) {
		return getBeneficiarioModelByIdContacto(id).toPayload();
    }
	
	public Beneficiario getBeneficiarioModelByIdContacto(@PathVariable Long id) {
		return beneficiarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
				() -> new ResourceNotFoundException("Beneficiario contacto", "id", id));
    }
	
	public List<BeneficiarioPayload> getBeneficiarios() {
		//return beneficiarioRepository.findAll();
		return beneficiarioRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public BeneficiarioPayload altaBeneficiario (BeneficiarioPayload payload) {
		return this.altaBeneficiarioModel(payload).toPayload();
	}
	
	/**
	 * Si ingresa un ID y "Contacto" no existe en la BD, no se dará de alta.
	 * @param payload
	 * @return model
	 */
	public Beneficiario altaBeneficiarioModel(BeneficiarioPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como beneficiario a dar de alta, por favor ingrese datos de un beneficiario.");

		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeBeneficiario = this.existeBeneficiarioPorIdContacto(id);
			if(existeBeneficiario)
				throw new BadRequestException("Ya existe Beneficiario con ID '" + id.toString() + "' cargado. "
						+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		}
		
		// 2) Alta/Modificar Persona
		PersonaFisica persona = personaFisicaService.altaModificarPersonaFisicaModel(payload);
		// 3) Alta Beneficiario
		Beneficiario beneficiario = new Beneficiario(payload);
		beneficiario.setPersonaFisica(persona);
		return beneficiarioRepository.save(beneficiario);
	}
	
	//Es reciclado de alta y modificar
	/**
	 * Este método sirve para services superiores. No controllers.
	 * @param payload a modificar/alta.
	 * @return Model modificado/creado en BD.
	 */
	public Beneficiario altaModificarBeneficiarioModel(BeneficiarioPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como beneficiario a dar de alta/modificar, por favor ingrese datos de un beneficiario.");
		
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta:
			boolean existeBeneficiario = beneficiarioRepository.existsByPersonaFisica_Contacto_Id(payload.getId());
			if(existeBeneficiario)
				return this.modificarBeneficiarioModel(payload);
		}
		//Existe o no persona
		return altaBeneficiarioModel(payload);
	}
	
	public void bajaBeneficiario(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Beneficiario m = getBeneficiarioModelByIdContacto(id);
		m.setEstadoActivoBeneficiario(false);
		m.setPersonaFisica(null);
		m = beneficiarioRepository.save(m);
		
		//Eliminar objeto en todo lo que esta asociado Beneficiario
		actividadService.bajaBeneficiarioEnActividades(m.getId());
		
		beneficiarioRepository.delete(m);	//Temporalmente se elimina de la BD			
		
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaBeneficiarioSiExiste(Long id) {
		if(existeBeneficiarioPorIdContacto(id)) {
			bajaBeneficiario(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public BeneficiarioPayload modificarBeneficiario(BeneficiarioPayload payload) {
		return modificarBeneficiarioModel(payload).toPayload();
	}
	
	public Beneficiario modificarBeneficiarioModel(BeneficiarioPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Beneficiario model = getBeneficiarioModelByIdContacto(payload.getId());
			model.modificar(payload);
			return beneficiarioRepository.save(model);
		}
		throw new BadRequestException("No se puede modificar Beneficiario sin ID");
	}
	
	public boolean existeBeneficiarioPorIdContacto(Long id) {
		return beneficiarioRepository.existsByPersonaFisica_Contacto_Id(id);
	}
	
	public ResponseEntity<?> buscarPersonaFisicaSiExiste(Long id) {
		boolean existeBeneficiario = beneficiarioRepository.existsByPersonaFisica_Contacto_Id(id);
		if(existeBeneficiario)
			throw new BadRequestException("Ya existe Beneficiario con ID '" + id.toString() + "' cargado. "
					+ "Es posible que sea otro número o quiera ir a la pantalla de modificar.");
		return personaFisicaService.buscarPersonaFisicaSiExiste(id);
	}
}
