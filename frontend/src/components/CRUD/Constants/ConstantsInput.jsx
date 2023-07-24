import React, { useEffect, useState } from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";
import { format, subYears } from 'date-fns';

//Este js es para "Create" y "Update" en gran parte.

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        ¡Este campo es obligatorio!
      </div>
    );
  }
};

//Exclusivo de "create"
const IngreseIdAsociar = ({idToSearch, onChangeIdToSearch }) => {
    return(
        <div className = "form-group">
            <label> Ingrese ID del contacto a asociar (anteriormente ya cargado): </label>
            <Input placeholder="Ingrese ID" id="idToSearch" name="idSearch" type="number" className="form-control" 
                value={idToSearch} onChange={onChangeIdToSearch} validations={[required]}/>
        </div>
    );
};

//Exclusivo de "update"
const IngreseIdBuscar = ({idToSearch, onChangeIdToSearch }) => {
    return(
        <div className = "form-group">
            <label> Ingrese ID del contacto a buscar (anteriormente ya cargado): </label>
            <Input placeholder="Ingrese ID" id="idToSearch" name="idSearch" type="number" className="form-control" 
                value={idToSearch} onChange={onChangeIdToSearch} validations={[required]}/>
        </div>
    );
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

const IdShowInput = ({ show, data, handleInputChange }) => {
    return(
        <div>
            {data.id && show && (
                <IdInput data={data} handleInputChange={handleInputChange} />
            )}
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

//Persona jurídica
const InternoTelefonoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Interno de teléfono: </label>
            <Input disabled={disabled} placeholder="Interno de teléfono" id="internoTelefono" name="internoTelefono" type="text" className="form-control" 
                value={data.internoTelefono} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

//Un quilombo para hacer que Select sea select (minusculas)
const TipoPersonaJuridicaInput = ({ disabled, data, handleInputChange }) => {
    const [selectedOption, setSelectedOption] = useState(data.tipoPersonaJuridica);

    const handleChange = (event) => {
        console.log("Antes selected option: " + selectedOption);
        const newValue = event.target.value;
        setSelectedOption(newValue);
        handleInputChange(event);
        console.log("Después selected option: " + selectedOption);
        console.log("Entre aquí 1.5: " + newValue);
    };
    
    return(
        <div className = "form-group">
            <label> Tipo de persona jurídica: </label>
            <select 
            disabled={disabled} 
            name="tipoPersonaJuridica" 
            value={selectedOption} 
            className="form-control" 
            onChange={handleChange}
            //validations={[required]}
            >
                <option value="">Seleccione</option>
                <option value="OSC">OSC</option>
                <option value="EMPRESA">Empresa</option>
                <option value="INSTITUCION">Institución</option>
                <option value="ORGANISMO_DEL_ESTADO">Organismo del estado</option>
            </select>
        </div>
    );
};

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

//Beneficiario
const IdOngInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Nro de ID original de ONG: </label>
            <Input disabled={disabled} placeholder="Nro de ID original de ONG" id="idONG" name="idONG" type="number" className="form-control" 
                value={data.idONG} onChange={handleInputChange}/>
        </div>
    );
};

const LegajoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Nro de legajo: </label>
            <Input disabled={disabled} placeholder="Legajo" id="legajo" name="legajo" type="number" className="form-control" 
                value={data.legajo} onChange={handleInputChange}/>
        </div>
    );
};

const LugarDeNacimientoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Lugar de nacimiento: </label>
            <Input disabled={disabled} placeholder="Lugar de nacimiento" name="lugarDeNacimiento" type="text" className="form-control" 
                value={data.lugarDeNacimiento} onChange={handleInputChange}/>
        </div>
    );
};

const SeRetiraSoloInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Se retira solo: </label>
            {/*{console.log("ID: " + data.id)}
            {console.log("Se retira solo: " + data.seRetiraSolo)}
            {console.log(data)}
            */}
            <Select disabled={disabled} name="seRetiraSolo" value={data.seRetiraSolo ? "true" : "false"} className="form-control" onChange={handleInputChange} validations={[required]}>
                <option value="">Seleccione</option>
                <option value="true">Si</option>
                <option value="false">No</option>
            </Select>
        </div>
    );
};

const CuidadosEspecialesInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Cuidados especiales: </label>
            <Input disabled={disabled} placeholder="Cuidados especiales" name="cuidadosEspeciales" type="text" className="form-control" 
                value={data.cuidadosEspeciales} onChange={handleInputChange}/>
        </div>
    );
};

const EscuelaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Escuela: </label>
            <Input disabled={disabled} placeholder="Escuela" name="escuela" type="text" className="form-control" 
                value={data.escuela} onChange={handleInputChange}/>
        </div>
    );
};

const GradoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Grado: </label>
            <Input disabled={disabled} placeholder="Grado" name="grado" type="text" className="form-control" 
                value={data.grado} onChange={handleInputChange}/>
        </div>
    );
};

const TurnoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Turno: </label>
            <Input disabled={disabled} placeholder="Turno" name="turno" type="text" className="form-control" 
                value={data.turno} onChange={handleInputChange}/>
        </div>
    );
};

//Trabajador
const DatosBancariosInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Datos bancarios: </label>
            <Input disabled={disabled} placeholder="Datos bancarios" name="datosBancarios" type="text" className="form-control" 
                value={data.datosBancarios} onChange={handleInputChange}/>
        </div>
    );
};

//Empleado
const FuncionInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Función: </label>
            <Input disabled={disabled} placeholder="Función" name="funcion" type="text" className="form-control" 
                value={data.funcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const DescripcionEmpleadoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Descripción de su función: </label>
            <Input disabled={disabled} placeholder="Descripción de su función" name="descripcion" type="text" className="form-control" 
                value={data.descripcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

//Profesional
const ProfesionInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Profesión: </label>
            <Input disabled={disabled} placeholder="Profesión" name="profesion" type="text" className="form-control" 
                value={data.profesion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

//Colaborador
const AreaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Área: </label>
            <Input disabled={disabled} placeholder="Área" name="area" type="text" className="form-control" 
                value={data.area} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

//Consejo ad honorem
const FuncionConsejoAdHonoremInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Función: </label>
            <Input disabled={disabled} placeholder="Función" name="funcion" type="text" className="form-control" 
                value={data.funcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};



export {
    //Otros
    required,
    //Contacto
    IngreseIdAsociar,
    IngreseIdBuscar,
    IdInput,
    IdShowInput,
    NombreDescripcionForContactoInput,
    NombreDescripcionForPersonaInput,
    CuitInput,
    DomicilioInput,
    EmailInput,
    TelefonoInput,
    //PersonaJuridica
    InternoTelefonoInput,
    TipoPersonaJuridicaInput,
    //Persona
    NombreInput,
    ApellidoInput,
    DniInput,
    FechaDeNacimientoInput,
    //Beneficiario
    IdOngInput,
    LegajoInput,
    LugarDeNacimientoInput,
    SeRetiraSoloInput,
    CuidadosEspecialesInput,
    EscuelaInput,
    GradoInput,
    TurnoInput,
    //Trabajador
    DatosBancariosInput,
    //Empleado
    FuncionInput,
    DescripcionEmpleadoInput,
    //Profesional
    ProfesionInput,
    //Colaborador
    AreaInput,
    //Consejo ad honorem
    FuncionConsejoAdHonoremInput,
    //Voluntario
    //...no tiene items independientes.
}