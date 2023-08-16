import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
//import OldBaseService from '../../../services/trash-can/OldBaseService';
import { useNavigate } from 'react-router-dom';
//import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
//import { PersonaCreateInput } from '../Constants/ConstantsInputModel';
import {IngreseIdAsociar} from '../Constants/ConstantsInput';
import { PersonaCreateInput } from '../Constants/ConstantsInputModel';

/**
 * ejemplo: (cargarPersonaDefault, PersonaCreateInput, PersonaService, "la", "persona", "/personafisica")
 */
//function CreateBaseComponent(cargarBaseDefault, BaseCreateInput, Service, el_la, nombreBase, direccionTable) {
const CreateBaseComponent = (cargarBaseDefault, BaseCreateInput, Service, el_la, nombreBase, direccionTable) => {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const [baseModel, setBaseModel] = useState(cargarBaseDefault);
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
        setBaseModel(cargarBaseDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        if (checkBtnSearch.current.context._errors.length === 0) {
            Service.search(idToSearch).then
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
                    setBaseModel(prevBase => ({ ...prevBase, ...response.data }));
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
        setBaseModel({ ...baseModel, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
       let data = {...baseModel}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            Service.create(data).then
                (response => {
                    setBaseModel(prevPerson => ({ ...prevPerson, ...response.data }));
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

    const newBaseModel = () => {
        setBaseModel(cargarBaseDefault);
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
        navigate(direccionTable);//"/personafisica"
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
                                <h4>¡Has cargado {el_la} {nombreBase}!</h4>
                                <button className="btn btn-success" onClick={newBaseModel}>
                                  Agregar {el_la==="la" ? "nueva" : "nuevo"} {nombreBase}
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
                                        <button className="btn btn-light" onClick={newBaseModel} style={{marginLeft: "00px"}}>
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
                                            {/*
                                            <div className = "form-group">
                                                <label> Ingrese ID del contacto a asociar (anteriormente ya cargado): </label>
                                                <Input placeholder="Ingrese ID" id="idToSearch" name="idSearch" type="number" className="form-control" 
                                                    value={idToSearch} onChange={onChangeIdToSearch} validations={[required]}/>
                                            </div>
                                            */}
                                            <IngreseIdAsociar idToSearch={idToSearch} onChangeIdToSearch={onChangeIdToSearch} />

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
                                    
                                    {console.log("Llegue aqui 1")}
                                    {/*<BaseCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={baseModel} handleInputChange={handleInputChange} />*/}
                                    <PersonaCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={baseModel} handleInputChange={handleInputChange} />
                                    {console.log("Llegue aqui 2")}

                                    <div className="form-group">
                                        <button className="btn btn-success" href="#" disabled={loading}>
                                            <span></span>
                                            <span></span>
                                            <span></span>
                                            <span></span>
                                            {loading && (
                                                <span className="spinner-border spinner-border-sm"></span>
                                            )}
                                            Cargar {nombreBase}
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

export default CreateBaseComponent;