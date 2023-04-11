import React, { useState, useRef } from 'react';
import Input from "react-validation/build/input";
import { format, subYears } from 'date-fns';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        ¡Este campo es obligatorio!
      </div>
    );
  }
};

const IdInput = ({ data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label htmlFor="id"> ID: </label>
            <Input disabled="disabled" placeholder="ID" id="id" name="id" type="number" className="form-control" 
            value={data.id} onChange={handleInputChange}/>
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForContactoInput = ({data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Nombre/Descripción: </label>
            <Input placeholder="Nombre/Descripción" name="nombreDescripcion" type="text" className="form-control" 
                value={data.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

//Cambia el nombre del label nomás
const NombreDescripcionForPersonaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Descripción: </label>
            <Input disabled={disabled} placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                value={data.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const CuitInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Cuit: </label>
            <Input disabled={disabled} placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                value={data.cuit} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const DomicilioInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Domicilio: </label>
            <Input disabled={disabled} placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                value={data.domicilio} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const EmailInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Email: </label>
            <Input disabled={disabled} placeholder="Email" id="email" name="email" type="text" className="form-control" 
                value={data.email} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const TelefonoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Telefono: </label>
            <Input disabled={disabled} placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                value={data.telefono} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const ContactoInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            {contactoSearchEncontrado && (
                <IdInput data={data} handleInputChange={handleInputChange} />
            )}
            <NombreDescripcionForContactoInput disabled="false" data={data} handleInputChange={handleInputChange} />
            <CuitInput disabled="false" data={data} handleInputChange={handleInputChange} />
            <DomicilioInput disabled="false" data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled="false" data={data} handleInputChange={handleInputChange} />
            <TelefonoInput disabled="false" data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

//Persona
const NombreInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Nombre: </label>
            <Input disabled={disabled} placeholder="Nombre" id="nombre" name="nombre" type="text" className="form-control" 
                value={data.nombre} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const ApellidoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Apellido: </label>
            <Input disabled={disabled} placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                value={data.apellido} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const DniInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> DNI: </label>
            <Input disabled={disabled} placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                value={data.dni} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const FechaDeNacimientoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Fecha de nacimiento: </label>
            <Input disabled={disabled} placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                value={data.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 0), 'yyyy-MM-dd')}/>
        </div>
    );
};


const PersonaInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            {contactoSearchEncontrado && (
                <IdInput data={data} handleInputChange={handleInputChange} />
            )}
            <NombreInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <ApellidoInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <DniInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <FechaDeNacimientoInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <CuitInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DomicilioInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TelefonoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NombreDescripcionForPersonaInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}



export {
    ContactoInput,
    PersonaInput,

    IdInput,
    NombreDescripcionForContactoInput,
    NombreDescripcionForPersonaInput,
    CuitInput,
    DomicilioInput,
    EmailInput,
    TelefonoInput,
}