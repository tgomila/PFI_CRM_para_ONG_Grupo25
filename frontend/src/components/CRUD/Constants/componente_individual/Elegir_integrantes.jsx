import React, { forwardRef, useEffect, useState, useRef, useMemo } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";

import ContactoService from "../../../../services/ContactoService";
import PersonaService from "../../../../services/PersonaService";
import BeneficiarioService from "../../../../services/BeneficiarioService";
import ProfesionalService from "../../../../services/ProfesionalService";

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
  const [showModalSeleccionar, setShowModalSeleccionar] = useState(false);
  const [showModalCrearItem, setShowModalCrearItem] = useState(false);
  const [integrantesActualesAux, setIntegrantesActualesAux] = useState(null);
  const [candidatosAIntegrantes, setCandidatosAIntegrantes] = useState(null);
  const [isEditableAux, setIsEditableAux] = useState(false);
  const [item, setItem] = useState(null);//este es para casos como agregar item factural o item actividad.

  useEffect(() => {
    setIsEditableAux(isEditable ? true : false);
  }, []);

  useEffect(() => {
    if(showModalSeleccionar && !candidatosAIntegrantes){
      ServiceDeIntegrantes.getAll().then((res) => {
        setCandidatosAIntegrantes(res.data);
      });
    }
    setIntegrantesActualesAux(integrantesActuales);
    if(!showModalSeleccionar){
      forzarRender();//Fue hecho para forzar actualizar la tabla
    }
  }, [showModalSeleccionar]);

  const [numeroDeIntegrantes, setNumeroDeIntegrantes] = useState(0);
  useEffect(() => {
    
    if (Array.isArray(integrantesActuales)) {//Es lista
      setNumeroDeIntegrantes(integrantesActuales.length);
      integrantesActuales.forEach((integranteActual) => {
      });
    } else if (integrantesActuales) {//Es 1 objeto
      setNumeroDeIntegrantes(1);
    } else {//Esta vacío
      setNumeroDeIntegrantes(0);
    }

    if(!showModalSeleccionar){
      setIntegrantesActualesAux(integrantesActuales);
    }
  }, [integrantesActuales])

  const selectedRows = useMemo(() => {
    if (candidatosAIntegrantes) {
      const selectedRowIds = {};
      if (Array.isArray(integrantesActuales)) {
        integrantesActuales.forEach((integranteActual) => {
          const rowIndex = candidatosAIntegrantes.findIndex((integrante) => integrante.id === integranteActual.id);
          if (rowIndex >= 0) {
            selectedRowIds[rowIndex] = true;
          }
        });
      } else {
        const rowIndex = candidatosAIntegrantes.findIndex((integrante) => integrante.id === integrantesActuales?.id);
        if (rowIndex >= 0) {
          selectedRowIds[rowIndex] = true;
        }
      }
      return selectedRowIds;
    }
    return {};
  }, [integrantesActuales, candidatosAIntegrantes]);

  const handleOpenModalSeleccionar = () => {
    setShowModalSeleccionar(true);
  };

  const handleCloseModalSeleccionar = () => {
    setShowModalSeleccionar(false);
  };

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

  const integrAux = [
    {
        "id": 2,
        "nombreDescripcion": "Piba tenant 2"
    },
    {
        "id": 3,
        "nombreDescripcion": "Pibe tenant 2"
    }
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
      <label> {integrantesActuales ? 
        ((maxIntegrantesSelected && maxIntegrantesSelected === 1)
          ? nombreTipoIntegrante.charAt(0).toUpperCase() + nombreTipoIntegrante.slice(1)
          : nombreTipoIntegrantePrural.charAt(0).toUpperCase() + nombreTipoIntegrantePrural.slice(1)
        ) + ":" : "No hay " + nombreTipoIntegrante + " asociad"+ (el_la === "el" ? "o" : "a")}
      </label>
      {maxIntegrantesSelected === 1 && RenderMostrarContacto(integrantesActualesAux, tipoDatoParaFoto)}
      {integrantesActuales && !forzarRenderizado && (!maxIntegrantesSelected || maxIntegrantesSelected > 1) && (
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
      {/*integrantesActuales && !forzarRenderizado && maxIntegrantesSelected === 1 && (
        <div>
          {RenderMostrarContacto(integrantesActualesAux, tipoDatoParaFoto, "")}
        </div>
      )*/}
      <br/>

      {isEditableAux && (
        <button type="button" className="btn btn-primary" onClick={handleOpenModalSeleccionar}>
          <span style={{ display: 'flex', alignItems: 'center' }}>
          {integrantesActuales ? "Cambiar" : "Agregar"} {maxIntegrantesSelected === 1 ? nombreTipoIntegrante : nombreTipoIntegrantePrural}
          </span>
        </button>
      )}

      <Modal show={showModalSeleccionar} onHide={handleCloseModalSeleccionar} size="xl">
        <Modal.Header>
          <Modal.Title>Seleccione {maxIntegrantesSelected >= 1 && maxIntegrantesSelected + " "}{maxIntegrantesSelected === 1 ? nombreTipoIntegrante : nombreTipoIntegrantePrural}</Modal.Title>
          <button className="close" onClick={handleCloseModalSeleccionar}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body>
          <div>
            {!candidatosAIntegrantes ? (
              <div>
                <span className="spinner-border spinner-border-sm"></span>
                <p>Cargando datos de {nombreTipoIntegrantePrural}...</p>
              </div>
            ) : (
              <div style={{ overflowY: 'auto' }}>
                {(maxIntegrantesSelected >= 1) && (numeroDeIntegrantes >= maxIntegrantesSelected) && (
                  <>
                    <>Se ha alcanzado el máximo de {maxIntegrantesSelected} {maxIntegrantesSelected != 1 ? nombreTipoIntegrantePrural : nombreTipoIntegrante} seleccionado{maxIntegrantesSelected != 1 && 's'}</>
                    <br/>
                    <>Si selecciona más {nombreTipoIntegrantePrural}, no se agregarán todos. Solo se {maxIntegrantesSelected === 1 ? "agregará el primer ID seleccionado" : "agregarán los primeros " + maxIntegrantesSelected + " ID's seleccionados" } de la tabla</>
                    <br/>
                    <>En caso de que quiera cambiar de {maxIntegrantesSelected === 1 ? nombreTipoIntegrante : nombreTipoIntegrantePrural}, deseleccione {maxIntegrantesSelected === 1 ? el_la + " " + nombreTipoIntegrante : (el_la === "el" ? "los" : "las") + " " + nombreTipoIntegrantePrural} que no desee y agregue otr{el_la === 'el' ? 'o' : 'a'}{maxIntegrantesSelected > 1 && 's'}</>
                  </>
                )}
                <TablaGenericaConFoto
                  columnsIn={columns}
                  Service={ServiceDeIntegrantes}
                  dataIn={candidatosAIntegrantes}
                  visibilidadInput={"SOLO_VISTA"}
                  nombreTipoDatoParaModuloVisibilidad={nombreTipoDatoParaModuloVisibilidad}
                  tipoDatoParaFoto={tipoDatoParaFoto}
                  el_la={el_la}
                  nombreTipoDato={nombreTipoIntegrante}
                  handleInputChange = {handleInputChange}
                  nombreHandleInputChange = {nombreHandleInputChange}
                  integrantesActuales = {integrantesActualesAux}
                  maxIntegrantesSelected = {maxIntegrantesSelected}
                />
              </div>
            )}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModalSeleccionar}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>

      {CreateItemComponent && isEditableAux && (
        <button type="button" className="btn btn-primary" onClick={handleOpenModalCrearItem}>
          <span style={{ display: 'flex', alignItems: 'center' }}>
            Crear item de {nombreTipoIntegrante}
          </span>
        </button>
      )}

      <Modal show={showModalCrearItem} onHide={handleCloseModalCrearItem} size="xl">
        <Modal.Header>
          <Modal.Title> Agregar item de {nombreTipoIntegrante} </Modal.Title>
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

const ModalSeleccionarIntegrantesContacto = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre/Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Email",
      accessor: "email",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Cuit",
      accessor: "cuit",
      type: "number",
    },
    {
      Header: "Domicilio",
      accessor: "domicilio",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Telefono",
      accessor: "telefono",
      type: "number",
    },
  ];

  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columns}
        tipoDatoParaFoto={"contacto"}
        nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : ContactoService}
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

