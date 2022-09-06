package com.pfi.crm;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.TipoPersonaJuridica;
import com.pfi.crm.multitenant.tenant.payload.*;
import com.pfi.crm.multitenant.tenant.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.service.BeneficiarioService;
import com.pfi.crm.multitenant.tenant.service.ColaboradorService;
import com.pfi.crm.multitenant.tenant.service.ConsejoAdHonoremService;
import com.pfi.crm.multitenant.tenant.service.EmpleadoService;
import com.pfi.crm.multitenant.tenant.service.PersonaJuridicaService;
import com.pfi.crm.multitenant.tenant.service.ProfesionalService;
import com.pfi.crm.multitenant.tenant.service.VoluntarioService;
import com.pfi.crm.payload.request.SignUpRequest;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	
	/*@Autowired
	private AuthController AuthController;*/
	
  /**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		
		// here your code ...
		cargarTenant1();
		cargarTenant2();
		cargarTenant3();
		
		return;
	}
	
	public void cargarTenant1() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant1");
		Optional<Role> rol = roleRepository.findByName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			roleRepository.save(new Role(RoleName.ROLE_USER));
			roleRepository.save(new Role(RoleName.ROLE_ADMIN));
			roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
			roleRepository.save(new Role(RoleName.ROLE_MODERATOR));

			cargarBeneficiariosTenant1();
			cargarVoluntariosTenant1();
			cargarProfesionalesTenant1();
			cargarEmpleadosTenant1();
			cargarColaboradorTenant1();
			cargarConsejoAdHonoremTenant1();
			cargarPersonaJuridicaTenant1();

			// cargarUsuariosTenant1();
		}
	}
	
	public void cargarTenant2() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant2");
		Optional<Role> rol = roleRepository.findByName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			roleRepository.save(new Role(RoleName.ROLE_USER));
			roleRepository.save(new Role(RoleName.ROLE_ADMIN));
			roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
			roleRepository.save(new Role(RoleName.ROLE_MODERATOR));

			cargarBeneficiariosTenant2();
			cargarVoluntariosTenant2();
			cargarProfesionalesTenant2();
			cargarEmpleadosTenant2();
			cargarColaboradorTenant2();
			cargarConsejoAdHonoremTenant2();
			cargarPersonaJuridicaTenant2();

			// cargarUsuariosTenant1();
		}
	}
	
	public void cargarTenant3() {
		// Setear base de datos o schema
		DBContextHolder.setCurrentDb("tenant3");
		Optional<Role> rol = roleRepository.findByName(RoleName.ROLE_USER);
		if (!rol.isPresent()) {
			roleRepository.save(new Role(RoleName.ROLE_USER));
			roleRepository.save(new Role(RoleName.ROLE_ADMIN));
			roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
			roleRepository.save(new Role(RoleName.ROLE_MODERATOR));
		}
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
		m.setDomicilio("Uruguar 782, piso 1, depto B");
		m.setEmail("carlagomez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
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
		m.setNombreDescripcion("Piba");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Sancez reta 382");
		m.setEmail("cecilialopez@gmail.com");
		m.setTelefono("9516-6545");

		// PersonaFisica
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
		m.setNombre("Esteban");
		m.setApellido("Quito");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
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
		m.setNombre("Ricardo");
		m.setApellido("Frachia");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
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
		m.setNombre("Julieta");
		m.setApellido("Veraniegos");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
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
		m.setNombre("Liliana");
		m.setApellido("Feracios");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
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
		m.setNombre("Gabriel");
		m.setApellido("Luchetti");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Empleado
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
		m.setNombre("Nahuel");
		m.setApellido("Vacca");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
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
		m.setNombre("Zara");
		m.setApellido("Gimenez");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
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
		m.setNombre("Micaela");
		m.setApellido("Gimenez");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
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
		m.setNombre("Trinidad");
		m.setApellido("Cabellos");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
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
		m.setNombre("Susana");
		m.setApellido("Dora");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
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
		
		SignUpRequest s = new SignUpRequest();
		s.setName("Federico del tenant 1");
		s.setUsername("fulano1");
		s.setEmail("fulano1@gmail.com");
		s.setPassword(passwordEncoder.encode("123456"));
		
		
		//AuthController.registerUser(s);
	}
	
	
	
	
	//Tenant 2
	public void cargarBeneficiariosTenant2() {
		BeneficiarioPayload m = new BeneficiarioPayload();

		// Contacto
		m.setNombreDescripcion("Piba tenant 2");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 8, depto B");
		m.setEmail("felicitastron@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
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
		m.setDomicilio("Uruguar 782, piso 1, depto B");
		m.setEmail("carlagomez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
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
		VoluntarioPayload m = new VoluntarioPayload();

		// Contacto
		m.setNombreDescripcion("Voluntario muy duro tenant 2");
		m.setCuit("20-25745349-9");
		m.setDomicilio("Dominguez 457, piso 3, depto B");
		m.setEmail("martinlopez@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
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
		m.setNombre("Eugenia");
		m.setApellido("Varela");
		m.setFechaNacimiento(LocalDate.of(1984, 2, 15));

		// Profesional
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
		m.setNombre("Alejandro");
		m.setApellido("Gris");
		m.setFechaNacimiento(LocalDate.of(1980, 1, 20));

		// Profesional
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
		m.setNombre("Fernanda");
		m.setApellido("Arevalo");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
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
		m.setNombre("Susana");
		m.setApellido("Miento");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setProfesion("Medica");
		// Fin Profesional
		profesionalService.altaProfesional(m);	
	}
	
	public void cargarEmpleadosTenant2() {
		EmpleadoPayload m = new EmpleadoPayload();

		// Contacto
		m.setNombreDescripcion("Empleada");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Gallo 7923, piso 4, depto A");
		m.setEmail("gabrielluchetti@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Florencia");
		m.setApellido("Altamirano");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Empleado
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
		m.setNombre("Emilio");
		m.setApellido("Paz");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
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
		m.setNombre("Patricia");
		m.setApellido("Castro");
		m.setFechaNacimiento(LocalDate.of(1985, 3, 17));

		// Empleado
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
		m.setNombre("Josefina");
		m.setApellido("Marruecos");
		m.setFechaNacimiento(LocalDate.of(1982, 6, 24));

		// Colaborador
		m.setArea("Area de diversiomes");
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
		m.setNombre("Karina");
		m.setApellido("Rekini");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
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
		m.setNombre("Jorge");
		m.setApellido("Gomez");
		m.setFechaNacimiento(LocalDate.of(1987, 6, 24));

		// Colaborador
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
		
		SignUpRequest s = new SignUpRequest();
		s.setName("Federico del tenant 2");
		s.setUsername("fulano2");
		s.setEmail("fulano2@gmail.com");
		s.setPassword(passwordEncoder.encode("123456"));
		
		
		//AuthController.registerUser(s);
	}
	
}