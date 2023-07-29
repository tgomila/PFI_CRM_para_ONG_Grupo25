import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import ImageService from '../../../services/ImageService';
import { useNavigate, useLocation } from 'react-router-dom';
import { FotoPerfil } from './ConstantsCrudElements';
import { required } from './ConstantsInput';

import "../../../Styles/CRUD.scss";

/**
 * Originalmente fue hecho para Create, quizas a futuro se haga para Modificar también
 * @param {Array} cargarDatosDefault traelo mediante ejemplo: import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
 * @param {string} DatoUpdateInput traelo mediante ejemplo: import { PersonaUpdateInput } from '../Constants/ConstantsInputModel';
 * @param {string} tipoDatoForImageService si es tipos de personas (empleado, beneficiario, etc) es 'contacto', sinó else, si es producto es 'producto'. Si no hay foto ejemplo 'factura', poner "" para no mostrar
 * @param {Object} Service ejemplo EmpleadoService
 * @param {Array} dataIn si por ejemplo un Producto ya tiene cargado su proveedor contacto, no es necesario llamarlo.
 * @param {string} urlTablaDato ejemplo '/empleado', es para cuando presiona el botón cancelar
 * @param {boolean} isVentanaEmergente true o false
 * @param {string} el_la 'el' o 'la'
 * @param {string} nombreTipoDato ejemplo 'empleado', 'beneficiario'
 * @param {string} typeCRUD ingresar 'CREATE' ó 'UPDATE' para saber que mostrar
 * @returns 
 */
