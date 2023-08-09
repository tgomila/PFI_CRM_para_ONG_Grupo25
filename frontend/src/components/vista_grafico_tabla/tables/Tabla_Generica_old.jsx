import React, { useMemo, useState, useEffect } from "react";
import modulosService from "../../../services/modulosService";
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
  RenderBotonVer,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"./Tabla_Variables";

import {
  GlobalFilter,
  DefaultColumnFilter,
  fuzzyTextFilterFn,
} from"./Tabla_Filters";

/**
 * @description
 * Se utilizará para ver todo tipo de personas, es la tabla principal
 * 
 * @param {Array} columns - Columnas de la tabla.
 * @param {Object} Service - Objeto para obtener datos mediante Axios.
 * @param {string} visibilidadInput - Ejemplo "EDITAR" permite editar tabla, "SOLO_VISTA" solo ver tabla.
 * @param {string} nombreTipoDatoParaModuloVisibilidad - Nombre del módulo. Ejemplo "PERSONA"
 * @param {string} el_la - Género del tipo de dato.
 * @param {string} nombreTipoDato - Nombre del tipo de dato.
 * @returns {JSX.Element} - Componente de la tabla genérica.
 */
const TablaGenericaPersona = ({ columns, Service, visibilidadInput, nombreTipoDatoParaModuloVisibilidad, el_la, nombreTipoDato }) => {
  return (
    <div>
      <TablaGenericaConFoto
        columns={columns}
        Service={Service}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={nombreTipoDatoParaModuloVisibilidad}
        tipoDatoParaFoto={"contacto"}
        el_la={el_la}
        nombreTipoDato={nombreTipoDato}
      />
    </div>
  );
};

/**
 * @description
 * Se utilizará para ver todo tipo de personas, es la tabla principal
 * 
 * @param {Array} columns - Columnas de la tabla.
 * @param {Array} dataIn - data precargada, sin foto (esto sirve para actividad, en vez de Service.getAll, usar algun metodo distinto afuera como getMisActividades)
 * @param {Object} Service - Objeto para obtener datos mediante Axios.
 * @param {string} visibilidadInput - Nombre de la visibilidad. Es opcional, ejemplo "" o "EDITAR"
 * @param {string} nombreTipoDatoParaModuloVisibilidad - Nombre del módulo. Ejemplo "PERSONA"
 * @param {string} tipoDatoParaFoto - Tipo de dato para la foto. Ejemplo "contacto", "producto"
 * @param {string} el_la - Género del tipo de dato.
 * @param {string} nombreTipoDato - Nombre del tipo de dato.
 * @returns {JSX.Element} - Componente de la tabla genérica.
 */
const TablaGenericaConFoto = ({ columns, dataIn, Service, visibilidadInput, nombreTipoDatoParaModuloVisibilidad, tipoDatoParaFoto, el_la, nombreTipoDato }) => {
  const [data, setData] = useState([]);
  const [columnsData, setColumnsData] = useState([]);
  const [visibilidad, setVisibilidad] = useState("");
  const [isDataColumnsReady, setIsDataColumnsReady] = useState(false);
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    if (visibilidadInput !== "") {
      setVisibilidad(visibilidadInput);
      setIsVisibilidadReady(true);
    } else {
      let modulo = modulosService.getVisibilidadByModulo(nombreTipoDatoParaModuloVisibilidad);
      modulo.then((response) => {
        if (response) {
          setVisibilidad(response);
          setIsVisibilidadReady(true);
        }
      });
    }

    agregarFotos();

  }, []);

  useEffect(() => {
    //En caso de que cambies de "all actividades" a "mis actividades", poner foto a sus nuevos data's
    agregarFotos();
  }, [dataIn]);

  const agregarFotos = () => {
    if(dataIn){
      const { columns: modifiedColumns, data: modifiedData } = agregarFotoData(columns, dataIn, tipoDatoParaFoto);
      setData(modifiedData);
      setColumnsData(modifiedColumns);
      setIsDataColumnsReady(true);
    } else {
      Service.getAll().then(async (res) => {
        const { columns: modifiedColumns, data: modifiedData } = await agregarFotoData(columns, res.data, tipoDatoParaFoto);
        setData(modifiedData);
        setColumnsData(modifiedColumns);
        setIsDataColumnsReady(true);
      });
    }
  }

  if (!isDataColumnsReady || !isVisibilidadReady) {
    return null;//evita la renderización prematura
  }

  return (
    <div>
      <TablaGenerica
        columnsIn={columnsData}
        dataIn={data}
        visibilidad={visibilidad}
        Service={Service}
        el_la={el_la}
        nombreTipoDato={nombreTipoDato}
      />
    </div>
  );
}

