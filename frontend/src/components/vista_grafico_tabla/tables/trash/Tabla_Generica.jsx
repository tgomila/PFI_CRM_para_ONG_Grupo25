import React, { forwardRef, useMemo, useState, useEffect, useRef } from "react";
import ImageService from "../../../../services/ImageService";
import modulosService from "../../../../services/modulosService";

import { useTable, usePagination, useSortBy, useRowState } from "react-table";

import "../../../../Styles/Tabla.scss";

import { useNavigate } from 'react-router-dom';

//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip } from "react-bootstrap";
//import "../../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { FaTrashAlt, FaRegEdit } from "react-icons/fa";
//import ReactToolTip from 'react-tooltip';
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfil,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"../Tabla_Variables";


//Que tenes que cambiar si queres reciclar este código para otro tipo de persona:
//Por ejemplo persona a empleado, cambiar palabras de:
//"Persona" por "Empleado"
//"PERSONA" por "EMPLEADO", fijate como se llama en postman, por lo general es en mayúsculas
//"persona" por "empleado", fijate que si dice nueva persona, cambialo por "nuevo" con o

/**
 * 
 * @param {*} columns 
 * @param {*} Service 
 * @param {*} nombreVisibilidad 
 * @param {*} el_la 
 * @param {*} nombreTipoDato 
 * @returns 
 */
/**
 * 
 * @param {Array} columnas 
 * @param {*} Service (opcional si no hay dataInput y no hay tipoDatoParaFoto). Es el js del service, ejemplo "ContactoService".
 * @param {string} nombreVisibilidad ejemplo "CONTACTO", o forzar la visibilidad a "SOLO_VISTA", "EDITAR".
 * @param {string} el_la solo poner "el" o "la". Es para textos como: "el contacto", "la persona".
 * @param {string} nombreTipoDato "contacto"
 * @returns 
 */
const TablaGenericaPersona = (columns, Service, nombreVisibilidad, el_la, nombreTipoDato) => {
  return TablaGenerica(null, columns, Service, "contacto", nombreVisibilidad, el_la, nombreTipoDato)
}










const TablaGenericaWithProps = (props) => {
  const { dataInput, columnas, Service, tipoDatoParaFoto, nombreVisibilidad, el_la, nombreTipoDato } = props;
  
  console.log("------------------------------------------------------------")
  console.log("dataInput:");
  console.log(dataInput);
  console.log("columnas:");
  console.log(columnas);
  console.log("tipoDatoParaFoto:");
  console.log(tipoDatoParaFoto);
  console.log("Service:");
  console.log(Service);
  console.log("nombreVisibilidad:");
  console.log(nombreVisibilidad);
  console.log("el_la:");
  console.log(el_la);
  console.log("nombreTipoDato:");
  console.log(nombreTipoDato);
  
  return TablaGenerica(dataInput, columnas, tipoDatoParaFoto, Service, nombreVisibilidad, el_la, nombreTipoDato);
};


/**
 * 
 * @param {Array} dataInput (opcional) data con/sin foto. Si no hay data, se usa Service para obtenerlo.
 * @param {Array} columnas 
 * @param {*} Service (opcional si no hay dataInput y no hay tipoDatoParaFoto). Es el js del service, ejemplo "ContactoService".
 * @param {string} tipoDatoParaFoto (dataInput deberia ser vacío) ejemplo "contacto", "programaDeActividades" en minusculas, es para llamar a backend ImageService
 * @param {string} nombreVisibilidad ejemplo "CONTACTO", o forzar la visibilidad a "SOLO_VISTA", "EDITAR".
 * @param {string} nombreVisibilidad solo poner "el" o "la". Es para textos como: "el contacto", "la persona".
 * @param {string} nombreTipoDato "contacto"
 * @returns 
 */
