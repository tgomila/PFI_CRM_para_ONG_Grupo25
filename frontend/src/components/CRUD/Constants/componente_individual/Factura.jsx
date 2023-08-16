import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

import { ModalSeleccionarIntegrantesContacto, ModalSeleccionarIntegrantes } from './Elegir_integrantes';

import ContactoService from '../../../../services/ContactoService';
import PersonaService from '../../../../services/PersonaService';

const FechaInput = ({ disabled, data, handleInputChange }) => {
    return (
      <div className="form-group">
        <label> Fecha: </label>
        <br/>
        <DatePicker
          disabled={disabled}
          selected={data.fecha ? new Date(data.fecha) : null}
          onChange={(date) =>
            handleInputChange({
              target: {
                name: 'fecha',
                value: date ? date.toISOString() : '',
              },
            })
          }
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={15}
          timeCaption="Hora"
          dateFormat="yyyy-MM-dd HH:mm"
          className="form-control"
          minDate={new Date()}
          maxDate={new Date(3000, 0, 1)}
          placeholderText="Selecciona una fecha y hora"
          autoComplete="off"
        />
      </div>
    );
};

const FechaRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha: <br/>{data.fecha}</label>
        </div>
    );
};

const ClienteInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <br/>
            <ModalSeleccionarIntegrantesContacto
                integrantesActuales = {data.cliente}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"cliente"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"cliente"}
                nombreTipoIntegrantePrural = {"clientes"}
            />
        </div>
    );
};

const EmisorFacturaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <br/>
            <ModalSeleccionarIntegrantesContacto
                integrantesActuales = {data.emisorFactura}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"emisorFactura"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"emisor de factura"}
                nombreTipoIntegrantePrural = {"emisores de factura"}
            />
        </div>
    );
};


const cargarFacturaDefault = {
    id: "",
    fecha: "",
    cliente: null,
    emisorFactura: null,
}

const FacturaCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ClienteInput disabled={false} data={data} handleInputChange={handleInputChange} />
            <EmisorFacturaInput disabled={false} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const FacturaUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <FechaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ClienteInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <EmisorFacturaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const FacturaRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <FechaRead data={data} />
                    <ClienteInput disabled={true} data={data} />
                    <EmisorFacturaInput disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarFacturaDefault,
    FacturaCreateInput,
    FacturaUpdateInput,
    FacturaRead,
}