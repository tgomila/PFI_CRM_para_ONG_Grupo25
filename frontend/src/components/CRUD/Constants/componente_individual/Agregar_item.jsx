import React, { forwardRef, useEffect, useState, useRef, useMemo } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";

import FacturaService from "../../../../services/FacturaService";
import ActividadService from "../../../../services/ActividadService";

import { TablaGenericaPersona, TablaGenericaConFoto } from "../../../vista_grafico_tabla/tables/Tabla_Generica";

import { RenderMostrarContacto } from "../../../vista_grafico_tabla/tables/Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"../../../vista_grafico_tabla/tables/Tabla_Filters";

import { columnsFacturaItems } from "../../../vista_grafico_tabla/tables/TablaFactura";
import { CreateFacturaItemComponent } from "../../Create/CreateFacturaComponent";

import { columnsActividad } from "../../../vista_grafico_tabla/tables/TablaActividad";
import CreateActividadItemComponent from "../../Create/CreateActividadComponent";

//Leer estos:
//   El más completo:
//   https://codesandbox.io/s/react-table-selected-row-data-with-filters-e79c2?file=/src/App.js:8138-8159
//
//   https://codesandbox.io/s/tannerlinsley-react-table-kitchen-sink-gw0ih?file=/src/App.js:8220-8249

/**
 * @description
 * Se utilizará para ver ver o agregar/editar integrantes de un CRUD de tipo dato, como una actividad.
 * 
 * @param {Object/Array} integrantesActuales - puede ser null, 1 o varios integrantes
 * @param {Array} columnsIn - Columnas de la tabla.cto) para modificar los integrantes (ejemplo proveedor).
 * @param {Object} CreateItemComponent - Es si podes crear items (ejemplo CreateActividadComponent para programa de actividades).
 * @param {string} tipoDatoParaFoto - ejemplo ("contacto").
 * @param {string} nombreTipoDatoParaModuloVisibilidad - ejemplo ("CONTACTO").
 * @param {Object} ServiceDeIntegrantes - Objeto para obtener datos mediante Axios.
 * @param {Object} handleInputChange - Objeto padre (ejemplo producto) para modificar los integrantes (ejemplo proveedor).
 * @param {string} nombreHandleInputChange - Nombre del integrante en json, ejemplo ("beneficiarios").
 * @param {number} maxIntegrantesSelected - (nulo || <0) = infinitos integrantes, >=1 es número "finito" de integrantes.
 * @param {boolean} isEditable - true/false si aparece el botón para agregar o cambiar integrantes.
 * @param {string} el_la - Género del tipo de integrante.
 * @param {string} nombreTipoIntegrante - Nombre del tipo del integrante (ejemplo "actividad").
 * @param {string} nombreTipoIntegrantePrural - Nombre del tipo del integrante en prural (ejemplo "actividades").
 * @returns {JSX.Element} - Componente de la tabla de integrantes.
 */
