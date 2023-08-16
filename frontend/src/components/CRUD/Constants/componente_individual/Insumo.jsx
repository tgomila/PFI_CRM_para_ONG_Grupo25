import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

import 'react-datepicker/dist/react-datepicker.css';

const TipoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Tipo: </label>
            <Input disabled={disabled} placeholder="Tipo" name="tipo" type="text" className="form-control" 
                value={data.tipo} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const TipoRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Tipo:
                <br />
            {data.tipo}</label>
        </div>
    );
};

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

const StockActualInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Stock actual: </label>
            <Input disabled={disabled} placeholder="Stock actual" name="stockActual" type="number" className="form-control" 
                value={data.stockActual} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const StockActualRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Stock actual:
                <br />
            {data.stockActual}</label>
        </div>
    );
};

const FragilInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Fragil: </label>
            <select disabled={disabled} name="fragil" value={data.fragil} className="form-control" onChange={handleInputChange} validations={[required]}>
                <option value={true}>Si</option>
                <option value={false}>No</option>
            </select>
        </div>
    );
}

const FragilRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fragil: <br/>{data.fragil ? "Si" : "No"}</label>
        </div>
    );
};

const cargarInsumoDefault = {
    id: "",
    tipo: "",
    descripcion: "",
    stockActual: "",
    fragil: false,
}

const InsumoCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TipoInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <StockActualInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FragilInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const InsumoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <TipoInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <StockActualInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FragilInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const InsumoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <TipoRead data={data} />
                    <DescripcionRead data={data}/>
                    <StockActualRead data={data} />
                    <FragilRead data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarInsumoDefault,
    InsumoCreateInput,
    InsumoUpdateInput,
    InsumoRead,
}