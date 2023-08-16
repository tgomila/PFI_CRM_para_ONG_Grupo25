import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

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

const FechaInicioInput = ({ disabled, data, handleInputChange }) => {
    return (
      <div className="form-group">
        <label> Fecha de inicio: </label>
        <br/>
        <DatePicker
          disabled={disabled}
          selected={data.fechaInicio ? new Date(data.fechaInicio) : null}
          onChange={(date) =>
            handleInputChange({
              target: {
                name: 'fechaInicio',
                value: date ? date.toISOString() : '',
              },
            })
          }
          dateFormat="yyyy-MM-dd"
          className="form-control"
          minDate={new Date()}
          maxDate={new Date(3000, 0, 1)}
          placeholderText="Selecciona una fecha"
          autoComplete="off"
        />
      </div>
    );
};

const FechaInicioRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de inicio: <br/>{data.fechaInicio}</label>
        </div>
    );
};

const FechaFinInput = ({ disabled, data, handleInputChange }) => {
    return (
      <div className="form-group">
        <label> Fecha de finalización: </label>
        <br/>
        <DatePicker
          disabled={disabled}
          selected={data.fechaFin ? new Date(data.fechaFin) : null}
          onChange={(date) =>
            handleInputChange({
              target: {
                name: 'fechaFin',
                value: date ? date.toISOString() : '',
              },
            })
          }
          dateFormat="yyyy-MM-dd"
          className="form-control"
          minDate={new Date()}
          maxDate={new Date(3000, 0, 1)}
          placeholderText="Selecciona una fecha"
          autoComplete="off"
        />
      </div>
    );
};

const FechaFinRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de finalización: <br/>{data.fechaFin}</label>
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
            <FechaInicioInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaFinInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
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
                    <FechaInicioInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaFinInput disabled={false} data={data} handleInputChange={handleInputChange} />
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
                    <FechaInicioRead data={data} />
                    <FechaFinRead data={data} />
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