import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";
import CheckButton from "react-validation/build/button";
import ProfesionalService from '../../../services/ProfesionalService';
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

function CreateProfesionalComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarProfesionalDefault = {
        id: "",
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: "",
        dni: "",
        nombre: "",
        apellido: "",
        fechaNacimiento: format(subYears(new Date(), 18), 'yyyy-MM-dd'),
        datosBancarios: "",
        profesion: ""
    };
    const [profesional, setProfesional] = useState(cargarProfesionalDefault);
    const [submitted, setSubmitted] = useState(false);

    //Search de ID
    const formSearch = useRef();
    const checkBtnSearch = useRef();
    const [idToSearch, setIdToSearch] = useState("");
    const [loadingSearch, setLoadingSearch] = useState(false);
    const [messageSearch, setMessageSearch] = useState("");
    const [mostrarSearchID, setMostrarSearchID] = useState(false);
    const [contactoSearchEncontrado, setContactoSearchEncontrado] = useState(false);//Solo con ID es suficiente
    const [personaSearchEncontrada, setPersonaSearchEncontrada] = useState(false);//Este sirve para Empleado, etc para reciclado
    const [forzarRenderizado, setForzarRenderizado ] = useState(false);
    const onChangeIdToSearch = (e) => {
        const idToSearch = e.target.value;
        setIdToSearch(idToSearch);
      };
    const handleSearch = (e) => {
        e.preventDefault();
        setProfesional(cargarProfesionalDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        if (checkBtnSearch.current.context._errors.length === 0) {
            ProfesionalService.search(idToSearch).then
                (response => {
                    if(response.data.id)
                        setContactoSearchEncontrado(true);
                    if(response.data.dni)
                        setPersonaSearchEncontrada(true);
                    //setP({
                    //    id: response.data.id,
                    //    nombreDescripcion: response.data.nombreDescripcion,
                    //    //...sigue
                    //});
                    setProfesional(prevPerson => ({ ...prevPerson, ...response.data }));
                    setLoadingSearch(false);
                    changeShowNoSearch();
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
        else{
            setLoadingSearch(false);
        }
    }

    const handleInputChange = event => {
        //Trae literalmente copia de "<Input", pero con value reemplazado con el value que escribió el usuario.
        const { name, value } = event.target;
        setProfesional({ ...profesional, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
       let data = {...profesional}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            ProfesionalService.create(data).then
                (response => {
                    setProfesional(prevPerson => ({ ...prevPerson, ...response.data }));
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

    const newProfesional = () => {
        setProfesional(cargarProfesionalDefault);
        setLoading(false);
        setSubmitted(false);
        setMostrarSearchID(false);
        setLoadingSearch(false);
        console.log("Antes: " + contactoSearchEncontrado);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        forzarRender();
        console.log("Cambie a false");
        console.log("Despues: " + contactoSearchEncontrado);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    const forzarRender = () => {
        setForzarRenderizado(true);
        setTimeout(() => {
            setForzarRenderizado(false);
        }, 100);
    }

    const changeShowSearch = () => {
        setMostrarSearchID(true);
    }

    const changeShowNoSearch = () => {
        setMostrarSearchID(false);
    }

    const cancel = () => {
        navigate("/profesional");
        window.location.reload();
    }
    
    return (
        <div className="submit-form">
            <div>
                {/**<div className = "container">*/}
                <div>
                    <div className = "row">
                        <div className = "card col-md-6 offset-md-3 offset-md-3">
                          {submitted ? (
                            <div>
                                <h4>¡Has cargado el profesional!</h4>
                                <button className="btn btn-success" onClick={newProfesional}>
                                  Agregar nuevo profesional
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
                                </button>
                            </div>
                          ) : (
                            <div className = "card-body">
                                {(!contactoSearchEncontrado && !mostrarSearchID) && (
                                    <div>
                                        <button className="btn btn-light" onClick={changeShowSearch} style={{marginLeft: "00px"}}>
                                            ¿Fue cargado anteriormente y desea asociarlo? Presione aquí
                                        </button>
                                    </div>
                                )}
                                {(contactoSearchEncontrado && !mostrarSearchID) && (
                                    <div className = "form-group">
                                        <div>
                                        <button className="btn btn-light" onClick={changeShowSearch} style={{marginLeft: "00px"}}>
                                            Realizar otra Búsqueda
                                        </button>
                                        <button className="btn btn-light" onClick={newProfesional} style={{marginLeft: "00px"}}>
                                            Cancelar Búsqueda
                                        </button>
                                        </div>
                                        <div>
                                            {(contactoSearchEncontrado && !personaSearchEncontrada) && (
                                            <label style={{color: '#86af49'}}> ¡Contacto a asociar encontrado! </label>
                                            )}
                                            {(contactoSearchEncontrado && personaSearchEncontrada) && (
                                                <label style={{color: '#86af49'}}> ¡Persona a asociar encontrada! </label>
                                            )}
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
                                {(forzarRenderizado) && (<div></div>)}
                                <Form onSubmit={handleSubmit} ref={form}>
                                    {contactoSearchEncontrado && (
                                        <div className = "form-group">
                                            <label htmlFor="id"> ID: </label>
                                            <Input disabled="disabled" placeholder="ID" id="id" name="id" type="number" className="form-control" 
                                            value={profesional.id} onChange={handleInputChange}/>
                                        </div>
                                    )}

                                    <div className = "form-group">
                                        <label> Nombre: </label>
                                        <Input disabled={personaSearchEncontrada} placeholder="Nombre" id="nombre" name="nombre" type="text" className="form-control" 
                                            value={profesional.nombre} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Apellido: </label>
                                        <Input disabled={personaSearchEncontrada} placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                                            value={profesional.apellido} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> DNI: </label>
                                        <Input disabled={personaSearchEncontrada} placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                                            value={profesional.dni} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Fecha de nacimiento: </label>
                                        <Input disabled={personaSearchEncontrada} placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                                            value={profesional.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                                            min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 1), 'yyyy-MM-dd')}/>
                                    </div>
                                    
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input disabled={contactoSearchEncontrado} placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={profesional.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input disabled={contactoSearchEncontrado} placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={profesional.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input disabled={contactoSearchEncontrado} placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={profesional.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input disabled={contactoSearchEncontrado} placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={profesional.telefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Descripción: </label>
                                        <Input disabled={contactoSearchEncontrado} placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={profesional.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Datos bancarios: </label>
                                        <Input placeholder="Datos bancarios" name="datosBancarios" type="text" className="form-control" 
                                            value={profesional.datosBancarios} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Profesión: </label>
                                        <Input placeholder="Profesión" name="profesion" type="text" className="form-control" 
                                            value={profesional.profesion} onChange={handleInputChange} validations={[required]}/>
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
                                            Cargar profesional
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
            <br></br><br></br>
        </div>
    );
};

export default CreateProfesionalComponent;