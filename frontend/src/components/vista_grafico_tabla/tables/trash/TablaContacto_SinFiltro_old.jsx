import React, { forwardRef, useMemo, useState, useEffect, useRef } from "react";
import ContactoService from "../../../../services/ContactoService";
import ImageService from "../../../../services/ImageService";
import modulosService from "../../../../services/modulosService";

import { useTable, usePagination, useSortBy, useRowState } from "react-table";

import "../../../Styles/Tabla.scss";

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

function TablaContacto() {
  const [data, setData] = useState([]);
  const [visibilidad, setVisibilidad] = useState("");
  //const [fotoPerfil, setFotoPerfil] = useState(null);
  //const [columnNames, setColumnNames] = useState([]);

  useEffect(() => {

    let modulo = modulosService.getVisibilidadByModulo("CONTACTO");
    modulo.then(async (response) => {
      if(response){
        setVisibilidad(response);
      }
    });

    ContactoService.getAll().then(async (res) => {
      const newData = await ImageService.getAllContactoTablaFotos(res.data);
      setData(newData);
    });

  }, []);

  const columns = useMemo(() => {
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
          Cell: ({ row }) => RenderBotonBorrar(row.original.id, row.original.nombreDescripcion, ContactoService),
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
            Cell: ({ row }) => RenderBotonBorrar(row.original.id, row.original.nombreDescripcion, ContactoService),
          },
        ],
      },
      /*{
        // Second group - Persona
        Header: "Datos de persona",
        // Second group columns
        columns: [
          {
            Header: "Nombre",
            accessor: "nombreDescripcion",
          },
        ],
      },*/
    ],
    []
  );


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
  return (
    <div className="componentePrincipal">
      
      {/**Botones agregar quitar */}
      {visibilidad === "EDITAR" && (
        <div className="row">
          <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create")}> Nuevo contacto</button>
          &nbsp;&nbsp;&nbsp;
          <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/update")}> Modificar contacto</button>
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

export default TablaContacto;
