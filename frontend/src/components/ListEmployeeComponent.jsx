import React, { useState, useEffect  } from "react";
import EmployeeService from "../services/EmployeeService";


import { useTable, usePagination } from "react-table";




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
    prepareRow
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 }
    },
    usePagination
  );




  const { pageIndex, pageSize } = state;

  // Render the UI for your table
  return (
    <div>

      
    <table>
      <thead>
        {headerGroups.map((headerGroup) => (
          <tr {...headerGroup.getHeaderGroupProps()}>
            {headerGroup.headers.map((column) => (
              <th {...column.getHeaderProps()}>{column.render("Header").toUpperCase()}</th>
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
                return <td {...cell.getCellProps()}>{cell.render("Cell")}</td>;
              })}
            </tr>
          );
        })}
      </tbody>
    </table>


    

    <div>
        <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
          {"<<"}
        </button>{" "}
        <button onClick={() => previousPage()} disabled={!canPreviousPage}>
          Previous
        </button>{" "}
        <button onClick={() => nextPage()} disabled={!canNextPage}>
          Next
        </button>{" "}
        <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
          {">>"}
        </button>{" "}
        <span>
          Page{" "}
          <strong>
            {pageIndex + 1} of {pageOptions.length}
          </strong>{" "}
        </span>
        <span>
          | Go to page:{" "}
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
        <select
          value={pageSize}
          onChange={(e) => setPageSize(Number(e.target.value))}
        >
          {[10, 25, 50].map((pageSize) => (
            <option key={pageSize} value={pageSize}>
              Show {pageSize}
            </option>
          ))}
        </select>
      </div>


      
    </div>


  );
}





function ListEmployeeComponent(redireccionamiento) {





  const [data, setData] = useState([]);



  console.log(redireccionamiento);

  const [employees, setEmployees] = useState([]);

  const actualizarTabla = () => {};

  //Se llama al Endpoint y se trae todo los datos
  const componentDidMount = () => {};



  useEffect(() => {
    // Fetch data
    // Update the document title using the browser API

      EmployeeService.getEmployees(redireccionamiento).then((res) => {
        setData(res.data);
      });
    
    //console.log(employees);
    

  }, []);

  

  const columns = Object.keys(data[0] || []).map((key) => ({
    Header: key,
    accessor: key
  }));



  React.useEffect(() => {

    /*
    // Update the document title using the browser API
    EmployeeService.getEmployees(redireccionamiento).then((res) => {
      setEmployees(res.data);
    });
    console.log(employees);
*/
  }, []);

  //this.addEmployee = this.addEmployee.bind(this);
  //this.editEmployee = this.editEmployee.bind(this);
  //this.deleteEmployee = this.deleteEmployee.bind(this);

  //this.state.showMiContacto = true;

  const deleteEmployee = (id) => {
    EmployeeService.deleteEmployee(id).then((res) => {
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
    <div className="ComponentePrincipal">
    <h2 className="TituloComponentePrincipal">Employees List</h2>

      <div className="row">
        <button className="btn btn-primary" onClick={addEmployee}>
          {" "}
          Add Employee
        </button>
      </div>
      <br></br>
      <div className="row">



      <Table columns={columns} data={data} />


      </div>
    </div>
  );
}

export default ListEmployeeComponent;