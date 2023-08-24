import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';
import { FechaInicioFinInput, FechaRead } from './ConstantsInputGeneric';

import { ModalSeleccionarIntegrantesContacto } from './Elegir_integrantes';

import ContactoService from '../../../../services/ContactoService';
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

const CantidadInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Cantidad: </label>
            <Input disabled={disabled} placeholder="Cantidad" name="cantidad" type="number" className="form-control" 
                value={data.cantidad} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const CantidadRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Cantidad:
                <br />
            {data.cantidad}</label>
        </div>
    );
};

const HaSidoDevueltoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Ha sido devuelto: </label>
            <select disabled={disabled} name="haSidoDevuelto" value={data.haSidoDevuelto} className="form-control" onChange={handleInputChange} validations={[required]}>
                <option value={true}>Si</option>
                <option value={false}>No</option>
            </select>
        </div>
    );
}

const HaSidoDevueltoRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Ha sido devuelto: <br/>{data.haSidoDevuelto ? "Si" : "No"}</label>
        </div>
    );
};

const PrestamistaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <br/>
            <ModalSeleccionarIntegrantesContacto
                integrantesActuales = {data.prestamista}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"prestamista"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"prestamista"}
                nombreTipoIntegrantePrural = {"prestamistas"}
            />
        </div>
    );
};

const PrestatarioInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <br/>
            <ModalSeleccionarIntegrantesContacto
                integrantesActuales = {data.prestatario}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"prestatario"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"prestatario"}
                nombreTipoIntegrantePrural = {"prestatarios"}
            />
        </div>
    );
};


const cargarPrestamoDefault = {
    id: "",
    descripcion: "",
    cantidad: "",
    fechaPrestamoInicio: "",
    fechaPrestamoFin: "",
    haSidoDevuelto: false,
    prestamista: null,
    prestatario: null,
}

const PrestamoCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <CantidadInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            {/* <FechaPrestamoInicioInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaPrestamoFinInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} /> */}
            <FechaInicioFinInput 
                disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} 
                propertyFechaInicio="fechaPrestamoInicio" propertyFechaFin="fechaPrestamoFin" 
                labelTextFechaInicio="Fecha de inicio de préstamo" labelTextFechaFin="Fecha de fin de préstamo" 
                isHour={true}
            />
            <HaSidoDevueltoInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <PrestamistaInput disabled={false} data={data} handleInputChange={handleInputChange} />
            <PrestatarioInput disabled={false} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const PrestamoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <CantidadInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    {/* <FechaPrestamoInicioInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaPrestamoFinInput disabled={false} data={data} handleInputChange={handleInputChange} /> */}
                    <FechaInicioFinInput 
                        disabled={false} data={data} handleInputChange={handleInputChange} 
                        propertyFechaInicio="fechaPrestamoInicio" propertyFechaFin="fechaPrestamoFin" 
                        labelTextFechaInicio="Fecha de inicio de préstamo" labelTextFechaFin="Fecha de fin de préstamo" 
                        isHour={true}
                    />
                    <HaSidoDevueltoInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <PrestamistaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <PrestatarioInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const PrestamoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                <IdRead data={data} />
                <DescripcionRead data={data} />
                <CantidadRead data={data} />
                {/* <FechaPrestamoInicioRead data={data} />
                <FechaPrestamoFinRead data={data} /> */}
                <FechaRead data={data} propertyFecha="fechaPrestamoInicio" labelText="Fecha de inicio de préstamo"/>
                <FechaRead data={data} propertyFecha="fechaPrestamoFin" labelText="Fecha de fin de préstamo"/>
                <HaSidoDevueltoRead data={data} />
                <PrestamistaInput disabled={true} data={data} />
                <PrestatarioInput disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarPrestamoDefault,
    PrestamoCreateInput,
    PrestamoUpdateInput,
    PrestamoRead,
}