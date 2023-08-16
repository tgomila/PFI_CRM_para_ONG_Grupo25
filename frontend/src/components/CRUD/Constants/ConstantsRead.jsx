import React, { useEffect } from 'react'

//Este .js es para "Read" en gran parte.

const IdRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label htmlFor="id"> ID: <br/>{data.id}</label>
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForContactoRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Nombre/Descripción: <br/>{data.nombreDescripcion}</label>
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForPersonaRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Descripción: <br/>{data.nombreDescripcion}</label>
        </div>
    );
};

const CuitRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Cuit: <br/>{data.cuit}</label>
        </div>
    );
};

const DomicilioRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Domicilio:
                <br />
            {data.domicilio}</label>
        </div>
    );
};

const EmailRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Email: <br/>{data.email}</label>
        </div>
    );
};

const TelefonoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Telefono: <br/>{data.telefono}</label>
        </div>
    );
};

//Persona Jurídica
const InternoTelefonoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Interno telefono: <br/>{data.internoTelefono}</label>
        </div>
    );
};

const TipoPersonaJuridicaRead = ({ data }) => {
    let tipoPersonaJuridica = "";

    useEffect(() => {
        tipoPersonaJuridica = data.tipoPersonaJuridica ? data.tipoPersonaJuridica : "";
        if(tipoPersonaJuridica === "INSTITUCION") {
            tipoPersonaJuridica = "Institución";
        } else if(tipoPersonaJuridica === "EMPRESA") {
            tipoPersonaJuridica = "Empresa";
        } else if(tipoPersonaJuridica === "ORGANISMO_DEL_ESTADO") {
            tipoPersonaJuridica = "Organismo del estado";
        } else if(tipoPersonaJuridica === "OSC") {
            tipoPersonaJuridica = "OSC";
        } else {
            const formattedValue = tipoPersonaJuridica
                .toLowerCase()
                .replace(/_/g, " ")
                .replace(/\b\w/g, (match) => match.toUpperCase());
            
            tipoPersonaJuridica = formattedValue;
        }
    }, [data]);

    
    
    return(
        <div className = "form-group">
            <label> Tipo de persona juridica: <br/>{tipoPersonaJuridica}</label>
        </div>
    );
};

//Persona
const NombreRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nombre: <br/>{data.nombre}</label>
        </div>
    );
};

const ApellidoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Apellido: <br/>{data.apellido}</label>
        </div>
    );
};

const DniRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> DNI: <br/>{data.dni}</label>
        </div>
    );
};

const FechaDeNacimientoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de nacimiento: <br/>{data.fechaNacimiento}</label>
        </div>
    );
};

const EdadRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de nacimiento: <br/>{data.edad}</label>
        </div>
    );
};

//Beneficiario
const IdOngRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nro de ID original de ONG: <br/>{data.idONG}</label>
        </div>
    );
};

const LegajoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nro de legajo: <br/>{data.legajo}</label>
        </div>
    );
};

const LugarDeNacimientoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Lugar de nacimiento: <br/>{data.lugarDeNacimiento}</label>
        </div>
    );
};

const SeRetiraSoloRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Se retira solo: <br/>{data.seRetiraSolo ? "Si" : "No"}</label>
        </div>
    );
};

const CuidadosEspecialesRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Cuidados especiales: <br/>{data.cuidadosEspeciales}</label>
        </div>
    );
};

const EscuelaRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Escuela: <br/>{data.escuela}</label>
        </div>
    );
};

const GradoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Grado: <br/>{data.grado}</label>
        </div>
    );
};

const TurnoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Turno: <br/>{data.turno}</label>
        </div>
    );
};

//No creo que se use
const GenericStringRead = ({ stringLabel, data }) => {
    return(
        <div className = "form-group">
            <label> {stringLabel}: <br/>{data.turno}</label>
        </div>
    );
};

//Trabajador read
const DatosBancariosRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Datos bancarios: <br/>{data.datosBancarios}</label>
        </div>
    );
};

//Empleado
const FuncionEmpleadoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Función del empleado: <br/>{data.funcion}</label>
        </div>
    );
};

const DescripcionDelEmpleadoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Descripción de su función: <br/>{data.descripcion}</label>
        </div>
    );
};

//Profesional
const ProfesionRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Profesión: <br/>{data.profesion}</label>
        </div>
    );
};

//Colaborador
const AreaRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Área: <br/>{data.area}</label>
        </div>
    );
};

//ConsejoAdHonorem
const FuncionConsejoAdHonoremRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Función: <br/>{data.funcion}</label>
        </div>
    );
};

//Voluntari
//   no tiene dato extra



export {
    //Contacto
    IdRead,
    NombreDescripcionForContactoRead,
    NombreDescripcionForPersonaRead,
    CuitRead,
    DomicilioRead,
    EmailRead,
    TelefonoRead,
    //Persona Jurídica
    InternoTelefonoRead,
    TipoPersonaJuridicaRead,
    //Persona
    NombreRead,
    ApellidoRead,
    DniRead,
    FechaDeNacimientoRead,
    EdadRead,
    //Beneficiario
    IdOngRead,
    LegajoRead,
    LugarDeNacimientoRead,
    SeRetiraSoloRead,
    CuidadosEspecialesRead,
    EscuelaRead,
    GradoRead,
    TurnoRead,
    GenericStringRead,
    //Trabajador
    DatosBancariosRead,
    //Empleado
    FuncionEmpleadoRead,
    DescripcionDelEmpleadoRead,
    //Profesional
    ProfesionRead,
    //Colaborador
    AreaRead,
    //ConsejoAdHonorem
    FuncionConsejoAdHonoremRead
    //Voluntario
    //...
}