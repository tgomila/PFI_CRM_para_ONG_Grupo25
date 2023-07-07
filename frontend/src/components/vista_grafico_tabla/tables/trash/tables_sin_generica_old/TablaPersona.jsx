import React, { useMemo, useState, useEffect, useRef } from "react";
import modulosService from "../../../../services/modulosService";
import PersonaService from "../../../../services/PersonaService";
import TablaGenerica from "./Tabla_Generica";
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"../Tabla_Variables";

const TablaPersona = () => {
  const [visibilidad, setVisibilidad] = useState("");

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo("PERSONA");
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
        Header: "Nombre",
        accessor: "nombre",
        type: "string",
      },
      {
        Header: "Apellido",
        accessor: "apellido",
        type: "string",
      },
      {
        Header: "Fecha de nacimiento",
        accessor: "fechaNacimiento",
        type: "string",
      },
      {
        Header: "Edad",
        accessor: "edad",
        type: "number",
      },
      {
        Header: "Descripción",
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
            return RenderBotonBorrar(row.original?.id, nombreCompleto, PersonaService);
          },
        }
      );
    }

  return baseColumns;
  }, [visibilidad]);
  
  return(
    <div>
      <TablaGenerica
        columns={columns}
        Service={PersonaService}
        moduloName={"PERSONA"}
        visibilidad={"EDITAR"}//Cambiar luego
        tipoDatoParaFoto={"contacto"}
        el_la={"el"}
        nombreTipoDato={"persona"}
      />
    </div>
  );
}

export default TablaPersona;