const CreateReadUpdateGenericoConFoto = ({cargarDatosDefault, DatoUpdateInput, tipoDatoForImageService, Service, dataIn, urlTablaDato, isVentanaEmergente, el_la, nombreTipoDato, typeCRUD}) => {
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
    const [mostrarSearchID, setMostrarSearchID] = useState(false);
    const [contactoSearchEncontrado, setContactoSearchEncontrado] = useState(false);//Solo con ID es suficiente
    const [personaSearchEncontrada, setPersonaSearchEncontrada] = useState(false);//Este sirve para Empleado, etc para reciclado
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
        setDatos(cargarDatosDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setSearchEncontrado(false);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
        formSearch.current?.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        //console.log("location.state.id: " + location.state.id);
        let isNoErrorSearch = !(checkBtnSearch.current?.context?._errors.length > 0);
        console.log("no error: " + isNoErrorSearch);

        if (isNoErrorSearch) {
            
            const methodToCall = typeCRUD === 'CREATE' ? "search" : "getById";
            //anteriormente era Service.getByid o Service.search
            Service[methodToCall](idToSearch ? idToSearch : location.state.id).then
                (response => {
                    if(response.data.id){
                        setSearchEncontrado(true);
                        setContactoSearchEncontrado(true);
                    }
                    if(response.data.dni)
                        setPersonaSearchEncontrada(true);
                    setDatos(prevDatos => ({ ...prevDatos, ...response.data }));
                    setLoadingSearch(false);
                    setMostrarSearchID(false);
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
        setDatos({ ...datos, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();

        let data = {...datos}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            const methodToCall = typeCRUD === "CREATE" ? "create" : "update";
            console.log(data);
            console.log(methodToCall);
            //anteriormente era Service.create o Service.update
            Service[methodToCall](data)
                .then(response => {
                //    return new Promise((resolve) => {
                    setDatos(prevDatos => ({ ...prevDatos, ...response.data }));
                    setSubmitted(true);//Info subida, aún la foto no subida.
                    setSubmittedMessage("¡Has "+ (typeCRUD === 'CREATE' ? 'creado ' : 'modificado ') + el_la_aux + " " + nombreTipoDato_aux + "!");
                    console.log("Datos subidos!");
                    console.log("response.data:");
                    console.log(response.data);
                //    resolve();
                //    });
                //})
                //.then(() => {//Este then es porque "setDatos" es async y cuando llamo a datos.id me lo daba nulo :v
                    console.log("tipoDatoForImageService:");
                    console.log(tipoDatoForImageService);
                    console.log("Datos:");
                    console.log(datos);
                    console.log("Datos.id:");
                    console.log(datos.id);
                    console.log("Foto subida:");
                    console.log(fotoSubida);
                    
                    //Originalmente era datos.id, pero como es asincrónico, mejor usar response.data.id
                    if(tipoDatoForImageService && fotoSubida && response.data.id){//Si hay foto en el objeto, si hay una foto archivo que subió el usuario, si hay un ID para asociarlo
                        console.log("Voy a subir la foto:");
                        console.log(fotoSubida);
                        ImageService.uploadImage(response.data.id, tipoDatoForImageService, fotoSubida).then
                            (response => {
                                setSubmittedFoto(true);
                                setSubmittedMessage("¡Has " + (typeCRUD === 'CREATE' ? 'creado ' : 'modificado ') + el_la_aux + " " + nombreTipoDato_aux + " y su foto!");
                                console.log("Se subió la foto :D");
                                console.log(response);
                            },
                            (error) => {
                                setSubmittedFoto(false);
                                setSubmittedMessage("¡Has " + (typeCRUD === 'CREATE' ? 'creado ' : 'modificado ') + el_la_aux + " " + nombreTipoDato_aux + "! Pero su foto por error no ha sido " + (typeCRUD === 'CREATE' ? 'creada' : 'modificada'));
                                setLoading(false);
                                console.log("error:");
                                console.log(error);
                            }
                        );
                    }
                    else{
                        setSubmittedFoto(false);
                        setSubmittedMessage("¡Has " + (typeCRUD === 'CREATE' ? 'creado ' : 'modificado ') + el_la_aux + " " + nombreTipoDato_aux + "!");
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
                    setSubmittedMessage("Error al " + (typeCRUD === 'CREATE' ? 'crear ' : 'modificar ') + (el_la_aux === 'la' ? 'a la ' : 'al ') + nombreTipoDato_aux);
                    setLoading(false);
                    setMessage(resMessage);
                }
            );
        } else {
            setSubmittedMessage("No hubo " + (typeCRUD === 'CREATE' ? 'creación' : 'modificación'));
            setLoading(false);
        }
    };

    const newDatos = () => {
        setDatos(cargarDatosDefault);
        setLoading(false);
        setSubmitted(false);
        setFotoSubida(false);
        setSubmittedFoto(false);
        setMostrarSearchID(typeCRUD === 'CREATE' ? false : true);
        setContactoSearchEncontrado(false);
        setPersonaSearchEncontrada(false);
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
        if(location?.state?.id){//En caso de que diste en tabla a botón "Editar ID: 1" aquí se obtiene su ID.
            window.scrollTo({ top: 0, behavior: "smooth" });
            setIdToSearch(location.state.id);
            handleEntrySearch();
        } else if(dataIn) {
            setDatos(dataIn);
            setMostrarSearchID(false);//typeCRUD === 'CREATE' ? false : true);
        }
        else{
            setMostrarSearchID(typeCRUD === 'CREATE' ? false : true);//Para casos de UPDATE, mostrar el search
        }
        setElLa_aux(el_la ? el_la : "el");
        if(nombreTipoDato){
            setNombreTipoDato_aux(nombreTipoDato);
            setNombreTipoDatoPrimeraLetraMayuscula(nombreTipoDato.charAt(0).toUpperCase() + nombreTipoDato.slice(1).toLowerCase());
        } else {
            setNombreTipoDato_aux("dato");
            setNombreTipoDatoPrimeraLetraMayuscula("Dato");
        }
        window.scrollTo({ top: 0, behavior: "smooth" });
    }, []);

    //Esto es porque si pones setMostrarSearchID(true) directamente en action de botón, hace muchos renders y se rompe xd
    const changeShowSearch = () => {
        setMostrarSearchID(true);
    }

    //Esto es porque si pones setMostrarSearchID(false) directamente en action de botón, hace muchos renders y se rompe xd
    const changeShowNoSearch = () => {
        setMostrarSearchID(false);
    }

    /**
     * @description
     * Se usa solo para Create, es buscarId de una persona para crear un empleado, ya te da id y datos de persona
     * Tiene que estar dentro del <div className = "card-body"> del return principal
     * @returns pantalla de búsqueda arriba de modificar inputs
     */
    const CreateSearch = () => {
        return (
            <div>
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
                        <button className="btn btn-light" onClick={newDatos} style={{marginLeft: "00px"}}>
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
            </div>
        );
    }

    const UpdateReadSearch = () => {
        return (
            <div>
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
                {(!mostrarSearchID && searchEncontrado) && (
                    <div className = "form-group">
                        <div>
                        <button className="btn btn-light" onClick={newDatos} style={{marginLeft: "00px"}}>
                            Cancelar/Realizar otra Búsqueda
                        </button>
                        </div>
                        <div>
                            {(searchEncontrado) && (
                                (typeCRUD === 'UPDATE' ? (
                                    <label style={{color: '#86af49'}}>¡{nombreTipoDatoPrimeraLetraMayuscula} a modificar encontrad{el_la === "el" ? "o" : "a"}! </label>
                                ) : (
                                    <label style={{color: '#86af49'}}>¡{nombreTipoDatoPrimeraLetraMayuscula} encontrad{el_la === "el" ? "o" : "a"}! </label>
                                ))
                            )}
                        </div>
                    </div>
                )}
            </div>
        );
    }

    return (
        <div className="submit-form">
            <div className = "row">
                <div className = {isVentanaEmergente ? "" : "card col-md-6 offset-md-3 offset-md-3"}>
                    {!submitted ? (
                        <div className = "card-body">
                            {typeCRUD === 'CREATE' && tipoDatoForImageService === 'contacto' ? <CreateSearch/> : <UpdateReadSearch/>}
                            {(forzarRenderizado) && (<div></div>)}
                            {(!mostrarSearchID) && (
                                <div>
                                    {/* Si hay foto (ejemplo persona, producto) mostrar, si no hay foto ejemplo factura, no mostrar */}
                                    {tipoDatoForImageService && 
                                        <FotoPerfil
                                            id = {datos.id}
                                            setFotoSubida = {setFotoSubida}
                                            visibilidad = {(typeCRUD === 'CREATE' || typeCRUD === 'UPDATE') ? "EDITAR" : "SOLO_VISTA"}
                                            tipoFoto = {tipoDatoForImageService}
                                        />
                                    }
                                
                                    {(typeCRUD === 'CREATE' || typeCRUD === 'UPDATE') && (
                                        <div className="form-group">
                                            <Form onSubmit={handleSubmit} ref={form}>
                                                <DatoUpdateInput 
                                                    contactoSearchEncontrado={contactoSearchEncontrado}
                                                    personaSearchEncontrada={personaSearchEncontrada}
                                                    data={datos} 
                                                    handleInputChange={handleInputChange} 
                                                />
                                                
                                                <div className="form-group">
                                                    <button className="btn btn-success" href="#" disabled={loading}>
                                                        <span></span>
                                                        <span></span>
                                                        <span></span>
                                                        <span></span>
                                                        {loading && (
                                                            <span className="spinner-border spinner-border-sm"></span>
                                                        )}
                                                        {typeCRUD === 'CREATE' ? 'Crear' : 'Modificar'} {nombreTipoDatoPrimeraLetraMayuscula}
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
                                        </div>
                                    )}
                                    {typeCRUD === 'READ' && (//Solo aparece si es solo lectura
                                        <div className="form-group">
                                            <DatoUpdateInput 
                                                data={datos} 
                                            />
                                        </div>
                                    )}
                                    {!isVentanaEmergente && (
                                        <button className="btn btn-info" onClick={cancel} style={{marginLeft: "00px"}}>
                                            {typeCRUD === 'READ' ? 'Regresar a la lista' : 'Cancelar'}
                                        </button>
                                    )}
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className="col-12">
                            <h4 className = "col-12">{submittedMessage}</h4>
                            <button className="btn btn-success mt-2 mx-2 col-12 col-lg-4" onClick={newDatos}>
                                {typeCRUD === 'CREATE' ? 'Crear nuev' : 'Modificar otr'}{el_la_aux === "el" ? "o" : "a"} {nombreTipoDato_aux}
                            </button>
                            {/*datos.id && (
                                <button 
                                className="btn btn-info mt-2 mx-2 col-12 col-lg-7" 
                                onClick={() => {
                                    //navigate( window.location.pathname, {state:{id:datos.id}});
                                    //window.location.reload();
                                    setIdToSearch(datos.id);
                                    handleEntrySearch();
                                    }
                                }
                                >
                                    Ver y/o modificar a{el_la_aux === "el" ? 'l ' : ' la '} {typeCRUD === 'CREATE' ? 
                                    'nuev'+(el_la_aux === "el" ? "o " : "a ")+nombreTipoDato_aux+' cread'+(el_la_aux === "el" ? "o " : "a ")
                                    : 
                                    nombreTipoDato_aux + ' modificad'+(el_la_aux === "el" ? "o " : "a ")}
                                    . ID: {datos.id}
                                </button>
                            )*/}
                            {console.log("Aqui hay datos:")}
                            {(console.log(datos))}
                            <button className="btn btn-back mt-2 mb-2 col-12 col-lg-4" onClick={cancel}>
                                Regresar a la lista
                            </button>
                            <br/><br/><label>Si desea verlo, el ID es {datos.id}</label>
                        </div>
                    )}
                </div>
            </div>
            <br></br><br></br>
        </div>
    );
};

export default CreateReadUpdateGenericoConFoto;