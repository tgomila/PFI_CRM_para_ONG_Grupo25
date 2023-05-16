package com.pfi.crm.multitenant.tenant.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
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
	
	public String bajaBeneficiario(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Beneficiario m = getBeneficiarioModelByIdContacto(id);
		String message = "Se ha dado de baja a beneficiario";
		
		//Eliminar objeto en todo lo que esta asociado Beneficiario
		String aux = actividadService.bajaBeneficiarioEnActividades(m.getId());
		if(aux != null && !aux.isEmpty())
			message += ". " + aux;
		m.setEstadoActivoBeneficiario(false);
		m.setPersonaFisica(null);
		m = beneficiarioRepository.save(m);
		
		beneficiarioRepository.delete(m);	//Temporalmente se elimina de la BD			
		
		
		aux = personaFisicaService.bajaPersonaFisicaSiNoTieneAsociados(id);
		if(aux != null && !aux.isEmpty())
			message += ". " + aux;
		return message;
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

	/**
	 * Sirve para generador de beneficiarios y modificar su create date principalmente
	 * @param payload
	 * @return
	 */
	public Beneficiario modificarBeneficiarioModel(Beneficiario model) {
		if (model != null && model.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
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
	
	
	
	
	
	
	

	/**
	 * Solo para uso de testing
	 * @return
	 */
	public List<BeneficiarioPayload> generar_100_Beneficiarios(int anioCreacion){
		List<Beneficiario> listAltas = new ArrayList<Beneficiario>();
		for(int i=0; i<100; i++) {
			BeneficiarioPayload payloadGenerado = this.beneficiarioGenerator();
			Beneficiario model = this.altaBeneficiarioModel(payloadGenerado);
			listAltas.add(model);
		}
		List<BeneficiarioPayload> listAltasPayload = new ArrayList<BeneficiarioPayload>();
		List<LocalDateTime> fechas = personaFisicaService.generarCienFechasDistribuidasPorAnio(anioCreacion);
		for(Beneficiario alta: listAltas) {
			if(fechas.isEmpty())
				break;
			Instant fecha = fechas.get(0).toInstant(ZoneOffset.UTC);
			alta.setCreatedAt(fecha);
			alta.getContacto().setCreatedAt(fecha);
			fechas.remove(0);
			BeneficiarioPayload payload = this.modificarBeneficiarioModel(alta).toPayload();
			listAltasPayload.add(payload);
		}
		return listAltasPayload;
	}
	
	public BeneficiarioPayload beneficiarioGenerator() {
		return beneficiarioGenerator(6, 18);
	}
	
	public BeneficiarioPayload beneficiarioGenerator(Integer inputMinimoEdadPersona, Integer inputMaximoEdadPersona) {
		
		PersonaFisicaPayload personaGenerada = personaFisicaService.personaFisicaGenerator(inputMinimoEdadPersona, inputMaximoEdadPersona, "Beneficiario");
		BeneficiarioPayload beneficiarioGenerado = new BeneficiarioPayload();
		beneficiarioGenerado.modificarPersonaFisica(personaGenerada);
		
		Random random = new Random();
		
		//ID ONG
		long numeroGenerado = random.nextLong(10000);
		beneficiarioGenerado.setIdONG(numeroGenerado);
		
		//Legajo
		int legajoGenerado = random.nextInt(100000);
		String legajo = String.format("%04d", legajoGenerado);
		legajo = "10"+legajo;
		beneficiarioGenerado.setLegajo(Long.parseLong(legajo));
		
		//Lugar de nacimiento
		final List<String> BARRIOS = Arrays.asList("Almagro", "Belgrano", "Boedo", "Caballito", "Chacarita",
				"Coghlan", "Colegiales", "Flores", "La Boca", "Mataderos", "Núñez", "Palermo", "Parque Chacabuco",
				"San Cristóbal", "San Nicolás", "Saavedra", "Versalles", "Villa Crespo", "Villa del Parque",
				"Villa Devoto", "Villa Lugano", "Villa Luro", "Villa Ortúzar", "Villa Pueyrredón", "Villa Real",
				"Villa Riachuelo", "Villa Santa Rita", "Villa Soldati", "Villa Urquiza");
		beneficiarioGenerado.setLugarDeNacimiento(BARRIOS.get(random.nextInt(BARRIOS.size())));
		
		//Se retira solo:
		int aleatorio = random.nextInt(10)+1;
		boolean seRetiraSolo = aleatorio > 7 ? true : false;
		beneficiarioGenerado.setSeRetiraSolo(seRetiraSolo);
		
		//Cuidados especiales
		final List<String> PARTE1 = Arrays.asList("Necesita ", "Requiere ", "No le gusta ", "Le agradaría ");
		final List<String> PARTE2 = Arrays.asList("jugar con bloquecitos ", "hablar ", "la asistencia médica", "la atención",
				"amigos", "actividades", "atención de los padres", "sentirse independiente");
		aleatorio = random.nextInt(10)+1;
		
		if(aleatorio<=6) {
			beneficiarioGenerado.setCuidadosEspeciales("Ninguno");
		}
		else {
			String parte1 = PARTE1.get(random.nextInt(PARTE1.size()));
			String parte2 = PARTE2.get(random.nextInt(PARTE2.size()));
			beneficiarioGenerado.setCuidadosEspeciales(parte1+parte2);
		}
		
		//Escuela
		final List<String> COLEGIOS = Arrays.asList("Colegio Nacional Buenos Aires", "Colegio Nacional de Buenos Aires",
				"Colegio Nacional de Buenos Aires - Escuela Superior de Comercio Carlos Pellegrini",
				"Instituto Superior de Educación Física", "Escuela Nacional de Educación Técnica Nº1 Otto Krause",
				"Escuela Técnica ORT Argentina", "Instituto Ballester", "Escuela Normal Superior en Lenguas Vivas",
				"Colegio San José", "Colegio Nacional de Educación Técnica Nº35",
				"Escuela Técnica Nº3 DE 1 Prof. Félix Aguilar", "Instituto Santa María",
				"Instituto Bernasconi", "Colegio del Salvador", "Colegio San Francisco de Sales",
				"Instituto Libre de Segunda Enseñanza", "Escuela de Educación Técnica Nº27 Hipólito Yrigoyen",
				"Instituto San José de Flores", "Colegio Goethe Buenos Aires", "Colegio Jean Piaget");
		beneficiarioGenerado.setEscuela(COLEGIOS.get(random.nextInt(COLEGIOS.size())));
		
		//Grado
		LocalDate fechaNacimiento = beneficiarioGenerado.getFechaNacimiento();
		Period periodDiferenciaAnios = Period.between(fechaNacimiento, LocalDate.of(LocalDate.now().getYear(), 7, 31));
		int edad = periodDiferenciaAnios.getYears();
		if(edad<=12) {
			int nroPrimaria = edad-5;
			beneficiarioGenerado.setGrado(nroPrimaria+"º grado");
		}
		else if(edad<=18) {
			int nroSecundaria = edad-13;
			beneficiarioGenerado.setGrado(nroSecundaria+"º año");
		}
		else {
			beneficiarioGenerado.setGrado("Terminado");
		}
		
		//Turno
		aleatorio = random.nextInt(10)+1;
		String turno = aleatorio <=7 ? "Mañana" : aleatorio <=9 ? "Tarde" : "Noche";
		beneficiarioGenerado.setTurno(turno);
		
		return beneficiarioGenerado;
	}
	
	
}
