import React, { forwardRef, useEffect, useState, useRef, useMemo } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";

import ContactoService from "../../../../services/ContactoService";
import PersonaService from "../../../../services/PersonaService";

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
const ModalSeleccionarIntegrantes = ({integrantesActuales, columnsIn, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const [showModal, setShowModal] = useState(false);
  const [integrantesActualesAux, setIntegrantesActualesAux] = useState(null);
  const [candidatosAIntegrantes, setCandidatosAIntegrantes] = useState(null);

  useEffect(() => {
    if(showModal && !candidatosAIntegrantes){
      ServiceDeIntegrantes.getAll().then((res) => {
        setCandidatosAIntegrantes(res.data);
      });
    }
    setIntegrantesActualesAux(integrantesActuales);
    if(!showModal){
      forzarRender();//Fue hecho para forzar actualizar la tabla
    }
  }, [showModal]);

  const [numeroDeIntegrantes, setNumeroDeIntegrantes] = useState(0);
  useEffect(() => {
    console.log("Cambió integrantes actuales");
    console.log(integrantesActuales);
    
    if (Array.isArray(integrantesActuales)) {//Es lista
      setNumeroDeIntegrantes(integrantesActuales.length);
      console.log("integrantesActuales.length: " + integrantesActuales.length);
      integrantesActuales.forEach((integranteActual) => {
        console.log("Cada uno:");
        console.log(integranteActual)
      });
    } else if (integrantesActuales) {//Es 1 objeto
      setNumeroDeIntegrantes(1);
      console.log("Es objeto");
      console.log(integrantesActuales);
    } else {//Esta vacío
      setNumeroDeIntegrantes(0);
      console.log("Es null o es objeto");
      console.log(integrantesActuales);
    }

    if(!showModal){
      setIntegrantesActualesAux(integrantesActuales);
    }
  }, [integrantesActuales])

  const selectedRows = useMemo(() => {
    if (candidatosAIntegrantes) {
      // console.log("dataTable");
      // console.log(dataTable);
      // console.log("integrantesActuales");
      // console.log(integrantesActuales);
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
      //console.log("selectedRowIds");
      //console.log(selectedRowIds);
      return selectedRowIds;
    }
    return {};
  }, [integrantesActuales, candidatosAIntegrantes]);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
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

  // useEffect(() => {
  //   console.log("integrantesActuales");
  //   console.log(integrantesActuales);
  // }, [integrantesActuales]);

  const [forzarRenderizado, setForzarRenderizado ] = useState(false);
  const forzarRender = () => {
    /*setForzarRenderizado(true);
    setTimeout(() => {
      setForzarRenderizado(false);
    }, 100);*/
  }
  
  return(
    <div className = "form-group">
      <label> {integrantesActuales ? nombreTipoIntegrante.charAt(0).toUpperCase() + nombreTipoIntegrante.slice(1) + ":" : "No hay " + nombreTipoIntegrante + " asociad"+ (el_la === "el" ? "o" : "a")}</label>
      {integrantesActuales && !forzarRenderizado && maxIntegrantesSelected > 1 && (
        //TODO Debería haber una tabla
          <div style={{ overflowY: 'auto' }}>
          {console.log("integrantesActuales")}
          {console.log(integrantesActuales)}
          <TablaGenericaConFoto
            columns={columns}
            Service={ServiceDeIntegrantes}
            dataIn={integrantesActuales}
            visibilidadInput={"SIN_BOTONES"}
            nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
            tipoDatoParaFoto={"contacto"}
            el_la={"el"}
            nombreTipoDato={"proveedor"}
            style={{ overflowY: 'auto' }}
          />
          </div>
      )}
      {integrantesActuales && !forzarRenderizado && maxIntegrantesSelected === 1 && (
        <div>
          {console.log("integrantesActuales")}
          {console.log(integrantesActuales)}
          {RenderMostrarContacto(integrantesActualesAux, "contacto", "")}
        </div>
      )}
      <br/>

      <button type="button" className="btn btn-primary" onClick={handleOpenModal}>
        <span style={{ display: 'flex', alignItems: 'center' }}>
        {integrantesActuales ? "Cambiar" : "Agregar"} {maxIntegrantesSelected === 1 ? nombreTipoIntegrante : nombreTipoIntegrantePrural}
        </span>
      </button>

      <Modal show={showModal} onHide={handleCloseModal} size="xl">
        <Modal.Header>
          <Modal.Title>Seleccione {maxIntegrantesSelected >0 && maxIntegrantesSelected + " "}{maxIntegrantesSelected < 2 ? nombreTipoIntegrante : nombreTipoIntegrantePrural}</Modal.Title>
          <button className="close" onClick={handleCloseModal}>
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
                {console.log("candidatosAIntegrantes")}
                {console.log(candidatosAIntegrantes)}
                {numeroDeIntegrantes >= maxIntegrantesSelected && (
                  <>
                    <>Se ha alcanzado el máximo de {maxIntegrantesSelected} {nombreTipoIntegrantePrural} aceptados</>
                    <br/>
                    <>Si selecciona más {nombreTipoIntegrantePrural}, solo se agregarán los primeros {maxIntegrantesSelected} ID's</>
                  </>
                )}
                <TablaGenericaConFoto
                  columns={columns}
                  Service={ServiceDeIntegrantes}
                  dataIn={candidatosAIntegrantes}
                  visibilidadInput={"SOLO_VISTA"}
                  nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
                  tipoDatoParaFoto={"contacto"}
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
          <Button variant="secondary" onClick={handleCloseModal}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>

  );
};

const ModalSeleccionarIntegrantesContacto = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
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
        ServiceDeIntegrantes = {ServiceDeIntegrantes}
        handleInputChange = {handleInputChange}
        nombreHandleInputChange = {nombreHandleInputChange}
        maxIntegrantesSelected = {maxIntegrantesSelected}
        el_la = {el_la}
        nombreTipoIntegrante = {nombreTipoIntegrante}
        nombreTipoIntegrantePrural = {nombreTipoIntegrantePrural}
    />
  );
}

const ModalSeleccionarIntegrantesBeneficiario = ({integrantesActuales, ServiceDeIntegrantes, handleInputChange, nombreHandleInputChange, maxIntegrantesSelected, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
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
      Cell: ({ value }) => (value ? "✅" : "❌")
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
        ServiceDeIntegrantes = {ServiceDeIntegrantes}
        handleInputChange = {handleInputChange}
        nombreHandleInputChange = {nombreHandleInputChange}
        maxIntegrantesSelected = {maxIntegrantesSelected}
        el_la = {el_la}
        nombreTipoIntegrante = {nombreTipoIntegrante}
        nombreTipoIntegrantePrural = {nombreTipoIntegrantePrural}
    />
  );
}


export {
  ModalSeleccionarIntegrantes,
  ModalSeleccionarIntegrantesContacto,
}
