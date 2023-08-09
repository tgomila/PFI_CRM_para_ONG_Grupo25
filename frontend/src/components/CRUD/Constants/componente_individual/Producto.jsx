import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

//import { ModalSeleccionarIntegrantes } from '../../../vista_grafico_tabla/tables/Tabla_Variables';
import { ModalSeleccionarIntegrantesContacto, ModalSeleccionarIntegrantes } from './Elegir_integrantes';

import { RenderMostrarContacto } from '../../../vista_grafico_tabla/tables/Tabla_Variables';
import ContactoService from '../../../../services/ContactoService';

//Cambia el nombre del label nomás
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

const PrecioVentaInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Precio venta: </label>
            <Input disabled={disabled} placeholder="(solo número y punto, ej: 2100.50)" name="precioVenta" type="number" className="form-control" 
                value={data.precioVenta} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const PrecioVentaRead = ({data}) => {
    return(
        <div className = "form-group">
            <label>
                Precio venta:
                <br />
                {parseFloat(data.precioVenta).toLocaleString("es-AR", {
                    style: "currency",
                    currency: "ARS",
                    minimumFractionDigits: 2,
                })}
            </label>
        </div>
    );
};

const CantFijaCompraInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Cantidad fija de compra: </label>
            <Input disabled={disabled} placeholder="Cantidad fija de compra" name="cantFijaCompra" type="number" className="form-control" 
                value={data.cantFijaCompra} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const CantFijaCompraRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Cantidad fija de compra:
                <br />
            {data.cantFijaCompra}</label>
        </div>
    );
};

const CantMinimaStockInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Cantidad mínima de stock: </label>
            <Input disabled={disabled} placeholder="Cantidad mínima de stock" name="cantMinimaStock" type="number" className="form-control" 
                value={data.cantMinimaStock} onChange={handleInputChange} validations={[required]}/>
        </div>
    );
};

const CantMinimaStockRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Cantidad mínima de stock:
                <br />
            {data.cantMinimaStock}</label>
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

const ProveedorInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            {/* <label> {data.proveedor ? "Proveedor:" : "No hay proveedor asociado"}</label>
            {data.proveedor ? RenderMostrarContacto(data.proveedor, "contacto", "") : ""} */}
            <br/>
            <ModalSeleccionarIntegrantes
                integrantesActuales = {data.proveedor}
                ServiceDeIntegrantes = {ContactoService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"proveedor"}
                maxIntegrantesSelected = {1}
                isEditable = {true}
                el_la = {"el"}
                nombreTipoIntegrante = {"proveedor"}
                nombreTipoIntegrantePrural = {"proveedores"}
            />
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

// const FragilInput = ({ disabled, data, handleInputChange }) => {
//     const [selectedOption, setSelectedOption] = useState("");
//     const booleanToText = (value) => {
//         console.log("value");
//         console.log(value);
//         if(typeof value === "boolean"){
//             setSelectedOption(data.fragil ? "true" : "false");
//             console.log("boolean: " + data.fragil ? "true" : "false");
//         } else {
//             setSelectedOption(data.fragil);//es texto
//             console.log("text: " + data.fragil);
//         }
//     }
//     useEffect(() => {//transformo boolean a string, total springboot me lo convierte string a boolean.
//         booleanToText(data.fragil);
//     }, []);

    

//     const handleChange = (event) => {
//         console.log("Antes selected option: " + selectedOption);
//         const newValue = event.target.value;
//         setSelectedOption(newValue);
//         handleInputChange(event);
//         console.log("Después selected option: " + selectedOption);
//         console.log("Entre aquí 1.5: " + newValue);
//     };

//     return(
//         <div className = "form-group">
//             <label> Fragil: </label>
//             <select disabled={disabled} name="fragil" value={selectedOption} className="form-control" onChange={handleChange} validations={[required]}>
//                 <option value="">Seleccione</option>
//                 <option value={"true"}>Si</option>
//                 <option value={"false"}>No</option>
//             </select>
//         </div>
//     );
// };

// const FragilInput = ({ disabled, data, handleInputChange }) => {
//     const [selectedValue, setSelectedValue] = useState(data.fragil === true ? "true" : "false");
  
//     useEffect(() => {
//       setSelectedValue(data.fragil === true ? "true" : "false");
//     }, [data.fragil]);
  
//     const handleSelectChange = (event) => {
//       const value = event.target.value;
//       setSelectedValue(value);
//       handleInputChange({
//         target: {
//           name: "fragil",
//           value: value === "true" ? true : false,
//         },
//       });
//     };
  
//     return (
//       <div className="form-group">
//         <label> Fragil: </label>
//         <select
//           disabled={disabled}
//           name="fragil"
//           value={selectedValue}
//           className="form-control"
//           onChange={handleSelectChange}
//           validations={[required]}
//         >
//           <option value="">Seleccione</option>
//           <option value="true">Si</option>
//           <option value="false">No</option>
//         </select>
//       </div>
//     );
//   };

const FragilRead = ({ data }) => {
    return(
        <div className = "form-group">
            <label> Fragil: <br/>{data.fragil ? "Si" : "No"}</label>
        </div>
    );
};

const cargarProductoDefault = {
    id: "",
    tipo: "",
    descripcion: "",
    precioVenta: "",
    cantFijaCompra: "",
    cantMinimaStock: "",
    stockActual: "",
    fragil: false,
    proveedor: null//{}
}

const ProductoCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TipoInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DescripcionInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <PrecioVentaInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <CantFijaCompraInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <CantMinimaStockInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <StockActualInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <ProveedorInput disabled={false} data={data} handleInputChange={handleInputChange} />
            <FragilInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ProductoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <TipoInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <DescripcionInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <PrecioVentaInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <CantFijaCompraInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <CantMinimaStockInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <StockActualInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <ProveedorInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <FragilInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ProductoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <TipoRead data={data} />
                    <DescripcionRead data={data} />
                    <PrecioVentaRead data={data} />
                    <CantFijaCompraRead data={data} />
                    <CantMinimaStockRead data={data} />
                    <StockActualRead data={data} />
                    <FragilRead data={data} />
                </div>
            )}
        </div>
    );
}



export {
    //Create, Update
    IdInput,
    IdShowInput,
    TipoInput,
    DescripcionInput,
    PrecioVentaInput,
    CantFijaCompraInput,
    CantMinimaStockInput,
    StockActualInput,
    FragilInput,
    
    //Read
    IdRead,
    TipoRead,
    DescripcionRead,
    PrecioVentaRead,
    CantFijaCompraRead,
    CantMinimaStockRead,
    StockActualRead,
    FragilRead,

    //CRUD armado (lo que se va a usar)
    cargarProductoDefault,
    ProductoCreateInput,
    ProductoUpdateInput,
    ProductoRead,
}