//aux para tabla de contactos o productos con foto
const agregarFotoData = async (columns, data, tipoDatoParaFoto) => {

  const newData = await ImageService.getAllFotos(data, tipoDatoParaFoto, "tabla");
  const newColumns = [...columns]; // Clonar las columnas existentes

  newColumns.splice(1, 0, {
    Header: "Foto",
    Cell: ({ row }) => RenderFotoPerfilRow(row, tipoDatoParaFoto),
  });

  return { columns: newColumns, data: newData };
};

const TablaGenerica = ({columnsIn, dataIn, visibilidad, Service, el_la, nombreTipoDato}) => {
  
  const [data, setData] = useState([]);
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

  const columns = useMemo(() => {
    let baseColumns = [...columnsIn];
    //La vista siempre estaría
    if (visibilidad === "SOLO_VISTA" || visibilidad === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Ver',
          Cell: ({ row }) => {
            const { nombre, apellido, nombreDescripcion, descripcion } = row.original || {};
            let nombreCompleto = '';//Vacío (solo mostrar "desea borar id: 1" en vez de nombre)
            if (nombre && apellido) {//Beneficiarios, Empleados, etc
              nombreCompleto = `${nombre} ${apellido}`;
            } else if (nombre) {//Una persona sin apellido
              nombreCompleto = nombre;
            } else if (apellido) {//Una persona sin nombre
              nombreCompleto = apellido;
            } else if (nombreDescripcion) {//Contacto
              nombreCompleto = nombreDescripcion;
            } else if (descripcion) {//Facturas, insumos, etc
              nombreCompleto = descripcion;
            }
            return RenderBotonVer(row.original?.id, nombreCompleto);
          },
        }
      );
    }
    if (visibilidad === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Editar',
          Cell: ({ row }) => {
            const { nombre, apellido, nombreDescripcion, descripcion } = row.original || {};
            let nombreCompleto = '';//Vacío (solo mostrar "desea borar id: 1" en vez de nombre)
            if (nombre && apellido) {//Beneficiarios, Empleados, etc
              nombreCompleto = `${nombre} ${apellido}`;
            } else if (nombre) {//Una persona sin apellido
              nombreCompleto = nombre;
            } else if (apellido) {//Una persona sin nombre
              nombreCompleto = apellido;
            } else if (nombreDescripcion) {//Contacto
              nombreCompleto = nombreDescripcion;
            } else if (descripcion) {//Facturas, insumos, etc
              nombreCompleto = descripcion;
            }
            return RenderBotonEditar(row.original?.id, nombreCompleto);
          },
        },
        {
          Header: 'Borrar',
          //Cell: ({ row }) => RenderBotonBorrar(row.original?.id, row.original?.nombreDescripcion, ContactoService),
          Cell: ({ row }) => {
            const { nombre, apellido, nombreDescripcion, descripcion } = row.original || {};
            let nombreCompleto = '';//Vacío (solo mostrar "desea borar id: 1" en vez de nombre)
            if (nombre && apellido) {//Beneficiarios, Empleados, etc
              nombreCompleto = `${nombre} ${apellido}`;
            } else if (nombre) {//Una persona sin apellido
              nombreCompleto = nombre;
            } else if (apellido) {//Una persona sin nombre
              nombreCompleto = apellido;
            } else if (nombreDescripcion) {//Contacto
              nombreCompleto = nombreDescripcion;
            } else if (descripcion) {//Facturas, insumos, etc
              nombreCompleto = descripcion;
            }
            return RenderBotonBorrar(row.original?.id, nombreCompleto, Service);
          },
        }
      );
    }

  return baseColumns;
  }, [columnsIn, visibilidad]);

  useEffect(() => {
    if (dataIn) {
      setData(dataIn);
    } else{
      Service.getAll().then((res) => {
        setData(res.data);
      });
    }
  }, []);


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
            &nbsp;&nbsp;&nbsp;
            <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/read")}>
              <span style={{ display: 'flex', alignItems: 'center' }}>
                <FaRegEdit style={{ marginRight: '5px', fontSize: '20px' }}/> Ver {nombreTipoDato}
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

export {
  TablaGenericaPersona,
  TablaGenericaConFoto,
  TablaGenerica,
};