const ModalSeleccionarIntegrantes = ({integrantesActuales, columnsIn, CreateItemComponent, tipoDatoParaFoto="contacto", nombreTipoDatoParaModuloVisibilidad="CONTACTO", ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const [showModalCrearItem, setShowModalCrearItem] = useState(false);
  const [integrantesActualesAux, setIntegrantesActualesAux] = useState(null);
  const [isEditableAux, setIsEditableAux] = useState(false);
  const [item, setItem] = useState(null);//este es para casos como agregar item factural o item actividad.

  useEffect(() => {
    setIsEditableAux(isEditable ? true : false);
  }, []);

  useEffect(() => {
    if(item){
      console.log("Me dio de alta el item");
      console.log(item);
      console.log("integrantesActualesAux");
      console.log(integrantesActualesAux);
      console.log("integrantesActuales");
      console.log(integrantesActuales);
      if (integrantesActuales) {
        //setIntegrantesActualesAux([...integrantesActualesAux, item]);
        handleInputChange({ target: { name: nombreHandleInputChange, value: [...integrantesActuales, item]} });
      } else {
        //setIntegrantesActualesAux([item]);
        handleInputChange({ target: { name: nombreHandleInputChange, value: [item]} });
      }
      setItem(null);
      setShowModalCrearItem(false);
      setIntegrantesActualesAux(integrantesActuales);
      console.log("Ahora es null y cerré modal");
      console.log(item);
      console.log("integrantesActualesAux");
      console.log(integrantesActualesAux);
      console.log("integrantesActuales");
      console.log(integrantesActuales);
      forzarRender();//Fue hecho para forzar actualizar la tabla
    }
  }, [item]);

  const handleOpenModalCrearItem = () => {
    setShowModalCrearItem(true);
  };

  const handleCloseModalCrearItem = () => {
    setShowModalCrearItem(false);
  };

  const columns = columnsIn ? columnsIn : [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre/Descripción",
      accessor: "nombreDescripcion",
      type: "string",
    },
  ];

  const [forzarRenderizado, setForzarRenderizado ] = useState(false);
  const forzarRender = () => {
    setForzarRenderizado(true);
    setTimeout(() => {
      setForzarRenderizado(false);
    }, 100);
  }
  
  return(
    <div className = "form-group">
      <label> {integrantesActuales
        ? nombreTipoIntegrantePrural.charAt(0).toUpperCase() + nombreTipoIntegrantePrural.slice(1) + ":"
        : "No hay " + nombreTipoIntegrante + " asociad"+ (el_la === "el" ? "o" : "a")}
      </label>
      <br/>
      {integrantesActuales && !forzarRenderizado && (
        <div style={{ overflowY: 'auto' }}>
          <TablaGenericaConFoto
            columnsIn={columns}
            Service={ServiceDeIntegrantes}
            dataIn={integrantesActuales}
            visibilidadInput={"SIN_BOTONES"}
            nombreTipoDatoParaModuloVisibilidad={nombreTipoDatoParaModuloVisibilidad}
            tipoDatoParaFoto={tipoDatoParaFoto}
            el_la={"el"}
            nombreTipoDato={"integrante"}
            style={{ overflowY: 'auto' }}
          />
        </div>
      )}

      {CreateItemComponent && isEditableAux && (
        <button type="button" className="btn btn-primary" onClick={handleOpenModalCrearItem}>
          <span style={{ display: 'flex', alignItems: 'center' }}>
            Agregar {nombreTipoIntegrante}
          </span>
        </button>
      )}

      <Modal show={showModalCrearItem} onHide={handleCloseModalCrearItem} size="xl">
        <Modal.Header>
          <Modal.Title> Agregar {nombreTipoIntegrante} </Modal.Title>
          <button className="close" onClick={handleCloseModalCrearItem}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body>
          <div style={{ overflowY: 'auto' }}>
            {CreateItemComponent && (
              <CreateItemComponent
                isPantallaCompleta = {false}
                setAgregarItem={setItem}
              />
            )}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModalCrearItem}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>

  );
};

const ModalSeleccionarIntegrantesFacturaItem = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la="el", nombreTipoIntegrante="item", nombreTipoIntegrantePrural="items"}) => {
  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columnsFacturaItems}
        CreateItemComponent = { CreateFacturaItemComponent }
        tipoDatoParaFoto={null}
        nombreTipoDatoParaModuloVisibilidad={"FACTURA"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : FacturaService}
        handleInputChange = {handleInputChange}
        nombreHandleInputChange = {nombreHandleInputChange}
        maxIntegrantesSelected = {maxIntegrantesSelected}
        isEditable = {isEditable}
        el_la = {el_la}
        nombreTipoIntegrante = {nombreTipoIntegrante}
        nombreTipoIntegrantePrural = {nombreTipoIntegrantePrural}
    />
  );
}

const ModalSeleccionarIntegrantesActividades = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la="el", nombreTipoIntegrante="item", nombreTipoIntegrantePrural="items"}) => {
  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columnsActividad}
        CreateItemComponent = { CreateActividadItemComponent }
        tipoDatoParaFoto={null}
        nombreTipoDatoParaModuloVisibilidad={"ACTIVIDAD"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : ActividadService}
        handleInputChange = {handleInputChange}
        nombreHandleInputChange = {nombreHandleInputChange}
        maxIntegrantesSelected = {maxIntegrantesSelected}
        isEditable = {isEditable}
        el_la = {el_la}
        nombreTipoIntegrante = {nombreTipoIntegrante}
        nombreTipoIntegrantePrural = {nombreTipoIntegrantePrural}
    />
  );
}


export {
  ModalSeleccionarIntegrantes,
  
  //actividad //Programa de actividades
  ModalSeleccionarIntegrantesFacturaItem,
  ModalSeleccionarIntegrantesActividades,
}
