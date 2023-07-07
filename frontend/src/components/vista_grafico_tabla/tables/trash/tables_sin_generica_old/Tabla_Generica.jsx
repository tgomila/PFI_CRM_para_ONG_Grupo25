import React, { useState, useEffect } from "react";
import ImageService from "../../../services/ImageService";

import { useTable, 
  usePagination, 
  useSortBy, 
  useExpanded,
  useRowState, 
  useFilters, 
  useGlobalFilter, 
  useGroupBy,
} from "react-table";

import "../../../Styles/Tabla.scss";

import { useNavigate } from 'react-router-dom';

//import "../../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import {
  FaSistrix,
  FaRegEdit,
  FaSortAlphaDownAlt,
  FaSortAlphaDown,
  FaSortNumericDown,
  FaSortNumericDownAlt,
  FaSort,
  FaSortUp,
  FaSortDown,
} from "react-icons/fa";
import { MdOutlinePersonAddAlt1 } from "react-icons/md";
//import ReactToolTip from 'react-tooltip';
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"./Tabla_Variables";

import {
  GlobalFilter,
  DefaultColumnFilter,
  fuzzyTextFilterFn,
} from"./Tabla_Filters";

//Que tenes que cambiar si queres reciclar este código para otro tipo de persona:
//Por ejemplo persona a empleado, cambiar palabras de:
//"Persona" por "Empleado"
//"PERSONA" por "EMPLEADO", fijate como se llama en postman, por lo general es en mayúsculas
//"persona" por "empleado", fijate que si dice nueva persona, cambialo por "nuevo" con o

