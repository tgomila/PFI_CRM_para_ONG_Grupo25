package com.pfi.crm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.model.DonacionTipo;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.TipoPersonaJuridica;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.*;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.service.*;

@Component
public class CargarDatosEjemplo implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MasterTenantService masterTenantService;
	
	@Autowired
	private ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@Autowired
	private ModuloMarketService moduloMarketService;
	
	@Autowired
	private ContactoService contactoService;
	
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Autowired
	private VoluntarioService voluntarioService;
	
	@Autowired
	private ProfesionalService profesionalService;
	
	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private ConsejoAdHonoremService consejoAdHonoremService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private InsumoService insumoService;
	
	@Autowired
	private FacturaService facturaService;
	
	@Autowired
	private PrestamoService prestamoService;
	
	@Autowired
	private ProgramaDeActividadesService programaDeActividadesService;
	
	@Autowired
	private DonacionService donacionService;
	
  /**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		
		// here your code ...
		cargarMasterTenantSiNoExisten();
		
		//cargarTenant1();
		//cargarTenant2();
		//cargarTenant3();
		
		return;
	}
	
	//No funciona versión automatizada
	public void cargarMasterTenantSiNoExisten() {
		List<TenantPayload> tenants = masterTenantService.getTenants();
		
		if(tenants.size() == 0) {
			masterTenantService.altaTenant(new TenantPayload(300, "tenant3", "ONG Sapito"));
			System.out.println("\n\n***Reinicie para cargar datos del tenant3.***\n\n");
			System.out.println("Y asegure antes que TenantDatabaseConfig.java esté en 'create' en casi final de la línea");
			return;
		}
		if(tenants.size() == 1) {
			String tenantDeAlta = tenants.get(0).getDbName();
			
			//Cargar datos tenant3
			if(tenantDeAlta.equalsIgnoreCase("tenant3")) {
				cargarTenant3();
				System.out.println("Datos cargados de tenant3.");
				masterTenantService.altaTenant(new TenantPayload(200, "tenant2", "ONG Comida para los chicos"));
				masterTenantService.bajaTenant("tenant3");
				System.out.println("\nHa finalizado la carga de datos de tenant3");
				System.out.println("\n\n***Reinicie para cargar datos del tenant2.***\n\n");
				return;
			}
			
			//Cargar datos tenant2
			else if(tenantDeAlta.equalsIgnoreCase("tenant2")) {
				cargarTenant2();
				System.out.println("Datos cargados de tenant2.");
				masterTenantService.altaTenant(new TenantPayload(100, "tenant1", "ONG Mi Arbolito"));
				masterTenantService.bajaTenant("tenant2");
				System.out.println("\nHa finalizado la carga de datos de tenant2");
				System.out.println("\n\n***Reinicie para cargar datos del tenant1.***\n\n");
				return;
			}
			
			//Cargar datos tenant1
			else if(tenantDeAlta.equalsIgnoreCase("tenant1")) {
				cargarTenant1();
				System.out.println("Datos cargados de tenant1.");
				
				masterTenantService.altaTenant(new TenantPayload(200, "tenant2", "ONG Comida para los chicos"));
				masterTenantService.altaTenant(new TenantPayload(300, "tenant3", "ONG Sapito"));
				System.out.println("\n\n***Todos los datos de tenants ya han sido cargados. Por favor realice los pasos.***\n\n");
				System.out.println("Pasos a realizar:\n"
						+ "  1) Vaya a TenantDatabaseConfig.java y cambie 'create' a 'none'.\n"
						+ "  2) Apague el servidor.\n"
						+ "  3) Guarde el archivo editado.\n"
						+ "  4) Vuelva a encender, y ya esta.\n"
				);
				return;
			}
			else {
				System.out.println("Chequea el nombre del tenant si es tenant1, 2 o 3.");
				return;
			}
		}
		
		if(tenants.size() == 2) {
			System.out.println("Chequear si los tenants estan bien cargados.");
			return;
		}
		if(tenants.size() == 3) {
			//Ante la duda si posee create, hago chequeo de cargar datos por si se borraron
			cargarTenant1();
			cargarTenant2();
			cargarTenant3();
			System.out.println("Tenants bien cargados. Continue con el desarrollo.");
			return;
		}

		//if(!masterTenantService.existTenantId("tenant3"))
		//	masterTenantService.altaTenant(new TenantPayload(300, "tenant3", "ONG Sapito"));
		
		//if(!masterTenantService.existTenantId("tenant2"))
		//	masterTenantService.altaTenant(new TenantPayload(200, "tenant2", "ONG Comida para los chicos"));
		
		//if(!masterTenantService.existTenantId("tenant1"))
		//		masterTenantService.altaTenant(new TenantPayload(100, "tenant1", "ONG Mi Arbolito"));
		

	}
	
	public void cargarTenant1() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant1");
		//BeneficiarioPayload b = beneficiarioService.getBeneficiarioByIdContacto(Long.parseLong("1"));
		//if (null == b) {
		Optional<Role> rol = roleRepository.findByRoleName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			cargarRolesYModulos();
			cargarSuscripcionModulosTenant1();

			cargarBeneficiariosTenant1();
			cargarVoluntariosTenant1();
			cargarProfesionalesTenant1();
			cargarEmpleadosTenant1();
			cargarColaboradorTenant1();
			cargarConsejoAdHonoremTenant1();
			cargarPersonaJuridicaTenant1();
			cargarUsuariosTenant1();
			cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant1();
			cargarDonacionesTenant1();
		}
	}
	
	public void cargarTenant2() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant2");
		//BeneficiarioPayload b = beneficiarioService.getBeneficiarioByIdContacto(Long.parseLong("1"));
		//if (null == b) {
		Optional<Role> rol = roleRepository.findByRoleName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			cargarRolesYModulos();
			cargarSuscripcionModulosTenant2();
			
			cargarContactoComoVoluntarioYEmpleadoTenant2();
			cargarBeneficiariosTenant2();
			cargarVoluntariosTenant2();
			cargarProfesionalesTenant2();
			cargarEmpleadosTenant2();
			cargarColaboradorTenant2();
			cargarConsejoAdHonoremTenant2();
			cargarPersonaJuridicaTenant2();
			cargarUsuariosTenant2();
			cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant2();
			cargarDonacionesTenant2();
		}
	}
	
	public void cargarTenant3() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant3");
		//BeneficiarioPayload b = beneficiarioService.getBeneficiarioByIdContacto(Long.parseLong("1"));
		//if (null == b) {
		Optional<Role> rol = roleRepository.findByRoleName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			cargarRolesYModulos();
			cargarSuscripcionModulosTenant3();
			cargarUsuariosDefault();
		}
	}
	
	public void cargarRolesYModulos() {
		
		List<RoleName> rolesOrdenados = Arrays.asList(RoleName.values()).stream()
				.sorted(Comparator.comparingInt(RoleName::getPriority))
				.collect(Collectors.toList());
		
		//rolesOrdenados.stream().map(r -> roleRepository.save(new Role(r)) );
		for(RoleName roleName: rolesOrdenados) {
			roleRepository.save(new Role(roleName));
			//cargarModuloVisibilidadPorRol(role); //Es del role que devuelve el service de arriba
		}
		moduloVisibilidadPorRolService.agregarTodosLosModulos();
		moduloMarketService.chequearYDarDeAltaModulosMarket();
	}
	
	public void cargarSuscripcionModulosTenant1() {
		moduloMarketService.suscripcionPremiumMes();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
	}
	
	public void cargarSuscripcionModulosTenant2() {
		moduloMarketService.activarPrueba7dias();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
	}
	
	public void cargarSuscripcionModulosTenant3() {
		moduloMarketService.activarPrueba7dias();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
		moduloMarketService.desuscribirEn5min();	//Para ver si ya no tiene acceso.
	}
	
	
	
	public void cargarBeneficiariosTenant1() {
		BeneficiarioPayload m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Pibe");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipeGarcia@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Felipe");
		m.setApellido("Garcia");
		m.setFechaNacimiento(LocalDate.of(2010, 1, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("001234"));
		m.setLegajo(Long.parseLong("1090555"));
		m.setLugarDeNacimiento("Lanús");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Necesita asistencia psicologica");
		m.setEscuela("Colegio Nº123");
		m.setGrado("5º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("mariajosefina@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("josefina");
		m.setApellido("Ruiz");
		m.setFechaNacimiento(LocalDate.of(2012, 9, 14));

		// Beneficiario
		m.setIdONG(Long.parseLong("007612"));
		m.setLegajo(Long.parseLong("1015375"));
		m.setLugarDeNacimiento("Palermo");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Necesita contensión");
		m.setEscuela("Colegio Nº46");
		m.setGrado("3º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Uruguay 782, piso 1, depto B");
		m.setEmail("carlagomez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("josefina");
		m.setApellido("Ruiz");
		m.setFechaNacimiento(LocalDate.of(2010, 9, 14));

		// Beneficiario
		m.setIdONG(Long.parseLong("008741"));
		m.setLegajo(Long.parseLong("1036782"));
		m.setLugarDeNacimiento("La Matanza");
		m.setSeRetiraSolo(true);
		m.setCuidadosEspeciales("Ninguno");
		m.setEscuela("Colegio Nº3");
		m.setGrado("7º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		
////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Sancez reta 382");
		m.setEmail("cecilialopez@gmail.com");
		m.setTelefono("9516-6545");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Cecilia");
		m.setApellido("Lopez");
		m.setFechaNacimiento(LocalDate.of(2010, 9, 14));

		// Beneficiario
		m.setIdONG(Long.parseLong("005613"));
		m.setLegajo(Long.parseLong("1087946"));
		m.setLugarDeNacimiento("Colegiales");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Bebe mucha agua");
		m.setEscuela("Colegio Nº3");
		m.setGrado("2º grado");
		m.setTurno("Tarde");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
	}
	
	public void cargarVoluntariosTenant1() {
		VoluntarioPayload m = new VoluntarioPayload();

		// Contacto
		m.setNombreDescripcion("Voluntario duro");
		m.setCuit("20-1297349-9");
		m.setDomicilio("Peralta 457, piso 2, depto A");
		m.setEmail("julioroque@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1297349);
		m.setNombre("Julio");
		m.setApellido("Roque");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		//Voluntario
		//No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario blando");
		m.setCuit("20-1297349-9");
		m.setDomicilio("Peralta 457, piso 2, depto A");
		m.setEmail("santiagogomez@gmail.com");
		m.setTelefono("9466-7813");

		// PersonaFisica
		m.setDni(1297349);
		m.setNombre("Santiago");
		m.setApellido("Gomez");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntaria");
		m.setCuit("20-1297349-9");
		m.setDomicilio("Samalia 789, piso 17, depto C");
		m.setEmail("agustinacampos@gmail.com");
		m.setTelefono("6516-7896");

		// PersonaFisica
		m.setDni(1297349);
		m.setNombre("Agustina");
		m.setApellido("Campos");
		m.setFechaNacimiento(LocalDate.of(1920, 7, 14));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
	}
	
	
	
	
	
	public void cargarProfesionalesTenant1() {
		ProfesionalPayload m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Profesional Don psicologo");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida Las Heras 4578, piso 4, depto B");
		m.setEmail("estebanquito@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Esteban");
		m.setApellido("Quito");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Psicologo");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Tecnico electricista");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Buhos 6518, piso 2, depto C");
		m.setEmail("ricardofrachia@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Ricardo");
		m.setApellido("Frachia");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Tecnico electricista");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Contadora");
		m.setCuit("20-654164-9");
		m.setDomicilio("Cecelia 2816, piso 9, depto C");
		m.setEmail("julietaveraniegos@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(654164);
		m.setNombre("Julieta");
		m.setApellido("Veraniegos");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Contadora");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Medica clinica");
		m.setCuit("20-654164-9");
		m.setDomicilio("Uruguay 731, piso 1, depto A");
		m.setEmail("lilianaferacios@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(654164);
		m.setNombre("Liliana");
		m.setApellido("Feracios");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Medica");
		// Fin Profesional
		profesionalService.altaProfesional(m);	
	}
	
	public void cargarEmpleadosTenant1() {
		EmpleadoPayload m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Empleado");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Gallo 7923, piso 4, depto A");
		m.setEmail("gabrielluchetti@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Gabriel");
		m.setApellido("Luchetti");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Desktop Helper");
		m.setDescripcion("Da las altas y bajas de beneficiarios");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Jefe CEO");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("estebanquito@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Nahuel");
		m.setApellido("Vacca");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Jefe");
		m.setDescripcion("Da las altas y bajas de empleados");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Asistente CEO");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Quintana 237, piso 1, depto F");
		m.setEmail("zaragimenez@gmail.com");
		m.setTelefono("6541-5616");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Zara");
		m.setApellido("Gimenez");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Asistente del jefe");
		m.setDescripcion("Da las altas y bajas de beneficiarios");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
	}
	
	public void cargarColaboradorTenant1() {
		
		ColaboradorPayload m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Falacios 6091, piso 3, depto B");
		m.setEmail("micaelagimenez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Micaela");
		m.setApellido("Gimenez");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area administrativa");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		
		
		///////////////////////
		m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("20-27894284-9");
		m.setDomicilio("Polonia 741, piso 1, depto A");
		m.setEmail("trinidadcabellos@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(27894284);
		m.setNombre("Trinidad");
		m.setApellido("Cabellos");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area gerencial");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		///////////////////////
		m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("20-2756419-9");
		m.setDomicilio("Cacimodo 127, piso 7, depto D");
		m.setEmail("susanadora@gmail.com");
		m.setTelefono("6541-3854");

		// PersonaFisica
		m.setDni(2756419);
		m.setNombre("Susana");
		m.setApellido("Dora");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area de docencia");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		
		
		
	}
	
	
	
	
	public void cargarConsejoAdHonoremTenant1() {
		
		ConsejoAdHonoremPayload m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeroAdHonorem");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Paraguay 803, piso 6, depto A");
		m.setEmail("pedrodiarse@gmail.com");
		m.setTelefono("4436-9567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Pedro");
		m.setApellido("Diarse");
		m.setFechaNacimiento(LocalDate.of(1997, 1, 20));

		// ConsejoAdHonorem
		m.setFuncion("Da buenos consejos");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeraAdHonorem Feli");
		m.setCuit("20-274569465-9");
		m.setDomicilio("Gallo 956, piso 4, depto A");
		m.setEmail("felicitasvicines@gmail.com");
		m.setTelefono("4657-1656");

		// PersonaFisica
		m.setDni(274569465);
		m.setNombre("Felicitas");
		m.setApellido("visines");
		m.setFechaNacimiento(LocalDate.of(1984, 10, 12));

		// ConsejoAdHonorem
		m.setFuncion("Administradora del consejo");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeroAdHonorem Feli");
		m.setCuit("20-274569465-9");
		m.setDomicilio("Sartinez 654, piso 1, depto B");
		m.setEmail("juanquintana@gmail.com");
		m.setTelefono("6541-9841");

		// PersonaFisica
		m.setDni(274569465);
		m.setNombre("Juan");
		m.setApellido("Quintana");
		m.setFechaNacimiento(LocalDate.of(1998, 2, 22));

		// ConsejoAdHonorem
		m.setFuncion("Asistente del consejo");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
	}
	
	public void cargarPersonaJuridicaTenant1() {
		
		PersonaJuridicaPayload m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Coto");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Las Heras 9872");
		m.setEmail("coto-digital@gmail.com");
		m.setTelefono("0800-9000-8080");

		// Persona Juridica
		m.setInternoTelefono("07");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.EMPRESA);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Colegio lacroze");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Lacroze 9872");
		m.setEmail("colegiolacroze@gmail.com");
		m.setTelefono("6546-6541");

		// Persona Juridica
		m.setInternoTelefono("03");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.INSTITUCION);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Ente gobernamental");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida de mayo 100");
		m.setEmail("entegobernamental@lanacion.gov.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("17");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.ORGANISMO_DEL_ESTADO);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Fundacion caritas felices");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida corrientes 7642");
		m.setEmail("caritasfelices@caritasfelices.org.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.OSC);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
	}
	
	public void cargarUsuariosTenant1() {
		
		cargarUsuariosDefault();
		
		String nombre = "Federico del tenant 1";
		String username = "fulano1";
		String email = "fulano1@gmail.com";
		cargarUsuarioBasico(nombre, username, email, RoleName.ROLE_EMPLOYEE);
		
	}
	
	public void cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant1() {
		
		//Producto
		ContactoPayload proveedor1 = new ContactoPayload();
		proveedor1.setNombreDescripcion("Juguetería los 3 chanchitos");
		proveedor1.setCuit("20-12345678-9");
		proveedor1.setDomicilio("Av. Independencia 1000");
		proveedor1.setEmail("jugueterialos3chanchitos@abierto.com");
		proveedor1.setTelefono("1234-5678");
		proveedor1 = contactoService.altaContacto(proveedor1);
		
		ProductoPayload producto1 = new ProductoPayload();
		producto1.setTipo("Juego de mesa");
		producto1.setDescripcion("Rompecabeza");
		producto1.setPrecioVenta(BigDecimal.valueOf(1250.00));
		producto1.setCantFijaCompra(5);
		producto1.setCantMinimaStock(15);
		producto1.setStockActual(25);
		producto1.setFragil(false);
		producto1.setProveedor(proveedor1);
		producto1 = productoService.altaProducto(producto1);
		
		ProductoPayload producto2 = new ProductoPayload();
		producto2.setTipo("Comida/Juguete");
		producto2.setDescripcion("Huevo de chocolate con sorpresa");
		producto2.setPrecioVenta(BigDecimal.valueOf(400.00));
		producto2.setCantFijaCompra(20);
		producto2.setCantMinimaStock(50);
		producto2.setStockActual(60);
		producto2.setFragil(true);
		producto2.setProveedor(proveedor1);
		producto2 = productoService.altaProducto(producto2);
		
		//Insumos
		InsumoPayload insumo1 = new InsumoPayload();
		insumo1.setTipo("Juguetes para los chicos");
		insumo1.setDescripcion("Autitos");
		insumo1.setStockActual(25);
		insumo1.setFragil(false);
		insumo1 = insumoService.altaInsumo(insumo1);
		
		InsumoPayload insumo2 = new InsumoPayload();
		insumo2.setTipo("Vaso");
		insumo2.setDescripcion("Vasos de vidrio");
		insumo2.setStockActual(50);
		insumo2.setFragil(true);
		insumo2 = insumoService.altaInsumo(insumo2);
		
		//Factura
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime todayMinus1month = today.minusMonths(1);
		LocalDateTime fechaFactura1 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 20, 13, 15);
		FacturaPayload factura1 = new FacturaPayload();
		factura1.setFecha(fechaFactura1);
		factura1.setCliente(null);
		factura1.setEmisorFactura(proveedor1);
		//
		FacturaItemPayload factura1_item1 = new FacturaItemPayload();
		factura1_item1.setDescripcion(producto1.getDescripcion());
		factura1_item1.setUnidades(producto1.getCantFijaCompra()*3);
		factura1_item1.setPrecioUnitario(producto1.getPrecioVenta());
		factura1_item1.setPrecio(producto1.getPrecioVenta().multiply(BigDecimal.valueOf(factura1_item1.getUnidades())));
		factura1.agregarItemFactura(factura1_item1);
		//
		FacturaItemPayload factura1_item2 = new FacturaItemPayload();
		factura1_item2.setDescripcion(producto2.getDescripcion());
		factura1_item2.setUnidades(producto2.getCantFijaCompra()*4);
		factura1_item2.setPrecioUnitario(producto2.getPrecioVenta());
		factura1_item2.setPrecio(producto2.getPrecioVenta().multiply(BigDecimal.valueOf(factura1_item2.getUnidades())));
		factura1.agregarItemFactura(factura1_item2);
		//
		factura1 = facturaService.altaFactura(factura1);
		
		LocalDateTime fechaFactura2 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 25, 17, 30);
		FacturaPayload factura2 = new FacturaPayload();
		factura2.setFecha(fechaFactura2);
		factura2.setCliente(null);
		factura2.setEmisorFactura(proveedor1);
		//
		FacturaItemPayload factura2_item1 = new FacturaItemPayload();
		factura2_item1.setDescripcion(producto1.getDescripcion());
		factura2_item1.setUnidades(producto1.getCantFijaCompra()*4);
		factura2_item1.setPrecioUnitario(producto1.getPrecioVenta());
		factura2_item1.setPrecio(producto1.getPrecioVenta().multiply(BigDecimal.valueOf(factura2_item1.getUnidades())));
		factura2.agregarItemFactura(factura2_item1);
		//
		FacturaItemPayload factura2_item2 = new FacturaItemPayload();
		factura2_item2.setDescripcion(producto2.getDescripcion());
		factura2_item2.setUnidades(producto2.getCantFijaCompra()*6);
		factura2_item2.setPrecioUnitario(producto2.getPrecioVenta());
		factura2_item2.setPrecio(producto2.getPrecioVenta().multiply(BigDecimal.valueOf(factura2_item2.getUnidades())));
		factura2.agregarItemFactura(factura2_item2);
		//
		factura2 = facturaService.altaFactura(factura2);
		
		
		//Prestamo
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<BeneficiarioPayload> beneficiarios = beneficiarioService.getBeneficiarios();
		ContactoPayload empleado1 = contactoService.getContactoById(empleados.get(0).getId());
		ContactoPayload beneficiario1 = contactoService.getContactoById(beneficiarios.get(0).getId());
		
		PrestamoPayload prestamo1 = new PrestamoPayload();
		prestamo1.setDescripcion("Radio a pilas Panasonic");
		prestamo1.setCantidad(1);
		prestamo1.setFechaPrestamoInicio(LocalDateTime.now());
		prestamo1.setFechaPrestamoFin(LocalDateTime.now().plusMonths(2));
		prestamo1.setHaSidoDevuelto(false);
		prestamo1.setPrestamista(empleado1);
		prestamo1.setPrestatario(beneficiario1);
		prestamo1 = prestamoService.altaPrestamo(prestamo1);
		
		//Programa de actividades
		List<ProfesionalPayload> profesionales = profesionalService.getProfesionales();
		ProgramaDeActividadesPayload programa = new ProgramaDeActividadesPayload();
		ActividadPayload actividad = new ActividadPayload();
		actividad.setFechaHoraDesde(LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 1, 12, 00));
		actividad.setFechaHoraHasta(actividad.getFechaHoraDesde().plusHours(3));
		actividad.agregarProfesional(profesionales.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(1));
		actividad.setDescripcion("Clases a beneficiarios por el profesor: " + actividad.getProfesionales().get(0).getNombre()+" "+actividad.getProfesionales().get(0).getApellido());
		programa.agregarActividadesPorSemana(10, actividad);
		programa.setDescripcion(actividad.getDescripcion());
		programaDeActividadesService.altaProgramaDeActividades(programa);
	}
	
	public void cargarDonacionesTenant1() {
		//Donacion 1 con contacto
		ContactoPayload donante = new ContactoPayload();
		donante.setNombreDescripcion("Donante Don Roque");
		donante.setCuit("20-12345678-9");
		donante.setDomicilio("Av. Don Monte 1000");
		donante.setEmail("eldonante@donacion.com");
		donante.setTelefono("1234-5678");
		donante = contactoService.altaContacto(donante);
		
		DonacionPayload donacion = new DonacionPayload();
		donacion.setId(null);
		donacion.setFecha(LocalDateTime.now().minusMonths(1));
		donacion.setDonante(donante);
		donacion.setTipoDonacion(DonacionTipo.DINERO);
		donacion.setDescripcion("$100.000");
		donacion = donacionService.altaDonacion(donacion);
		
		//Donacion anónima
		DonacionPayload donacion2 = new DonacionPayload();
		donacion2.setId(null);
		donacion2.setFecha(LocalDateTime.now().minusDays(1));
		donacion2.setDonante(null);
		donacion2.setTipoDonacion(DonacionTipo.INSUMO);
		donacion2.setDescripcion("Juguete ladrillitos");
		donacion2 = donacionService.altaDonacion(donacion2);
	}
	
	
	
	//Tenant 2
	public void cargarContactoComoVoluntarioYEmpleadoTenant2() {
		ContactoPayload contactoPayload = new ContactoPayload();

		// Contacto
		contactoPayload.setNombreDescripcion("Súper Contacto como Voluntario y Empleado");
		contactoPayload.setCuit("20-1235678-9");
		contactoPayload.setDomicilio("Avenida siempre falsa 123, piso 8, depto B");
		contactoPayload.setEmail("megacontacto@gmail.com");
		contactoPayload.setTelefono("1234-4567");
		
		contactoPayload = contactoService.altaContacto(contactoPayload);
		
		
		
		/////////////////////////////////////////////////////////////////////
		VoluntarioPayload voluntarioPayload;
		
		//Asociar a contacto id=1.
		ContactoPayload getContactoPayloadDB = contactoService.getContactoById(contactoPayload.getId());

		// Contacto
		voluntarioPayload = new VoluntarioPayload();
		voluntarioPayload.modificarContacto(getContactoPayloadDB);

		// PersonaFisica
		voluntarioPayload.setDni(1235678);
		voluntarioPayload.setNombre("Super Persona");
		voluntarioPayload.setApellido("Voluntario y Empleado");
		voluntarioPayload.setFechaNacimiento(LocalDate.of(2000, 1, 15));

		//Voluntario
		//No tiene otros atributos
		voluntarioService.altaVoluntario(voluntarioPayload);
		
		//Fin asociar voluntario
		
		
		
		/////////////////////////////////////////////////////////////////////
		
		EmpleadoPayload empleadoPayload = new EmpleadoPayload();
		
		//Test asociar a contacto id=1.
		ContactoPayload getContactoPayloadDB_2 = contactoService.getContactoById(contactoPayload.getId());

		// Contacto
		empleadoPayload = new EmpleadoPayload();
		empleadoPayload.modificarContacto(getContactoPayloadDB_2);

		// PersonaFisica
		empleadoPayload.setDni(1235678);
		empleadoPayload.setNombre("Super Persona modificado");
		empleadoPayload.setApellido("Voluntario y Empleado modificado");
		empleadoPayload.setFechaNacimiento(LocalDate.of(2000, 1, 15));

		// Empleado
		empleadoPayload.setDatosBancarios("CBU: 001234");
		empleadoPayload.setFuncion("Jefe Empleado y Voluntario");
		empleadoPayload.setDescripcion("Super Jefe y voluntario");
		// Fin Empleado
		
		empleadoService.altaEmpleado(empleadoPayload);
		//Fin asociar empleado
	}
	
	public void cargarBeneficiariosTenant2() {
		
		BeneficiarioPayload m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 8, depto B");
		m.setEmail("felicitastron@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Felicitas");
		m.setApellido("tron");
		m.setFechaNacimiento(LocalDate.of(2010, 1, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("000123"));
		m.setLegajo(Long.parseLong("1070123"));
		m.setLugarDeNacimiento("Villa Urquiza");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Le gusta jugar solo con bloquecitos");
		m.setEscuela("Colegio Nº700");
		m.setGrado("6º grado");
		m.setTurno("Tarde");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Pibe tenant 2");
		m.setCuit("20-45123456-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 8, depto C");
		m.setEmail("ricardosojo@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(45123456);
		m.setNombre("Ricardo");
		m.setApellido("Sojo");
		m.setFechaNacimiento(LocalDate.of(2012, 7, 10));

		// Beneficiario
		m.setIdONG(Long.parseLong("004270"));
		m.setLegajo(Long.parseLong("1015375"));
		m.setLugarDeNacimiento("Belgrano");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Necesita integración");
		m.setEscuela("Colegio Nº34");
		m.setGrado("3º grado");
		m.setTurno("Tarde");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Uruguay 782, piso 1, depto B");
		m.setEmail("carlagomez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("josefina");
		m.setApellido("Ruiz");
		m.setFechaNacimiento(LocalDate.of(2010, 9, 14));

		// Beneficiario
		m.setIdONG(Long.parseLong("008741"));
		m.setLegajo(Long.parseLong("1036782"));
		m.setLugarDeNacimiento("La Matanza");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Ninguno");
		m.setEscuela("Colegio Nº3");
		m.setGrado("7º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		
////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Pibe tenant 2");
		m.setCuit("20-44123456-9");
		m.setDomicilio("Sancez reta 174");
		m.setEmail("agustinpeña@gmail.com");
		m.setTelefono("4970-1876");

		// PersonaFisica
		m.setDni(44123456);
		m.setNombre("Agustin");
		m.setApellido("Peña");
		m.setFechaNacimiento(LocalDate.of(2010, 2, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("000349"));
		m.setLegajo(Long.parseLong("1040937"));
		m.setLugarDeNacimiento("Caballito");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Bebe mucho jugo");
		m.setEscuela("Colegio Nº4");
		m.setGrado("1º grado");
		m.setTurno("Noche");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
	}
	
	public void cargarVoluntariosTenant2() {
		VoluntarioPayload m;
		
		///////////////////////////////////

		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario muy duro tenant 2");
		m.setCuit("20-25745349-9");
		m.setDomicilio("Dominguez 457, piso 3, depto B");
		m.setEmail("martinlopez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(25745349);
		m.setNombre("Martin");
		m.setApellido("Lopez");
		m.setFechaNacimiento(LocalDate.of(1990, 5, 10));

		//Voluntario
		//No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario muy blando");
		m.setCuit("20-2049753-9");
		m.setDomicilio("Dominguez 568, piso 4, depto C");
		m.setEmail("francowellington@gmail.com");
		m.setTelefono("9466-7813");

		// PersonaFisica
		m.setDni(2049753);
		m.setNombre("Franco");
		m.setApellido("Wellington");
		m.setFechaNacimiento(LocalDate.of(1990, 3, 10));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntaria");
		m.setCuit("20-1297349-9");
		m.setDomicilio("Pimento 789, piso 18, depto D");
		m.setEmail("camilacampos@gmail.com");
		m.setTelefono("7818-3617");

		// PersonaFisica
		m.setDni(1297349);
		m.setNombre("Camila");
		m.setApellido("Campos");
		m.setFechaNacimiento(LocalDate.of(1920, 7, 17));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
	}
	
	
	
	
	
	public void cargarProfesionalesTenant2() {
		ProfesionalPayload m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Profesora Doña profe");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida Las Profesoras 2373, piso 2, depto D");
		m.setEmail("eugeniavarela@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Eugenia");
		m.setApellido("Varela");
		m.setFechaNacimiento(LocalDate.of(1984, 2, 15));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Profesora");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Pintor");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Ñandues 5470, piso 1, depto A");
		m.setEmail("alejandrogrisgmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Alejandro");
		m.setApellido("Gris");
		m.setFechaNacimiento(LocalDate.of(1980, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Pintor");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Administradora");
		m.setCuit("20-26166542-9");
		m.setDomicilio("Cecelia 2818, piso 11, depto D");
		m.setEmail("fernandaarevalo@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(26166542);
		m.setNombre("Fernanda");
		m.setApellido("Arevalo");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Administrador de eventos");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Medica clinica");
		m.setCuit("20-654164-9");
		m.setDomicilio("Uruguay 741, piso 1, depto A");
		m.setEmail("susanamiento@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(654164);
		m.setNombre("Susana");
		m.setApellido("Miento");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Medica");
		// Fin Profesional
		profesionalService.altaProfesional(m);	
	}
	
	public void cargarEmpleadosTenant2() {
		EmpleadoPayload m = new EmpleadoPayload();
		
		/////////////////////////////////////////////
		
		// Contacto
		m = new EmpleadoPayload();
		m.setNombreDescripcion("Empleada");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Gallo 7923, piso 4, depto A");
		m.setEmail("gabrielluchetti@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Florencia");
		m.setApellido("Altamirano");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Asistente");
		m.setDescripcion("Da las altas y bajas de profesionales");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Soporte telefónico");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 3, depto A");
		m.setEmail("emiliopaz@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Emilio");
		m.setApellido("Paz");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Soporte");
		m.setDescripcion("Atiene llamadas de beneficiarios");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Jefa Gral");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Quintana 237, piso 1, depto F");
		m.setEmail("patriciacastro@gmail.com");
		m.setTelefono("6541-5616");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Patricia");
		m.setApellido("Castro");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Jefa Gral");
		m.setDescripcion("Da todas las órdenes del establecimiento");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
	}
	
	public void cargarColaboradorTenant2() {
		
		ColaboradorPayload m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Cantillan 456, piso x, depto A");
		m.setEmail("josefinamarruecos@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Josefina");
		m.setApellido("Marruecos");
		m.setFechaNacimiento(LocalDate.of(1982, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area de diversiones");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		
		
		///////////////////////
		m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora tenant 2");
		m.setCuit("20-27894284-9");
		m.setDomicilio("Suiza 6543, piso 3, depto A");
		m.setEmail("karinarequini@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setDni(27894284);
		m.setNombre("Karina");
		m.setApellido("Rekini");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area social");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		///////////////////////
		m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaborador tenant 2");
		m.setCuit("20-2756419-9");
		m.setDomicilio("Dentri 631, piso 12, depto C");
		m.setEmail("jorgegomez@gmail.com");
		m.setTelefono("6123-3854");

		// PersonaFisica
		m.setDni(2756419);
		m.setNombre("Jorge");
		m.setApellido("Gomez");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area de deportes");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		
		
		
	}
	
	
	
	
	public void cargarConsejoAdHonoremTenant2() {
		
		ConsejoAdHonoremPayload m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeroAdHonorem tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Paraguay 803, piso 6, depto A");
		m.setEmail("pedrodiarse@gmail.com");
		m.setTelefono("4436-9567");

		// PersonaFisica
		m.setDni(1235678);
		m.setNombre("Matias");
		m.setApellido("Pelorroso");
		m.setFechaNacimiento(LocalDate.of(1997, 1, 20));

		// ConsejoAdHonorem
		m.setFuncion("Da muy buenos consejos");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeraAdHonorem tenant 2");
		m.setCuit("20-274569465-9");
		m.setDomicilio("Arenales 123, piso 4, depto A");
		m.setEmail("julietanamias@gmail.com");
		m.setTelefono("4657-1656");

		// PersonaFisica
		m.setDni(274569465);
		m.setNombre("Julieta");
		m.setApellido("Namias");
		m.setFechaNacimiento(LocalDate.of(1984, 10, 12));

		// ConsejoAdHonorem
		m.setFuncion("Administradora del consejo");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeroAdHonorem Mario tenant 2");
		m.setCuit("20-274569465-9");
		m.setDomicilio("Perez 456, piso 1, depto C");
		m.setEmail("mariogomez@gmail.com");
		m.setTelefono("6541-9841");

		// PersonaFisica
		m.setDni(274569465);
		m.setNombre("Mario");
		m.setApellido("Perez");
		m.setFechaNacimiento(LocalDate.of(1998, 2, 22));

		// ConsejoAdHonorem
		m.setFuncion("Asistente del consejo");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
	}
	
	public void cargarPersonaJuridicaTenant2() {
		
		PersonaJuridicaPayload m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Colegio santa fe");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Av Santa Fe 534");
		m.setEmail("colegiosantafe@gmail.com");
		m.setTelefono("4570-31967");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.INSTITUCION);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();
		
		// Contacto
		m.setNombreDescripcion("Jumbo");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Las Heras 9872");
		m.setEmail("jumbo@gmail.com");
		m.setTelefono("0800-9000-8080");

		// Persona Juridica
		m.setInternoTelefono("07");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.EMPRESA);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();
				
		// Contacto
		m.setNombreDescripcion("Ente gobernamental tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida de mayo 100");
		m.setEmail("entegobernamental@lanacion.gov.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("17");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.ORGANISMO_DEL_ESTADO);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Fundacion caritas felices tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida corrientes 7642");
		m.setEmail("caritasfelices@caritasfelices.org.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.OSC);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
	}
	
	public void cargarUsuariosTenant2() {
		
		cargarUsuariosDefault();
		
		String nombre = "Federico del tenant 2";
		String username = "fulano2";
		String email = "fulano2@gmail.com";
		cargarUsuarioBasico(nombre, username, email, RoleName.ROLE_EMPLOYEE);
	}
	

	
	public void cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant2() {
		
		//Producto
		ContactoPayload proveedor1 = new ContactoPayload();
		proveedor1.setNombreDescripcion("kiosco 7 dias");
		proveedor1.setCuit("20-12345678-9");
		proveedor1.setDomicilio("9 de julio 1000");
		proveedor1.setEmail("kiosco7dias@abierto.com");
		proveedor1.setTelefono("4123-4567");
		proveedor1 = contactoService.altaContacto(proveedor1);

		ContactoPayload proveedor2 = new ContactoPayload();
		proveedor2.setNombreDescripcion("Bazar cocineros en acción");
		proveedor2.setCuit("20-12345678-9");
		proveedor2.setDomicilio("Av. Independencia 1000");
		proveedor2.setEmail("bazarcocinerosenaccion@abierto.com");
		proveedor2.setTelefono("1234-5678");
		proveedor2 = contactoService.altaContacto(proveedor2);
		
		ProductoPayload producto1 = new ProductoPayload();
		producto1.setTipo("Galletita");
		producto1.setDescripcion("Galletitas rellenas");
		producto1.setPrecioVenta(BigDecimal.valueOf(250.00));
		producto1.setCantFijaCompra(10);
		producto1.setCantMinimaStock(100);
		producto1.setStockActual(120);
		producto1.setFragil(false);
		producto1.setProveedor(proveedor1);
		producto1 = productoService.altaProducto(producto1);
		
		ProductoPayload producto2 = new ProductoPayload();
		producto2.setTipo("Botella");
		producto2.setDescripcion("Agua");
		producto2.setPrecioVenta(BigDecimal.valueOf(125.00));
		producto2.setCantFijaCompra(10);
		producto2.setCantMinimaStock(100);
		producto2.setStockActual(100);
		producto2.setFragil(false);
		producto2.setProveedor(proveedor1);
		producto2 = productoService.altaProducto(producto2);
		
		ProductoPayload producto3 = new ProductoPayload();
		producto3.setTipo("Cubierto");
		producto3.setDescripcion("Tenedor, cuchillo (sin filo), cuchara de aluminio");
		producto3.setPrecioVenta(BigDecimal.valueOf(1000.00));
		producto3.setCantFijaCompra(10);
		producto3.setCantMinimaStock(100);
		producto3.setStockActual(150);
		producto3.setFragil(false);
		producto3.setProveedor(proveedor2);
		producto3 = productoService.altaProducto(producto3);
		
		//Insumos
		InsumoPayload insumo1 = new InsumoPayload();
		insumo1.setTipo("Cubiertos");
		insumo1.setDescripcion("Tenedor, cuchillo (sin filo), cuchara de aluminio");
		insumo1.setStockActual(100);
		insumo1.setFragil(false);
		insumo1 = insumoService.altaInsumo(insumo1);
		
		InsumoPayload insumo2 = new InsumoPayload();
		insumo2.setTipo("Vaso");
		insumo2.setDescripcion("Vasos de vidrio");
		insumo2.setStockActual(50);
		insumo2.setFragil(true);
		insumo2 = insumoService.altaInsumo(insumo2);
		
		//Factura
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime todayMinus1month = today.minusMonths(1);
		LocalDateTime fechaFactura1 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 20, 13, 15);
		FacturaPayload factura1 = new FacturaPayload();
		factura1.setFecha(fechaFactura1);
		factura1.setCliente(null);
		factura1.setEmisorFactura(proveedor1);
		//
		FacturaItemPayload factura1_item1 = new FacturaItemPayload();
		factura1_item1.setDescripcion(producto1.getDescripcion());
		factura1_item1.setUnidades(producto1.getCantFijaCompra()*2);
		factura1_item1.setPrecioUnitario(producto1.getPrecioVenta());
		factura1_item1.setPrecio(producto1.getPrecioVenta().multiply(BigDecimal.valueOf(factura1_item1.getUnidades())));
		factura1.agregarItemFactura(factura1_item1);
		//
		FacturaItemPayload factura1_item2 = new FacturaItemPayload();
		factura1_item2.setDescripcion(producto2.getDescripcion());
		factura1_item2.setUnidades(producto2.getCantFijaCompra()*4);
		factura1_item2.setPrecioUnitario(producto2.getPrecioVenta());
		factura1_item2.setPrecio(producto2.getPrecioVenta().multiply(BigDecimal.valueOf(factura1_item2.getUnidades())));
		factura1.agregarItemFactura(factura1_item2);
		//
		factura1 = facturaService.altaFactura(factura1);
		
		LocalDateTime fechaFactura2 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 25, 17, 30);
		FacturaPayload factura2 = new FacturaPayload();
		factura2.setFecha(fechaFactura2);
		factura2.setCliente(null);
		factura2.setEmisorFactura(proveedor1);
		//
		FacturaItemPayload factura2_item1 = new FacturaItemPayload();
		factura2_item1.setDescripcion(producto3.getDescripcion());
		factura2_item1.setUnidades(producto3.getCantFijaCompra()*4);
		factura2_item1.setPrecioUnitario(producto3.getPrecioVenta());
		factura2_item1.setPrecio(producto3.getPrecioVenta().multiply(BigDecimal.valueOf(factura2_item1.getUnidades())));
		factura2.agregarItemFactura(factura2_item1);
		//
		factura2 = facturaService.altaFactura(factura2);
		
		
		//Prestamo
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<BeneficiarioPayload> beneficiarios = beneficiarioService.getBeneficiarios();
		ContactoPayload empleado1 = contactoService.getContactoById(empleados.get(0).getId());
		ContactoPayload beneficiario1 = contactoService.getContactoById(beneficiarios.get(0).getId());
		
		PrestamoPayload prestamo1 = new PrestamoPayload();
		prestamo1.setDescripcion("Vaso plástico superhéroes");
		prestamo1.setCantidad(2);
		prestamo1.setFechaPrestamoInicio(LocalDateTime.now());
		prestamo1.setFechaPrestamoFin(LocalDateTime.now().plusMonths(2));
		prestamo1.setHaSidoDevuelto(false);
		prestamo1.setPrestamista(empleado1);
		prestamo1.setPrestatario(beneficiario1);
		prestamo1 = prestamoService.altaPrestamo(prestamo1);
		
		//Programa de actividades
		List<ProfesionalPayload> profesionales = profesionalService.getProfesionales();
		ProgramaDeActividadesPayload programa = new ProgramaDeActividadesPayload();
		ActividadPayload actividad = new ActividadPayload();
		actividad.setFechaHoraDesde(LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 1, 12, 00));
		actividad.setFechaHoraHasta(actividad.getFechaHoraDesde().plusHours(3));
		actividad.agregarProfesional(profesionales.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(1));
		actividad.setDescripcion("Prácticas de cocina a beneficiarios por el profesor: " + actividad.getProfesionales().get(0).getNombre()+" "+actividad.getProfesionales().get(0).getApellido());
		programa.agregarActividadesPorSemana(10, actividad);
		programa.setDescripcion(actividad.getDescripcion());
		programaDeActividadesService.altaProgramaDeActividades(programa);
	}
	
	public void cargarDonacionesTenant2() {
		//Donacion 1 con contacto
		ContactoPayload donante = new ContactoPayload();
		donante.setNombreDescripcion("Donante Don Roque");
		donante.setCuit("20-12345678-9");
		donante.setDomicilio("Av. Don Monte 1000");
		donante.setEmail("eldonante@donacion.com");
		donante.setTelefono("1234-5678");
		donante = contactoService.altaContacto(donante);
		
		DonacionPayload donacion = new DonacionPayload();
		donacion.setId(null);
		donacion.setFecha(LocalDateTime.now().minusMonths(1));
		donacion.setDonante(donante);
		donacion.setTipoDonacion(DonacionTipo.DINERO);
		donacion.setDescripcion("$100.000");
		donacion = donacionService.altaDonacion(donacion);
		
		//Donacion anónima
		DonacionPayload donacion2 = new DonacionPayload();
		donacion2.setId(null);
		donacion2.setFecha(LocalDateTime.now().minusDays(1));
		donacion2.setDonante(null);
		donacion2.setTipoDonacion(DonacionTipo.INSUMO);
		donacion2.setDescripcion("Galletitas variadas");
		donacion2 = donacionService.altaDonacion(donacion2);
	}
	
	public void cargarUsuariosDefault() {
		
		cargarUsuarioBasico("admin", RoleName.ROLE_ADMIN);
		cargarUsuarioBasico("employee", RoleName.ROLE_EMPLOYEE);
		cargarUsuarioBasico("profesional", RoleName.ROLE_PROFESIONAL);
		cargarUsuarioBasico("user", RoleName.ROLE_USER);
		
	}
	
	private void cargarUsuarioBasico(String nombre, RoleName rol) {
		String name = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);//primera letra mayúscula
		String username = nombre;
		String email = nombre + "@" + nombre + ".com";
		cargarUsuarioBasico(name, username, email, rol);
	}
	
	private void cargarUsuarioBasico(String name, String username, String email, RoleName rol) {
		User user = new User();
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode("123456"));
		User result = userService.altaUsuario(user);
		System.out.println("Usuario '" + result.getUsername() + "' dado de alta.");
		if(rol!=null)
			userService.agregarRol(username, rol);
	}
	
}




















//Basura de código
/*public void cargarModuloVisibilidadPorRol(Role role) {
	switch(role.getRoleName()) {
	case ROLE_ADMIN:
		cargarModuloVisibilidadRolAdmin(role);
		break;
	case ROLE_EMPLOYEE:
		cargarModuloVisibilidadRolEmployee(role);
		break;
	case ROLE_PROFESIONAL:
		cargarModuloVisibilidadRolProfesional(role);
		break;
	case ROLE_USER:
		cargarModuloVisibilidadRolUser(role);
		break;
	default:
		break;
	}
	
}	

public void cargarModuloVisibilidadRolAdmin(Role role) {
	moduloVisibilidadPorRolService.agregarTodosLosModulos();
	
	ModuloVisibilidadPorRol modulo = new ModuloVisibilidadPorRol();
	modulo.setRole(role);
	modulo.agregarModulo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.USERS, ModuloTipoVisibilidadEnum.EDITAR);
	
	modulo.agregarModulo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.INSUMO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.MARKETPLACE, ModuloTipoVisibilidadEnum.EDITAR);
	moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(modulo);
}	

public void cargarModuloVisibilidadRolEmployee(Role role) {
	ModuloVisibilidadPorRol modulo = new ModuloVisibilidadPorRol();
	modulo.setRole(role);
	modulo.agregarModulo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.EDITAR);
	
	modulo.agregarModulo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.INSUMO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR);
	moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(modulo);
}

public void cargarModuloVisibilidadRolProfesional(Role role) {
	ModuloVisibilidadPorRol modulo = new ModuloVisibilidadPorRol();
	modulo.setRole(role);
	modulo.agregarModulo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.SOLO_VISTA);
			
	modulo.agregarModulo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR);
	modulo.agregarModulo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR);
	moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(modulo);
}

public void cargarModuloVisibilidadRolUser(Role role) {
	ModuloVisibilidadPorRol modulo = new ModuloVisibilidadPorRol();
	modulo.setRole(role);
	modulo.agregarModulo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	modulo.agregarModulo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.SOLO_VISTA);
	moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(modulo);
}*/


