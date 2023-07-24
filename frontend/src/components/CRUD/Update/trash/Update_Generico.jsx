import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import ImageService from '../../../../services/ImageService';
import { useNavigate, useLocation } from 'react-router-dom';
import { FotoPerfil } from '../../Constants/ConstantsCrudElements';
import { required } from '../../Constants/ConstantsInput';

import "../../../Styles/CRUD.scss";

//Si editas Update_Generico, recordá editar también Create_genérico, son casi iguales
/**
 * 
 * @param {Array} cargarDatosDefault traelo mediante ejemplo: import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
 * @param {string} DatoUpdateInput traelo mediante ejemplo: import { PersonaUpdateInput } from '../Constants/ConstantsInputModel';
 * @param {string} tipoDatoForImageService si es tipos de personas (empleado, beneficiario, etc) es 'contacto', sinó else, si es producto es 'producto'
 * @param {Object} Service ejemplo EmpleadoService
 * @param {string} urlTablaDato ejemplo '/empleado', es para cuando presiona el botón cancelar
 * @param {string} el_la 'el' o 'la'
 * @param {string} nombreTipoDato ejemplo 'empleado', 'beneficiario'
 * @returns 
 */
const UpdateGenericoConFoto = ({cargarDatosDefault, DatoUpdateInput, tipoDatoForImageService, Service, urlTablaDato, el_la, nombreTipoDato}) => {
    let navigate = useNavigate();
    const location = useLocation();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const [datos, setDatos] = useState(cargarDatosDefault);
    const [submitted, setSubmitted] = useState(false);
    const [submittedMessage, setSubmittedMessage] = useState("");

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

    //Para subir la foto
    const [fotoSubida, setFotoSubida] = useState(false);//Archivo de foto subido por el usuario
    const [submittedFoto, setSubmittedFoto] = useState(false);//Se hace true si el archivo de foto se subió al backend con éxito
    
    //Para el botón
    const handleSearch = (e) => {
        e.preventDefault();
        handleEntrySearch();
    };

    //Para cuando ingresas id desde la tabla (botón "editar")
    const handleEntrySearch = () => {
        //e.preventDefault();
        //console.log("Mostrar: " + mostrarSearchID);
        setDatos(cargarDatosDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setSearchEncontrado(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        //console.log("location.state.id: " + location.state.id);
        if (checkBtnSearch.current.context._errors.length === 0) {
            Service.getById(idToSearch ? idToSearch : location.state.id).then
                (response => {
                    if(response.data.id)
                        setSearchEncontrado(true);
                    setDatos(prevDatos => ({ ...prevDatos, ...response.data }));
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
        setDatos({ ...datos, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();

        let data = {...datos}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            Service.update(data).then
                (response => {
                    setDatos(prevDatos => ({ ...prevDatos, ...response.data }));
                    setSubmitted(true);//Info subida, aún la foto no subida.
                    setSubmittedMessage("¡Has modificado " + el_la_aux + " " + nombreTipoDato_aux + "!");
                    if(fotoSubida && datos.id){
                        console.log("Voy a subir la foto:");
                        console.log(fotoSubida);
                        ImageService.uploadImage(datos.id, tipoDatoForImageService, fotoSubida).then
                            (response => {
                                setSubmittedFoto(true);
                                setSubmittedMessage("¡Has modificado " + el_la_aux + " " + nombreTipoDato_aux + " y su foto!");
                                console.log("Se subió la foto :D");
                                console.log(response);
                            },
                            (error) => {
                                setSubmittedFoto(false);
                                setSubmittedMessage("¡Has modificado " + el_la_aux + " " + nombreTipoDato_aux + "! Pero su foto por error no ha sido modificado");
                                setLoading(false);
                                console.log("error:");
                                console.log(error);
                            }
                        );
                    }
                    else{
                        setSubmittedFoto(false);
                        setSubmittedMessage("¡Has modificado " + el_la_aux + " " + nombreTipoDato_aux + "!");
                        console.log("No hay foto para subir");
                        console.log("fotoSubida:");
                        console.log(fotoSubida);
                        console.log("datos.id");
                        console.log(datos.id);
                        setLoading(false);
                    }
                    setLoading(false);
                    window.scrollTo({ top: 0, behavior: "smooth" }); //Para mostrar cartel "Has cargado X componente!"
                },
                (error) => {
                    const resMessage =
                      (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                      error.message ||
                      error.toString();
                    
                    setSubmittedFoto(false);
                    setSubmittedMessage("Error al modificar " + (el_la_aux === 'la' ? 'a la ' : 'al ') + nombreTipoDato_aux);
                    setLoading(false);
                    setMessage(resMessage);
                }
            );
        } else {
            setSubmittedMessage("No hubo modificación");
            setLoading(false);
        }
    };

    const newDatos = () => {
        setDatos(cargarDatosDefault);
        setLoading(false);
        setSubmitted(false);
        setFotoSubida(false);
        setSubmittedFoto(false);
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
        navigate(urlTablaDato);//ejemplo "/personafisica"
        //window.location.reload();
    }

    const [nombreTipoDato_aux, setNombreTipoDato_aux] = useState("");
    const [nombreTipoDatoPrimeraLetraMayuscula, setNombreTipoDatoPrimeraLetraMayuscula] = useState("");
    const [el_la_aux, setElLa_aux] = useState("");
    useEffect(() => {
        if(location.state && location.state.id){
            setIdToSearch(location.state.id);
            handleEntrySearch();
        }
        if(nombreTipoDato){
            setNombreTipoDato_aux(nombreTipoDato);
            setNombreTipoDatoPrimeraLetraMayuscula(nombreTipoDato.charAt(0).toUpperCase() + nombreTipoDato.slice(1).toLowerCase());
        } else {
            setNombreTipoDato_aux("dato");
            setNombreTipoDatoPrimeraLetraMayuscula("Dato");
        }
        if(el_la){
            setElLa_aux(el_la);
        } else {
            setElLa_aux("el");
        }
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);
    
    return (
        <div className="submit-form">
            {/**<div className = "container">*/}
            <div className = "row">
                <div className = "card col-md-6 offset-md-3 offset-md-3">
                    {!submitted ? (
                        <div className = "card-body">
                            {(mostrarSearchID) && (
                                <div className = "form-group">
                                    <Form onSubmit={handleSearch} ref={formSearch}>
                                        <div className = "form-group">
                                            <label> Ingrese ID de{el_la === "el" ? "l" : " la"} {nombreTipoDato_aux} a buscar: </label>
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
                                    <button className="btn btn-light" onClick={newDatos} style={{marginLeft: "00px"}}>
                                        Cancelar/Realizar otra Búsqueda
                                    </button>
                                    </div>
                                    <div>
                                        {(searchEncontrado) && (
                                            <label style={{color: '#86af49'}}> ¡{nombreTipoDatoPrimeraLetraMayuscula} a modificar encontrado! </label>
                                        )}
                                    </div>
                                </div>
                            )}
                            {(forzarRenderizado) && (<div></div>)}
                            {(!mostrarSearchID) && (
                                <div className="form-group">
                                    <FotoPerfil id={datos.id} setFotoSubida={setFotoSubida} visibilidad={"EDITAR"} />
                                    <Form onSubmit={handleSubmit} ref={form}>
                                        <DatoUpdateInput data={datos} handleInputChange={handleInputChange} />

                                        <div className="form-group">
                                            <button className="btn btn-success" href="#" disabled={loading}>
                                                <span></span>
                                                <span></span>
                                                <span></span>
                                                <span></span>
                                                {loading && (
                                                    <span className="spinner-border spinner-border-sm"></span>
                                                )}
                                                Modificar {nombreTipoDatoPrimeraLetraMayuscula}
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
                    ) : (
                        <div>
                            <h4>{submittedMessage}</h4>
                            <button className="btn btn-success" onClick={newDatos}>
                                Modificar otr{el_la_aux === "el" ? "o" : "a"} {nombreTipoDato_aux}
                            </button>
                            <button className="btn btn-back" onClick={cancel}>
                                Regresar a la lista
                            </button>
                        </div>
                    )}
                </div>
            </div>
            <br></br><br></br>
        </div>
    );
};

export default UpdateGenericoConFoto;