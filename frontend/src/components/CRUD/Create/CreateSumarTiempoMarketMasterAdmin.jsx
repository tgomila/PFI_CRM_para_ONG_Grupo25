import React, { useEffect, useState } from 'react'
import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import MasterTenantAdminService from '../../../services/MasterTenantAdminService';
import { ModalSeleccionarTenant } from "../Constants/componente_individual/Elegir_integrantes";
import { FechaInput } from "../Constants/componente_individual/ConstantsInputGeneric";
import { required } from '../Constants/ConstantsInput';

/**
 * @description
 * Se utilizará para ver ver o agregar/editar integrantes de un CRUD de tipo dato, como una actividad.
 * 
 * @param {boolean} isPantallaCompleta - true si es create clásico, false si es un Modal
 * @param {Object} setAgregarItem - un set clásico que si se agrega item, entonces es un modal
 * @returns {JSX.Element} - Componente de la tabla de integrantes.
 */
const CreateSumarTiempoMarketMasterAdmin = ({isPantallaCompleta=true, }) => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarModificarMasterTenantMarketDefault}
            DatoUpdateInput = {PostCreateInput}
            Service = {MasterTenantAdminService}
            methodToCallInput = {'postSumarTiempoMarket'}
            urlTablaDato = {'/master_tenant/market'}
            isPantallaCompleta = {isPantallaCompleta}//Modal agregar item o creacte clasico
            el_la = {'la'}
            nombreTipoDato = {'creación de sumatoria de tiempos a marketplace de tenants'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

const cargarModificarMasterTenantMarketDefault = {
    tenantId: null,
    modulo: "",
    quitarPruebaUtilizadaATenant: false,
    fechaMaximaSuscripcion: null,
    numeroTiempoASumar: null,
    diaMesAnio: "",
}

//Bugs
const TenantsInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            Cuidado, sin seleccionar ONG's significa, que desea realizar la acción en todas las ONG's
            <ModalSeleccionarTenant
                integrantesActuales = {data.tenants}
                handleInputChange = {handleInputChange}
                isEditable = {true}
                maxIntegrantesSelected = {1}
            />
        </div>
    );
};
const TenantIDInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Número de tenant ID: </label>
            <input disabled={disabled} placeholder="Número de tenant ID" name="tenantId" type="number" className="form-control" 
                value={data.unidades} onChange={handleInputChange}/>
        </div>
    );
};

const ModuloSelect = ({ disabled, data, handleInputChange }) => {
    const [selectedOption, setSelectedOption] = useState(data.modulo);

    const handleChange = (event) => {
        console.log("Antes selected option: " + selectedOption);
        const newValue = event.target.value;
        setSelectedOption(newValue);
        handleInputChange(event);
        console.log("Después selected option: " + selectedOption);
        console.log("Entre aquí 1.5: " + newValue);
    };
    
    return(
        <div className = "form-group">
            <label> Modulo: </label>
            <select 
            disabled={disabled} 
            name="modulo" 
            value={selectedOption} 
            className="form-control" 
            onChange={handleChange}
            //validations={[required]}
            >
                <option value="">Todos</option>
                <option value="ACTIVIDAD">Actividad</option>
                <option value="FACTURA">Factura</option>
                <option value="INSUMO">Insumo</option>
                <option value="PRESTAMO">Prestamo</option>
                <option value="PROYECTO">Proyecto</option>
                <option value="CHAT">Chat</option>
            </select>
        </div>
    );
};

const QuitarPruebaUtilizadaATenant = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Quitar prueba utilizada a tenant: </label>
            <select disabled={disabled} name="quitarPruebaUtilizadaATenant" value={data.quitarPruebaUtilizadaATenant} className="form-control" onChange={handleInputChange} validations={[required]}>
                <option value={false}>No</option>
                <option value={true}>Si</option>
            </select>
        </div>
    );
}

const NumeroTiempoASumar = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Número de tiempo a sumar: </label>
            <input disabled={disabled} placeholder="Número de tiempo a sumar" name="numeroTiempoASumar" type="number" className="form-control" 
                value={data.unidades} onChange={handleInputChange}/>
        </div>
    );
};

const DiaMesAnio = ({ disabled, data, handleInputChange }) => {
    const [selectedOption, setSelectedOption] = useState(data.diaMesAnio);

    const handleChange = (event) => {
        console.log("Antes selected option: " + selectedOption);
        const newValue = event.target.value;
        setSelectedOption(newValue);
        handleInputChange(event);
        console.log("Después selected option: " + selectedOption);
        console.log("Entre aquí 1.5: " + newValue);
    };
    
    return(
        <div className = "form-group">
            <label> Tipo de fecha: </label>
            <select 
            disabled={disabled} 
            name="diaMesAnio" 
            value={selectedOption} 
            className="form-control" 
            onChange={handleChange}
            //validations={[required]}
            >
                <option value="">Seleccione</option>
                <option value="DIA">Día</option>
                <option value="MES">Mes</option>
                <option value="ANIO">Año</option>
            </select>
        </div>
    );
};

const PostCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    const today = new Date();
    const maxDate = new Date(today.getFullYear() + 2, today.getMonth(), today.getDate());
    return(
        <div>
            <h1>Sumatoria de fechas</h1>
            <TenantIDInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ModuloSelect disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <br/>
            Si desea quitar la prueba utilizada, solo dele click a si, pero no es necesario rellenar los otros datos siguientes
            <QuitarPruebaUtilizadaATenant disabled={""} data={data} handleInputChange={handleInputChange} />
            <br/>
            Si desea setear una fecha, solo agregue una fecha posterior a hoy, e inferior a 2 años. El resto no es necesario rellenar dado que no se realizará dicha acción
            <FechaInput
                disabled={searchEncontrado}
                data={data}
                handleInputChange={handleInputChange}
                propertyFecha={"fechaMaximaSuscripcion"}
                labelText={"Fecha máxima de suscripción"}
                isHour={true}
                minDateInput={today}
                maxDateInput={maxDate}
            />
            <br/>
            Si desea sumar tiempo, asegure que no tenga seteado una fecha, y haya seleccionado "no" a quitar pruebas.
            <NumeroTiempoASumar disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DiaMesAnio disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

export default CreateSumarTiempoMarketMasterAdmin;