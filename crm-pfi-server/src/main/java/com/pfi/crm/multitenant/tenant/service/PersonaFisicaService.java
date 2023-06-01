package com.pfi.crm.multitenant.tenant.service;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
	
	public PersonaFisicaPayload getPersonaFisicaByDni(@PathVariable int dni) {
        return this.getPersonaFisicaModelByDni(dni).toPayload();
    }
	
	public PersonaFisica getPersonaFisicaModelByDni(int dni) {
        return personaFisicaRepository.findByDni(dni).orElseThrow(
                () -> new ResourceNotFoundException("PersonaFisica", "dni", dni));
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
		int dni = payload.getDni();
		if(personaFisicaRepository.existsByDni(dni)) { //Si existe DNI de persona ya de alta:
			// 1) No permito dar un 2do mismo dni de alta, solo alta si no existe dni.
			PersonaFisica personaYaDeAlta = getPersonaFisicaModelByDni(dni);
			throw new BadRequestException("Ya existe Persona con DNI '" + personaYaDeAlta.getDni() + "' cargado "
					+ "en la Base de Datos, cuyo ID de Persona es: '" + personaYaDeAlta.getId() + "', "
					+ "nombre: '" + personaYaDeAlta.getNombre() != null ? personaYaDeAlta.getNombre() : "(vacío)" + "', "
					+ "apellido: '" + personaYaDeAlta.getApellido() != null ? personaYaDeAlta.getApellido() : "(vacío)" + "'. "
					+ "Es posible que haya ingresado mal el DNI o quiera ir a la pantalla modificar antes "
					+ "que dar de alta una misma persona.");
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
		
		boolean existePersona = false;
		if(payload.getId() != null) { //Si existe ID contacto asociado de alta, entonces si modifico
			existePersona = personaFisicaRepository.existsByContacto_Id(payload.getId());
			if(existePersona)
				return this.modificarPersonaFisicaModel(payload);
		}
		existePersona = personaFisicaRepository.existsByDni(payload.getDni());
		if(existePersona) {//Si existe DNI de persona asociada de alta, pero su ID payload no tiene nada que ver, entonces por las dudas no modifico
			PersonaFisica personaYaDeAlta = getPersonaFisicaModelByDni(payload.getDni());
			throw new BadRequestException("Ya existe Persona con DNI '" + personaYaDeAlta.getDni() + "' cargado "
					+ "en la Base de Datos, cuyo ID de Persona es: '" + personaYaDeAlta.getId() + "', "
					+ "nombre: '" + personaYaDeAlta.getNombre() != null ? personaYaDeAlta.getNombre() : "(vacío)" + "', "
					+ "apellido: '" + personaYaDeAlta.getApellido() != null ? personaYaDeAlta.getApellido() : "(vacío)" + "'. "
					+ "Si ese esa es la persona que desea asociar, por favor use el botón de '¿Fue cargado anteriormente...' "
					+ "e ingrese el ID: '" + personaYaDeAlta.getDni() + "' para asociarlo. "
					+ "Sino verifique el dni ingresado en pantalla, "
					+ "o modifique datos de la persona en pantalla 'Modificar Persona' si hay algún dato mal cargado.");
		}
		
		
		//Existe o no persona
		return altaPersonaFisicaModel(payload);
	}
	
	/**
	 * Baja de Persona Física y sus models asociados a la persona. Sirve para controlador y no services superiores.
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
		
		String aux = contactoService.bajaContacto(id);
		if(aux != null && !aux.isEmpty())
			message += ". " + aux;
		
		return message;
	}
	
	/**
	 * Este método se llama en bajas de services superiores. Si no tiene empleado, beneficiario, etc asociado, se debe eliminar.
	 * @param id de la persona
	 * @return "mensaje..." si se dio de baja, "" empty si no se dio de baja porque tiene asociados.
	 */
	public String bajaPersonaFisicaSiNoTieneAsociados(Long id) {
		if(!existePersonaFisicaPorIdContacto(id))
			return "";
		if(		   beneficiarioService.existeBeneficiarioPorIdContacto(id)
				|| colaboradorService.existeColaboradorPorIdContacto(id)
				|| consejoAdHonoremService.existeConsejoAdHonoremPorIdContacto(id)
				|| empleadoService.existeEmpleadoPorIdContacto(id)
				|| profesionalService.existeProfesionalPorIdContacto(id)
				|| voluntarioService.existeVoluntarioPorIdContacto(id) ) {
			return "";
		}
		//Si llegue acá, no tiene asociados
		return this.bajaPersonaFisica(id);
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
		if (payload != null && payload.getId() != null) {	//Primero busco por ID
			//Necesito el id de persona Fisica o se crearia uno nuevo
			PersonaFisica model = this.getPersonaFisicaModelByIdContacto(payload.getId());
			model.modificar(payload);
			return personaFisicaRepository.save(model);
		}
		if (payload != null) {	//Sino busco por dni
			boolean existePersona = personaFisicaRepository.existsByDni(payload.getDni());
			if(existePersona) {
				PersonaFisica model = getPersonaFisicaModelByDni(payload.getDni());//
				model.modificar(payload);
				return personaFisicaRepository.save(model);
			}
		}
		throw new BadRequestException("No se puede modificar Persona sin ID o DNI inexistente en la base de datos");
	}
	
	/**
	 * Sirve para generador de personas y modificar su create date principalmente
	 * @param payload
	 * @return
	 */
	public PersonaFisica modificarPersonaFisicaModel(PersonaFisica model) {
		if (model != null && model.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
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
	 * Solo para uso de testing
	 * @return
	 */
	public List<PersonaFisicaPayload> generar_100_Personas(int anioCreacion){
		List<PersonaFisica> listAltas = new ArrayList<PersonaFisica>();
		for(int i=0; i<100; i++) {
			PersonaFisicaPayload payloadGenerado = this.personaFisicaGenerator();
			PersonaFisica model = this.altaPersonaFisicaModel(payloadGenerado);
			listAltas.add(model);
		}
		List<PersonaFisicaPayload> listAltasPayload = new ArrayList<PersonaFisicaPayload>();
		List<LocalDateTime> fechas = contactoService.generarCienFechasDistribuidasPorAnio(anioCreacion);
		for(PersonaFisica alta: listAltas) {
			if(fechas.isEmpty())
				break;
			Instant fecha = fechas.get(0).toInstant(ZoneOffset.UTC);
			alta.setCreatedAt(fecha);
			alta.getContacto().setCreatedAt(fecha);
			fechas.remove(0);
			PersonaFisicaPayload payload = this.modificarPersonaFisicaModel(alta).toPayload();
			listAltasPayload.add(payload);
		}
		return listAltasPayload;
	}
	
	public List<LocalDateTime> generarCienFechasDistribuidasPorAnio(int anioCreacion){
		return contactoService.generarCienFechasDistribuidasPorAnio(anioCreacion);
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
		LocalDate minFechaNacimiento = now.minusYears(edadMaxima+1).plusDays(1);
		LocalDate maxFechaNacimiento = now.minusYears(edadMinima);
		
		long daysBetween = ChronoUnit.DAYS.between(minFechaNacimiento, maxFechaNacimiento);
		long randomDays = ThreadLocalRandom.current().nextLong(daysBetween + 1);
		LocalDate fechaDeNacimiento = minFechaNacimiento.plusDays(randomDays);
		
		personaGenerada.setFechaNacimiento(fechaDeNacimiento);
		
		//dni + cuit
		int añoNacimiento = fechaDeNacimiento.getYear();
		int dniAleatorioEstimado;
		//Estimación dni por millón: (pensado en abril 2023 por si se actualiza)
		//40m a 55m = 0  a 25 años, 2023-1998
		//30m a 40m = 26 a 39 años, 1984-1997
		//20m a 30m = 40 a 57 años, 1966-1983
		//10m a 20m = 58 a 69 años, 1954-1965
		// 0m a 10m = +70 años
		
		//DNI "aleatorio" estimado
		//Ejemplo AB.BBB.BBB = A dni minimo por edad + B.. cuanto vale 1 día en 10M * dias de 26 a 39 años
		double nro10M = 9999999.0; //es casi 10M
		if(añoNacimiento >= 1998){//DNI 30M a 40M
			double loQueValeUnDiaEntre1998yEsteAño = nro10M / (15.0*365.0);//Asumo rango 15 años en 10M de habitantes para el futuro.
			double diasExtras1998 = ChronoUnit.DAYS.between(LocalDate.of(1998,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1998*loQueValeUnDiaEntre1998yEsteAño;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1998yEsteAño-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 40000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1984){//DNI 30M a 40M
			double diasEntre1984y1997 = ChronoUnit.DAYS.between(LocalDate.of(1984,1,1), LocalDate.of(1997,12,31));
			double loQueValeUnDiaEntre1984y1997 = nro10M / diasEntre1984y1997;
			double diasExtras1984 = ChronoUnit.DAYS.between(LocalDate.of(1984,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1984*loQueValeUnDiaEntre1984y1997;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1984y1997-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 30000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1966){//DNI 20M a 30M
			double diasEntre1966y1983 = ChronoUnit.DAYS.between(LocalDate.of(1966,1,1), LocalDate.of(1983,12,31));
			double loQueValeUnDiaEntre1966y1983 = nro10M / diasEntre1966y1983;
			double diasExtras1966 = ChronoUnit.DAYS.between(LocalDate.of(1966,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1966*loQueValeUnDiaEntre1966y1983;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1966y1983-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 20000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1954){//DNI 10M a 20M
			double diasEntre1954y1965 = ChronoUnit.DAYS.between(LocalDate.of(1954,1,1), LocalDate.of(1965,12,31));
			double loQueValeUnDiaEntre1954y1965 = nro10M / diasEntre1954y1965;
			double diasExtras1954 = ChronoUnit.DAYS.between(LocalDate.of(1954,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1954*loQueValeUnDiaEntre1954y1965;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1954y1965-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 10000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1923){//DNI 10M a 20M
			double diasEntre1923y1953 = ChronoUnit.DAYS.between(LocalDate.of(1923,1,1), LocalDate.of(1953,12,31));
			double loQueValeUnDiaEntre1923y1953 = 8999999 / diasEntre1923y1953;
			double diasExtras1923 = ChronoUnit.DAYS.between(LocalDate.of(1923,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1923*loQueValeUnDiaEntre1923y1953;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1923y1953-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 1000000 + sumarDniAleatorio;
		}
		
		else {
			dniAleatorioEstimado = random.nextInt(100000, 999999);
		}
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
		else
			personaGenerada.setNombreDescripcion("Testing descripción de persona");
		
		
		
		return personaGenerada;
	}
	
	public void testCorregirDni() {
		List<PersonaFisica> personas = personaFisicaRepository.findAll().stream().map(e -> this.setearDniAleatorio(e, false)).collect(Collectors.toList());
		if(!personas.isEmpty())
			personaFisicaRepository.saveAll(personas);
	}
	
	private PersonaFisica setearDniAleatorio(PersonaFisica payload, boolean isFemenino) {
		Random random = new Random();
		
		//dni + cuit
		LocalDate fechaDeNacimiento = payload.getFechaNacimiento();
		int añoNacimiento = fechaDeNacimiento.getYear();
		int dniAleatorioEstimado;
		//Estimación dni por millón: (pensado en abril 2023 por si se actualiza)
		//40m a 55m = 0  a 25 años, 2023-1998
		//30m a 40m = 26 a 39 años, 1984-1997
		//20m a 30m = 40 a 57 años, 1966-1983
		//10m a 20m = 58 a 69 años, 1954-1965
		// 0m a 10m = +70 años
		
		//DNI "aleatorio" estimado
		//Ejemplo AB.BBB.BBB = A dni minimo por edad + B.. cuanto vale 1 día en 10M * dias de 26 a 39 años
		double nro10M = 9999999.0; //es casi 10M
		if(añoNacimiento >= 1998){//DNI 30M a 40M
			double loQueValeUnDiaEntre1998yEsteAño = nro10M / (15.0*365.0);//Asumo rango 15 años en 10M de habitantes para el futuro.
			double diasExtras1998 = ChronoUnit.DAYS.between(LocalDate.of(1998,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1998*loQueValeUnDiaEntre1998yEsteAño;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1998yEsteAño-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 40000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1984){//DNI 30M a 40M
			double diasEntre1984y1997 = ChronoUnit.DAYS.between(LocalDate.of(1984,1,1), LocalDate.of(1997,12,31));
			double loQueValeUnDiaEntre1984y1997 = nro10M / diasEntre1984y1997;
			double diasExtras1984 = ChronoUnit.DAYS.between(LocalDate.of(1984,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1984*loQueValeUnDiaEntre1984y1997;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1984y1997-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 30000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1966){//DNI 20M a 30M
			double diasEntre1966y1983 = ChronoUnit.DAYS.between(LocalDate.of(1966,1,1), LocalDate.of(1983,12,31));
			double loQueValeUnDiaEntre1966y1983 = nro10M / diasEntre1966y1983;
			double diasExtras1966 = ChronoUnit.DAYS.between(LocalDate.of(1966,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1966*loQueValeUnDiaEntre1966y1983;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1966y1983-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 20000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1954){//DNI 10M a 20M
			double diasEntre1954y1965 = ChronoUnit.DAYS.between(LocalDate.of(1954,1,1), LocalDate.of(1965,12,31));
			double loQueValeUnDiaEntre1954y1965 = nro10M / diasEntre1954y1965;
			double diasExtras1954 = ChronoUnit.DAYS.between(LocalDate.of(1954,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1954*loQueValeUnDiaEntre1954y1965;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1954y1965-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 10000000 + sumarDniAleatorio;
		}
		
		else if(añoNacimiento >= 1923){//DNI 10M a 20M
			double diasEntre1923y1953 = ChronoUnit.DAYS.between(LocalDate.of(1923,1,1), LocalDate.of(1953,12,31));
			double loQueValeUnDiaEntre1923y1953 = 8999999 / diasEntre1923y1953;
			double diasExtras1923 = ChronoUnit.DAYS.between(LocalDate.of(1923,1,1), fechaDeNacimiento);
			double sumarDni = diasExtras1923*loQueValeUnDiaEntre1923y1953;
			double sumarDniMinimo = sumarDni;
			double sumarDniMaximo = sumarDni + loQueValeUnDiaEntre1923y1953-1;
			int sumarDniAleatorio = Double.valueOf(random.nextDouble(sumarDniMinimo, sumarDniMaximo)).intValue();
			dniAleatorioEstimado = 1000000 + sumarDniAleatorio;
		}
		
		else {
			dniAleatorioEstimado = random.nextInt(100000, 999999);
		}
		payload.setDni(dniAleatorioEstimado);
		
		//Modifico su cuit
		int nroUnoAlNueveAleatorio = random.nextInt(9) + 1;
		String cuit = (isFemenino ? "27-" : "20-") + dniAleatorioEstimado + "-" + nroUnoAlNueveAleatorio;
		payload.setCuit(cuit);
		
		return payload;
	}
}
