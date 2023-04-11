import React, { useState, useEffect } from "react";
import BaseService from "../../services/BaseService";

import { useTable, usePagination } from "react-table";

import "../../Styles/TablasDinamicas.scss";

import { useNavigate } from 'react-router-dom';

import {
  Route,
  Routes,
  BrowserRouter
} from "react-router-dom";


function Table({ columns, data }) {
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
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
    },
    usePagination
  );

  const { pageIndex, pageSize } = state;
  window.scrollTo({ top: 0, behavior: "smooth" });

  // Render the UI for your table
  return (
    <div className="TablasDinamicas">
      <div className="tablaDinamica">
        <table>
          <thead>
            {headerGroups.map((headerGroup) => (
              <tr {...headerGroup.getHeaderGroupProps()}>
                {headerGroup.headers.map((column) => (
                  <th {...column.getHeaderProps()}>
                    {column.render("Header").toUpperCase()}
                  </th>
                ))}

                  <th>
                    EDITAR
                  </th>

                  <th>
                    BORRAR
                  </th>
    
              </tr>
            ))}
          </thead>

          <tbody {...getTableBodyProps()}>
            {page.map((row, i) => {
              prepareRow(row);
              return (
                <tr {...row.getRowProps()}>
                  {row.cells.map((cell) => {
                    console.log("Aqui test:");
                    console.log(cell);
                    return (
                      <td {...cell.getCellProps()}>{(cell.column.id!="id" && cell.value== true) ? "✅" : (cell.value== false) ? "❌" : cell.render("Cell")}</td>
                    );
                  })}

                  <td>
                    <button
                      className="buttonAnimadoVerde"
                    >
                      {" "}
                      Editar
                    </button>
                  </td>


                  <td>
                    <button
                      className="buttonAnimadoRojo"
                    >
                      {" "}
                      Borrar
                    </button>
                  </td>




                </tr>
              );
            })}
          </tbody>
        </table>

        <div>
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
  );
}

function TablasDinamicas(redireccionamiento) {
  const [data, setData] = useState([]);
  const [columnNames, setColumnNames] = useState([]);
  const [direccion, setDireccion] = useState(redireccionamiento);

  console.log(redireccionamiento);

  
  const navigate = useNavigate();

  const [employees, setEmployees] = useState([]);

  const actualizarTabla = () => {};

  //Se llama al Endpoint y se trae todo los datos
  const componentDidMount = () => {};



  useEffect(() => {
    // Fetch data
    // Update the document title using the browser API

    BaseService.getAll(redireccionamiento).then((res) => {
      setData(res.data);
    });

    BaseService.getColumnNames(redireccionamiento).then((res) => {
      setColumnNames(res.data);
    });

    setDireccion(redireccionamiento);

    console.log(direccion);

  }, [redireccionamiento]);

  //const columns = Object.keys(data[0] || []).map((key) => ({
  //const columns = Object.keys(columnNames || []).map((key) => ({
  const columns = Object.entries(columnNames || []).map(([key,value]) => ({
    Header: value.toString(),
    accessor: key,
  }));
  console.log("columnaaas: ");
  console.log(columns);
  console.log("Fin lectura columnas");

  //this.addEmployee = this.addEmployee.bind(this);
  //this.editEmployee = this.editEmployee.bind(this);
  //this.deleteEmployee = this.deleteEmployee.bind(this);

  //this.state.showMiContacto = true;

  const deleteEmployee = (id) => {
    BaseService.delete(id).then((res) => {
      this.setState({
        employees: this.state.employees.filter(
          (employee) => employee.id !== id
        ),
      });
    });
  };

  const viewEmployee = (id) => {
    this.props.history.push(`/view-employee/${id}`);
  };

  //te redirige el router a la direccion "/add-employee/" + "id"
  const editEmployee = (id) => {
    this.props.history.push(`/add-employee/${id}`);
  };

  const addEmployee = () => {
    this.props.history.push("/add-employee/_add");
  };

  return (
    <div className="TablasDinamicas">
      <div className="ComponentePrincipal">
        <h2 className="TituloComponentePrincipal">Tablas Dinámicas</h2>

        <div className="row">

        {/*<button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create", {state:{seccionURL:redireccionamiento.redireccionamiento, firstName:"tomas",lastName:"gomila",emailId:"tomas@gomila.com"}})}> Add Employee</button>*/}
        <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create", {state:{seccionURL:redireccionamiento.redireccionamiento, firstName:"tomas",lastName:"gomila",emailId:"tomas@gomila.com"}})}> Agregar {redireccionamiento.redireccionamiento}</button>
        &nbsp;&nbsp;&nbsp;
        <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/update", {state:{seccionURL:redireccionamiento.redireccionamiento, firstName:"tomas",lastName:"gomila",emailId:"tomas@gomila.com"}})}> Modificar {redireccionamiento.redireccionamiento}</button>


        </div>
        <br></br>
        <div className="row">
          <Table columns={columns} data={data} />
        </div>
      </div>
    </div>
  );
}

export default TablasDinamicas;
