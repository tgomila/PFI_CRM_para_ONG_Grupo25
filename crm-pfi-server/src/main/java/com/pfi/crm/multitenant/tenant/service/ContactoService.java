package com.pfi.crm.multitenant.tenant.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.payload.ContactoAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;

@Service
public class ContactoService {
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FacturaService facturaService;
	
	public ContactoPayload getContactoById(@PathVariable Long id) {
		return this.getContactoModelById(id).toPayload();
    }
	
	public Contacto getContactoModelById(@PathVariable Long id) {
		return contactoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contacto", "id", id));
    }
	
	public List<ContactoPayload> getContactos() {
		//return contactoRepository.findAllByEstadoActivoContactoTrue();
		return contactoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
		//return contactoRepository.findAll().stream().filter(a -> a.getEstadoActivoContacto()).map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ContactoPayload altaContacto (ContactoAbstractPayload payload) {
		return this.altaContactoModel(payload).toPayload();
	}
	
	public Contacto altaContactoModel (ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a dar de alta, por favor ingrese un contacto.");
		if(payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de contacto: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		payload.setId(null);
		return contactoRepository.save(new Contacto(payload));
	}
	
	/**
	 * Si ingresa un ID y no existe en la BD, no se dará de alta.
	 * @param payload contacto
	 * @return Model contacto
	 */
	public Contacto altaModificarContactoModel (ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a dar de alta/modificar, por favor ingrese un contacto.");
		if(payload.getId() != null) //Si existe ID contacto asociado de alta:
			return this.modificarContactoModel(payload);
		else
			return this.altaContactoModel(payload);
	}
	
	public String bajaContacto(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		
		Contacto m = this.getContactoModelById(id);
		m.setEstadoActivoContacto(false);
		m.setFechaBajaContacto(LocalDate.now());
		
		String message = "Se ha dado de baja: Contacto";
		String aux = "";
		
		//Dar de baja a personaFisica y todo su alrededor
		aux = personaFisicaService.bajaPersonaFisicaSiExiste(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Dar de baja a personaJuridica
		aux = personaJuridicaService.bajaPersonaJuridicaSiExiste(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Dar de baja su user
		//Por las dudas no lo hago, a ver si doy de baja un admin o alguien importante.
		//Mejor que lo hagan aparte.
		//aux = userService.bajaUsuariosPorContacto(id);
		//message += !aux.isEmpty() ? (". "+aux) : "";
		aux = userService.desasociarContactoDeUsers(id);
		message += !aux.isEmpty() ? (". "+aux) : "";
		
		//Mantener facturas pero quitar su contacto
		facturaService.quitarContactoDeSusFacturas(id);
		
		m = contactoRepository.save(m);
		contactoRepository.delete(m);		//Temporalmente se elimina de la BD
		//return ResponseEntity.ok().body(new ApiResponse(true, message));
		
		message += " de id: " + id.toString() + "";
		return message;
	}
	
	/**
	 * Este método se llama en bajas de services superiores. Si no tiene PersonaFisica o PersonaJuridica asociado, se debe eliminar.
	 * @param id
	 * @return
	 */
	public String bajaContactoSiNoTieneAsociados(Long id) {
		if(!existeContacto(id))
			return "";
		if(personaFisicaService.existePersonaFisicaPorIdContacto(id)
				|| personaJuridicaService.existePersonaJuridicaPorIdContacto(id)) {
			return "";
		}
		//Si llegue acá, no tiene asociados
		return this.bajaContacto(id);
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public boolean bajaContactoSiExiste(Long id) {
		if(existeContacto(id)) {
			bajaContacto(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public ContactoPayload modificarContacto(ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a modificar. Por favor ingrese un contacto que no sea null.");
		if(payload.getId() == null) //Necesito el id de contacto o se crearia uno nuevo
			throw new BadRequestException("No se puede modificar contacto sin ID");//return null;
		
		Contacto model = this.getContactoModelById(payload.getId());
		model.modificar(payload);
		return contactoRepository.save(model).toPayload();
	}
	
	//Sirve para services superiores, como personaFisicaService
	public Contacto modificarContactoModel(ContactoAbstractPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como contacto a modificar. Por favor ingrese un contacto que no sea null.");
		if(payload.getId() == null) //Necesito el id de contacto o se crearia uno nuevo
			throw new BadRequestException("No se puede modificar contacto sin ID");
		if(!existeContacto(payload.getId()))
			throw new BadRequestException("Ha ingresado un ID '" + payload.getId().toString() + "' a modificar, y no existe. "
					+ "Es posible que sea otro número o haya sido dado de baja.");
		
		Contacto model = this.getContactoModelById(payload.getId());
		model.modificar(payload);
		return contactoRepository.save(model);
	}
	
	/**Sirve para generador de contactos y modificar su create date principalmente
	 * @param payload
	 * @return
	 */
	public Contacto modificarContactoModel(Contacto model) {
		if(model == null)
			throw new BadRequestException("Ha introducido un null como contacto a modificar. Por favor ingrese un contacto que no sea null.");
		if(model.getId() == null) //Necesito el id de contacto o se crearia uno nuevo
			throw new BadRequestException("No se puede modificar contacto sin ID");
		if(!existeContacto(model.getId()))
			throw new BadRequestException("Ha ingresado un ID '" + model.getId().toString() + "' a modificar, y no existe. "
					+ "Es posible que sea otro número o haya sido dado de baja.");
		
		return contactoRepository.save(model);
	}
	
	/**
	 * Método hecho para otros services, por si ingresan un payload y desean buscarlo o darlo de alta.
	 * @param payload (ContactoPayload)
	 * @return Contacto (model) dado de alta, o encontrado en la BD si fue dado de alta, o null si payload == null.
	 */
	public Contacto buscarOAlta(ContactoAbstractPayload payload) {
		//if(payload == null)
		//	throw new BadRequestException("Ha introducido un null como contacto a dar de alta, por favor ingrese un contacto.");
		if(payload != null) {
			if(payload.getId() != null) {//Lo busco
				return this.getContactoModelById(payload.getId());
			}
			else {//Doy de alta su prestamista (Contacto)
				return this.altaContactoModel(payload);
			}
		}
		return null;
		
	}
	
	public boolean existeContacto(Long id) {
		return contactoRepository.existsById(id);
	}
	
	
	//Dashboard
	public List<ContactoPayload> getContactosCreadosEsteMes() {
		return contactoRepository.findContactosCreatedThisMonth(LocalDate.now()).stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public List<Map<String, Object>> countContactosCreadosEsteAnioPorMes() {
		List<Map<String, Object>> countContactosCreatedThisYearByMonth = contactoRepository.countContactosCreatedThisYearByMonth(LocalDate.now());
		return countContactosCreatedThisYearByMonth;
	}
	
	public List<Map<String, Object>> countContactosCreadosUltimos12meses() {
		LocalDateTime start = LocalDateTime.now().minusMonths(11).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999);
		List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth = contactoRepository.countContactosCreatedLast12MonthsByMonth(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
		return countContactosCreatedLast12MonthsByMonth;
	}
	
	public List<Map<String, Object>> countContactosByAnioMes(int anio) {
		LocalDateTime start = LocalDateTime.of(anio, 1, 1, 0, 0, 0, 0);
		LocalDateTime end = LocalDateTime.of(anio, 12, 31, 23, 59, 59, 999);
		List<Map<String, Object>> countContactosCreadosAnioPasado = contactoRepository.countContactosCreatedByMonthBetweenDates(start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
		return countContactosCreadosAnioPasado;
	}
	
	
	
	//Trash
	public LocalDate contactosCreadosEnLosUltimos30Dias() {
		LocalDate initial = LocalDate.now();
		LocalDate start = initial.withDayOfMonth(1);
		//LocalDate end = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
		
		return start;
		//LocalDate start = LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).withDayOfMonth(1);
		//LocalDate end = LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).plusMonths(1).withDayOfMonth(1).minusDays(1);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Solo para uso de testing
	 * @return
	 */
	public List<ContactoPayload> generar_100_Contactos(int anioCreacion){
		List<Contacto> listAltas = new ArrayList<Contacto>();
		for(int i=0; i<100; i++) {
			ContactoPayload payloadGenerado = this.contactoGenerator();
			Contacto model = this.altaContactoModel(payloadGenerado);
			listAltas.add(model);
		}
		List<ContactoPayload> listAltasPayload = new ArrayList<ContactoPayload>();
		List<LocalDateTime> fechas = generarCienFechasDistribuidasPorAnio(anioCreacion);
		for(Contacto alta: listAltas) {
			if(fechas.isEmpty())
				break;
			alta.setCreatedAt(fechas.get(0).toInstant(ZoneOffset.UTC));
			fechas.remove(0);
			ContactoPayload payload = this.modificarContactoModel(alta).toPayload();
			listAltasPayload.add(payload);
		}
		return listAltasPayload;
	}
	
	public List<LocalDateTime> generarCienFechasDistribuidasPorAnio(int anio){
		
		LocalDate mes = LocalDate.of(anio, 1, 1);
		List<LocalDateTime> fechas = new ArrayList<>();
		
		//Enero
		mes = LocalDate.of(anio, 1, 1);
		fechas.addAll(generarFechasMes(mes, 9, 8));
		
		//Febrero
		mes = LocalDate.of(anio, 2, 1);
		fechas.addAll(generarFechasMes(mes, 7, 7));
		
		//Marzo
		mes = LocalDate.of(anio, 3, 1);
		fechas.addAll(generarFechasMes(mes, 4, 3));
		
		//Abril
		mes = LocalDate.of(anio, 4, 1);
		fechas.addAll(generarFechasMes(mes, 2, 1));
		
		//Mayo
		mes = LocalDate.of(anio, 5, 1);
		fechas.addAll(generarFechasMes(mes, 1, 1));
		
		//Junio
		mes = LocalDate.of(anio, 6, 1);
		fechas.addAll(generarFechasMes(mes, 6, 5));
		
		//Julio
		mes = LocalDate.of(anio, 7, 1);
		fechas.addAll(generarFechasMes(mes, 7, 7));
		
		//Agosto
		mes = LocalDate.of(anio, 8, 1);
		fechas.addAll(generarFechasMes(mes, 2, 2));
		
		//Septiembre
		mes = LocalDate.of(anio, 9, 1);
		fechas.addAll(generarFechasMes(mes, 4, 3));
		
		//Octubre
		mes = LocalDate.of(anio, 10, 1);
		fechas.addAll(generarFechasMes(mes, 1, 1));
		
		//Noviembre
		mes = LocalDate.of(anio, 11, 1);
		fechas.addAll(generarFechasMes(mes, 4, 3));
		
		//Diciembre
		mes = LocalDate.of(anio, 12, 1);
		fechas.addAll(generarFechasMes(mes, 6, 6));
		
		Collections.sort(fechas);
		
		return fechas;
	}
	
	public List<LocalDateTime> generarFechasMes(LocalDate mes, int lunVie, int finde){
		LocalDate startDate = mes.withDayOfMonth(1);
		LocalDate endDate = mes.withDayOfMonth(mes.getMonth().length(mes.isLeapYear()));
		
		List<LocalDateTime> weekdays = new ArrayList<>();
		List<LocalDateTime> weekends = new ArrayList<>();
		
		Random random = new Random();
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				weekends.add(date.atTime(9 + random.nextInt(9), random.nextInt(60)));
			} else {
				weekdays.add(date.atTime(9 + random.nextInt(4), random.nextInt(60)));
			}
		}
		
		Collections.shuffle(weekdays);
		List<LocalDateTime> diasDeSemana = weekdays.subList(0, lunVie);
		
		
		Collections.shuffle(weekends);
		List<LocalDateTime> findes = weekends.subList(0, finde);
		
		List<LocalDateTime> dias = new ArrayList<>(diasDeSemana);
		dias.addAll(findes);
		Collections.sort(dias);
		
		return dias;
	}
	
	
	/**
	 * Solo para uso de testing
	 * @return contacto testing con datos randoms.
	 */
	public ContactoPayload contactoGenerator() {
		ContactoPayload contactoGenerado = new ContactoPayload();
		
		//NombreDescripcion
		contactoGenerado.setNombreDescripcion("Testing nombre de contacto");
		
		//Cuit
		Random random = new Random();
        int numAleatorio = random.nextInt(10);
        int dniAleatorio;
        if (numAleatorio < 1) { // 10% de probabilidad
        	dniAleatorio = random.nextInt(10000000, 20000000);
        } else if (numAleatorio < 3) { // 20% de probabilidad
        	dniAleatorio =  random.nextInt(20000000, 30000000);
        } else { // 70% de probabilidad
        	dniAleatorio =  random.nextInt(30000000, 40000000);
        }
        int nroUnoAlNueveAleatorio = random.nextInt(9) + 1;
        contactoGenerado.setCuit("23-"+dniAleatorio+"-"+nroUnoAlNueveAleatorio);
		
		//Domicilio
		final List<String> CALLES = Arrays.asList(
				"Av. Florida", "Av. Corrientes", "Av. Rivadavia", "Av. Cabildo", "Av. Santa Fe", "Av. Callao", "San Martín", "Av. 9 de Julio", "Avenida de Mayo",
				"Av. Pueyrredón", "Av. Córdoba", "Lavalle", "Paraguay", "Av. Alem", "Av. Hipólito Yrigoyen", "Av. Sarmiento", "Moreno",
				"Bulnes", "Bolivar", "Azcuénaga", "Libertad", "Bartolomé Mitre", "Riobamba", "Arenales", "Lavalle",
				"Tucumán", "Uruguay", "Honduras", "Thames"
		);
		int nro_1_999_aleatorio = random.nextInt(999) + 1;
		String direccionAleatoria = CALLES.get(random.nextInt(CALLES.size())) + " " + nro_1_999_aleatorio;
		numAleatorio = random.nextInt(10);
		if(numAleatorio > 5) {
			//Piso 1 a 15
			int piso = random.nextInt(15) + 1;
			//Letra departamento A a G
			char depto = (char) (random.nextInt(7) + 'A');
			direccionAleatoria += ", piso "+piso+depto;
		}
		contactoGenerado.setDomicilio(direccionAleatoria);
		
		//email
		contactoGenerado.setEmail("testing@testing.com");
		
		//Telefono
		contactoGenerado.setTelefono("15-"+random.nextInt(9999)+"-"+random.nextInt(9999));
		
		return contactoGenerado;
	}
}
	
enum Meses {
	ENERO(1, 0.17),
	FEBRERO(2, 0.14),
	MARZO(3, 0.07),
	ABRIL(4, 0.03),
	MAYO(5, 0.02),
	JUNIO(6, 0.11),
	JULIO(7, 0.14),
	AGOSTO(8, 0.04),
	SEPTIEMBRE(9, 0.07),
	OCTUBRE(10, 0.02),
	NOVIEMBRE(11, 0.07),
	DICIEMBRE(12, 0.12);
	
	private final int nroMes;
	private final double porcentaje;
	private Meses(int nroMes, double porcentaje) {
		this.nroMes = nroMes;
		this.porcentaje = porcentaje;
	}
	public int getNroMes() {return nroMes;}
	public double getPorcentaje() {return porcentaje;}
}
