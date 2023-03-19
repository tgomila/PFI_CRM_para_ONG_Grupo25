import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import PersonaService from '../../../services/PersonaService';
import {useNavigate} from 'react-router-dom';
import { format, subYears } from 'date-fns';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        ¡Este campo es obligatorio!
      </div>
    );
  }
};

function CreatePersonaComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarPersonaDefault = {
        id: "",
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: "",
        dni: "",
        nombre: "",
        apellido: "",
        fechaNacimiento: format(subYears(new Date(), 18), 'yyyy-MM-dd')
    }
    const [persona, setPersona] = useState(cargarPersonaDefault);
    const [submitted, setSubmitted] = useState(false);

    //Search de ID
    const formSearch = useRef();
    const checkBtnSearch = useRef();
    const [idToSearch, setIdToSearch] = useState("");
    const [loadingSearch, setLoadingSearch] = useState(false);
    const [messageSearch, setMessageSearch] = useState("");
    const [mostrarSearchID, setMostrarSearchID] = useState(false);
    const [mostrarLabelSearchEncontrado, setMostrarLabelSearchEncontrado ] = useState(false);
    const onChangeIdToSearch = (e) => {
        const idToSearch = e.target.value;
        setIdToSearch(idToSearch);
      };
    const handleSearch = (e) => {
        e.preventDefault();
        setPersona(cargarPersonaDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        if (checkBtnSearch.current.context._errors.length === 0) {
            PersonaService.search(idToSearch).then
                (response => {
                    setPersona({
                        id: response.data.id,
                        nombreDescripcion: response.data.nombreDescripcion,
                        cuit: response.data.cuit,
                        domicilio: response.data.domicilio,
                        email: response.data.email,
                        telefono: response.data.telefono,
                        dni: response.data.dni,
                        nombre: response.data.nombre,
                        apellido: response.data.apellido,
                        fechaNacimiento: response.data.fechaNacimiento
                    });
                    setLoadingSearch(false);
                    changeShowNoSearch();
                    setMostrarLabelSearchEncontrado(true);
                    setTimeout(() => {
                        setMostrarLabelSearchEncontrado(false);
                    }, 5000);
                    window.scrollTo({ top: 0, behavior: "smooth" });
                },
                (error) => {
                    const resMessage =
                      (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                      error.message ||
                      error.toString();
          
                    setLoadingSearch(false);
                    setMessageSearch(resMessage);
                }
            );

        }
    }

    const handleInputChange = event => {
        const { name, value } = event.target;
        //console.log("inicio event:");
        //console.log(event);
        //console.log("Fin event");
        //console.log("inicio event.target:");
        //console.log(event.target);
        //console.log("Fin event.target");
        //console.log("name: " + name + ", value: " + value);
        setPersona({ ...persona, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
        var data = {
            id: persona.id,
            nombreDescripcion: persona.nombreDescripcion,
            cuit: persona.cuit,
            domicilio: persona.domicilio,
            email: persona.email,
            telefono: persona.telefono,
            dni: persona.dni,
            nombre: persona.nombre,
            apellido: persona.apellido,
            fechaNacimiento: persona.fechaNacimiento
        }
        if (checkBtn.current.context._errors.length === 0) {
            PersonaService.create(data).then
                (response => {
                    setPersona({
                        id: response.data.id,
                        nombreDescripcion: response.data.nombreDescripcion,
                        cuit: response.data.cuit,
                        domicilio: response.data.domicilio,
                        email: response.data.email,
                        telefono: response.data.telefono,
                        dni: response.data.dni,
                        nombre: response.data.nombre,
                        apellido: response.data.apellido,
                        fechaNacimiento: response.data.fechaNacimiento
                    });
                setSubmitted(true);
                window.scrollTo({ top: 0, behavior: "smooth" }); //Para mostrar cartel "Has cargado X componente!"
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

    const newPersona = () => {
        setPersona(cargarPersonaDefault);
        setLoading(false);
        setSubmitted(false);
        setMostrarSearchID(false);
        setLoadingSearch(false);
    }

    /*const changeShowSearch = () => {
        if(mostrarSearchID)
            setMostrarSearchID(false);
        else
            setMostrarSearchID(true);

    }*/

    const changeShowSearch = () => {
        setMostrarSearchID(true);
    }

    const changeShowNoSearch = () => {
        setMostrarSearchID(false);
    }

    const cancel = () => {
        navigate("/personafisica");
        window.location.reload();
    }
    
    return (
        <div className="submit-form">
            <div>
                <br></br>
                <div className = "container">
                    <div className = "row">
                        <div className = "card col-md-6 offset-md-3 offset-md-3">
                          {submitted ? (
                            <div>
                                <h4>¡Has cargado la persona!</h4>
                                <button className="btn btn-success" onClick={newPersona}>
                                  Agregar nueva persona
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
                                </button>
                            </div>
                          ) : (
                            <div className = "card-body">
                                {(!persona.id && !mostrarSearchID) && (
                                    <div>
                                        <button className="btn btn-light" onClick={changeShowSearch} style={{marginLeft: "00px"}}>
                                            ¿Fue cargado anteriormente y desea asociarlo? Presione aquí
                                        </button>
                                    </div>
                                )}
                                {(persona.id && !mostrarSearchID) && (
                                    <div className = "form-group">
                                        <div>
                                        <button className="btn btn-light" onClick={changeShowSearch} style={{marginLeft: "00px"}}>
                                            Realizar otra Búsqueda
                                        </button>
                                        <button className="btn btn-light" onClick={newPersona} style={{marginLeft: "00px"}}>
                                            Cancelar Búsqueda
                                        </button>
                                        </div>
                                        <div>
                                            <label style={{color: '#86af49'}}> ¡Contacto a asociar encontrado! </label>
                                        </div>
                                    </div>
                                )}
                                {(mostrarSearchID) && (
                                    <div className = "form-group">
                                        <Form onSubmit={handleSearch} ref={formSearch}>
                                            <div className = "form-group">
                                                <label> Ingrese ID del contacto a asociar (anteriormente ya cargado): </label>
                                                <Input placeholder="Ingrese ID" id="idToSearch" name="idSearch" type="number" className="form-control" 
                                                    value={idToSearch} onChange={onChangeIdToSearch} validations={[required]}/>
                                            </div>

                                            <div className="form-group">
                                                <button className="btn btn-success" href="#" disabled={loadingSearch}>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    {loadingSearch && (
                                                        <span className="spinner-border spinner-border-sm"></span>
                                                    )}
                                                    Buscar ID
                                                </button>
                                            </div>
                                            {messageSearch && (
                                                <div className="form-group">
                                                    <div className="alert alert-danger" role="alert">
                                                        {messageSearch}
                                                    </div>
                                                </div>
                                            )}
                                            <CheckButton style={{ display: "none" }} ref={checkBtnSearch} />
                                        </Form>
                                        
                                        <button className="btn btn-danger" onClick={changeShowNoSearch} style={{marginLeft: "00px"}}>
                                            Cancelar
                                        </button>
                                    </div>
                                )}
                                <Form onSubmit={handleSubmit} ref={form}>
                                    {persona.id && (
                                        <div className = "form-group">
                                            <label htmlFor="id"> ID: </label>
                                            <Input disabled={persona.id} placeholder="ID" id="id" name="id" type="number" className="form-control" 
                                            value={persona.id} onChange={handleInputChange}/>
                                        </div>
                                    )}

                                    <div className = "form-group">
                                        <label> Nombre: </label>
                                        <Input placeholder="Nombre" id="nombre" name="nombre" type="text" className="form-control" 
                                            value={persona.nombre} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Apellido: </label>
                                        <Input placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                                            value={persona.apellido} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> DNI: </label>
                                        <Input placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                                            value={persona.dni} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Fecha de nacimiento: </label>
                                        <Input placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                                            value={persona.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                                            min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 1), 'yyyy-MM-dd')}/>
                                    </div>
                                    
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input disabled={persona.id} placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={persona.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input disabled={persona.id} placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={persona.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input disabled={persona.id} placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={persona.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input disabled={persona.id} placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={persona.telefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Descripción: </label>
                                        <Input disabled={persona.id} placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={persona.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
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
                                            Cargar persona
                                        </button>
                                    </div>
                                    {message && (
                                        <div className="form-group">
                                            <div className="alert alert-danger" role="alert">
                                                {message}
                                            </div>
                                        </div>
                                    )}
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

export default CreatePersonaComponent;