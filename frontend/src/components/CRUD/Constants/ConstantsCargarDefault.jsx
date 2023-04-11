import { format, subYears } from 'date-fns';

const cargarContactoDefault = {
    id: "",
    nombreDescripcion: "",
    cuit: "",
    domicilio: "",
    email: "",
    telefono: ""
}

const cargarPersonaJuridicaDefault = {
    ...cargarContactoDefault,
    internoTelefono: ""
};

const cargarPersonaDefault = {
    ...cargarContactoDefault,
    dni: "",
    nombre: "",
    apellido: "",
    fechaNacimiento: format(subYears(new Date(), 18), 'yyyy-MM-dd')
};

//Sirve para Colaborador, Empleado y Profesional.
const cargarTrabajadorDefault = {
    ...cargarContactoDefault,
    datosBancarios: ""
};

const cargarBeneficiarioDefault = {
    ...cargarPersonaDefault,
    idONG: "",
    legajo: "",
    lugarDeNacimiento: "",
    seRetiraSolo: "",
    cuidadosEspeciales: "",
    escuela: "",
    grado: "",
    turno: ""
};



const cargarColaboradorDefault = {
    ...cargarTrabajadorDefault,
    area: ""
};

const cargarConsejoAdHonoremDefault = {
    ...cargarPersonaDefault,
    funcion: ""
};

const cargarEmpleadoDefault = {
    ...cargarTrabajadorDefault,
    funcion: "",
    descripcion: ""
};

const cargarProfesionalDefault = {
    ...cargarTrabajadorDefault,
    profesion: ""
};

const cargarVoluntarioDefault = {
    ...cargarPersonaDefault
};

export {
    cargarContactoDefault,
    cargarPersonaJuridicaDefault,
    cargarPersonaDefault,
    cargarBeneficiarioDefault,
    cargarColaboradorDefault,
    cargarConsejoAdHonoremDefault,
    cargarEmpleadoDefault,
    cargarProfesionalDefault,
    cargarVoluntarioDefault
}