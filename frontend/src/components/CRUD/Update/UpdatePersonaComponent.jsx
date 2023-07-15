import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import PersonaService from '../../../services/PersonaService';
import ImageService from '../../../services/ImageService';
import { useNavigate, useLocation } from 'react-router-dom';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaUpdateInput } from '../Constants/ConstantsInputModel';
import { FotoPerfil } from '../Constants/ConstantsCrudElements';
import { required } from '../Constants/ConstantsInput';

import "../../../Styles/CRUD.scss";

//Reciclado de UpdatePersonaComponent, y reciclado de create
function UpdatePersonaComponent() {
    let navigate = useNavigate();
    const location = useLocation();

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
    const [mostrarSearchID, setMostrarSearchID] = useState(true);
    const [searchEncontrado, setSearchEncontrado] = useState(false);//Solo con ID es suficiente
    const [forzarRenderizado, setForzarRenderizado ] = useState(false);
    const onChangeIdToSearch = (e) => {
        const idToSearch = e.target.value;
        setIdToSearch(idToSearch);
    };
    
    //Para el botón
    const handleSearch = (e) => {
        e.preventDefault();
        handleEntrySearch();
    };

    //Para cuando ingresas id desde la tabla (botón "editar")
    const handleEntrySearch = () => {
        //e.preventDefault();
        //console.log("Mostrar: " + mostrarSearchID);
        setPersona(cargarPersonaDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setSearchEncontrado(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        //console.log("location.state.id: " + location.state.id);
        if (checkBtnSearch.current.context._errors.length === 0) {
            PersonaService.getById(idToSearch ? idToSearch : location.state.id).then
                (response => {
                    if(response.data.id)
                        setSearchEncontrado(true);
                    setPersona(prevPersona => ({ ...prevPersona, ...response.data }));
                    setLoadingSearch(false);
                    setMostrarSearchID(false);
                    window.scrollTo({ top: 0, behavior: "smooth" });
                    console.log("Entre aquí");
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
            PersonaService.update(data).then
                (response => {
                    setPersona(prevPersona => ({ ...prevPersona, ...response.data }));
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
        setMostrarSearchID(true);
        setSearchEncontrado(false);
        setLoadingSearch(false);
        forzarRender();
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    const forzarRender = () => {
        setForzarRenderizado(true);
        setTimeout(() => {
            setForzarRenderizado(false);
        }, 100);
    }

    const cancel = () => {
        navigate("/personafisica");
        //window.location.reload();
    }

    useEffect(() => {
        if(location.state && location.state.id){
            window.scrollTo({ top: 0, behavior: "smooth" });
            //console.log(location.state.id);
            setIdToSearch(location.state.id);
            //console.log(idToSearch);
            handleEntrySearch();
        }
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    const [isFotoClicked, setIsFotoClicked] = useState(false);

    const handleFotoClick = () => {
        setIsFotoClicked(!isFotoClicked);
    };

    const [fotoSubida, setFotoSubida] = useState(false);
    const handleFotoChange = () => {
        if(fotoSubida && persona.id){
            console.log("Voy a subirlo");
            console.log(fotoSubida);
            console.log("Voy a subirlo");
            ImageService.uploadImageContacto(persona.id, fotoSubida).then
                (response => {
                    console.log("Se subió la foto :D");
                    console.log(response);
                },
                (error) => {
                    console.log("error:");
                    console.log(error);
                }
            );
        }
        else{
            console.log("No hay foto para subir");
            console.log("fotoSubida:");
            console.log(fotoSubida);
            console.log("persona.id");
            console.log(persona.id);
        }
    }

    useEffect(() => {
        console.log("Cambió fotoSubida");
        console.log(fotoSubida);
    }, [fotoSubida]);
    
    return (
        <div className="submit-form">
            <div>
                {/**<div className = "container">*/}
                <div>
                    <div className = "row">
                        <div className = "card col-md-6 offset-md-3 offset-md-3">
                          {!submitted ? (
                            <div className = "card-body">
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
                                    </div>
                                )}
                                {(searchEncontrado && !mostrarSearchID) && (
                                    <div className = "form-group">
                                        <div>
                                        <button className="btn btn-light" onClick={newPersona} style={{marginLeft: "00px"}}>
                                            Cancelar/Realizar otra Búsqueda
                                        </button>
                                        </div>
                                        <div>
                                            {(searchEncontrado) && (
                                                <label style={{color: '#86af49'}}> ¡Persona a modificar encontrado! </label>
                                            )}
                                        </div>
                                    </div>
                                )}
                                {(forzarRenderizado) && (<div></div>)}
                                {(!mostrarSearchID) && (
                                    <div className="form-group">
                                        <FotoPerfil id={persona.id} setFotoSubida={setFotoSubida} visibilidad={"EDITAR"} />
                                        <Form onSubmit={handleSubmit} ref={form}>
                                            <PersonaUpdateInput data={persona} handleInputChange={handleInputChange} />

                                            <div className="form-group">
                                                <button className="btn btn-success" href="#" disabled={loading}>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    {loading && (
                                                        <span className="spinner-border spinner-border-sm"></span>
                                                    )}
                                                    Modificar Persona
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
                                        <button className="btn btn-danger" onClick={handleFotoChange} style={{marginLeft: "00px", marginRight: "10px"}}>
                                            Subir foto :v
                                        </button>
                                        <button className="btn btn-danger" onClick={cancel} style={{marginLeft: "00px"}}>
                                            Cancelar
                                        </button>
                                    </div>
                                )}
                            </div>
                          ) : (
                            <div>
                                <h4>¡Has modificado la persona!</h4>
                                <button className="btn btn-success" onClick={newPersona}>
                                  Modificar otra persona
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
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

export default UpdatePersonaComponent;