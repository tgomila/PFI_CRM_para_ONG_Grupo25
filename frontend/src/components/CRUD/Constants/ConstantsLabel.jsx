import React from 'react';
import Input from "react-validation/build/input";

//Este .js es para "Read" en gran parte.

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        ¡Este campo es obligatorio!
      </div>
    );
  }
};

const IngreseIdBuscar = ({idToSearch, onChangeIdToSearch }) => {
    return(
        <div className = "form-group">
            <label> Ingrese ID del contacto a buscar (anteriormente ya cargado): </label>
            <Input placeholder="Ingrese ID" id="idToSearch" name="idSearch" type="number" className="form-control" 
                value={idToSearch} onChange={onChangeIdToSearch} validations={[required]}/>
        </div>
    );
};

const IdLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label htmlFor="id"> ID: {data.id}</label>
        </div>
    );
};

const IdShowLabel = ({ show, data }) => {
    return(
        <div>
            {show && (
                <IdLabel data={data} />
            )}
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForContactoLabel = ({data}) => {
    return(
        <div className = "form-group">
            <label> Nombre/Descripción: {data.nombreDescripcion}</label>
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForPersonaLabel = ({data}) => {
    return(
        <div className = "form-group">
            <label> Descripción: {data.nombreDescripcion}</label>
        </div>
    );
};

const CuitLabel = ({data}) => {
    return(
        <div className = "form-group">
            <label> Cuit: {data.cuit}</label>
        </div>
    );
};

const DomicilioLabel = ({data}) => {
    return(
        <div className = "form-group">
            <label> Domicilio: {data.domicilio}</label>
        </div>
    );
};

const EmailLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Email: {data.email}</label>
        </div>
    );
};

const TelefonoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Telefono: {data.telefono}</label>
        </div>
    );
};

//Persona
const NombreLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nombre: {data.nombre}</label>
        </div>
    );
};

const ApellidoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Apellido: {data.apellido}</label>
        </div>
    );
};

const DniLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> DNI: {data.dni}</label>
        </div>
    );
};

const FechaDeNacimientoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de nacimiento: {data.fechaNacimiento}</label>
        </div>
    );
};

//Beneficiario
const IdOngLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nro de ID original de ONG: {data.idONG}</label>
        </div>
    );
};

const LegajoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Nro de legajo: {data.legajo}</label>
        </div>
    );
};

const LugarDeNacimientoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Lugar de nacimiento: {data.lugarDeNacimiento}</label>
        </div>
    );
};

const SeRetiraSoloLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Se retira solo: {data.seRetiraSolo ? "Si" : "No"}</label>
        </div>
    );
};

const CuidadosEspecialesLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Cuidados especiales: {data.cuidadosEspeciales}</label>
        </div>
    );
};

const EscuelaLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Escuela: {data.escuela}</label>
        </div>
    );
};

const GradoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Grado: {data.grado}</label>
        </div>
    );
};

const TurnoLabel = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Turno: {data.turno}</label>
        </div>
    );
};



export {
    //Otros
    required,
    //Contacto
    IngreseIdBuscar,
    IdLabel,
    IdShowLabel,
    NombreDescripcionForContactoLabel,
    NombreDescripcionForPersonaLabel,
    CuitLabel,
    DomicilioLabel,
    EmailLabel,
    TelefonoLabel,
    //Persona
    NombreLabel,
    ApellidoLabel,
    DniLabel,
    FechaDeNacimientoLabel,
    //Beneficiario
    IdOngLabel,
    LegajoLabel,
    LugarDeNacimientoLabel,
    SeRetiraSoloLabel,
    CuidadosEspecialesLabel,
    EscuelaLabel,
    GradoLabel,
    TurnoLabel
}