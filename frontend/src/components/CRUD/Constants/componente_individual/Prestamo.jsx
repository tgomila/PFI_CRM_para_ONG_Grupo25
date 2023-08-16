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

const FechaPrestamoInicioInput = ({ disabled, data, handleInputChange }) => {
    return (
      <div className="form-group">
        <label> Fecha de inicio de préstamo: </label>
        <br/>
        <DatePicker
          disabled={disabled}
          selected={data.fechaPrestamoInicio ? new Date(data.fechaPrestamoInicio) : null}
          onChange={(date) =>
            handleInputChange({
              target: {
                name: 'fechaPrestamoInicio',
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

const FechaPrestamoInicioRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de inicio de préstamo: <br/>{data.fechaPrestamoInicio}</label>
        </div>
    );
};

const FechaPrestamoFinInput = ({ disabled, data, handleInputChange }) => {
    return (
      <div className="form-group">
        <label> Fecha de fin de préstamo: </label>
        <br/>
        <DatePicker
          disabled={disabled}
          selected={data.fechaPrestamoFin ? new Date(data.fechaPrestamoFin) : null}
          onChange={(date) =>
            handleInputChange({
              target: {
                name: 'fechaPrestamoFin',
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
          minDate={data.fechaPrestamoFin ? data.fechaPrestamoFin : new Date()}
          maxDate={new Date(3000, 0, 1)}
          placeholderText="Selecciona una fecha y hora"
          autoComplete="off"
        />
      </div>
    );
};

const FechaPrestamoFinRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fecha de fin de préstamo: <br/>{data.fechaPrestamoFin}</label>
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
            <FechaPrestamoInicioInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaPrestamoFinInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
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
                    <FechaPrestamoInicioInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FechaPrestamoFinInput disabled={false} data={data} handleInputChange={handleInputChange} />
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
                <FechaPrestamoInicioRead data={data} />
                <FechaPrestamoFinRead data={data} />
                <HaSidoDevueltoRead data={data} />
                <PrestamistaInput disabled={true} data={data} />
                <PrestatarioInput disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //Create, Update
    IdInput,
    IdShowInput,
    DescripcionInput,
    CantidadInput,
    FechaPrestamoInicioInput,
    FechaPrestamoFinInput,
    HaSidoDevueltoInput,
    PrestamistaInput,
    PrestatarioInput,
    
    //Read
    IdRead,
    DescripcionRead,
    CantidadRead,
    FechaPrestamoInicioRead,
    FechaPrestamoFinRead,
    HaSidoDevueltoRead,
    //PrestamistaRead,
    //PrestatarioRead,

    //CRUD armado (lo que se va a usar)
    cargarPrestamoDefault,
    PrestamoCreateInput,
    PrestamoUpdateInput,
    PrestamoRead,
}