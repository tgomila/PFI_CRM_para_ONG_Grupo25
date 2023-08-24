import React from 'react'
import Input from "react-validation/build/input";

import { required, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';
import { FechaRead } from './ConstantsInputGeneric';

import { ModalSeleccionarIntegrantesActividades } from './Agregar_item';

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

const ModalActividades = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesActividades
                integrantesActuales = {data.actividades}
                //ServiceDeIntegrantes = {ActividadService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"actividades"}
                maxIntegrantesSelected = {null}
                isEditable = {!disabled}
                el_la = {"la"}
                nombreTipoIntegrante = {"actividad"}
                nombreTipoIntegrantePrural = {"actividades"}
            />
        </div>
    );
};


const cargarProgramaDeActividadesDefault = {
    id: null,
    descripcion: "",
    fechaDesde: null,
    fechaHasta: null,
    actividades: null,
}

const ProgramaDeActividadesCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ModalActividades disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ProgramaDeActividadesUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ModalActividades disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ProgramaDeActividadesRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <DescripcionRead data={data} />
                    <FechaRead data={data} propertyFecha="fechaDesde" labelText="Fecha desde"/>
                    <FechaRead data={data} propertyFecha="fechaHasta" labelText="Fecha hasta"/>
                    <ModalActividades disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarProgramaDeActividadesDefault,
    ProgramaDeActividadesCreateInput,
    ProgramaDeActividadesUpdateInput,
    ProgramaDeActividadesRead,
}