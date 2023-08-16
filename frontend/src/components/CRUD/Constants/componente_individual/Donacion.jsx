import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

import { ModalSeleccionarIntegrantesContacto } from './Elegir_integrantes';

import ContactoService from '../../../../services/ContactoService';

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

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
          minDate={data.fecha ? data.fecha : new Date()}
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

const DonanteInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <br/>
            <ModalSeleccionarIntegrantesContacto
                integrantesActuales = {data.donante}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"donante"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"el"}
                nombreTipoIntegrante = {"donante"}
                nombreTipoIntegrantePrural = {"donantes"}
            />
        </div>
    );
};

const TipoDonacionInput = ({ disabled, data, handleInputChange }) => {
    const [selectedOption, setSelectedOption] = useState(data.tipoDonacion);

    const handleChange = (event) => {
        const newValue = event.target.value;
        setSelectedOption(newValue);
        handleInputChange(event);
    };
    
    return(
        <div className = "form-group">
            <label> Tipo de donación: </label>
            <select 
            disabled={disabled} 
            name="tipoDonacion" 
            value={selectedOption} 
            className="form-control" 
            onChange={handleChange}
            //validations={[required]}
            >
                <option value="">Seleccione</option>
                <option value="INSUMO">Insumo</option>
                <option value="DINERO">Dinero</option>
                <option value="SERVICIO">Servicio</option>
                <option value="OTRO">Otro</option>
            </select>
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

const ValorAproximadoDeLaDonacionInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Valor aproximado de la donación: </label>
            <Input disabled={disabled} placeholder="(solo número y punto, ej: 2100.50)" name="valorAproximadoDeLaDonacion" type="number" className="form-control" 
                value={data.valorAproximadoDeLaDonacion} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const ValorAproximadoDeLaDonacionRead = ({data}) => {
    return(
        <div className = "form-group">
            <label>
                Valor aproximado de la donación:
                <br />
                {parseFloat(data.valorAproximadoDeLaDonacion).toLocaleString("es-AR", {
                    style: "currency",
                    currency: "ARS",
                    minimumFractionDigits: 2,
                })}
            </label>
        </div>
    );
};

const cargarDonacionDefault = {
    id: "",
    fecha: "",
    donante: null,
    tipoDonacion: "",
    descripcion: "",
    valorAproximadoDeLaDonacion: "",
}

const DonacionCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <FechaInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DonanteInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TipoDonacionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ValorAproximadoDeLaDonacionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const DonacionUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <FechaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <DonanteInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <TipoDonacionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ValorAproximadoDeLaDonacionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const DonacionRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <FechaRead data={data} />
                    <DonanteInput disabled={true} data={data}/>
                    <TipoDonacionInput disabled={true} data={data} />
                    <DescripcionRead data={data} />
                    <ValorAproximadoDeLaDonacionRead data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //CRUD armado (lo que se va a usar)
    cargarDonacionDefault,
    DonacionCreateInput,
    DonacionUpdateInput,
    DonacionRead,
}