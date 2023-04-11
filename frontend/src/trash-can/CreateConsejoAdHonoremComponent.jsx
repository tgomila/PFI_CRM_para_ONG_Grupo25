import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import ConsejoAdHonoremService from '../services/ConsejoAdHonoremService';
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

function CreateConsejoAdHonoremComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarConsejoAdHonoremDefault = {
        id: null,
        nombreDescripcion: "",
        cuit: "",
        domicilio: "",
        email: "",
        telefono: "",
        dni: "",
        nombre: "",
        apellido: "",
        fechaNacimiento: format(subYears(new Date(), 18), 'yyyy-MM-dd'),
        funcion: "",
    }
    const [consejoAdHonorem, setConsejoAdHonorem] = useState(cargarConsejoAdHonoremDefault);
    const [submitted, setSubmitted] = useState(false);

    const handleInputChange = event => {
        const { name, value } = event.target;
        setConsejoAdHonorem({ ...consejoAdHonorem, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
        var data = {
            id: consejoAdHonorem.id,
            nombreDescripcion: consejoAdHonorem.nombreDescripcion,
            cuit: consejoAdHonorem.cuit,
            domicilio: consejoAdHonorem.domicilio,
            email: consejoAdHonorem.email,
            telefono: consejoAdHonorem.telefono,
            dni: consejoAdHonorem.dni,
            nombre: consejoAdHonorem.nombre,
            apellido: consejoAdHonorem.apellido,
            fechaNacimiento: consejoAdHonorem.fechaNacimiento,
            funcion: consejoAdHonorem.funcion
        }
        if (checkBtn.current.context._errors.length === 0) {
            ConsejoAdHonoremService.create(data).then
                (response => {
                    setConsejoAdHonorem({
                        id: response.data.id,
                        nombreDescripcion: response.data.nombreDescripcion,
                        cuit: response.data.cuit,
                        domicilio: response.data.domicilio,
                        email: response.data.email,
                        telefono: response.data.telefono,
                        dni: response.data.dni,
                        nombre: response.data.nombre,
                        apellido: response.data.apellido,
                        fechaNacimiento: response.data.fechaNacimiento,
                        funcion: response.data.funcion
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

    const newConsejoAdHonorem = () => {
        setConsejoAdHonorem(cargarConsejoAdHonoremDefault);
        setLoading(false);
        setSubmitted(false);
    }

    const cancel = () => {
        navigate("/consejoadhonorem");
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
                                <h4>¡Has cargado consejo ad honorem!</h4>
                                <button className="btn btn-success" onClick={newConsejoAdHonorem}>
                                  Agregar nuevo consejo ad honorem
                                </button>
                                <button className="btn btn-back" onClick={cancel}>
                                  Regresar a la lista
                                </button>
                            </div>
                          ) : (
                            <div className = "card-body">
                                <Form onSubmit={handleSubmit} ref={form}>
        
                                    <div className = "form-group">
                                        <label> Nombre: </label>
                                        <Input placeholder="Nombre" id="nombre" name="nombre" type="text" className="form-control" 
                                            value={consejoAdHonorem.nombre} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Apellido: </label>
                                        <Input placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                                            value={consejoAdHonorem.apellido} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> DNI: </label>
                                        <Input placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                                            value={consejoAdHonorem.dni} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Función: </label>
                                        <Input placeholder="Función" id="funcion" name="funcion" type="text" className="form-control" 
                                            value={consejoAdHonorem.funcion} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Descripción: </label>
                                        <Input placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={consejoAdHonorem.nombreDescripcion} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Fecha de nacimiento: </label>
                                        <Input placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                                            value={consejoAdHonorem.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                                            min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 1), 'yyyy-MM-dd')}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={consejoAdHonorem.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={consejoAdHonorem.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={consejoAdHonorem.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={consejoAdHonorem.telefono} onChange={handleInputChange} validations={[required]}/>
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
                                            Cargar consejo ad honorem
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

export default CreateConsejoAdHonoremComponent;