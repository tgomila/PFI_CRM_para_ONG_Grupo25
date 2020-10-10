package com.pfi.crm;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pfi.crm.controller.AuthController;
import com.pfi.crm.model.Colaborador;
import com.pfi.crm.model.ConsejoAdHonorem;
import com.pfi.crm.model.Empleado;
import com.pfi.crm.model.PersonaJuridica;
import com.pfi.crm.model.Role;
import com.pfi.crm.model.RoleName;
import com.pfi.crm.model.TipoPersonaJuridica;
import com.pfi.crm.payload.*;
import com.pfi.crm.payload.request.SignUpRequest;
import com.pfi.crm.repository.RoleRepository;
import com.pfi.crm.service.BeneficiarioService;
import com.pfi.crm.service.ColaboradorService;
import com.pfi.crm.service.ConsejoAdHonoremService;
import com.pfi.crm.service.EmpleadoService;
import com.pfi.crm.service.PersonaJuridicaService;
import com.pfi.crm.service.ProfesionalService;
import com.pfi.crm.service.VoluntarioService;

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
	
	/*@Autowired
	private AuthController AuthController;*/
	
  /**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    // here your code ...
	  Optional<Role> rol = roleRepository.findByName(RoleName.ROLE_USER);
	  if(!rol.isPresent()) {
		  roleRepository.save(new Role(RoleName.ROLE_USER));
		  roleRepository.save(new Role(RoleName.ROLE_ADMIN));
		  roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
		  roleRepository.save(new Role(RoleName.ROLE_MODERATOR));
		  
		  
		  
		  
		  cargarBeneficiarios();
		  cargarVoluntarios();
		  cargarProfesionales();
		  cargarEmpleados();
		  cargarColaborador();
		  cargarConsejoAdHonorem();
		  cargarPersonaJuridica();
		  
		  //cargarUsuarios();
	  }
	  
    return;
  }
  
  
  
  
  
  
	public void cargarBeneficiarios() {
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
	
	public void cargarVoluntarios() {
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
	
	
	
	
	
	public void cargarProfesionales() {
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
	
	public void cargarEmpleados() {
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
	
	public void cargarColaborador() {
		
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
	
	
	
	
	public void cargarConsejoAdHonorem() {
		
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
	
	public void cargarPersonaJuridica() {
		
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
	
	public void cargarUsuarios() {
		
		SignUpRequest s = new SignUpRequest();
		s.setName("fulano");
		s.setUsername("fulano");
		s.setEmail("fulano@gmail.com");
		s.setPassword("123456");
		
		
		//AuthController.registerUser(s);
	}
	
}