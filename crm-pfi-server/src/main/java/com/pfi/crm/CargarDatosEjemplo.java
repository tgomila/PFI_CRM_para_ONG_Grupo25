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
	private PersonaFisicaService personaFisicaService;
	
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
	private ProyectoService proyectoService;
	
	@Autowired
	private ProgramaDeActividadesService programaDeActividadesService;
	
	@Autowired
	private DonacionService donacionService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
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
			masterTenantService.altaTenant(new TenantPayload(300, "tenant3", "ONG Sapito", "+541131105305"));
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
				masterTenantService.altaTenant(new TenantPayload(200, "tenant2", "ONG Comida para los chicos", "+541131105305"));
				masterTenantService.bajaTenant("tenant3");
				System.out.println("\nHa finalizado la carga de datos de tenant3");
				System.out.println("\n\n***Reinicie para cargar datos del tenant2.***\n\n");
				return;
			}
			
			//Cargar datos tenant2
			else if(tenantDeAlta.equalsIgnoreCase("tenant2")) {
				cargarTenant2();
				System.out.println("Datos cargados de tenant2.");
				
				/**Esto es por si quiero usar Postgres. Mi sistema solo permite 1 DB para todos los schemas, por el DIALECT
				 * Es porque el "engine" solo lo podes configurar para 1 DataBase a la vez, elijo MySQL.
				 * Si queres saber por que, anda a TenantDatabaseConfig y busca dialect, solo podes elegir 1.
				 **/
				//MasterTenant masterTenant = new MasterTenant(new TenantPayload(100, "tenant1", "ONG Mi Arbolito"));
				//masterTenant.setTenantClientId(100);
				//masterTenant.setDbName("tenant1");
				//masterTenant.setDriverClass("org.postgresql.Driver");
				//masterTenant.setUrl("jdbc:postgresql://localhost:5432/tenant1?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
				//masterTenant.setUserName("postgres");
				//masterTenant.setPassword("1234");
				//masterTenantService.altaTenant(masterTenant);
				
				masterTenantService.altaTenant(new TenantPayload(100, "tenant1", "ONG Mi Arbolito", "+541131105305"));
				masterTenantService.bajaTenant("tenant2");
				System.out.println("\nHa finalizado la carga de datos de tenant2");
				System.out.println("\n\n***Reinicie para cargar datos del tenant1.***\n\n");
				return;
			}
			
			//Cargar datos tenant1
			else if(tenantDeAlta.equalsIgnoreCase("tenant1")) {
				cargarTenant1();
				System.out.println("Datos cargados de tenant1.");
				
				masterTenantService.altaTenant(new TenantPayload(200, "tenant2", "ONG Comida para los chicos", "+541131105305"));
				masterTenantService.altaTenant(new TenantPayload(300, "tenant3", "ONG Sapito", "+541131105305"));
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

			cargarEmpleadosTenant1();
			cargarProfesionalesTenant1();
			cargarVoluntariosTenant1();
			cargarColaboradorTenant1();
			cargarConsejoAdHonoremTenant1();
			cargarPersonaJuridicaTenant1();
			cargarBeneficiariosTenant1();
			cargarUsuariosTenant1();
			cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant1();
			cargarDonacionesTenant1();
			//copiarFotosDeTestHaciaDB();
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
			cargarEmpleadosTenant2();
			cargarProfesionalesTenant2();
			cargarVoluntariosTenant2();
			cargarColaboradorTenant2();
			cargarConsejoAdHonoremTenant2();
			cargarPersonaJuridicaTenant2();
			cargarBeneficiariosTenant2();
			cargarUsuariosTenant2();
			cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant2();
			cargarDonacionesTenant2();
			copiarFotosDeTestHaciaDB();
			
			//generarCienBeneficiariosTenant2();
			//generar30productosTenant2();
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
		moduloMarketService.activarPrueba7dias();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
	}
	
	public void cargarSuscripcionModulosTenant2() {
		moduloMarketService.suscripcionPremiumMes();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
	}
	
	public void cargarSuscripcionModulosTenant3() {
		moduloMarketService.activarPrueba7dias();
		moduloVisibilidadPorRolService.cargarVisibilidadSuscripcionDefault();
		moduloMarketService.desuscribirEn5min();	//Para ver si ya no tiene acceso.
	}
	
	public void cargarEmpleadosTenant1() {
		EmpleadoPayload m = new EmpleadoPayload();
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Jefe CEO");
		m.setCuit("20-30864214-9");
		m.setDomicilio("Avenida Callao 357, piso 4, depto A");
		m.setEmail("nahuelvacca@gmail.com");
		m.setTelefono("+54 11-5335-5646");

		// PersonaFisica
		m.setDni(30864214);
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

		// Contacto
		m.setNombreDescripcion("Empleado");
		m.setCuit("20-34325574-9");
		m.setDomicilio("Gallo 7923, piso 4, depto A");
		m.setEmail("gabrielluchetti@gmail.com");
		m.setTelefono("+54 11-9776-7368");

		// PersonaFisica
		m.setDni(34325574);
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
		m.setNombreDescripcion("Asistente CEO");
		m.setCuit("27-30863276-9");
		m.setDomicilio("Quintana 237, piso 1, depto F");
		m.setEmail("zaragimenez@gmail.com");
		m.setTelefono("6541-5616");

		// PersonaFisica
		m.setDni(30863276);
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
	
	public void cargarProfesionalesTenant1() {
		ProfesionalPayload m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Profesional psicólogo");
		m.setCuit("20-34325930-9");
		m.setDomicilio("Avenida Las Heras 4578, piso 4, depto B");
		m.setEmail("estebangarcia@gmail.com");
		m.setTelefono("+54 11-6711-1835");

		// PersonaFisica
		m.setDni(34325930);
		m.setNombre("Esteban");
		m.setApellido("García");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Psicólogo");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		////////////////////////////////////////
		m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Técnico electricista");
		m.setCuit("20-34324973-9");
		m.setDomicilio("Buhos 6518, piso 2, depto C");
		m.setEmail("ricardofrachia@gmail.com");
		m.setTelefono("+54 11-0607-6285");

		// PersonaFisica
		m.setDni(34324973);
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
		m.setCuit("27-34324316-9");
		m.setDomicilio("Cecelia 2816, piso 9, depto C");
		m.setEmail("julietaveraniegos@gmail.com");
		m.setTelefono("+54 11-2682-2232");

		// PersonaFisica
		m.setDni(34324316);
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
		m.setCuit("27-34324669-9");
		m.setDomicilio("Uruguay 731, piso 1, depto A");
		m.setEmail("lilianaferacios@gmail.com");
		m.setTelefono("+54 11-3245-3747");

		// PersonaFisica
		m.setDni(34324669);
		m.setNombre("Liliana");
		m.setApellido("Feracios");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Medica");
		// Fin Profesional
		profesionalService.altaProfesional(m);	
	}
	
	public void cargarVoluntariosTenant1() {
		VoluntarioPayload m = new VoluntarioPayload();

		// Contacto
		m.setNombreDescripcion("Voluntario que ayuda a los chicos");
		m.setCuit("20-34324401-9");
		m.setDomicilio("Peralta 457, piso 2, depto A");
		m.setEmail("julioroque@gmail.com");
		m.setTelefono("+54 11-1703-9114");

		// PersonaFisica
		m.setDni(34324401);
		m.setNombre("Julio");
		m.setApellido("Roque");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		//Voluntario
		//No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario que ayuda a los chicos");
		m.setCuit("20-34325049-9");
		m.setDomicilio("Peralta 457, piso 2, depto A");
		m.setEmail("santiagogomez@gmail.com");
		m.setTelefono("9466-7813");

		// PersonaFisica
		m.setDni(34325049);
		m.setNombre("Santiago");
		m.setApellido("Gomez");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntaria");
		m.setCuit("27-322484-9");
		m.setDomicilio("Samalia 789, piso 17, depto C");
		m.setEmail("agustinacampos@gmail.com");
		m.setTelefono("6516-7896");

		// PersonaFisica
		m.setDni(322484);
		m.setNombre("Agustina");
		m.setApellido("Campos");
		m.setFechaNacimiento(LocalDate.of(1920, 7, 14));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
	}
	
	public void cargarColaboradorTenant1() {
		
		ColaboradorPayload m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("27-32483988-9");
		m.setDomicilio("Falacios 6091, piso 3, depto B");
		m.setEmail("micaelapadin@gmail.com");
		m.setTelefono("+54 11-2856-8143");

		// PersonaFisica
		m.setDni(32483988);
		m.setNombre("Micaela");
		m.setApellido("Padín");
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
		m.setCuit("27-32485529-9");
		m.setDomicilio("Polonia 741, piso 1, depto A");
		m.setEmail("trinidadcabellos@gmail.com");
		m.setTelefono("+54 11-2870-1492");

		// PersonaFisica
		m.setDni(32485529);
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
		m.setCuit("27-32484553-9");
		m.setDomicilio("Cacimodo 127, piso 7, depto D");
		m.setEmail("susanadoravitali@gmail.com");
		m.setTelefono("6541-3854");

		// PersonaFisica
		m.setDni(32484553);
		m.setNombre("Susana Dora");
		m.setApellido("Vitali");
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
		m.setCuit("20-39325391-9");
		m.setDomicilio("Paraguay 803, piso 6, depto A");
		m.setEmail("pedrodiarse@gmail.com");
		m.setTelefono("4436-9567");

		// PersonaFisica
		m.setDni(39325391);
		m.setNombre("Pedro");
		m.setApellido("Diarse");
		m.setFechaNacimiento(LocalDate.of(1997, 1, 20));

		// ConsejoAdHonorem
		m.setFuncion("Aconseja a los superiores");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeraAdHonorem");
		m.setCuit("27-30558795-9");
		m.setDomicilio("Gallo 956, piso 4, depto A");
		m.setEmail("felicitasvicines@gmail.com");
		m.setTelefono("4657-1656");

		// PersonaFisica
		m.setDni(30558795);
		m.setNombre("Felicitas");
		m.setApellido("visines");
		m.setFechaNacimiento(LocalDate.of(1984, 10, 12));

		// ConsejoAdHonorem
		m.setFuncion("Aconseja a los empleados");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("ConsejeroAdHonorem");
		m.setCuit("20-40096431-9");
		m.setDomicilio("Sartinez 654, piso 1, depto B");
		m.setEmail("juanquintana@gmail.com");
		m.setTelefono("6541-9841");

		// PersonaFisica
		m.setDni(40096431);
		m.setNombre("Juan");
		m.setApellido("Quintana");
		m.setFechaNacimiento(LocalDate.of(1998, 2, 22));

		// ConsejoAdHonorem
		m.setFuncion("Asistente");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
	}
	
	public void cargarPersonaJuridicaTenant1() {
		
		PersonaJuridicaPayload m = new PersonaJuridicaPayload();

		// Contacto
		m.setNombreDescripcion("Coto");
		m.setCuit("20-123-9");
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
		m.setCuit("20-432-9");
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
		m.setCuit("20-234-9");
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
		m.setCuit("20-53917506-9");
		m.setDomicilio("Avenida corrientes 7642");
		m.setEmail("caritasfelices@caritasfelices.org.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.OSC);

		personaJuridicaService.altaPersonaJuridica(m);
		
	}
	
	
	public void cargarBeneficiariosTenant1() {
		BeneficiarioPayload m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Beneficiario");
		m.setCuit("20-48040461-9");
		m.setDomicilio("Zapiola 970, piso 4, depto A");
		m.setEmail("felipeGarcia@gmail.com");
		m.setTelefono("+54 11-2662-7378");

		// PersonaFisica
		m.setDni(48040461);
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
		m.setCuit("27-49809327-9");
		m.setDomicilio("Charcas 4431, piso 4, depto A");
		m.setEmail("mariajosefinaruiz@gmail.com");
		m.setTelefono("+54 11-8787-1768");

		// PersonaFisica
		m.setDni(49809327);
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
		m.setCuit("27-48474802-9");
		m.setDomicilio("Almafuerte 2740, piso 1, depto B");
		m.setEmail("analiaruiz@gmail.com");
		m.setTelefono("+54 11-2934-6639");

		// PersonaFisica
		m.setDni(48474802);
		m.setNombre("Analia");
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
		m.setCuit("27-48474349-9");
		m.setDomicilio("Sancez reta 382");
		m.setEmail("cecilialopez@gmail.com");
		m.setTelefono("9516-6545");

		// PersonaFisica
		m.setDni(48474349);
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
	
	public void cargarUsuariosTenant1() {
		User user;
		
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<ProfesionalPayload> profesionales = profesionalService.getProfesionales();
		//Admin
		if(empleados.size()>0) {
			user = cargarUserAux(empleados.get(0), RoleName.ROLE_ADMIN);
			user.setUsername("admin");
			userService.altaUsuario(user);
		}
		//Empleado
		if(empleados.size()>1) {
			user = cargarUserAux(empleados.get(1), RoleName.ROLE_EMPLOYEE);
			user.setUsername("employee");
			userService.altaUsuario(user);
		}
		if(empleados.size()>2) {
			user = cargarUserAux(empleados.get(2), RoleName.ROLE_EMPLOYEE);
			userService.altaUsuario(user);
		}
		//Profesional
		if(profesionales.size()>0) {
			user = cargarUserAux(profesionales.get(0), RoleName.ROLE_PROFESIONAL);
			user.setUsername("profesional");
			userService.altaUsuario(user);
		}
		if(profesionales.size()>1) {
			user = cargarUserAux(profesionales.get(1), RoleName.ROLE_PROFESIONAL);
			userService.altaUsuario(user);
		}
		//cargarUsuariosDefault();
		
	}
	
	private User cargarUserAux(PersonaFisicaAbstractPayload persona, RoleName roleName) {
		User user = new User();
		user.setName(persona.getNombre().split(" ")[0] + " " + persona.getApellido().split(" ")[0]);
		user.setUsername(persona.getNombre().split(" ")[0].toLowerCase().substring(0, 1) + persona.getApellido().split(" ")[0].toLowerCase());
		user.setEmail(persona.getEmail());
		user.setPassword(passwordEncoder.encode("123456"));
		user.setContacto(contactoService.getContactoModelById(persona.getId()));
		user.agregarRol(new Role(roleName));
		return user;
	}
	
	public void cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant1() {
		
		//Producto
		ContactoPayload proveedor1 = new ContactoPayload();
		proveedor1.setNombreDescripcion("Juguetería los 3 chanchitos");
		proveedor1.setCuit("20-567-9");
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

		InsumoPayload insumo3 = new InsumoPayload();
		insumo3.setTipo("Agua");
		insumo3.setDescripcion("Bidones de 5 litros de agua mineral");
		insumo3.setStockActual(30);
		insumo3.setFragil(false);
		insumo3 = insumoService.altaInsumo(insumo3);
		
		InsumoPayload insumo4 = new InsumoPayload();
		insumo4.setTipo("Mesa");
		insumo4.setDescripcion("Mesa de vidrio");
		insumo4.setStockActual(5);
		insumo4.setFragil(true);
		insumo4 = insumoService.altaInsumo(insumo4);
		
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
		
		
		//Get lista para payloads con personas
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<BeneficiarioPayload> beneficiarios = beneficiarioService.getBeneficiarios();
		
		//Prestamo
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
		
		//Proyecto
		PersonaFisicaPayload persona1 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(0).getId());
		PersonaFisicaPayload persona2 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(1).getId());
		PersonaFisicaPayload persona3 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(2).getId());
		ProyectoPayload proyecto1 = new ProyectoPayload();
		proyecto1.setDescripcion("Organizar calendario del mes siguiente");
		proyecto1.setFechaInicio(LocalDate.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 1));
		proyecto1.setFechaFin(proyecto1.getFechaInicio().plusDays(1));
		proyecto1.agregarInvolucrado(persona1);
		proyecto1.agregarInvolucrado(persona2);
		proyecto1.agregarInvolucrado(persona3);
		proyectoService.altaProyecto(proyecto1);
		
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
		donante.setCuit("20-26791462-9");
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
		donacion.setValorAproximadoDeLaDonacion(BigDecimal.valueOf(100000.00));
		donacion = donacionService.altaDonacion(donacion);
		
		//Donacion anónima
		DonacionPayload donacion2 = new DonacionPayload();
		donacion2.setId(null);
		donacion2.setFecha(LocalDateTime.now().minusDays(1));
		donacion2.setDonante(null);
		donacion2.setTipoDonacion(DonacionTipo.INSUMO);
		donacion2.setDescripcion("Juguete ladrillitos");
		donacion.setValorAproximadoDeLaDonacion(BigDecimal.valueOf(15000.00));
		donacion2 = donacionService.altaDonacion(donacion2);
	}
	
	
	
	//Tenant 2
	public void cargarContactoComoVoluntarioYEmpleadoTenant2() {
		ContactoPayload contactoPayload = new ContactoPayload();

		// Contacto
		contactoPayload.setNombreDescripcion("Jefa y voluntaria de la ONG");
		contactoPayload.setCuit("27-33230401-2");
		contactoPayload.setDomicilio("Lavalle 1978, piso 3, depto A");
		contactoPayload.setEmail("julietaalvarez@gmail.com");
		contactoPayload.setTelefono("+54 11-7162-5179");
		
		contactoPayload = contactoService.altaContacto(contactoPayload);
		
		
		
		/////////////////////////////////////////////////////////////////////
		VoluntarioPayload voluntarioPayload;
		
		//Asociar a contacto id=1.
		ContactoPayload getContactoPayloadDB = contactoService.getContactoById(contactoPayload.getId());

		// Contacto
		voluntarioPayload = new VoluntarioPayload();
		voluntarioPayload.modificarContacto(getContactoPayloadDB);

		// PersonaFisica
		voluntarioPayload.setDni(33230401);
		voluntarioPayload.setNombre("Julieta");
		voluntarioPayload.setApellido("Álvarez");
		voluntarioPayload.setFechaNacimiento(LocalDate.of(1988, 7, 9));

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
		empleadoPayload.setDni(33230401);
		empleadoPayload.setNombre("Julieta");
		empleadoPayload.setApellido("Álvarez");
		empleadoPayload.setFechaNacimiento(LocalDate.of(1977, 5, 16));

		// Empleado
		empleadoPayload.setDatosBancarios("CBU: 0057813-43583");
		empleadoPayload.setFuncion("Jefa de la ONG");
		empleadoPayload.setDescripcion("Da todas las órdenes del establecimiento");
		// Fin Empleado
		
		empleadoService.altaEmpleado(empleadoPayload);
		//Fin asociar empleado
	}
	
	public void cargarEmpleadosTenant2() {
		EmpleadoPayload m = new EmpleadoPayload();

		/////////////////////////////////////////////
		
		// Contacto
		m = new EmpleadoPayload();
		m.setNombreDescripcion("Empleada");
		m.setCuit("27-34326093-9");
		m.setDomicilio("Gallo 7923, piso 4, depto A");
		m.setEmail("frodriguez@gmail.com");
		m.setTelefono("+54 11-7874-5454");

		// PersonaFisica
		m.setDni(34326093);
		m.setNombre("Florencia");
		m.setApellido("Rodriguez");
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
		m.setNombreDescripcion("Empleado");
		m.setCuit("20-30862571-9");
		m.setDomicilio("Av. Pueyrredón 1719, piso 7, depto A");
		m.setEmail("gabrieldiaz@gmail.com");
		m.setTelefono("+54 11-4312-8166");
		
		// PersonaFisica
		m.setDni(30862571);
		m.setNombre("Gabriel");
		m.setApellido("Diaz");
		m.setFechaNacimiento(LocalDate.of(1989, 9, 17));
		
		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Chef del establecimiento");
		m.setDescripcion("Cocina comidas para los chicos");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		/////////////////////////////////////////////
		m = new EmpleadoPayload();
		
		// Contacto
		m.setNombreDescripcion("Empleada");
		m.setCuit("27-30863688-9");
		m.setDomicilio("Quintana 237, piso 1, depto F");
		m.setEmail("patriciacastro@gmail.com");
		m.setTelefono("6541-5616");
		
		// PersonaFisica
		m.setDni(30863688);
		m.setNombre("Patricia");
		m.setApellido("Castro");
		m.setFechaNacimiento(LocalDate.of(1981, 4, 23));
		
		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Nutricionista");
		m.setDescripcion("Aconseja a los chicos sobre nutrición de los alimentos");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		
		
		/////////////////////////////////////////////
		m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Empleado");
		m.setCuit("20-41007965-9");
		m.setDomicilio("Libertad 2568");
		m.setEmail("emiliopaz@gmail.com");
		m.setTelefono("+54 11-8157-3164");

		// PersonaFisica
		m.setDni(41007965);
		m.setNombre("Emilio");
		m.setApellido("Paz");
		m.setFechaNacimiento(LocalDate.of(1999, 3, 17));

		// Empleado
		m.setDatosBancarios("CBU: 001234");
		m.setFuncion("Soporte telefónico");
		m.setDescripcion("Atiende llamadas de beneficiarios");
		// Fin Empleado
		
		empleadoService.altaEmpleado(m);
		/////////////////////////////////////////////
	}
	
	
	public void cargarProfesionalesTenant2() {
		ProfesionalPayload m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Profesional");
		m.setCuit("27-30088948-9");
		m.setDomicilio("Viamonte 1678, piso 2, depto D");
		m.setEmail("eugeniavarela@gmail.com");
		m.setTelefono("+54 11-8032-5048");

		// PersonaFisica
		m.setDni(30088948);
		m.setNombre("Eugenia");
		m.setApellido("Varela");
		m.setFechaNacimiento(LocalDate.of(1984, 2, 15));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Profesora de cocina");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Profesional");
		m.setCuit("27-32215193-8");
		m.setDomicilio("Bolivar 294, piso 4E");
		m.setEmail("belenflores@gmail.com");
		m.setTelefono("+54 11-2803-9214");
		
		// PersonaFisica
		m.setDni(32215193);
		m.setNombre("Belen");
		m.setApellido("Flores");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));
		
		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Chef de eventos");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();

		// Contacto
		m.setNombreDescripcion("Profesional");
		m.setCuit("20-27808290-9");
		m.setDomicilio("Ñandues 5470, piso 1, depto A");
		m.setEmail("alejandrogris@gmail.com");
		m.setTelefono("+54 11-8992-3083");

		// PersonaFisica
		m.setDni(27808290);
		m.setNombre("Alejandro");
		m.setApellido("Varela");
		m.setFechaNacimiento(LocalDate.of(1980, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Mantenimiento");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Profesional");
		m.setCuit("27-34326122-9");
		m.setDomicilio("Cecelia 2818, piso 11, depto D");
		m.setEmail("fernandaarevalo@gmail.com");
		m.setTelefono("+54 11-9469-516");

		// PersonaFisica
		m.setDni(34326122);
		m.setNombre("Fernanda");
		m.setApellido("Arevalo");
		m.setFechaNacimiento(LocalDate.of(1995, 6, 3));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Administradora de eventos");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		
		////////////////////////////////////////
		m = new ProfesionalPayload();		
		
		// Contacto
		m.setNombreDescripcion("Profesional");
		m.setCuit("27-34325366-9");
		m.setDomicilio("Uruguay 741, piso 1, depto A");
		m.setEmail("camilarodriguez@gmail.com");
		m.setTelefono("+54 11-9114-9716");

		// PersonaFisica
		m.setDni(34325366);
		m.setNombre("Camila");
		m.setApellido("Rodriguez");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setDatosBancarios("CBU: 001234");
		m.setProfesion("Medica clinica");
		// Fin Profesional
		profesionalService.altaProfesional(m);
		
		////////////////////////////////////////
	}
	
	public void cargarVoluntariosTenant2() {
		VoluntarioPayload m;
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntaria");
		m.setCuit("27-41025384-9");
		m.setDomicilio("Av. Callao 939, piso 1, depto C");
		m.setEmail("nataliaramos@gmail.com");
		m.setTelefono("+54 11-0110-8613");

		// PersonaFisica
		m.setDni(41025384);
		m.setNombre("Natalia");
		m.setApellido("Ramos");
		m.setFechaNacimiento(LocalDate.of(1999, 1, 27));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		///////////////////////////////////

		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario");
		m.setCuit("20-39086152-9");
		m.setDomicilio("Alberti 243, piso 3, depto B");
		m.setEmail("martinlopez@gmail.com");
		m.setTelefono("+54 11-4040-3382");

		// PersonaFisica
		m.setDni(39086152);
		m.setNombre("Martin");
		m.setApellido("Lopez");
		m.setFechaNacimiento(LocalDate.of(1997, 7, 17));

		//Voluntario
		//No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntario");
		m.setCuit("20-34420171-9");
		m.setDomicilio("Sarmiento 3472, piso 4, depto C");
		m.setEmail("francowellington@gmail.com");
		m.setTelefono("9466-7813");

		// PersonaFisica
		m.setDni(34420171);
		m.setNombre("Franco");
		m.setApellido("Wellington");
		m.setFechaNacimiento(LocalDate.of(1990, 3, 10));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		///////////////////////////////////
		
		// Contacto
		m = new VoluntarioPayload();
		m.setNombreDescripcion("Voluntaria");
		m.setCuit("27-6492594-8");
		m.setDomicilio("Jorge Luis Borges 1854, piso 18, depto D");
		m.setEmail("camilacampos@gmail.com");
		m.setTelefono("7818-3617");

		// PersonaFisica
		m.setDni(6492594);
		m.setNombre("Camila");
		m.setApellido("Campos");
		m.setFechaNacimiento(LocalDate.of(1941, 7, 17));

		// Voluntario
		// No tiene otros atributos
		voluntarioService.altaVoluntario(m);
		
		///////////////////////////////////
	}
	
	public void cargarColaboradorTenant2() {
		
		ColaboradorPayload m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("27-29155744-9");
		m.setDomicilio("Cantillan 456, piso 8, depto A");
		m.setEmail("josefinamarruecos@gmail.com");
		m.setTelefono("+54 11-1434-1133");

		// PersonaFisica
		m.setDni(29155744);
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
		m.setNombreDescripcion("Colaborador");
		m.setCuit("20-32484836-9");
		m.setDomicilio("Dentri 631, piso 12, depto C");
		m.setEmail("jorgegomez@gmail.com");
		m.setTelefono("6123-3854");

		// PersonaFisica
		m.setDni(32484836);
		m.setNombre("Jorge");
		m.setApellido("Gomez");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area de deportes");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		///////////////////////
		
		m = new ColaboradorPayload();

		// Contacto
		m.setNombreDescripcion("Colaboradora");
		m.setCuit("27-32484760-9");
		m.setDomicilio("Suiza 6543, piso 3, depto A");
		m.setEmail("karinarekini@gmail.com");
		m.setTelefono("+54 11-5742-8141");

		// PersonaFisica
		m.setDni(32484760);
		m.setNombre("Karina");
		m.setApellido("Rekini");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
		m.setDatosBancarios("CBU: 001234");
		m.setArea("Area social");
		// Fin Colaborador

		colaboradorService.altaColaborador(m);
		
		///////////////////////
		
	}
	
	
	public void cargarConsejoAdHonoremTenant2() {
		
		ConsejoAdHonoremPayload m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("Consejero Ad Honorem");
		m.setCuit("20-4862239-9");
		m.setDomicilio("Paraguay 803, piso 6, depto A");
		m.setEmail("pedroperez@yahoo.com.ar");
		m.setTelefono("4436-9567");

		// PersonaFisica
		m.setDni(12026279);
		m.setNombre("Pedro");
		m.setApellido("Perez");
		m.setFechaNacimiento(LocalDate.of(1936, 10, 3));

		// ConsejoAdHonorem
		m.setFuncion("Consejero asesor ad honorem");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("Consejera Ad Honorem");
		m.setCuit("27-30558524-9");
		m.setDomicilio("Arenales 123, piso 4, depto A");
		m.setEmail("julietanamias@gmail.com");
		m.setTelefono("4657-1656");

		// PersonaFisica
		m.setDni(30558524);
		m.setNombre("Julieta");
		m.setApellido("Namias");
		m.setFechaNacimiento(LocalDate.of(1984, 10, 12));

		// ConsejoAdHonorem
		m.setFuncion("Administradora del consejo");
		
		consejoAdHonoremService.altaConsejoAdHonorem(m);
		
		
		
		
		///////////////////////////////////////////////////
		m = new ConsejoAdHonoremPayload();

		// Contacto
		m.setNombreDescripcion("Consejero Ad Honorem");
		m.setCuit("20-40095971-9");
		m.setDomicilio("Perez 456, piso 1, depto C");
		m.setEmail("marioperez@gmail.com");
		m.setTelefono("6541-9841");

		// PersonaFisica
		m.setDni(40095971);
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
		m.setCuit("30-52876213-8");
		m.setDomicilio("Av Santa Fe 534");
		m.setEmail("colegiosantafe@gmail.com");
		m.setTelefono("4570-3197");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.INSTITUCION);

		personaJuridicaService.altaPersonaJuridica(m);
		
		
		
		/////////////////////////////
		m = new PersonaJuridicaPayload();
		
		// Contacto
		m.setNombreDescripcion("Jumbo");
		m.setCuit("30-62719317-4");
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
		m.setNombreDescripcion("Ente gobernamental");
		m.setCuit("20-53917506-9");
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
		m.setCuit("30-59470647-9");
		m.setDomicilio("Avenida corrientes 7642");
		m.setEmail("caritasfelices@caritasfelices.org.ar");
		m.setTelefono("5614-6546");

		// Persona Juridica
		m.setInternoTelefono("05");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.OSC);

		personaJuridicaService.altaPersonaJuridica(m);
	}
	
	public void cargarBeneficiariosTenant2() {
		
		BeneficiarioPayload m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Beneficiaria");
		m.setCuit("27-46919073-9");
		m.setDomicilio("Av. Hipólito Yrigoyen 2772, piso 2, depto B");
		m.setEmail("lissetebrisa@gmail.com");
		m.setTelefono("+54 11-3963-1386");

		// PersonaFisica
		m.setDni(46919073);
		m.setNombre("Lissete");
		m.setApellido("Brisa");
		m.setFechaNacimiento(LocalDate.of(2008, 1, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("000123"));
		m.setLegajo(Long.parseLong("1070123"));
		m.setLugarDeNacimiento("Villa Urquiza");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("No puede comer maní");
		m.setEscuela("Colegio Nº700");
		m.setGrado("3º año");
		m.setTurno("Tarde");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Beneficiario");
		m.setCuit("20-45222208-9");
		m.setDomicilio("Sarmiento 2057, piso 3, depto A");
		m.setEmail("ricardosojo@gmail.com");
		m.setTelefono("+54 11-5665-3208");

		// PersonaFisica
		m.setDni(45222208);
		m.setNombre("Ricardo");
		m.setApellido("Sojo");
		m.setFechaNacimiento(LocalDate.of(2005, 10, 22));

		// Beneficiario
		m.setIdONG(Long.parseLong("004270"));
		m.setLegajo(Long.parseLong("1015375"));
		m.setLugarDeNacimiento("Belgrano");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Dieta sin harinas");
		m.setEscuela("Colegio Nº34");
		m.setGrado("5º año");
		m.setTurno("Tarde");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Beneficiaria");
		m.setCuit("27-46475954-9");
		m.setDomicilio("Uruguay 782, piso 1, depto B");
		m.setEmail("evelynruiz@gmail.com");
		m.setTelefono("+54 11-9978-3807");

		// PersonaFisica
		m.setDni(46475954);
		m.setNombre("Evelyn");
		m.setApellido("Ruiz");
		m.setFechaNacimiento(LocalDate.of(2007, 9, 14));

		// Beneficiario
		m.setIdONG(Long.parseLong("008741"));
		m.setLegajo(Long.parseLong("1036782"));
		m.setLugarDeNacimiento("La Matanza");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Ninguno");
		m.setEscuela("Colegio Nº3");
		m.setGrado("3º año");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
		
		
		
		////////////////////////////////////////////
		m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Beneficiario");
		m.setCuit("20-46396063-9");
		m.setDomicilio("Av. Raúl Scalabrini Ortiz 751, piso 7, depto C");
		m.setEmail("agustinpeña@gmail.com");
		m.setTelefono("4970-1876");

		// PersonaFisica
		m.setDni(46396063);
		m.setNombre("Nahuel");
		m.setApellido("Peña");
		m.setFechaNacimiento(LocalDate.of(2008, 2, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("000349"));
		m.setLegajo(Long.parseLong("1040937"));
		m.setLugarDeNacimiento("Caballito");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Azucar alto");
		m.setEscuela("Colegio Nº4");
		m.setGrado("2º año");
		m.setTurno("Noche");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		beneficiarioService.altaBeneficiario(m);
	}
	
	public void cargarUsuariosTenant2() {
		User user;
		
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<ProfesionalPayload> profesionales = profesionalService.getProfesionales();
		//Admin
		if(empleados.size()>0) {
			user = cargarUserAux(empleados.get(0), RoleName.ROLE_ADMIN);
			user.setUsername("admin");
			userService.altaUsuario(user);
		}
		//Empleado
		if(empleados.size()>1) {
			user = cargarUserAux(empleados.get(1), RoleName.ROLE_EMPLOYEE);
			user.setUsername("employee");
			userService.altaUsuario(user);
		}
		if(empleados.size()>2) {
			user = cargarUserAux(empleados.get(2), RoleName.ROLE_EMPLOYEE);
			userService.altaUsuario(user);
		}
		//Profesional
		if(profesionales.size()>0) {
			user = cargarUserAux(profesionales.get(0), RoleName.ROLE_PROFESIONAL);
			user.setUsername("profesional");
			userService.altaUsuario(user);
		}
		if(profesionales.size()>1) {
			user = cargarUserAux(profesionales.get(1), RoleName.ROLE_PROFESIONAL);
			userService.altaUsuario(user);
		}
		//cargarUsuariosDefault();
	}
	

	
	public void cargarProductosInsumosFacturaPrestamoProgramaDeActividadesTenant2() {
		
		//Producto
		ContactoPayload proveedor1 = new ContactoPayload();
		proveedor1.setNombreDescripcion("kiosco 7 dias");
		proveedor1.setCuit("20-65703546-9");
		proveedor1.setDomicilio("9 de julio 567");
		proveedor1.setEmail("kiosco7dias@abierto.com");
		proveedor1.setTelefono("4123-4567");
		proveedor1 = contactoService.altaContacto(proveedor1);

		ContactoPayload proveedor2 = new ContactoPayload();
		proveedor2.setNombreDescripcion("Bazar cocineros en acción");
		proveedor2.setCuit("20-67527374-9");
		proveedor2.setDomicilio("Av. Independencia 1137");
		proveedor2.setEmail("bazarcocinerosenaccion@abierto.com");
		proveedor2.setTelefono("1234-5678");
		proveedor2 = contactoService.altaContacto(proveedor2);

		ContactoPayload proveedor3 = new ContactoPayload();
		proveedor3.setNombreDescripcion("Dietética Daniel");
		proveedor3.setCuit("20-62714304-9");
		proveedor3.setDomicilio("Av. Independencia 1536");
		proveedor3.setEmail("bazarcocinerosenaccion@abierto.com");
		proveedor3.setTelefono("1234-5678");
		proveedor3 = contactoService.altaContacto(proveedor3);
		
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
		producto2.setTipo("Agua");
		producto2.setDescripcion("Bidón de agua de 5 litros");
		producto2.setPrecioVenta(BigDecimal.valueOf(700.00));
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
		
		ProductoPayload producto4 = new ProductoPayload();
		producto4.setTipo("Galletita");
		producto4.setDescripcion("Galletitas cookies con chispas de chocolate");
		producto4.setPrecioVenta(BigDecimal.valueOf(500.00));
		producto4.setCantFijaCompra(10);
		producto4.setCantMinimaStock(100);
		producto4.setStockActual(80);
		producto4.setFragil(false);
		producto4.setProveedor(proveedor1);
		producto4 = productoService.altaProducto(producto4);
		
		ProductoPayload producto5 = new ProductoPayload();
		producto5.setTipo("Arroz");
		producto5.setDescripcion("Arroz integral 1kg");
		producto5.setPrecioVenta(BigDecimal.valueOf(500.00));
		producto5.setCantFijaCompra(10);
		producto5.setCantMinimaStock(100);
		producto5.setStockActual(80);
		producto5.setFragil(false);
		producto5.setProveedor(proveedor3);
		producto5 = productoService.altaProducto(producto5);
		
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

		InsumoPayload insumo3 = new InsumoPayload();
		insumo3.setTipo("Silla");
		insumo3.setDescripcion("Silla de plástico");
		insumo3.setStockActual(50);
		insumo3.setFragil(false);
		insumo3 = insumoService.altaInsumo(insumo3);
		
		InsumoPayload insumo4 = new InsumoPayload();
		insumo4.setTipo("Mesa");
		insumo4.setDescripcion("Mesa de madera");
		insumo4.setStockActual(5);
		insumo4.setFragil(false);
		insumo4 = insumoService.altaInsumo(insumo4);
		
		//Factura
		ContactoPayload empleada_nutricionista = contactoService.getContactoSiExisteByCuit("27-30863688-9");
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime todayMinus1month = today.minusMonths(1);
		LocalDateTime fechaFactura1 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 20, 13, 15);
		FacturaPayload factura1 = new FacturaPayload();
		factura1.setFecha(fechaFactura1);
		factura1.setCliente(empleada_nutricionista);
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

		ContactoPayload empleado_chef = contactoService.getContactoSiExisteByCuit("20-30862571-9");
		LocalDateTime fechaFactura2 = LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 25, 17, 30);
		FacturaPayload factura2 = new FacturaPayload();
		factura2.setFecha(fechaFactura2);
		factura2.setCliente(empleado_chef);
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
		
		
		//Get lista para payloads con personas
		List<EmpleadoPayload> empleados = empleadoService.getEmpleados();
		List<BeneficiarioPayload> beneficiarios = beneficiarioService.getBeneficiarios();
		List<ProfesionalPayload> profesionales = profesionalService.getProfesionales();
		List<VoluntarioPayload> voluntarios = voluntarioService.getVoluntarios();
		List<ConsejoAdHonoremPayload> consejoAdHonorems = consejoAdHonoremService.getConsejoAdHonorems();
		
		//Prestamo
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
		
		//Proyecto
		LocalDateTime todayPlus1month = today.plusMonths(1);
		
		PersonaFisicaPayload persona1 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(0).getId());
		PersonaFisicaPayload persona2 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(1).getId());
		PersonaFisicaPayload persona3 = personaFisicaService.getPersonaFisicaByIdContacto(empleados.get(2).getId());
		ProyectoPayload proyecto1 = new ProyectoPayload();
		proyecto1.setDescripcion("Organizar calendario del mes siguiente");
		proyecto1.setFechaInicio(LocalDate.of(todayPlus1month.getYear(), todayPlus1month.getMonth(), 1));
		proyecto1.setFechaFin(proyecto1.getFechaInicio().plusDays(1));
		proyecto1.agregarInvolucrado(persona1);
		proyecto1.agregarInvolucrado(persona2);
		proyecto1.agregarInvolucrado(persona3);
		proyectoService.altaProyecto(proyecto1);
		
		PersonaFisicaPayload persona4 = personaFisicaService.getPersonaFisicaByIdContacto(profesionales.get(2).getId());
		ProyectoPayload proyecto2 = new ProyectoPayload();
		proyecto2.setDescripcion("Colocado de aire acondicionado");
		proyecto2.setFechaInicio(LocalDate.of(todayPlus1month.getYear(), todayPlus1month.getMonth(), 15));
		proyecto2.setFechaFin(proyecto2.getFechaInicio().plusDays(5));
		proyecto2.agregarInvolucrado(persona4);
		proyectoService.altaProyecto(proyecto2);
		
		PersonaFisicaPayload persona5 = personaFisicaService.getPersonaFisicaByIdContacto(voluntarios.get(1).getId());
		PersonaFisicaPayload persona6 = personaFisicaService.getPersonaFisicaByIdContacto(voluntarios.get(2).getId());
		PersonaFisicaPayload persona7 = personaFisicaService.getPersonaFisicaByIdContacto(voluntarios.get(3).getId());
		ProyectoPayload proyecto3 = new ProyectoPayload();
		proyecto3.setDescripcion("Marketing de recolección de fondos y donaciones");
		proyecto3.setFechaInicio(LocalDate.of(todayPlus1month.getYear(), todayPlus1month.getMonth(), 15));
		proyecto3.setFechaFin(proyecto3.getFechaInicio().plusMonths(3));
		proyecto3.agregarInvolucrado(persona5);
		proyecto3.agregarInvolucrado(persona6);
		proyecto3.agregarInvolucrado(persona7);
		proyectoService.altaProyecto(proyecto3);
		
		PersonaFisicaPayload persona8 = personaFisicaService.getPersonaFisicaByIdContacto(consejoAdHonorems.get(0).getId());
		PersonaFisicaPayload persona9 = personaFisicaService.getPersonaFisicaByIdContacto(consejoAdHonorems.get(1).getId());
		PersonaFisicaPayload persona10 = personaFisicaService.getPersonaFisicaByIdContacto(consejoAdHonorems.get(2).getId());
		ProyectoPayload proyecto4 = new ProyectoPayload();
		proyecto4.setDescripcion("Reunión de brainstorming para sugerencias de eventos de ONG");
		proyecto4.setFechaInicio(LocalDate.of(todayPlus1month.getYear(), todayPlus1month.getMonth(), 10));
		proyecto4.setFechaFin(proyecto4.getFechaInicio());
		proyecto4.agregarInvolucrado(persona8);
		proyecto4.agregarInvolucrado(persona9);
		proyecto4.agregarInvolucrado(persona10);
		proyectoService.altaProyecto(proyecto4);
		
		//Programa de actividades
		ProgramaDeActividadesPayload programa = new ProgramaDeActividadesPayload();
		ActividadPayload actividad = new ActividadPayload();
		actividad.setFechaHoraDesde(LocalDateTime.of(todayMinus1month.getYear(), todayMinus1month.getMonth(), 1, 12, 00));
		actividad.setFechaHoraHasta(actividad.getFechaHoraDesde().plusHours(3));
		actividad.agregarProfesional(profesionales.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(0));
		actividad.agregarBeneficiario(beneficiarios.get(1));
		actividad.setDescripcion("Prácticas de cocina a beneficiarios por la profesora: " + actividad.getProfesionales().get(0).getNombre()+" "+actividad.getProfesionales().get(0).getApellido());
		programa.agregarActividadesPorSemana(10, actividad);
		programa.setDescripcion(actividad.getDescripcion());
		programaDeActividadesService.altaProgramaDeActividades(programa);
	}
	
	public void cargarDonacionesTenant2() {
		//Donacion 1 con contacto
		PersonaFisicaPayload personaDonante = new PersonaFisicaPayload();
		personaDonante.setNombreDescripcion("Donante Victoria Lopez");
		personaDonante.setCuit("27-27106172-9");
		personaDonante.setDomicilio("Av. Corrientes 1351, piso 2D");
		personaDonante.setEmail("victorialopez@outlook.com");
		personaDonante.setTelefono("+54 11-1022-3672");
		
		// Donante como persona
		personaDonante.setDni(27106172);
		personaDonante.setNombre("Victoria");
		personaDonante.setApellido("Lopez");
		personaDonante.setFechaNacimiento(LocalDate.of(1980, 1, 25));
		personaDonante = personaFisicaService.altaPersonaFisica(personaDonante);
		ContactoPayload contactoDonante = contactoService.getContactoById(personaDonante.getId());
		
		DonacionPayload donacion = new DonacionPayload();
		donacion.setId(null);
		donacion.setFecha(LocalDateTime.now().minusMonths(1));
		donacion.setDonante(contactoDonante);
		donacion.setTipoDonacion(DonacionTipo.DINERO);
		donacion.setDescripcion("$100.000 pesos");
		donacion.setValorAproximadoDeLaDonacion(BigDecimal.valueOf(100000.00));
		donacion = donacionService.altaDonacion(donacion);
		
		//Donacion anónima
		DonacionPayload donacion2 = new DonacionPayload();
		donacion2.setId(null);
		donacion2.setFecha(LocalDateTime.now().minusDays(1));
		donacion2.setDonante(null);
		donacion2.setTipoDonacion(DonacionTipo.INSUMO);
		donacion2.setDescripcion("Budín horneado caseras");
		donacion.setValorAproximadoDeLaDonacion(BigDecimal.valueOf(1500.00));
		donacion2 = donacionService.altaDonacion(donacion2);
	}
	
	public void copiarFotosDeTestHaciaDB() {
		fileStorageService.cargarFotosContactoEjemploHaciaDB();
		fileStorageService.moveTestFilesToMainFolder("producto");
		fileStorageService.moveTestFilesToMainFolder("actividad");
		fileStorageService.moveTestFilesToMainFolder("programaDeActividades");
	}
	
	public void generarCienBeneficiariosTenant2() {
		beneficiarioService.generar_100_Beneficiarios(2022);
	}
	
	public void generar30productosTenant2() {
		productoService.generar_30_productos();
	}
	
	public void cargarUsuariosDefault() {
		
		cargarUsuarioBasico("admin", RoleName.ROLE_ADMIN);
		cargarUsuarioBasico("employee", RoleName.ROLE_EMPLOYEE);
		cargarUsuarioBasico("profesional", RoleName.ROLE_PROFESIONAL);
		//cargarUsuarioBasico("user", RoleName.ROLE_USER);
		
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