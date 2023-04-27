package com.pfi.crm.multitenant.tenant.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
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
	
	
	
	
	
	
	
	
	
	
	/**
	 * Solo para uso de Testing
	 * @return personaFisicaPayload generada
	 */
	public PersonaFisicaPayload personaFisicaGenerator() {
		return personaFisicaGenerator(null, null, null);
	}
	
	/**
	 * Solo para uso de Testing
	 * @param inputMinimoEdadPersona
	 * @param inputMaximoEdadPersona
	 * @param nombreDescripcion detalle de lo que hace
	 * @return personaFisicaPayload generada
	 */
	public PersonaFisicaPayload personaFisicaGenerator(Integer inputMinimoEdadPersona, Integer inputMaximoEdadPersona, String nombreDescripcion) {
		ContactoPayload contactoGenerado = contactoService.contactoGenerator();
		PersonaFisicaPayload personaGenerada = new PersonaFisicaPayload();
		personaGenerada.modificarContacto(contactoGenerado);
		
		//dni
		String[] parteCuit = personaGenerada.getCuit().split("-");//23-12345678-7 extraer el nro del medio
		String dni = parteCuit.length>=2 ? parteCuit[1] : "12345678";
		personaGenerada.setDni(Integer.parseInt(dni));
		
		//Nombre
		final String[] NOMBRES_FEMENINOS = {
				"Ana", "Belen", "Camila", "Carla", "Cecilia", "Delfina", "Emilia", "Florencia", "Gabriela",
				"Lucía", "Micaela", "Natalia", "Paola", "Paula", "Romina", "Sofía", "Sol", "Valentina", "Victoria",
				"Yanina"
		};
		
		final String[] NOMBRES_MASCULINOS = {
				"Agustín", "Alejandro", "Andrés", "Antonio", "Benjamín", "Carlos", "Diego", "Emiliano", "Facundo",
				"Federico", "Gabriel", "Gonzalo", "Ignacio", "Joaquín", "Juan", "Lautaro", "Lucas", "Manuel", "Matías",
				"Mauricio", "Maximiliano", "Nicolás", "Pablo", "Ramiro", "Santiago", "Sebastián", "Tomás"
		};
		
		Random random = new Random();
		boolean femenino = random.nextBoolean();
		if(femenino) {
			//Femenino
			personaGenerada.setNombre(NOMBRES_FEMENINOS[random.nextInt(NOMBRES_FEMENINOS.length)]);
		}
		else {
			//Masculino
			personaGenerada.setNombre(NOMBRES_MASCULINOS[random.nextInt(NOMBRES_MASCULINOS.length)]);
		}
		
		//Apellido
		final String[] APELLIDOS = {
				"González", "Rodríguez", "Gómez", "Pérez", "Fernández", "López", "Díaz", "Martínez", "Álvarez",
				"García", "Suárez", "Romero", "Castro", "Ortega", "Torres", "Sanchez", "Ramírez", "Flores", "Acosta",
				"Ramos"
		};
		personaGenerada.setApellido(APELLIDOS[random.nextInt(APELLIDOS.length)]);
		
		//Fecha de nacimiento
		int edadMinima = 1;
		if((inputMinimoEdadPersona != null && inputMinimoEdadPersona>0 && inputMinimoEdadPersona<120))
			edadMinima = inputMinimoEdadPersona.intValue();
		
		int edadMaxima = 120;
		if(inputMaximoEdadPersona != null && inputMaximoEdadPersona>0 && inputMaximoEdadPersona<120)
			edadMaxima = inputMinimoEdadPersona.intValue();
		
		//Se corrigen errores si algun input se ingresó mal
		if(edadMinima>edadMaxima) {
			edadMinima = 1;
			edadMaxima = 120;
		}
		
		if(inputMinimoEdadPersona == null && inputMaximoEdadPersona == null) {
			int numAleatorio = random.nextInt(100);
	        if (numAleatorio < 65) { // 65% de probabilidad
	        	edadMinima = 5;
	        	edadMaxima = 20;
	        } else if (numAleatorio < 90) { // 25% de probabilidad
	        	edadMinima = 21;
	        	edadMaxima = 45;
	        } else { // 10% de probabilidad
	        	edadMinima = 46;
	        	edadMaxima = 70;
	        }
		}
		LocalDate now = LocalDate.now();
		LocalDate minFechaNacimiento = now.minusYears(edadMaxima).plusDays(1);
		LocalDate maxFechaNacimiento = now.minusYears(edadMinima);
		
		long daysBetween = ChronoUnit.DAYS.between(minFechaNacimiento, maxFechaNacimiento);
		long randomDays = ThreadLocalRandom.current().nextLong(daysBetween + 1);
		LocalDate fechaDeNacimiento = minFechaNacimiento.plusDays(randomDays);
		
		personaGenerada.setFechaNacimiento(fechaDeNacimiento);
		
		//dni + cuit
		Period periodo = Period.between(fechaDeNacimiento, now);
		int edad = periodo.getYears();
		int dias = periodo.getDays();
		int dniAleatorioEstimado;
		//Estimación dni por millón: (pensado en abril 2023 por si se actualiza)
		//40m a 55m = 0 a 25 años
		//30m a 40m = 26 a 39 años
		//20m a 30m = 40 a 57 años
		//10m a 20m = 58 años a 69 años
		// 0m a 10m = +70 años
		
		//DNI "aleatorio" estimado
		//Ejemplo AA.BBB.BBB = AA dni minimo por edad + cuanto vale 1 día en 10M * dias de 26 a 39 años 
		double dniEstimadoAux;
		if(edad <= 25)//DNI 40M a 55M
			dniEstimadoAux = 40000000 + (14999999/(25*12*365))*((dias-25*12*365)*(-1));
		else if(edad <= 39)//DNI 30M a 40M, 26 a 39 años, hay 14 años inclusive
			dniEstimadoAux = 30000000 + (9999999/(14*12*365))*(((dias-39*12*365)+26*12*365)*(-1));
		else if(edad <= 56)//DNI 20M a 30M, 40 a 56 años, hay 17 años inclusive
			dniEstimadoAux = 20000000 + (9999999/(17*12*365))*(((dias-56*12*365)+40*12*365)*(-1));
		else if(edad <= 69)//DNI 10M a 20M, 57 a 69 años, hay 13 años inclusive
			dniEstimadoAux = 10000000 + (9999999/(13*12*365))*(((dias-69*12*365)+57*12*365)*(-1));
		else if(edad <= 100)//(aleatorio) DNI 1M a 10M, 70 a 100 años, hay 31 años inclusive
			dniEstimadoAux = 1000000  + (8999999/(31*12*365))*(((dias-100*12*365)+70*12*365)*(-1));
		else
			dniEstimadoAux = random.nextInt(100000, 999999);
		
		dniAleatorioEstimado = Double.valueOf(dniEstimadoAux).intValue();
		personaGenerada.setDni(dniAleatorioEstimado);
		
		//Modifico su cuit
		int nroUnoAlNueveAleatorio = random.nextInt(9) + 1;
		String cuit = (femenino ? "27-" : "20-") + dniAleatorioEstimado + "-" + nroUnoAlNueveAleatorio;
		personaGenerada.setCuit(cuit);
		
		//Modifico su dni
		String nombre = personaGenerada.getNombre();
		String apellido = personaGenerada.getApellido();
		//Minúsculas y sin acentos
		String nombreMinusculasSinAcento = Normalizer.normalize(nombre, Normalizer.Form.NFD)
		        .replaceAll("\\p{M}", "")
		        .toLowerCase();
		String apellidoMinusculasSinAcentos = Normalizer.normalize(apellido, Normalizer.Form.NFD)
		        .replaceAll("\\p{M}", "")
		        .toLowerCase();
		personaGenerada.setEmail(nombreMinusculasSinAcento + apellidoMinusculasSinAcentos + "@testing.com");
		
		//Modifico su descripción
		if(nombreDescripcion!=null)
			personaGenerada.setNombreDescripcion(nombreDescripcion);
		
		
		
		return personaGenerada;
	}
}
