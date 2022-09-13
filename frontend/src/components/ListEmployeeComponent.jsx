import React, { useState, useEffect  } from "react";
import EmployeeService from "../services/EmployeeService";


import { useTable } from "react-table";



function Table({ columns, data }) {
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow
  } = useTable({
    columns,
    data
  });

  // Render the UI for your table
  return (
    <table>
      <thead>
        {headerGroups.map((headerGroup) => (
          <tr {...headerGroup.getHeaderGroupProps()}>
            {headerGroup.headers.map((column) => (
              <th {...column.getHeaderProps()}>{column.render("Header")}</th>
            ))}
          </tr>
        ))}
      </thead>

      <tbody {...getTableBodyProps()}>
        {rows.map((row, i) => {
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

    setTimeout(() => {
      EmployeeService.getEmployees(redireccionamiento).then((res) => {
        setEmployees(res.data);
      });
    }, 3000);
    
    //console.log(employees);
    

    setData(employees);
  }, [employees]);

  

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



        {/*
        <table className="table table-striped table-bordered">
          <thead>


                




            <tr>
              <th> Employee First Name</th>
              <th> Employee Last Name</th>
              <th> Employee Email Id</th>
              <th> Actions</th>
            </tr>
          </thead>

          <tbody>


      

            {
            employees.map((employee) => (
                



              <tr key={employee.tenantClientId}>
                <td> {employee.tenantClientId} </td>
                <td> {employee.name}</td>
                <td> {employee.tenantClientId}</td>
                <td className="actionTable">
                  <button
                    onClick={() => this.editEmployee(employee.tenantClientId)}
                    className="btn btn-info"
                  >
                    Update{" "}
                  </button>
                  <button
                    style={{ marginLeft: "10px" }}
                    onClick={() => this.deleteEmployee(employee.tenantClientId)}
                    className="btn btn-danger"
                  >
                    Delete{" "}
                  </button>
                  <button
                    style={{ marginLeft: "10px" }}
                    onClick={() => this.viewEmployee(employee.tenantClientId)}
                    className="btn btn-info"
                  >
                    View{" "}
                  </button>
                </td>
              </tr>
            
            
            ))}
          </tbody>
        </table>

            */}

      </div>
    </div>
  );
}

export default ListEmployeeComponent;
