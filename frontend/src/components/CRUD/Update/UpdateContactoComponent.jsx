import React, { useState, useEffect, Component } from 'react'
import BaseService from '../../../services/BaseService';
import {useLocation} from 'react-router-dom';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        ¡Este campo es obligatorio!
      </div>
    );
  }
};

//Cosas a cambiar si se recicla:
//  - const redireccionamiento = ...
//  - const cargarDefault = ...

function CreateContactoComponent() {
    const redireccionamiento = 'contacto';

    useEffect(() => {
        if(id && id==='add')
            cargarDefault();
        else if(id)
            cargarDto(id);
        else
            cargarDefault();
    }, [id]);

    const cargarDefault = {
        id: null,
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: ""
    }
    const [currentDto, setDto] = useState(cargarDefault);
    const [message, setMessage] = useState("");

    const getDto = id => {
        BaseService.getById(redireccionamiento, id)
        .then(response => {
            setDto(response.data);
            console.log(response.data);
        })
        .catch(e => {
            console.log(e);
        });
    };

    
    

    const handleSubmit = (e) => {
        const contacto = {
            id: this.state.id,
            nombreDescripcion: this.state.email,
            cuit: this.state.cuit,
            domicilio: this.state.domicilio,
            email: this.state.email,
            telefono: this.state.telefono
        }
        
        BaseService.create(contacto).then(res =>{
            location.props.history.push('/contacto');
        });

    }
    
    this.changeIdHandler = this.changeIdHandler.bind(this);
    this.changeNombreDescripcionHandler = this.changeNombreDescripcionHandler.bind(this);
    this.changeCuitHandler = this.changeCuitHandler.bind(this);
    this.changeDomicilioHandler = this.changeDomicilioHandler.bind(this);
    this.changeEmailHandler = this.changeEmailHandler.bind(this);
    this.changeTelefonoHandler = this.changeTelefonoHandler.bind(this);
    this.saveOrUpdateContacto = this.saveOrUpdateContacto.bind(this);


    const saveOrUpdateContacto = (e) => {
        e.preventDefault();
        let contacto = {
            id: location.state.id, 
            nombreDescripcion: location.state.nombreDescripcion, 
            cuit: location.state.cuit, 
            domicilio: location.state.domicilio, 
            email: location.state.email, 
            telefono: location.state.telefono
        };
        console.log('contacto => ' + JSON.stringify(contacto));

        // step 5
        if(location.state.id === '_add'){
            BaseService.create(contacto).then(res =>{
                location.props.history.push('/contacto');
            });
        }else{
            BaseService.update(contacto, location.state.id).then( res => {
                location.props.history.push('/contacto');
            });
        }
    };
    
    const changeIdHandler= (event) => {
        location.setState({id: event.target.value});
    }

    const changeNombreDescripcionHandler= (event) => {
        location.setState({nombreDescripcion: event.target.value});
    }

    const changeCuitHandler= (event) => {
        location.setState({cuit: event.target.value});
    }

    const changeDomicilioHandler= (event) => {
        location.setState({domicilio: event.target.value});
    }

    const changeEmailHandler= (event) => {
        location.setState({email: event.target.value});
    }

    const changeTelefonoHandler= (event) => {
        location.setState({telefono: event.target.value});
    }

    const cancel = () => {
        location.props.history.push('/contacto');
    }

    const getTitle = () => {
        if( 0 === '_add'){
            return <h3 className="text-center">Add Contacto</h3>
        }else{
            return <h3 className="text-center">Update Contacto</h3>
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
                                <label> ID: </label>
                                <input placeholder="ID" name="id" className="form-control" 
                                    value={location.state.id} onChange={location.changeIdHandler}/>
                            </div>

                            <div className = "form-group">
                                <label> Nombre/Descripción: </label>
                                <input placeholder="Nombre/Descripción" name="nombreDescripcion" className="form-control" 
                                    value={location.state.nombreDescripcion} onChange={location.changeNombreDescripcionHandler}/>
                            </div>

                            <div className = "form-group">
                                <label> Cuit: </label>
                                <input placeholder="Cuit" name="cuit" className="form-control" 
                                    value={location.state.cuit} onChange={location.changeCuitHandler}/>
                            </div>

                            <div className = "form-group">
                                <label> Domicilio: </label>
                                <input placeholder="Domicilio" name="domicilio" className="form-control" 
                                    value={location.state.domicilio} onChange={location.changeDomicilioHandler}/>
                            </div>

                            <div className = "form-group">
                                <label> Email: </label>
                                <input placeholder="Email" name="email" className="form-control" 
                                    value={location.state.email} onChange={location.changeEmailHandler}/>
                            </div>

                            <div className = "form-group">
                                <label> Telefono: </label>
                                <input placeholder="Telefono" name="telefono" className="form-control" 
                                    value={location.state.telefono} onChange={location.changeTelefonoHandler}/>
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

export default CreateContactoComponent