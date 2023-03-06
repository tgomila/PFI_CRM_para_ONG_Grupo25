import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import ContactoService from '../../../services/ContactoService';
import {useLocation, useNavigate} from 'react-router-dom';

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
    const location = useLocation();
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarContactoDefault = {
        id: null,
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: ""
    }
    const [contacto, setContacto] = useState(cargarContactoDefault);
    const [submitted, setSubmitted] = useState(false);

    const handleInputChange = event => {
        const { name, value } = event.target;
        setContacto({ ...contacto, [name]: value });
    };

    const [descripcion, setDescripcion] = useState("");
    const onChangeDescripcion = (e) => {
        const descripcion = e.target.value;
        setDescripcion(descripcion);
    };

    const handleSubmit = (e) => {

        //PreventDefault hace que el dato no viaje en el url link expuesto
        e.preventDefault();

        setMessage("");
        setLoading(true);

        form.current.validateAll();



        var data = {
            id: contacto.id,
            nombreDescripcion: contacto.email,
            cuit: contacto.cuit,
            domicilio: contacto.domicilio,
            email: contacto.email,
            telefono: contacto.telefono
        }
        if (checkBtn.current.context._errors.length === 0) {
            ContactoService.create(data).then
                (response => {
                    setContacto({
                        id: response.data.id,
                        nombreDescripcion: response.data.nombreDescripcion,
                        cuit: response.data.cuit,
                        domicilio: response.data.domicilio,
                        email: response.data.email,
                        telefono: response.data.telefono
                    });
                console.log("Cambio a true submitted");
                setSubmitted(true);
                console.log(response.data);
                //location.props.history.push('/contacto');
                },
                (error) => {
                    const resMessage =
                      (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                      error.message ||
                      error.toString();
          
                    setLoading(false);
                    setMessage(resMessage);
                }
            );
        } else {
            setLoading(false);
        }
    };

    const newContacto = () => {
        setContacto(cargarContactoDefault);
        console.log("Cambio a false submitted");
        setSubmitted(false);
    }

    const cancel = () => {
        //location.props.history.push('/contacto');
        navigate("/contacto");
        window.location.reload();
    }
    
    return (
        <div className="submit-form">
            <div>
                <br></br>
                <div className = "container">
                    <div className = "row">
                        <div className = "card col-md-6 offset-md-3 offset-md-3">
                            {
                                
                            }
                          {submitted ? (
                            <div>
                                <h4>¡Has cargado el contacto!</h4>
                                <button className="btn btn-success" onClick={newContacto}>
                                  Agregar nuevo contacto
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
                                </button>
                            </div>
                          ) : (
                            <div className = "card-body">
                                <Form onSubmit={handleSubmit} ref={form}>
                                    <div className = "form-group">
                                        <label htmlFor="id"> ID: </label>
                                        <Input placeholder="ID" id="id" name="id" type="text" className="form-control" 
                                            value={contacto.id} onChange={handleInputChange}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Nombre/Descripción: </label>
                                        <Input placeholder="Nombre/Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={descripcion} onChange={onChangeDescripcion} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={contacto.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={contacto.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={contacto.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={contacto.telefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className="form-group">
                                        <button className="btn btn-success" href="#" disabled={loading}>
                                            <span></span>
                                            <span></span>
                                            <span></span>
                                            <span></span>
                                            {loading && (
                                                <span className="spinner-border spinner-border-sm"></span>
                                            )}
                                            Cargar contacto
                                        </button>
                                    </div>
                                    {message && (
                                        <div className="form-group">
                                            <div className="alert alert-danger" role="alert">
                                                {message}
                                            </div>
                                        </div>
                                    )}
                                    {/*
                                    <button className="btn btn-success" onClick={handleSubmit}>
                                        Añadir
                                    </button>
                                    <button className="btn btn-danger" onClick={cancel} style={{marginLeft: "10px"}}>
                                        Cancel
                                    </button>
                                    */}
                                    <CheckButton style={{ display: "none" }} ref={checkBtn} />
                                </Form>
                                <button className="btn btn-danger" onClick={cancel} style={{marginLeft: "00px"}}>
                                    Cancelar
                                </button>
                            </div>
                          )}
                        </div>
                    </div>
        
                </div>
            </div>
        </div>
    );
};

export default CreateContactoComponent