/*@Override
public void onApplicationEvent(final ApplicationReadyEvent event) {
	
	// here your code ...
	//La regla es, tenant3 es stop, creamos tenant2, luego 1 (mayor a menor), si existe tenant3, no cargarTenant
	
	//Primero no existe ningun tenant
	if(!masterTenantService.existTenantId("tenant3") 
			&& !masterTenantService.existTenantId("tenant2")
			&& !masterTenantService.existTenantId("tenant1")) {
		masterTenantService.altaTenant(new TenantPayload(300, "tenant3"));
		PfiApplication.restart();
	}
	System.out.println("\n\nENTRE ACAAAAAAAAAAAAAAAAAA\n\n");
	//cargarTenant3();
	
	
	//2da vuelta, ya se crearon las tablas de tenant 3
	if(!masterTenantService.existTenantId("tenant2")
			&& masterTenantService.existTenantId("tenant3")) {
		cargarTenant3();
		masterTenantService.bajaTenant("tenant3");
		masterTenantService.altaTenant(new TenantPayload(200, "tenant2"));
		PfiApplication.restart();
	}
	
	//3era vuelta, ya se crearon las tablas de tenant 2 y las cargo
	//Ahora preparo todo para tenant 1 dando de baja tenant 2 y dando de alta tenant 1
	if(masterTenantService.existTenantId("tenant2") 
			&& !masterTenantService.existTenantId("tenant1")
			&& !masterTenantService.existTenantId("tenant3")) {
		cargarTenant2();
		masterTenantService.bajaTenant("tenant2");
		masterTenantService.altaTenant(new TenantPayload(100, "tenant1"));
		PfiApplication.restart();
	}
	
	//4ta vuelta, ya se crearon las tablas de tenant 1 y las cargo
	//Ahora cargo los otros 2 tenants a la bd y listo
	if(masterTenantService.existTenantId("tenant1") 
			&& !masterTenantService.existTenantId("tenant2")
			&& !masterTenantService.existTenantId("tenant3")) {
		cargarTenant1();
		masterTenantService.altaTenant(new TenantPayload(100, "tenant2"));
		masterTenantService.altaTenant(new TenantPayload(100, "tenant3"));
	}
	
	
	
	
	
	return;
}*/