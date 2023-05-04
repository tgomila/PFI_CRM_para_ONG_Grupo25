import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import PersonaService from '../../../services/PersonaService';
import { useNavigate } from 'react-router-dom';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaCreateInput } from '../Constants/ConstantsInputModel';
import { required } from '../Constants/ConstantsInput';

function CreatePersonaComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const [persona, setPersona] = useState(cargarPersonaDefault);
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
    //const [mostrarLabelSearchEncontrado, setMostrarLabelSearchEncontrado ] = useState(false);
    const [forzarRenderizado, setForzarRenderizado ] = useState(false);
    const onChangeIdToSearch = (e) => {
        const idToSearch = e.target.value;
        setIdToSearch(idToSearch);
      };
    const handleSearch = (e) => {
        e.preventDefault();
        setPersona(cargarPersonaDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        if (checkBtnSearch.current.context._errors.length === 0) {
            PersonaService.search(idToSearch).then
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
                    setPersona(prevPerson => ({ ...prevPerson, ...response.data }));
                    setLoadingSearch(false);
                    changeShowNoSearch();
                    //setMostrarLabelSearchEncontrado(true);
                    //setTimeout(() => {
                    //    setMostrarLabelSearchEncontrado(false);
                    //}, 5000);
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
        setPersona({ ...persona, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
       let data = {...persona}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            PersonaService.create(data).then
                (response => {
                    setPersona(prevPerson => ({ ...prevPerson, ...response.data }));
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
        console.log("Antes: " + contactoSearchEncontrado);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        forzarRender();
        console.log("Cambie a false");
        console.log("Despues: " + contactoSearchEncontrado);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    /*const changeShowSearch = () => {
        if(mostrarSearchID)
            setMostrarSearchID(false);
        else
            setMostrarSearchID(true);

    }*/

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
        navigate("/personafisica");
        window.location.reload();
    }
    
    return (
        <div className="submit-form">
            <div>
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
                                        <button className="btn btn-light" onClick={newPersona} style={{marginLeft: "00px"}}>
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
                                    {//Antes solían estar los inputs aquí
                                    }
                                    <PersonaCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={persona} handleInputChange={handleInputChange} />

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
            <br></br><br></br>
        </div>
    );
};

export default CreatePersonaComponent;