import React, { useState, useRef, useEffect } from 'react'
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import ModuloMarketService from '../../services/ModuloMarketService';
import { useNavigate, useLocation } from 'react-router-dom';
import { required } from '../CRUD/Constants/ConstantsInput';

import Card from "react-credit-cards";
import "react-credit-cards/es/styles-compiled.css";
import "./RealizarPagoStyle.scss";

//Reciclado de UpdatePersonaComponent, y reciclado de create
function RealizarPagoComponent() {
    let navigate = useNavigate();
    const location = useLocation();

    const form = useRef();
    const checkBtn = useRef();
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const tiempoElegido = location.state.tiempo;
    const moduloElegido = location.state.modulo;
    const scroll_horizontal = location.state.scroll_horizontal;
    const scroll_vertical = location.state.scroll_vertical;

    const handleSubmitSuscripcion = (e) => {
        e.preventDefault();
        handleEntrySubmitSuscripcion();
    }

    const handleEntrySubmitSuscripcion = () => {

        setMessage("");
        setLoading(true);

        form.current.validateAll();
        //modulo, tiempo
        //const [modulos, setModulos] = useState();
    
        const responseError = (error) => {
            const resMessage =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
    
            setLoading(false);
            setMessage(resMessage);
        }
        
        const responseOk = (response) => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            //setModulos(response.data);
            console.log(response.data);
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
        if (checkBtn.current.context._errors.length === 0) {
            //trial, mes, anio
            if(tiempoElegido==="trial"){
                //Plan total
                if(moduloElegido==="all"){
                setLoading(true);
                ModuloMarketService.activarPrueba7dias().then
                    (response => {
                    responseOk(response);
                    },
                    (error) => {
                        responseError(error);
                    }
                );
                }
                //ACTIVIDAD, etc
                else if(moduloElegido){
                setLoading(true);
                ModuloMarketService.activarPrueba7diasByEnum(moduloElegido).then
                    (response => {
                    responseOk(response);
                    },
                    (error) => {
                        responseError(error);
                    }
                );
                }
                else{
                    //Hay tiempo, no hay modulo, no deberia llegar acá.
                    setLoading(false);
                }
            }
            else if(tiempoElegido==="mes"){
                //Plan total
                if(moduloElegido==="all"){
                setLoading(true);
                ModuloMarketService.suscripcionPremiumMes().then
                    (response => {
                    responseOk(response);
                    },
                    (error) => {
                        responseError(error);
                    }
                );
                }
                //ACTIVIDAD, etc
                else if(moduloElegido){
                    setLoading(true);
                    ModuloMarketService.suscripcionBasicMes(moduloElegido).then
                        (response => {
                        responseOk(response);
                        },
                        (error) => {
                            responseError(error);
                        }
                    );
                }
                else{
                    //Hay tiempo, no hay modulo, no deberia llegar acá.
                    setLoading(false);
                }
            }
            else if(tiempoElegido==="anio"){
                //Plan total
                if(moduloElegido==="all"){
                setLoading(true);
                ModuloMarketService.suscripcionPremiumAnio().then
                    (response => {
                    responseOk(response);
                    },
                    (error) => {
                        responseError(error);
                    }
                );
                }
                //ACTIVIDAD, etc
                else if(moduloElegido){
                setLoading(true);
                ModuloMarketService.suscripcionBasicAnio(moduloElegido).then
                    (response => {
                    responseOk(response);
                    },
                    (error) => {
                        responseError(error);
                    }
                );
                }
                else{
                    //Hay tiempo, no hay modulo, no deberia llegar acá.
                    setLoading(false);
                }
            }
            else{
                //No hay tiempo definido, no deberia llegar acá.
                setLoading(false);
            }
        }
    };

    const cancel = () => {
        console.log(location.state);
        if(typeof scroll_horizontal === "number" && !isNaN(scroll_horizontal) && typeof scroll_vertical === "number" && !isNaN(scroll_vertical)){
            navigate("/marketplace", {state:{scroll_horizontal: location.state.scroll_horizontal, 
                scroll_vertical: location.state.scroll_vertical}});
        }
        else{
            navigate("/marketplace");
        }
        //window.location.reload();
    }

    useEffect(() => {
        if(location.state.modulo && location.state.tiempo){
            window.scrollTo({ top: 0, behavior: "smooth" });
            console.log("modulo: " + location.state.modulo + ", tiempo: " + location.state.tiempo);
            console.log(location.state);
            if(location.state.tiempo === "trial")
                handleEntrySubmitSuscripcion();
        }

    }, []);

    
    return (
        <div className="submit-form">
            <div>
                <br></br><br></br>
                {/*<div className = "container">*/}
                <div>
                    <div className = "row">
                        {/*<div className = "card col-md-6 offset-md-3 offset-md-3">*/}
                        <div className = "card-form col-md-6 offset-md-3 offset-md-3">
                          {!submitted ? (
                            <div className = "card-body">
                                {(location.state.modulo && location.state.tiempo) && (
                                    <div className="form-group">
                                        <Form onSubmit={handleSubmitSuscripcion} ref={form}>
                                            {/*<PersonaUpdateInput data={persona} handleInputChange={handleInputChange} />*/}
                                            <div className="card-form-container">
                                                <CardForm modulo={moduloElegido} tiempo={tiempoElegido} />
                                            </div>
                                            <div className="text-center">
                                                <div className="form-group">
                                                    <br></br>
                                                    <button className="btn btn-success" href="#" disabled={loading}>
                                                        <span></span>
                                                        <span></span>
                                                        <span></span>
                                                        <span></span>
                                                        {loading && (
                                                            <span className="spinner-border spinner-border-sm"></span>
                                                        )}
                                                        Realizar pago y suscribirse
                                                    </button>
                                                </div>
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
                                        <br></br>
                                        <div className="text-center">
                                            <button className="btn btn-danger" onClick={cancel} style={{marginLeft: "00px"}}>
                                                Cancelar
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </div>
                          ) : (
                            <div>
                                <h4>¡Has realizado el pago!</h4>
                                <button className="btn btn-success" onClick={cancel}>
                                  Regresar a marketplace
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

export default RealizarPagoComponent;

//Créditos diseño de tarjeta a:
//Nombre: Andrew Choung
//Link: https://codesandbox.io/s/credit-card-form-50fy0?file=/src/components/CardForm.js:75-1746
function CardForm(data) {
    const [modulo, setModulo] = useState("");
    const [tiempo, setTiempo] = useState("");
    const [submittedData, setSubmittedData] = useState({});
    const cargarTarjetaDefault = {
        name: "Alejandro Gonzalez",
        cardNumber: "4123123412341234",
        expiry: "1230",
        cvc: "123"
    }
    const [tarjeta, setTarjeta] = useState(cargarTarjetaDefault);
    //Forzar render cuando apenas presiona 1 caracter
    useEffect(() => {
        if (tarjeta !== null) {
            setSubmittedData(tarjeta);
        }
    }, [tarjeta]);

    const handleInputChange = event => {
        //Trae literalmente copia de "<Input", pero con value reemplazado con el value que escribió el usuario.
        const { name, value } = event.target;
        const updatedTarjeta = { ...tarjeta, [name]: value };
        setTarjeta({ ...tarjeta, [name]: value });
    };
    
    function handleSubmit(e) {
      e.preventDefault();
      setSubmittedData(tarjeta);
    }

    const handleInputFocus = e => {
      //this.setState({
      //  focused: e.target.name
      //})
    }

    const[suscripcionDetalle, setSuscripcionDetalle] = useState("");
    useEffect(() => {
        if(data && data.modulo && (typeof data.modulo === "string") &&
            data.tiempo && (typeof data.tiempo === "string")){
                console.log("Entre aca")
            setModulo(data.modulo.toLowerCase());
            setTiempo(data.tiempo.toLowerCase());
            let moduloAux = data.modulo.toLowerCase();//Este let es porque no me renderiza en pantalla setModulo a menos que F5 actualice
            let tiempoAux = data.tiempo.toLowerCase();//idem moduloAux
            let texto = "Suscripción por ";
            if(tiempoAux === "trial")
                texto += "7 días ";
            else if(tiempoAux === "mes")
                texto += "1 mes ";
            else if(tiempoAux === "anio")
                texto += "1 año ";
            else
                texto += tiempoAux + " ";
            texto += "para ";
            if(moduloAux === "all")
                texto += "plan total.";
            else
                texto += moduloAux;
            setSuscripcionDetalle(texto);
        }
    }, [data]);

    useEffect(() => {
    }, []);

    return (
        <form>
            <h2 className="text-center">Formulario de tarjeta de crédito</h2>
            <div><span className="text-center">{suscripcionDetalle}</span></div>
            <div><span className="text-center">Ingrese datos ficticios</span></div>
            <div className="form-group mt-4">
                <input
                    placeholder="Nombre del dueño de la tarjeta"
                    id="name"
                    name="name"
                    type="text"
                    pattern='[a-z A-Z-]+'
                    className="form-control mt-3"
                    value={tarjeta.name}
                    onChange={handleInputChange}
                    onFocus={handleInputFocus}
                    validations={[required]}
                />
                <input
                    placeholder="Número de tarjeta"
                    id="cardNumber"
                    name="cardNumber"
                    type="tel"
                    pattern='[\d| ]{16,22}'
                    maxLength={19}
                    className="form-control mt-3"
                    value={tarjeta.cardNumber}
                    onChange={handleInputChange}
                    onFocus={handleInputFocus}
                    validations={[required]}
                />
                <div className="expiry-and-cvc-container mt-3">
                    <input
                        placeholder="MM/YY"
                        id="expiry"
                        name="expiry" type="text" className="form-control expiration-date-field" 
                        value={tarjeta.expiry}
                        pattern='\d\d/\d\d'
                        maxLength={4}
                        onChange={handleInputChange}
                        onFocus={handleInputFocus}
                        validations={[required]}
                    />
                    <input
                        placeholder="CVC"
                        id="cvc"
                        name="cvc"
                        type="text"
                        className="form-control cvc-field ml-3"
                        value={tarjeta.cvc}
                        pattern='\d{3}'
                        maxLength={3}
                        onChange={handleInputChange}
                        onFocus={handleInputFocus}
                        validations={[required]}
                    />
                </div>
            </div>
            {/*<button
            type="submit"
            className="btn btn-primary btn-block"
            onClick={handleSubmit}
            >
            Submit
            </button>*/}
            <hr />
            <Results data={submittedData} />
        </form>
    );
}

function Results(props) {
    const { data } = props;
    const [isFrontOfCardVisible, setIsFrontOfCardVisible] = useState(true);
    function toggleCardFlip(e) {
      e.preventDefault();
      setIsFrontOfCardVisible(!isFrontOfCardVisible);
    }
  
    return (
      <div className="mt-3">
        {/*<h4>Submitted: </h4>
        <div>{JSON.stringify(data)}</div>*/}
        <div className="mt-3 cursor-pointer" onClick={toggleCardFlip}>
          <Card
            cvc={data.cvc || ""}
            expiry={data.expiry || ""}
            name={data.name || ""}
            number={data.cardNumber || ""}
            focused={isFrontOfCardVisible ? "number" : "cvc"}
          />
        </div>
        <div className="text-center">
          <button className="btn btn-info mt-3" onClick={toggleCardFlip}>
            {isFrontOfCardVisible ? "Ver dorso de tarjeta" : "Ver frente de tarjeta"}
          </button>
        </div>
      </div>
    );
}