const TablaGenerica = (dataIn, columnas, Service, tipoDatoParaFoto, nombreVisibilidad, el_la, nombreTipoDato) => {
  const[columns, setColumns] = useState([]);
  if(columnas){
    setColumns(columnas);
  }
  console.log("Entre acá 1");
  console.log(columnas);
  console.log("columna:");
  console.log(columns);
  console.log("tipoDatoParaFoto:");
  console.log(tipoDatoParaFoto);
  const [data, setData] = useState([]);
  if(dataIn)
    setData(dataIn);
  const [visibilidad, setVisibilidad] = useState("");

  useEffect(() => {
    console.log("Entre acá 2");
    if (["SIN_SUSCRIPCION", "NO_VISTA", "SOLO_VISTA", "EDITAR"].includes(nombreVisibilidad)
    ) {
      setVisibilidad(nombreVisibilidad);
    } else {
      console.log(nombreVisibilidad);
      let modulo = modulosService.getVisibilidadByModulo(nombreVisibilidad);
      console.log("After:" + modulo);
      modulo.then(async (response) => {
        console.log(response);
        console.log("Entre acá 3");
        if(response){
          setVisibilidad(response);
        }
      });
      console.log("After2:" + modulo);
    }

    if(!dataIn){
      Service.getAll().then(async (res) => {
        console.log(res.data);
        if(tipoDatoParaFoto){//Si deberia agregarse fotos, lo agrego.
          const newData = await ImageService.getAllFotos(res.data, tipoDatoParaFoto, "tabla");
          setData(newData);
        } else {
          setData(res.data);
        }
      });

    }

  }, []);

  console.log("Entre acá 3");
  //Inicio de Agrego column foto si no existe
  if(tipoDatoParaFoto){
    const newfotoColumn = {
      Header: "Foto",
      Cell: ({ row }) => RenderFotoPerfil(row.original.id, tipoDatoParaFoto, row.original.imagen_tabla, row.original.nombreDescripcion),
    };
    const columnFotoExists = columns.some((column) => column.Header === newfotoColumn.Header);
    if(!columnFotoExists){
      //Posición 1 (luego de ID), 0 es borrar cero elementos, agregar newfotoColumn
      columns.splice(1, 0, newfotoColumn);//Agregar columna foto a columns
    }
  }
  //Fin agrego column foto si no existe.

  if (visibilidad === "EDITAR") {
    console.log("Entre acá 4");
    //push modifica columns, si usara concat solo devuelve un nuevo arreglo
    columns.push(
      {
        Header: 'Editar',
        Cell: ({ row }) => RenderBotonEditar(row.original.id),
      },
      {
        Header: 'Borrar',
        Cell: ({ row }) => RenderBotonBorrar(row.original.id, row.original.nombreDescripcion, Service),
      }
    );
  }

  /*const columns = useMemo(() => {
    let baseColumns = [
      {
        Header: "ID",
        accessor: "id",
      },
      {
        Header: "Foto",
        Cell: ({ row }) => RenderFotoPerfil(row.original.id, "contacto", row.original.imagen_tabla, row.original.nombreDescripcion),
      },
      {
        Header: "Nombre/Descripción",
        accessor: "nombreDescripcion",
      },
      {
        Header: "Email",
        accessor: "email",
      },
      {
        Header: "Cuit",
        accessor: "cuit",
      },
      {
        Header: "Telefono",
        accessor: "telefono",
      },
    ];
    if (visibilidad === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Editar',
          Cell: ({ row }) => RenderBotonEditar(row.original.id),
        },
        {
          Header: 'Borrar',
          Cell: ({ row }) => RenderBotonBorrar(row.original.id, row.original.nombreDescripcion, Service),
        }
      );
    }

  return baseColumns;
  }, [visibilidad]);

  const columns2 = useMemo(
    () => [
      {
        // first group - Contacto
        Header: "Datos de contactos",
        // First group columns
        columns: [
          {
            Header: "ID",
            accessor: "id",
          },
          {
            Header: "Nombre/Descripción",
            accessor: "nombreDescripcion",
          },
          {
            Header: "Email",
            accessor: "email",
          },
          {
            Header: "Cuit",
            accessor: "cuit",
          },
          {
            Header: "Telefono",
            accessor: "telefono",
          },
          {
            Header: 'Editar',
            Cell: ({ row }) => RenderBotonEditar(row.original.id),
          },
          {
            Header: 'Borrar',
            Cell: ({ row }) => RenderBotonBorrar(row.original.id, row.original.nombreDescripcion, Service),
          },
        ],
      },
      //{
        // Second group - Persona
      //  Header: "Datos de persona",
        // Second group columns
      //  columns: [
      //    {
      //      Header: "Nombre",
      //      accessor: "nombreDescripcion",
      //    },
      //  ],
      //},
    ],
    []
  );*/

  console.log("columna:");
  console.log(columns);
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    page,
    nextPage,
    previousPage,
    canPreviousPage,
    canNextPage,
    pageOptions,
    state,
    gotoPage,
    pageCount,
    setPageSize,
    prepareRow,
    allColumns,
    getToggleHideAllColumnsProps,
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
    },
    useSortBy,
    usePagination,
    useRowState
  );

  const { pageIndex, pageSize } = state;
  window.scrollTo({ top: 0, behavior: "smooth" });
  
  const navigate = useNavigate();


  // Render the UI for your table
  return(<div></div>);
  return (
    <div className="componentePrincipal">
      
      {/**Botones agregar quitar */}
      {visibilidad === "EDITAR" && (
        <div className="row">
          <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create")}>
            {el_la === "el" ? "Nuevo" : "Nueva"} {nombreTipoDato}</button>
          &nbsp;&nbsp;&nbsp;
          <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/update")}>
            Modificar {nombreTipoDato}</button>
        </div>
      )}
      
      
      {/**Tabla */}
      <br></br>
      <div className="tablaVista">
        <div className="tituloNroColumns"> &nbsp; Seleccione columnas a mostrar/ocultar &nbsp;</div>
        <div className="row custom-row">
          <IndeterminateCheckbox {...getToggleHideAllColumnsProps()} />
          
          {allColumns.map((column) => (
            <div className="cb action" key={column.id}>
              <label>
                <input type="checkbox" {...column.getToggleHiddenProps()} />{" "}
                <span>{column.Header}</span>
              </label>
            </div>
          ))}
        </div>
      </div>
      <br/>
      <div className="row">
        <div>
          <div className="tablaVista">
            <table>
              <thead>
                {headerGroups.map((headerGroup) => (
                  <tr {...headerGroup.getHeaderGroupProps()}>

                    {headerGroup.headers.map((column, index) => (
                      <th {...column.getHeaderProps()} className={headerGroup.headers.length === 1 ? 'th-one' : index === 0 ? 'th-first' : index === headerGroup.headers.length - 1 ? 'th-last' : 'th'}>
                        {column.render("Header").toUpperCase()}
                      </th>
                    ))}
        
                  </tr>
                ))}
              </thead>

              <tbody {...getTableBodyProps()}>
                {page.map((row, i) => {
                  prepareRow(row);
                  return (
                    <tr {...row.getRowProps()}>
                      {row.cells.map((cell) => {
                        return (
                          <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                        );
                      })}

                    </tr>
                  );
                })}
              </tbody>
            </table>

            <div className="tablaButtomBarra">
              <button
                className="buttonAnimadoAzul"
                onClick={() => gotoPage(0)}
                disabled={!canPreviousPage}
              >
                {"<<"}
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => previousPage()}
                disabled={!canPreviousPage}
              >
                Anterior
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => nextPage()}
                disabled={!canNextPage}
              >
                Siguiente
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => gotoPage(pageCount - 1)}
                disabled={!canNextPage}
              >
                {">>"}
              </button>{" "}
              <span>
                Página{" "}
                <strong>
                  {pageIndex + 1} de {pageOptions.length}
                </strong>{" "}
              </span>
              <span>
                | Ir a la Página:{" "}
                <input
                  type="number"
                  defaultValue={pageIndex + 1}
                  onChange={(e) => {
                    const pageNumber = e.target.value
                      ? Number(e.target.value) - 1
                      : 0;
                    gotoPage(pageNumber);
                  }}
                  style={{ width: "50px" }}
                />
              </span>{" "}
              <span className="miDivSelect">
                <span className="dropup">
                  <select
                    className="selectAnimado"
                    value={pageSize}
                    onChange={(e) => setPageSize(Number(e.target.value))}
                  >
                    {[10, 25, 50].map((pageSize) => (
                      <option
                        className="dropup-content"
                        key={pageSize}
                        value={pageSize}
                      >
                        Mostrar {pageSize}
                      </option>
                    ))}
                  </select>
                </span>
              </span>
            </div>
          </div>


        </div>
      </div>
    </div>
  );
}

export {
  TablaGenericaPersona,
  TablaGenericaWithProps,
  TablaGenerica,
}
