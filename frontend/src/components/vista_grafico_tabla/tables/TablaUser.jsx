import React, { useMemo, useState, useEffect, useRef } from "react";
import UserService from "../../../services/UserService";
import { TablaGenerica } from "./Tabla_Generica";
import { RenderFotoIntegranteRow } from "./Tabla_Variables";
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

const TablaUser = ({visibilidadInput}) => {

  const columns = useMemo(() => {
    let baseColumns = [
      {
        Header: "ID",
        accessor: "id",
        type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
      },
      {
        Header: "Contacto",
        Cell: ({ row }) => RenderFotoIntegranteRow(row, row.original?.contacto, "contacto"),
      },
      {
        Header: "Nombre",
        accessor: "name",
        filter: 'fuzzyText',
        type: "string",
      },
      {
        Header: "Email",
        accessor: "email",
        filter: 'fuzzyText',
        type: "string",
      },
    ];
    console.log("visibilidad: " + visibilidadInput);
    if (visibilidadInput === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Editar',
          Cell: ({ row }) => RenderBotonEditar(row.original?.id),
        },
        {
          Header: 'Borrar',
          Cell: ({ row }) => {
            const { nombre, apellido, nombreDescripcion } = row.original || {};
            let nombreCompleto = '';
            if (nombre && apellido) {
              nombreCompleto = `${nombre} ${apellido}`;
            } else if (nombre) {
              nombreCompleto = nombre;
            } else if (apellido) {
              nombreCompleto = apellido;
            } else if (nombreDescripcion) {
              nombreCompleto = nombreDescripcion;
            }
            return RenderBotonBorrar(row.original?.id, nombreCompleto, UserService);
          },
        }
      );
    }

  return baseColumns;
  }, [visibilidadInput]);
  
  return(//{columnsIn, data, visibilidad, Service, el_la, nombreTipoDato}) => {
    <div>
      <TablaGenerica
        columnsIn={columns}
        dataIn={null}
        Service={UserService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"USERS"}
        el_la={"el"}
        nombreTipoDato={"usuario"}
      />
    </div>
  );
}

export default TablaUser;
