import React, { useMemo, useState, useEffect, useRef } from "react";
import modulosService from "../../../services/modulosService";
import ContactoService from "../../../services/ContactoService";
import { TablaGenericaPersona } from "./Tabla_Generica";
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";

const TablaContacto = ({visibilidadInput}) => {

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
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columns}
        Service={ContactoService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
        el_la={"el"}
        nombreTipoDato={"contacto"}
      />
    </div>
  );
}

export default TablaContacto;
