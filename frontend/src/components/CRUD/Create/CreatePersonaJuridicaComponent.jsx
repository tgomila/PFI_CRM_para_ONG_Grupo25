import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";
import CheckButton from "react-validation/build/button";
import PersonaJuridicaService from '../../../services/PersonaJuridicaService';
import {useNavigate} from 'react-router-dom';

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
//  - const cargarDefault = ...

function CreatePersonaJuridicaComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarPersonaJuridicaDefault = {
        id: null,
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: "",
        internoTelefono: "",
        tipoPersonaJuridica: ""
    }
    const [personaJuridica, setPersonaJuridica] = useState(cargarPersonaJuridicaDefault);
    const [submitted, setSubmitted] = useState(false);

    const [enumTipoPersonaJuridica, setEnumTipoPersonaJuridica] = useState([]);
    useEffect(() => {
        PersonaJuridicaService.getEnumTipoPersonaJuridica().then((res) => {
            setEnumTipoPersonaJuridica(res.data);
        });
    }, []);
    const handleInputChange = event => {
        const { name, value } = event.target;
        setPersonaJuridica({ ...personaJuridica, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();

        var data = {
            id: personaJuridica.id,
            nombreDescripcion: personaJuridica.nombreDescripcion,
            cuit: personaJuridica.cuit,
            domicilio: personaJuridica.domicilio,
            email: personaJuridica.email,
            telefono: personaJuridica.telefono,
            internoTelefono: personaJuridica.internoTelefono,
            tipoPersonaJuridica: personaJuridica.tipoPersonaJuridica
        }
        if (checkBtn.current.context._errors.length === 0) {
            PersonaJuridicaService.create(data).then
                (response => {
                    setPersonaJuridica({
                        id: response.data.id,
                        nombreDescripcion: response.data.nombreDescripcion,
                        cuit: response.data.cuit,
                        domicilio: response.data.domicilio,
                        email: response.data.email,
                        telefono: response.data.telefono,
                        internoTelefono: response.data.internoTelefono,
                        tipoPersonaJuridica: response.data.tipoPersonaJuridica
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

    const newPersonaJuridica = () => {
        setPersonaJuridica(cargarPersonaJuridicaDefault);
        setLoading(false);
        setSubmitted(false);
    }

    const cancel = () => {
        navigate("/personajuridica");
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
                                <h4>¡Has cargado la persona jurídica!</h4>
                                <button className="btn btn-success" onClick={newPersonaJuridica}>
                                  Agregar nueva persona jurídica
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
                                </button>
                            </div>
                          ) : (
                            <div className = "card-body">
                                <Form onSubmit={handleSubmit} ref={form}>

                                    <div className = "form-group">
                                        <label> Nombre/Descripción: </label>
                                        <Input placeholder="Nombre/Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={personaJuridica.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={personaJuridica.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={personaJuridica.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={personaJuridica.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={personaJuridica.telefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Interno Telefono: </label>
                                        <Input placeholder="Interno Telefono" id="internoTelefono" name="internoTelefono" type="text" className="form-control" 
                                            value={personaJuridica.internoTelefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Tipo de persona jurídica: </label>
                                        {/*<Select placeholder="Tipo de persona jurídica" id="tipoPersonaJuridica" name="tipoPersonaJuridica" type="text" className="form-control" 
                                            value={personaJuridica.tipoPersonaJuridica} onChange={handleInputChange} validations={[required]}
                                        options={options}/>
                                        */}

                                        <Select name="tipoPersonaJuridica" value={personaJuridica.tipoPersonaJuridica} className="form-control" onChange={handleInputChange} validations={[required]}>
                                            <option value=''>Elija tipo de persona jurídica</option>
                                            {
                                                enumTipoPersonaJuridica.map((item) => (
                                                    <option value={item.value}>{item.label}</option>
                                                ))
                                            }

                                        </Select>

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
                                            Cargar persona jurídica
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

export default CreatePersonaJuridicaComponent;