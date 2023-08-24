import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";

import DatePicker from 'react-datepicker';
import { format } from 'date-fns';
import es from 'date-fns/locale/es';
import 'react-datepicker/dist/react-datepicker.css';
import { required } from '../ConstantsInput';

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

const FechaInicioFinInput = ({disabled, data, handleInputChange, propertyFechaInicio, propertyFechaFin, labelTextFechaInicio, labelTextFechaFin, isHour=false}) => {
    return(
        <>
            <FechaInput
                disabled = {disabled}
                data = {data}
                handleInputChange = {handleInputChange}
                propertyFecha = {propertyFechaInicio}
                labelText = {labelTextFechaInicio}
                isHour={isHour}
            />
            <FechaFinInput
                disabled = {disabled}
                data = {data}
                handleInputChange = {handleInputChange}
                propertyFechaInicio = {propertyFechaInicio}
                propertyFechaFin = {propertyFechaFin}
                labelTextFechaFin = {labelTextFechaFin}
                isHour={isHour}
            />
        </>
    )
}

const FechaInput = ({ disabled, data, handleInputChange, propertyFecha="fecha", labelText="Fecha", isHour=false }) => {
    const commonProps = {
      disabled: disabled,
      selected: data[propertyFecha] ? new Date(data[propertyFecha]) : null,
      onChange: (date) => {
        handleInputChange({
          target: {
            name: propertyFecha,
            //value: date ? date.toISOString() : '',//Este me dió problema, 00hs me lo enviaba al server como 03am (por algún horario buenos aires a UTC)
            value: date ? format(date, 'yyyy-MM-dd\'T\'HH:mm:ss') : '',
          },
        })
      },
      className: "form-control",
      minDate: null,
      maxDate: new Date(3000, 0, 1),
      placeholderText: "Selecciona una fecha",
      autoComplete: "off",
      locale: es,
    };
  
    const datePickerProps = isHour === true
      ? {
          ...commonProps,
          showTimeSelect: true,
          timeFormat: "HH:mm",
          timeIntervals: 15,
          timeCaption: "Hora",
          dateFormat: "yyyy-MM-dd HH:mm",
          minDate: null,//data[propertyFecha] ? new Date(data[propertyFecha]) : new Date(),
          placeholderText: "Selecciona fecha y hora",
        }
      : {
          ...commonProps,
          dateFormat: "yyyy-MM-dd",
        };
  
    return (
      <div className="form-group">
        <label> {labelText}: </label>
        <br/>
        <DatePicker {...datePickerProps} />
      </div>
    );
};

const FechaRead = ({ data, labelText="Fecha", propertyFecha }) => {
    let formattedDate = data[propertyFecha];

    if (typeof data[propertyFecha] === 'string' && data[propertyFecha].includes('T')) {
        formattedDate = format(new Date(data[propertyFecha]), 'dd/MM/yyyy HH:mm');
    } else {
        formattedDate = format(new Date(data[propertyFecha]), 'dd/MM/yyyy');
    }

    return(
        <div className="form-group">
            <label> {labelText}: <br/>{formattedDate}</label>
        </div>
    );
};

const FechaFinInput = ({ disabled, data, handleInputChange, propertyFechaInicio="fechaInicio", propertyFechaFin="fechaFin", labelTextFechaFin="Fecha de inicio", isHour=false }) => {
    const [minDate, setMinDate] = useState(data[propertyFechaInicio]);
    const [showWarning, setShowWarning] = useState(false);
    const [fechaNoValida, setFechaNoValida] = useState(null);

    useEffect(() => {
        if ((data[propertyFechaInicio] && data[propertyFechaFin] ) && (data[propertyFechaInicio] > data[propertyFechaFin])) {
            setFechaNoValida(data[propertyFechaFin]);
            setShowWarning(true);
            console.log("Red");
            handleInputChange({//Esto es si quiero forzar a cambiar la fecha
                target: {
                name: propertyFechaFin,
                value: data[propertyFechaInicio],
                },
            });
        } else {
            if(!showWarning){
                //Si hay showWarning es para avisar que el sistema ha cambiado la fecha.
                //Cuando hay !showWarning (en este caso), es para quitar el aviso de cambio de fecha
                setFechaNoValida(null);
            }
            setMinDate(data[propertyFechaInicio]);
            setShowWarning(false);
            console.log("Blue");
        }
    }, [data[propertyFechaInicio], data[propertyFechaFin], handleInputChange]);

    return (
      <div className="form-group">
        <FechaInput
            disabled = {disabled}
            data = {data}
            handleInputChange = {handleInputChange}
            propertyFecha = {propertyFechaFin}
            labelText = {labelTextFechaFin}
            isHour={isHour}
        />
        {fechaNoValida && (
            <div style={{ color: 'red' }}>
                No puede existir una fecha de finalización anterior a una fecha inicio.<br />
                Se ha cambiado la fecha de finalización {format(new Date(fechaNoValida), isHour ? 'dd/MM/yyyy HH:mm' : 'dd/MM/yyyy')} por
                 la misma fecha inicial {format(new Date(data[propertyFechaFin]), isHour ? 'dd/MM/yyyy HH:mm' : 'dd/MM/yyyy')}.
            </div>
        )}
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



export {
    FechaInicioFinInput,
    FechaInput,
    FechaRead,
}