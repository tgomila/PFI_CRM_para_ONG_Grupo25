import React, { useState, useRef } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";
import CheckButton from "react-validation/build/button";
import BeneficiarioService from '../services/BeneficiarioService';
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

function CreateBeneficiarioComponent() {
    let navigate = useNavigate();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");

    const cargarBeneficiarioDefault = {
        id: null,
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
    const [beneficiario, setBeneficiario] = useState(cargarBeneficiarioDefault);
    const [submitted, setSubmitted] = useState(false);

    const handleInputChange = event => {
        const { name, value } = event.target;
        setBeneficiario({ ...beneficiario, [name]: value });
    };

    const handleSubmit = (e) => {

        e.preventDefault(); //PreventDefault hace que el dato no viaje en el url link expuesto

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        
        var data = {
            id: beneficiario.id,
            nombreDescripcion: beneficiario.nombreDescripcion,
            cuit: beneficiario.cuit,
            domicilio: beneficiario.domicilio,
            email: beneficiario.email,
            telefono: beneficiario.telefono,
            dni: beneficiario.dni,
            nombre: beneficiario.nombre,
            apellido: beneficiario.apellido,
            fechaNacimiento: beneficiario.fechaNacimiento,
            idONG: beneficiario.idONG,
            legajo: beneficiario.legajo,
            lugarDeNacimiento: beneficiario.lugarDeNacimiento,
            seRetiraSolo: beneficiario.seRetiraSolo,
            cuidadosEspeciales: beneficiario.cuidadosEspeciales,
            escuela: beneficiario.escuela,
            grado: beneficiario.grado,
            turno: beneficiario.turno
        }
        if (checkBtn.current.context._errors.length === 0) {
            BeneficiarioService.create(data).then
                (response => {
                    setBeneficiario({
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
                        idONG: response.data.idONG,
                        legajo: response.data.legajo,
                        lugarDeNacimiento: response.data.lugarDeNacimiento,
                        seRetiraSolo: response.data.seRetiraSolo,
                        cuidadosEspeciales: response.data.cuidadosEspeciales,
                        escuela: response.data.escuela,
                        grado: response.data.grado,
                        turno: response.data.turno
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

    const newBeneficiario = () => {
        setBeneficiario(cargarBeneficiarioDefault);
        setLoading(false);
        setSubmitted(false);
    }

    const cancel = () => {
        navigate("/beneficiario");
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
                                <h4>¡Has cargado el beneficiario!</h4>
                                <button className="btn btn-success" onClick={newBeneficiario}>
                                  Agregar nuevo beneficiario
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
                                            value={beneficiario.nombre} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Apellido: </label>
                                        <Input placeholder="Apellido" id="apellido" name="apellido" type="text" className="form-control" 
                                            value={beneficiario.apellido} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> DNI: </label>
                                        <Input placeholder="Dni" id="dni" name="dni" type="number" className="form-control" 
                                            value={beneficiario.dni} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Fecha de nacimiento: </label>
                                        <Input placeholder="dd-mm-yyyy" id="fechaNacimiento" name="fechaNacimiento" type="date" className="form-control" 
                                            value={beneficiario.fechaNacimiento} onChange={handleInputChange} validations={[required]}
                                            min={format(subYears(new Date(), 120), 'yyyy-MM-dd')} max={format(subYears(new Date(), 1), 'yyyy-MM-dd')}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Cuit: </label>
                                        <Input placeholder="Cuit" id="cuit" name="cuit" type="text" className="form-control" 
                                            value={beneficiario.cuit} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Domicilio: </label>
                                        <Input placeholder="Domicilio" id="domicilio" name="domicilio" type="text" className="form-control" 
                                            value={beneficiario.domicilio} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Email: </label>
                                        <Input placeholder="Email" id="email" name="email" type="text" className="form-control" 
                                            value={beneficiario.email} onChange={handleInputChange} validations={[required]}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Telefono: </label>
                                        <Input placeholder="Telefono" id="telefono" name="telefono" type="text" className="form-control" 
                                            value={beneficiario.telefono} onChange={handleInputChange} validations={[required]}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Descripción: </label>
                                        <Input placeholder="Descripción" name="nombreDescripcion" type="text" className="form-control" 
                                            value={beneficiario.nombreDescripcion} onChange={handleInputChange}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Nro de ID original de ONG: </label>
                                        <Input placeholder="Nro de ID original de ONG" id="idONG" name="idONG" type="number" className="form-control" 
                                            value={beneficiario.idONG} onChange={handleInputChange}/>
                                    </div>
        
                                    <div className = "form-group">
                                        <label> Nro de legajo: </label>
                                        <Input placeholder="Legajo" id="legajo" name="legajo" type="number" className="form-control" 
                                            value={beneficiario.legajo} onChange={handleInputChange}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Lugar de nacimiento: </label>
                                        <Input placeholder="Lugar de nacimiento" name="lugarDeNacimiento" type="text" className="form-control" 
                                            value={beneficiario.lugarDeNacimiento} onChange={handleInputChange}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Se retira solo: </label>
                                        <Select name="seRetiraSolo" value={beneficiario.seRetiraSolo} className="form-control" onChange={handleInputChange} validations={[required]}>
                                            <option value=''>Seleccione</option>
                                            <option value="true">Si</option>
                                            <option value="false">No</option>
                                        </Select>
                                    </div>

                                    <div className = "form-group">
                                        <label> Cuidados especiales: </label>
                                        <Input placeholder="Cuidados especiales" name="cuidadosEspeciales" type="text" className="form-control" 
                                            value={beneficiario.cuidadosEspeciales} onChange={handleInputChange}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Escuela: </label>
                                        <Input placeholder="Escuela" name="escuela" type="text" className="form-control" 
                                            value={beneficiario.escuela} onChange={handleInputChange}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Grado: </label>
                                        <Input placeholder="Grado" name="grado" type="text" className="form-control" 
                                            value={beneficiario.grado} onChange={handleInputChange}/>
                                    </div>

                                    <div className = "form-group">
                                        <label> Turno: </label>
                                        <Input placeholder="Turno" name="turno" type="text" className="form-control" 
                                            value={beneficiario.turno} onChange={handleInputChange}/>
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
                                            Cargar beneficiario
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

export default CreateBeneficiarioComponent;