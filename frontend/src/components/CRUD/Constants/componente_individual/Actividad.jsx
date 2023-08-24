import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';
import { FechaInicioFinInput, FechaRead } from './ConstantsInputGeneric';

import { ModalSeleccionarIntegrantesBeneficiario, ModalSeleccionarIntegrantesProfesional } from './Elegir_integrantes';

import BeneficiarioService from '../../../../services/BeneficiarioService';
import ProfesionalService from '../../../../services/ProfesionalService';

const DescripcionInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Descripción: </label>
            <Input disabled={disabled} placeholder="Descripción" name="descripcion" type="text" className="form-control" 
                value={data.descripcion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const DescripcionItemInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Descripción: </label>
            <input disabled={disabled} placeholder="Descripción" name="descripcion" type="text" className="form-control" 
                value={data.descripcion} onChange={handleInputChange}/>
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

const BeneficiariosInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesBeneficiario
                integrantesActuales = {data.beneficiarios}
                ServiceDeIntegrantes = {BeneficiarioService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"beneficiarios"}
                maxIntegrantesSelected = {null}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"beneficiario"}
                nombreTipoIntegrantePrural = {"beneficiarios"}
            />
        </div>
    );
};

const ProfesionalesInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesProfesional
                integrantesActuales = {data.profesionales}
                ServiceDeIntegrantes = {ProfesionalService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"profesionales"}
                maxIntegrantesSelected = {null}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"profesional"}
                nombreTipoIntegrantePrural = {"profesionales"}
            />
        </div>
    );
};


const cargarActividadDefault = {
    id: null,
    descripcion: "",
    fechaHoraDesde: null,
    fechaHoraDesde: null,
    beneficiarios: null,
    profesionales: null,
}

const ActividadCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaInicioFinInput 
                disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} 
                propertyFechaInicio="fechaHoraDesde" propertyFechaFin="fechaHoraHasta" 
                labelTextFechaInicio="Fecha/Hora de inicio" labelTextFechaFin="Fecha/Hora de finalización" 
                isHour={true}
            />
            <BeneficiariosInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ProfesionalesInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ActividadUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaInicioFinInput 
                        disabled={false} data={data} handleInputChange={handleInputChange} 
                        propertyFechaInicio="fechaHoraDesde" propertyFechaFin="fechaHoraHasta" 
                        labelTextFechaInicio="Fecha/Hora de inicio" labelTextFechaFin="Fecha/Hora de finalización" 
                        isHour={true}
                    />
                    <BeneficiariosInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ProfesionalesInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ActividadRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <DescripcionRead data={data} />
                    <FechaRead data={data} propertyFecha="fechaHoraDesde" labelText="Fecha/Hora de inicio"/>
                    <FechaRead data={data} propertyFecha="fechaHoraHasta" labelText="Fecha/Hora de finalización"/>
                    <BeneficiariosInput disabled={true} data={data} />
                    <ProfesionalesInput disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



const ActividadItemCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionItemInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaInicioFinInput 
                disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} 
                propertyFechaInicio="fechaHoraDesde" propertyFechaFin="fechaHoraHasta" 
                labelTextFechaInicio="Fecha/Hora de inicio" labelTextFechaFin="Fecha/Hora de finalización" 
                isHour={true}
            />
            <BeneficiariosInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ProfesionalesInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ActividadItemUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionItemInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaInicioFinInput 
                        disabled={false} data={data} handleInputChange={handleInputChange} 
                        propertyFechaInicio="fechaHoraDesde" propertyFechaFin="fechaHoraHasta" 
                        labelTextFechaInicio="Fecha/Hora de inicio" labelTextFechaFin="Fecha/Hora de finalización" 
                        isHour={true}
                    />
                    <BeneficiariosInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ProfesionalesInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}


export {
    //CRUD armado (lo que se va a usar)
    cargarActividadDefault,
    ActividadCreateInput,
    ActividadUpdateInput,
    ActividadRead,

    ActividadItemCreateInput,
    ActividadItemUpdateInput,
}