import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import BeneficiarioService from '../../../services/BeneficiarioService';
import { useNavigate, useLocation } from 'react-router-dom';
import { cargarBeneficiarioDefault } from '../Constants/ConstantsCargarDefault';
import { BeneficiarioUpdateInput } from '../Constants/ConstantsInputModel';
import { required } from '../Constants/ConstantsInput';
import Select from "react-validation/build/select";
import { format, subYears } from 'date-fns';

//Reciclado de UpdatePersonaComponent, y reciclado de create
function UpdateBeneficiarioComponent(idEntrySearch) {
    let navigate = useNavigate();
    const location = useLocation();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const [beneficiario, setBeneficiario] = useState(cargarBeneficiarioDefault);
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
    const handleSearch = (e) => {
        e.preventDefault();
        setBeneficiario(cargarBeneficiarioDefault);
        setMessageSearch("");
        setLoadingSearch(true);
        setSearchEncontrado(false);
        formSearch.current.validateAll();
        //console.log("Llegue aquí, id: " + idToSearch);
        if (checkBtnSearch.current.context._errors.length === 0) {
            BeneficiarioService.getById(idToSearch).then
                (response => {
                    if(response.data.id)
                        setSearchEncontrado(true);
                    console.log(response.data);
                    response.data.seRetiraSolo = String(response.data.seRetiraSolo);
                    setBeneficiario(prevBeneficiario => ({ ...prevBeneficiario, ...response.data }));
                    console.log(response.data);
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
        setBeneficiario({ ...beneficiario, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
       let data = {...beneficiario}; //Copio datos a "data" para el json de alta
        if (checkBtn.current.context._errors.length === 0) {
            BeneficiarioService.update(data).then
                (response => {
                    setBeneficiario(prevBeneficiario => ({ ...prevBeneficiario, ...response.data }));
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

    const newBeneficiario = () => {
        setBeneficiario(cargarBeneficiarioDefault);
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
        navigate("/beneficiario");
        window.location.reload();
    }

    useEffect(() => {
        if(location.state.id){
            console.log(location.state.id);
            setIdToSearch(location.state.id)
            //handleSearch();
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    }, []);
    
    return (
        <div className="submit-form">
            <div>
                <br></br>
                <div className = "container">
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
                                        <button className="btn btn-light" onClick={newBeneficiario} style={{marginLeft: "00px"}}>
                                            Cancelar/Realizar otra Búsqueda
                                        </button>
                                        </div>
                                        <div>
                                            {(searchEncontrado) && (
                                                <label style={{color: '#86af49'}}> ¡Beneficiario a modificar encontrado! </label>
                                            )}
                                        </div>
                                    </div>
                                )}
                                {(forzarRenderizado) && (<div></div>)}
                                {(!mostrarSearchID) && (
                                    <div className="form-group">
                                        <Form onSubmit={handleSubmit} ref={form}>
                                            {searchEncontrado && (
                                                <div className = "form-group">
                                                    <label htmlFor="id"> ID: </label>
                                                    <Input disabled="disabled" placeholder="ID" id="id" name="id" type="number" className="form-control" 
                                                    value={beneficiario.id} onChange={handleInputChange}/>
                                                </div>
                                            )}

                                            <div className = "form-group">
                                                <label> Nombre: </label>
                                                <Input disabled={""} placeholder="Nombre" id="nombre" name="nombre" type="text" className="form-control" 
                                                    value={beneficiario.nombre} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Apellido: </label>
                                                <Input disabled={""} placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                                                    value={beneficiario.apellido} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> DNI: </label>
                                                <Input disabled={""} placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                                                    value={beneficiario.dni} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Fecha de nacimiento: </label>
                                                <Input disabled={""} placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                                                    value={beneficiario.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                                                    min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 1), 'yyyy-MM-dd')}/>
                                            </div>
                                            
                                            <div className = "form-group">
                                                <label> Cuit: </label>
                                                <Input disabled={""} placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                                    value={beneficiario.cuit} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Domicilio: </label>
                                                <Input disabled={""} placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                                    value={beneficiario.domicilio} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Email: </label>
                                                <Input disabled={""} placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                                    value={beneficiario.email} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Telefono: </label>
                                                <Input disabled={""} placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                                    value={beneficiario.telefono} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Descripción: </label>
                                                <Input disabled={""} placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                                    value={beneficiario.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Nro de ID original de ONG: </label>
                                                <Input placeholder="Nro de ID original de ONG" id="idONG" name="idONG" type="number" className="form-control" 
                                                    value={beneficiario.idONG} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                
                                            <div className = "form-group">
                                                <label> Nro de legajo: </label>
                                                <Input placeholder="Legajo" id="legajo" name="legajo" type="number" className="form-control" 
                                                    value={beneficiario.legajo} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Lugar de nacimiento: </label>
                                                <Input placeholder="Lugar de nacimiento" name="lugarDeNacimiento" type="text" className="form-control" 
                                                    value={beneficiario.lugarDeNacimiento} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Se retira solo: </label>
                                                <Select placeholder="Se retira solo" name="seRetiraSolo"  type="text" className="form-control"
                                                    value={beneficiario.seRetiraSolo} onChange={handleInputChange} validations={[required]}>
                                                    <option value="">Seleccione</option>
                                                    <option value="true">Si</option>
                                                    <option value="false">No</option>
                                                </Select>
                                            </div>

                                            <div className = "form-group">
                                                <label> Cuidados especiales: </label>
                                                <Input placeholder="Cuidados especiales" name="cuidadosEspeciales" type="text" className="form-control" 
                                                    value={beneficiario.cuidadosEspeciales} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Escuela: </label>
                                                <Input placeholder="Escuela" name="escuela" type="text" className="form-control" 
                                                    value={beneficiario.escuela} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Grado: </label>
                                                <Input placeholder="Grado" name="grado" type="text" className="form-control" 
                                                    value={beneficiario.grado} onChange={handleInputChange} validations={[required]}/>
                                            </div>

                                            <div className = "form-group">
                                                <label> Turno: </label>
                                                <Input placeholder="Turno" name="turno" type="text" className="form-control" 
                                                    value={beneficiario.turno} onChange={handleInputChange} validations={[required]}/>
                                            </div>
                                            {/*<BeneficiarioUpdateInput data={beneficiario} handleInputChange={handleInputChange} />*/}

                                            <div className="form-group">
                                                <button className="btn btn-success" href="#" disabled={loading}>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    <span></span>
                                                    {loading && (
                                                        <span className="spinner-border spinner-border-sm"></span>
                                                    )}
                                                    Modificar Beneficiario
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
                                <h4>¡Has modificado el beneficiario!</h4>
                                <button className="btn btn-success" onClick={newBeneficiario}>
                                  Modificar otro beneficiario
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

export default UpdateBeneficiarioComponent;