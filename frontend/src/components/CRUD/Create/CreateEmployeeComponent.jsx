import React, { Component } from 'react'
import BaseService from '../../../services/BaseService';
import {useLocation} from 'react-router-dom';

function CreateEmployeeComponent() {

    const location = useLocation();

    console.log(location.state.seccionURL);

    //this.changeFirstNameHandler = this.changeFirstNameHandler.bind(this);
    //this.changeLastNameHandler = this.changeLastNameHandler.bind(this);
    //this.saveOrUpdateEmployee = this.saveOrUpdateEmployee.bind(this);



    // step 3 //Se ejecuta luego del primer render
    //Esto ya no sirve en react hooks (react 16)
    //Ahora se usa useEffects
    const componentDidMount = () => {

        // step 4
        if(location.state.id === '_add'){
            return
        }else{
            BaseService.getById(location.state.id).then( (res) =>{
                let employee = res.data;
                location.setState({firstName: employee.firstName,
                    lastName: employee.lastName,
                    emailId : employee.emailId
                });
            });
        }        
    };


    const saveOrUpdateEmployee = (e) => {
        e.preventDefault();
        let employee = {firstName: location.state.firstName, lastName: location.state.lastName, emailId: location.state.emailId};
        console.log('employee => ' + JSON.stringify(employee));

        // step 5
        if(location.state.id === '_add'){
            BaseService.create(employee).then(res =>{
                location.props.history.push('/employees');
            });
        }else{
            BaseService.update(employee, location.state.id).then( res => {
                location.props.history.push('/employees');
            });
        }
    };
    
    const changeFirstNameHandler= (event) => {
        location.setState({firstName: event.target.value});
    }

    const changeLastNameHandler= (event) => {
        location.setState({lastName: event.target.value});
    }

    const changeEmailHandler= (event) => {
        location.setState({emailId: event.target.value});
    }

    const cancel = () => {
        location.props.history.push('/employees');
    }

    const getTitle = () => {
        if( 0 === '_add'){
            return <h3 className="text-center">Add Employee</h3>
        }else{
            return <h3 className="text-center">Update Employee</h3>
        }
    }
    
  return (
    <div>
    <br></br>
       <div className = "container">
            <div className = "row">
                <div className = "card col-md-6 offset-md-3 offset-md-3">
                    {
                        
                    }
                    <div className = "card-body">
                        <form>
                            <div className = "form-group">
                                <label> First Name: </label>
                                <input placeholder="First Name" name="firstName" className="form-control" 
                                    value={location.state.firstName} onChange={location.changeFirstNameHandler}/>
                            </div>
                            <div className = "form-group">
                                <label> Last Name: </label>
                                <input placeholder="Last Name" name="lastName" className="form-control" 
                                    value={location.state.lastName} onChange={location.changeLastNameHandler}/>
                            </div>
                            <div className = "form-group">
                                <label> Email Id: </label>
                                <input placeholder="Email Address" name="emailId" className="form-control" 
                                    value={location.state.emailId} onChange={location.changeEmailHandler}/>
                            </div>

                            <button className="btn btn-success" onClick={location.saveOrUpdateEmployee}>Save</button>
                            <button className="btn btn-danger"  style={{marginLeft: "10px"}}>Cancel</button>
                        </form>
                    </div>
                </div>
            </div>

       </div>
</div>
  )
}

export default CreateEmployeeComponent