const ModalSeleccionarIntegrantesPersona = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre",
      accessor: "nombre",
      type: "string",
    },
    {
      Header: "Apellido",
      accessor: "apellido",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Cuit",
      accessor: "cuit",
      type: "number",
    },
    {
      Header: "Edad",
      accessor: "edad",
      type: "number",
      Filter: SliderColumnFilter,
      filter: 'equals',
    },
    {
      Header: "Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
  ];

  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columns}
        tipoDatoParaFoto={"contacto"}
        nombreTipoDatoParaModuloVisibilidad={"PERSONA"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : PersonaService}
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

const ModalSeleccionarIntegrantesBeneficiario = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const columns = [
    {
      Header: "ID en sistema",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre",
      accessor: "nombre",
      type: "string",
    },
    {
      Header: "Apellido",
      accessor: "apellido",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Edad",
      accessor: "edad",
      type: "number",
      Filter: SliderColumnFilter,
      filter: 'equals',
    },
    {
      Header: "Se retira solo",
      accessor: "seRetiraSolo",
      Cell: ({ row, value }) => (row.original?.id ? (value ? "✅" : "❌") : "")
    },
    {
      Header: "Cuidados especiales",
      accessor: "cuidadosEspeciales",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
  ];

  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columns}
        tipoDatoParaFoto={"contacto"}
        nombreTipoDatoParaModuloVisibilidad={"BENEFICIARIO"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : BeneficiarioService}
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

const ModalSeleccionarIntegrantesProfesional = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, isEditable, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre",
      accessor: "nombre",
      type: "string",
    },
    {
      Header: "Apellido",
      accessor: "apellido",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Cuit",
      accessor: "cuit",
      type: "number",
    },
    {
      Header: "Edad",
      accessor: "edad",
      type: "number",
      Filter: SliderColumnFilter,
      filter: 'equals',
    },
    {
      Header: "Profesión",
      accessor: "profesion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
  ];

  return (
    <ModalSeleccionarIntegrantes
        integrantesActuales = {integrantesActuales}
        columnsIn={columns}
        tipoDatoParaFoto={"contacto"}
        nombreTipoDatoParaModuloVisibilidad={"PROFESIONAL"}
        ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : ProfesionalService}
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
  ModalSeleccionarIntegrantesContacto,  //Producto, donación, factura, prestamo
  ModalSeleccionarIntegrantesPersona,    //Proyecto
  ModalSeleccionarIntegrantesBeneficiario,  //Actividad
  ModalSeleccionarIntegrantesProfesional,   //Actividad
  
  //actividad //Programa de actividades
  //itemFactura
}
