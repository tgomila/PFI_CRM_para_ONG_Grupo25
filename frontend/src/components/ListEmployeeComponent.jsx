import React, { Component } from 'react'
import EmployeeService from '../services/EmployeeService'
import ContactoComponent from './ContactoComponent'

class ListEmployeeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
                employees: []
        }
        this.addEmployee = this.addEmployee.bind(this);
        this.editEmployee = this.editEmployee.bind(this);
        this.deleteEmployee = this.deleteEmployee.bind(this);
    
        
        this.state.showMiContacto = true;
    
    }

    deleteEmployee(id){
        EmployeeService.deleteEmployee(id).then( res => {
            this.setState({employees: this.state.employees.filter(employee => employee.id !== id)});
        });
    }
    viewEmployee(id){
        this.props.history.push(`/view-employee/${id}`);
    }

    //te redirige el router a la direccion "/add-employee/" + "id" 
    editEmployee(id){
        this.props.history.push(`/add-employee/${id}`);
    }

    //Se llama al Endpoint y se trae todo los datos
    componentDidMount(){
        EmployeeService.getEmployees().then((res) => {
            this.setState({ employees: res.data});
        }); 
    }

    addEmployee(){
        this.props.history.push('/add-employee/_add');
    }

    render() {
        return (
            <div>
                 <h2 className="text-center">Employees List</h2>
                 <div className = "row">
                    <button className="btn btn-primary" onClick={this.addEmployee}> Add Employee</button>
                 </div>
                 <br></br>
                 <div className = "row" style={this.state.showMiContacto ? {} : { display: 'none' }} >
                        

                        <table className = "table table-striped table-bordered">

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
                                    this.state.employees.map(
                                        employee => 
                                        <tr key = {employee.tenantClientId}>
                                            <td> { employee.tenantClientId} </td>   
                                            <td> {employee.name}</td>
                                            <td> {employee.tenantClientId}</td>
                                            <td>
                                                <button onClick={ () => this.editEmployee(employee.tenantClientId)} className="btn btn-info">Update </button>
                                                <button style={{marginLeft: "10px"}} onClick={ () => this.deleteEmployee(employee.tenantClientId)} className="btn btn-danger">Delete </button>
                                                <button style={{marginLeft: "10px"}} onClick={ () => this.viewEmployee(employee.tenantClientId)} className="btn btn-info">View </button>
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                            </table>

                 </div>

            </div>
        )
    }
}

export default ListEmployeeComponent
