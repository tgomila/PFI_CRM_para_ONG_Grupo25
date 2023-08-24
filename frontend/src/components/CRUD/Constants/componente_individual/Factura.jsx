import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';
import { FechaInput, FechaRead } from './ConstantsInputGeneric';

import { ModalSeleccionarIntegrantesContacto } from './Elegir_integrantes';
import { ModalSeleccionarIntegrantesFacturaItem } from './Agregar_item';

import ContactoService from '../../../../services/ContactoService';
import PersonaService from '../../../../services/PersonaService';

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

const ModalFacturaItem = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesFacturaItem
                integrantesActuales = {data.itemsFactura}
                //ServiceDeIntegrantes = {FacturaService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"itemsFactura"}
                maxIntegrantesSelected = {null}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"item de factura"}
                nombreTipoIntegrantePrural = {"items de factura"}
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
            <FechaInput
                disabled={searchEncontrado}
                data={data}
                handleInputChange={handleInputChange}
                propertyFecha={"fecha"}
                labelText={"Fecha"}
                isHour={true}
            />
            <ClienteInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmisorFacturaInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ModalFacturaItem disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const FacturaUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <FechaInput
                        disabled={false}
                        data={data}
                        handleInputChange={handleInputChange}
                        propertyFecha={"fecha"}
                        labelText={"Fecha"}
                        isHour={true}
                    />
                    <ClienteInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <EmisorFacturaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ModalFacturaItem disabled={false} data={data} handleInputChange={handleInputChange} />
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
                    <FechaRead data={data} propertyFecha="fecha" labelText="Fecha"/>
                    <ClienteInput disabled={true} data={data} />
                    <EmisorFacturaInput disabled={true} data={data} />
                    <ModalFacturaItem disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}

const cargarFacturaItemDefault = {
    id: "",
    descripcion: "",
    unidades: 1,
    precioUnitario: 0.00,
    precio: 0.00,
}

const DescripcionInput = ({ disabled, data, handleInputChange }) => {
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

const UnidadesInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Unidades: </label>
            <input disabled={disabled} placeholder="Unidades" name="unidades" type="number" className="form-control" 
                value={data.unidades} onChange={handleInputChange}/>
        </div>
    );
};

const UnidadesRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Unidades:
                <br />
            {data.unidades}</label>
        </div>
    );
};

const PrecioInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Precio: </label>
            <input disabled={disabled} placeholder="(solo número y punto, ej: 2100.50)" name="precio" type="number" className="form-control" 
                value={data.precio} onChange={handleInputChange}/>
        </div>
    );
};

const PrecioRead = ({data}) => {
    return(
        <div className = "form-group">
            <label>
                Precio:
                <br />
                {parseFloat(data.precio).toLocaleString("es-AR", {
                    style: "currency",
                    currency: "ARS",
                    minimumFractionDigits: 2,
                })}
            </label>
        </div>
    );
};

const PrecioUnitarioInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Precio unitario: </label>
            <input disabled={disabled} placeholder="(solo número y punto, ej: 2100.50)" name="precioUnitario" type="number" className="form-control" 
                value={data.precioUnitario} onChange={handleInputChange}/>
        </div>
    );
};

const PrecioUnitarioRead = ({data}) => {
    return(
        <div className = "form-group">
            <label>
                Precio unitario:
                <br />
                {parseFloat(data.precioUnitario).toLocaleString("es-AR", {
                    style: "currency",
                    currency: "ARS",
                    minimumFractionDigits: 2,
                })}
            </label>
        </div>
    );
};

const FacturaItemCreateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={true} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput show={true} data={data} handleInputChange={handleInputChange} />
            <UnidadesInput show={true} data={data} handleInputChange={handleInputChange} />
            <PrecioUnitarioInput show={true} data={data} handleInputChange={handleInputChange} />
            <PrecioInput show={true} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const FacturaItemUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput show={false} data={data} handleInputChange={handleInputChange} />
                    <UnidadesInput show={false} data={data} handleInputChange={handleInputChange} />
                    <PrecioUnitarioInput show={false} data={data} handleInputChange={handleInputChange} />
                    <PrecioInput show={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const FacturaItemRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <DescripcionInput disabled={true} data={data} />
                    <UnidadesInput disabled={true} data={data} />
                    <PrecioUnitarioInput disabled={true} data={data} />
                    <PrecioInput disabled={true} data={data} />
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

    cargarFacturaItemDefault,
    FacturaItemCreateInput,
    FacturaItemUpdateInput,
    FacturaItemRead,
}