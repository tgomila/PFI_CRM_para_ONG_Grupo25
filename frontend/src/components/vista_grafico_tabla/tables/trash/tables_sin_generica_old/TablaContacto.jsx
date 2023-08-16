import React, { useMemo, useState, useEffect, useRef } from "react";
import modulosService from "../../../../../services/modulosService";
import ContactoService from "../../../../../services/ContactoService";
import TablaGenerica from "../../Tabla_Generica";
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"../../Tabla_Variables";

const TablaContacto = () => {
  const [visibilidad, setVisibilidad] = useState("");

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo("CONTACTO");
    modulo.then(async (response) => {
      if(response){
        setVisibilidad(response);
      }
    });
  }, []);

  const columns = useMemo(() => {
    let baseColumns = [
      {
        Header: "ID",
        accessor: "id",
        type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
      },
      {
        Header: "Foto",
        Cell: ({ row }) => RenderFotoPerfilRow(row, "contacto"),
      },
      {
        Header: "Nombre/Descripción",
        accessor: "nombreDescripcion",
        type: "string",
      },
      {
        Header: "Email",
        accessor: "email",
        type: "string",
      },
      {
        Header: "Cuit",
        accessor: "cuit",
        type: "number",
      },
      {
        Header: "Telefono",
        accessor: "telefono",
        type: "number",
      },
    ];
    console.log("visibilidad: " + visibilidad);
    if (visibilidad === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Editar',
          Cell: ({ row }) => RenderBotonEditar(row.original?.id),
        },
        {
          Header: 'Borrar',
          Cell: ({ row }) => RenderBotonBorrar(row.original?.id, row.original?.nombreDescripcion, ContactoService),
        }
      );
    }

  return baseColumns;
  }, [visibilidad]);
  
  return(
    <div>
      <TablaGenerica
        columns={columns}
        Service={ContactoService}
        moduloName={"CONTACTO"}
        visibilidadInput={"EDITAR"}//Cambiar luego
        tipoDatoParaFoto={"contacto"}
        el_la={"el"}
        nombreTipoDato={"contacto"}
      />
    </div>
  );
}

export default TablaContacto;