function TablaGenerica({columns, Service, moduloName, visibilidad, tipoDatoParaFoto, el_la, nombreTipoDato}) {
  const [data, setData] = useState([]);
  //const [visibilidad, setVisibilidad] = useState("");
  console.log("Visibilidad: " + visibilidad);
  
  //const [fotoPerfil, setFotoPerfil] = useState(null);
  //const [columnNames, setColumnNames] = useState([]);

  useEffect(() => {
    // let modulo = modulosService.getVisibilidadByModulo("CONTACTO");
    // modulo.then(async (response) => {
    //   if(response){
    //     setVisibilidad(response);
    //   }
    // });

    Service.getAll().then(async (res) => {
      const newData = await ImageService.getAllFotos(res.data, tipoDatoParaFoto, "tabla");
      setData(newData);
    });

  }, []);
  
  //Inicio de filtros de búsqueda
  const [mostrarBusqueda, setMostrarBusqueda] = useState(false);
  const clickMostrarBusqueda = () => {
    setMostrarBusqueda(!mostrarBusqueda);
  };

  // Let the table remove the filter if the string is empty
  fuzzyTextFilterFn.autoRemove = val => !val

  const defaultColumn = React.useMemo(
    () => ({
      // Let's set up our default Filter UI
      Filter: DefaultColumnFilter,
      type: "string",
    }),
    []
  );

  const filterTypes = React.useMemo(
    () => ({
      // Add a new fuzzyTextFilterFn filter type.
      fuzzyText: fuzzyTextFilterFn,
      // Or, override the default text filter to use
      // "startWith"
      text: (rows, id, filterValue) => {
        return rows.filter(row => {
          const rowValue = row.values[id]
          return rowValue !== undefined
            ? String(rowValue)
                .toLowerCase()
                .startsWith(String(filterValue).toLowerCase())
            : true
        })
      },
    }),
    []
  );
  //Fin filtros de búsqueda


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
    visibleColumns,
    preGlobalFilteredRows,
    setGlobalFilter,
    state: { groupBy, expanded }
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
      defaultColumn,
      filterTypes,
    },
    useFilters, // useFilters!
    useGlobalFilter, // useGlobalFilter!
    useGroupBy,
    useSortBy,
    useExpanded,
    usePagination,
    useRowState,
  );

  const { pageIndex, pageSize } = state;
  //window.scrollTo({ top: 0, behavior: "smooth" });
  
  const navigate = useNavigate();


  // Render the UI for your table
  return (
    <div className="componentePrincipal">
      
      {/**Botones agregar quitar */}
      
      <div className="row">
        <button className="btn btn-info" onClick={clickMostrarBusqueda} style={{marginLeft: "00px"}}>
          <FaSistrix /> {mostrarBusqueda ? "Ocultar" : "Mostrar"} barra de búsqueda
        </button>
        {visibilidad === "EDITAR" && (
          <div>
            &nbsp;&nbsp;&nbsp;
            <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create")}>
              <span style={{ display: 'flex', alignItems: 'center' }}>
                <MdOutlinePersonAddAlt1 style={{ marginRight: '5px', fontSize: '20px' }}/> Nuev{el_la === "el" ? "o" : "a"} {nombreTipoDato}
              </span>
            </button>
            &nbsp;&nbsp;&nbsp;
            <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/update")}>
              <span style={{ display: 'flex', alignItems: 'center' }}>
                <FaRegEdit style={{ marginRight: '5px', fontSize: '20px' }}/> Modificar {nombreTipoDato}
              </span>
            </button>
          </div>
        )}
      </div>
      
      
      
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
            <table {...getTableProps()}>
              <thead>
                {mostrarBusqueda && (
                <tr>
                  <th
                    className='th-one'
                    colSpan={visibleColumns.length}
                    style={{
                      textAlign: 'left',
                    }}
                  >
                    <GlobalFilter
                      preGlobalFilteredRows={preGlobalFilteredRows}
                      globalFilter={state.globalFilter}
                      setGlobalFilter={setGlobalFilter}
                    />
                  </th>
                </tr>
                )}
                {headerGroups.map((headerGroup) => (
                  <tr {...headerGroup.getHeaderGroupProps()}>

                    {headerGroup.headers.map((column, index) => (
                      <th {...column.getHeaderProps()} className={mostrarBusqueda ? 'th' : headerGroup.headers.length === 1 ? 'th-one' : index === 0 ? 'th-first' : index === headerGroup.headers.length - 1 ? 'th-last' : 'th'}>
                        <div>
                          {column.canGroupBy && mostrarBusqueda ? (
                            // If the column can be grouped, let's add a toggle
                            <span {...column.getGroupByToggleProps()}>
                              {column.isGrouped ? '🔴 ' : '⚪ '}
                            </span>
                          ) : null}
                          <span {...column.getSortByToggleProps()}>
                            {column.render("Header").toUpperCase()}
                            {/* Add a sort direction indicator */}
                            {column.isSorted
                              ? (
                                <span className="icono-en-texto-dere">
                                {column.isSortedDesc
                                ? ' 🔽'
                                : ' 🔼'
                                //? ((column.type && column.type === "number") ? <FaSortNumericDownAlt/> : <FaSortAlphaDownAlt/>)
                                //: (column.type && column.type === "number" ? <FaSortNumericDown/> : <FaSortAlphaDown/>)
                                }</span>)
                              : ''}
                            {/*column.canSort && column.isSorted
                              ? column.isSortedDesc
                                ? <FaSortDown className="icono-en-texto-dere"/>
                                : <FaSortUp className="icono-en-texto-dere"/>
                              : <FaSort className="icono-en-texto-dere"/>
                            */}
                          </span>
                        </div>
                        {mostrarBusqueda && (
                          <div className="mostrarBusquedaDiv">
                            {column.canFilter ? column.render('Filter') : null}
                          </div>
                        )}
                        
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
                          <td {...cell.getCellProps()}>
                            {/*cell.render("Cell")*/}
                            {cell.isGrouped ? (
                              // If it's a grouped cell, add an expander and row count
                              <>
                                <span {...row.getToggleRowExpandedProps()}>
                                  {row.isExpanded ? '👇' : '👉'}
                                </span>{' '}
                                {cell.render('Cell', { editable: false })} (
                                {row.subRows.length})
                              </>
                            ) : cell.isAggregated ? (
                              // If the cell is aggregated, use the Aggregated
                              // renderer for cell
                              cell.render('Aggregated')
                            ) : cell.isPlaceholder ? null : ( // For cells with repeated values, render null
                              // Otherwise, just render the regular cell
                              cell.render('Cell', { editable: true })
                            )}
                          </td>
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

export default TablaGenerica;
