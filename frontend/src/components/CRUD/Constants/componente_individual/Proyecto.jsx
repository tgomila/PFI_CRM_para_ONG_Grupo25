import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import DatePicker from 'react-datepicker';
import { format } from 'date-fns';
import 'react-datepicker/dist/react-datepicker.css';
import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';
import { FechaInicioFinInput, FechaRead } from './ConstantsInputGeneric';

import { ModalSeleccionarIntegrantesPersona } from './Elegir_integrantes';

import PersonaService from '../../../../services/PersonaService';

const DescripcionInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Descripción: </label>
            <Input disabled={disabled} placeholder="Descripción" name="descripcion" type="text" className="form-control" 
                value={data.descripcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const DescripcionRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Descripción:
                <br />
            {data.descripcion}</label>
        </div>
    );
};

const InvolucradosInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesPersona
                integrantesActuales = {data.involucrados}
                ServiceDeIntegrantes = {PersonaService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"involucrados"}
                maxIntegrantesSelected = {null}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"involucrado"}
                nombreTipoIntegrantePrural = {"involucrados"}
            />
        </div>
    );
};

const cargarProyectoDefault = {
    id: "",
    descripcion: "",
    fechaInicio: "",
    fechaFin: "",
    involucrados: null//{}
}

const ProyectoCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            {/* <FechaInicioInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaFinInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} /> */}
            <FechaInicioFinInput 
                disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} 
                propertyFechaInicio="fechaInicio" propertyFechaFin="fechaFin" 
                labelTextFechaInicio="Fecha de inicio" labelTextFechaFin="Fecha de finalización" 
                isHour={true}
            />
            <InvolucradosInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ProyectoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    {/* <FechaInicioInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaFinInput disabled={false} data={data} handleInputChange={handleInputChange} /> */}
                    <FechaInicioFinInput 
                        disabled={false} data={data} handleInputChange={handleInputChange} 
                        propertyFechaInicio="fechaInicio" propertyFechaFin="fechaFin" 
                        labelTextFechaInicio="Fecha de inicio" labelTextFechaFin="Fecha de finalización" 
                        isHour={true}
                    />
                    <InvolucradosInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ProyectoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <DescripcionRead data={data} />
                    <FechaRead data={data} propertyFecha="fechaInicio" labelText="Fecha de inicio"/>
                    <FechaRead data={data} propertyFecha="fechaFin" labelText="Fecha de finalización"/>
                    {/* <FechaInicioRead data={data} />
                    <FechaFinRead data={data} /> */}
                    <InvolucradosInput disabled={true} data={data}/>
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarProyectoDefault,
    ProyectoCreateInput,
    ProyectoUpdateInput,
    ProyectoRead,
}