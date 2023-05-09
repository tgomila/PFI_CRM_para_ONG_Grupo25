import React, { useState, useRef, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import ModuloMarketService from "../services/ModuloMarketService";
import moment from "moment";

//import AuthService from "../services/auth.service";

import "../Styles/Marketplace.scss";




function Marketplace() {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [modulos, setModulos] = useState();

  const aunTieneModulosParaPruebaLibre = () => {
    return modulos && modulos.some(item => item.fechaPrueba7DiasUtilizada === null);
  }
  const buscarPrecioUnMes = (busqueda) => {
    try{
      const moduloEncontrado = modulos.find((item) => item.moduloEnum === busqueda);
      if (moduloEncontrado) {
        return formatter.format(moduloEncontrado.precioUnMes);
      } else {
        return "No se ha encontrado precio.";
      }

    } catch (e) {
      return "No se ha encontrado precio.";
    }
  };
  const buttonTrialSiNoFueCanjeado = (modulo) => {
    try{
      const moduloEncontrado = modulos.find((item) => item.moduloEnum === modulo);
      if (moduloEncontrado) {
        return <button className="card__btn" onClick={() => chooseSubscription({modulo}, "trial")}>¡Prueba 7 días! <span>&rarr;</span></button>;
      } else {
        return <button className="card__btn">Prueba ya probada</button>;
      }

    } catch (e) {
      return "No se ha encontrado si se utilizó trial en "+{modulo}+".";
    }
  };
  const buscarPrecioUnAnio = (busqueda) => {
    try{
      const moduloEncontrado = modulos.find((item) => item.moduloEnum === busqueda);
      if (moduloEncontrado) {
        return formatter.format(moduloEncontrado.precioUnAnio);
      } else {
        return "No se ha encontrado precio.";
      }

    } catch (e) {
      return "No se ha encontrado precio.";
    }
  };
  const buscarFechaPremiumMasBaja = () => {
    //Si hay alguna fecha nula, significa que nunca se ha suscripto, no hay plan total.
    const aux = modulos.filter(item => item.fechaMaximaSuscripcion === null);
    if(aux.length!=0){
      return null;
    }
    //Busco la fecha mas baja para mostrar como plan total.
    const fechasSuscripcionDefinidas = modulos.filter(objeto => objeto.fechaMaximaSuscripcion !== null);
    const fechasOrdenadas = fechasSuscripcionDefinidas.sort((a, b) => a.fechaMaximaSuscripcion.localeCompare(b.fechaMaximaSuscripcion));
    const fechaMasBaja = fechasOrdenadas[0]?.fechaMaximaSuscripcion;
    console.log("Fechas ordenadas");
    console.log(fechasOrdenadas);
    return formatFecha(fechaMasBaja);// new Date(fechaMasBaja).toLocaleDateString('es-AR');
  };
  const informacionSuscripcion = (busqueda) => {
    try{
      if(!busqueda){
        const suscripto = !(modulos && modulos.some(item => item.suscripcionActiva === false));
        const fechaMasBaja = buscarFechaPremiumMasBaja();
        const aux1 = modulos.some(item => item.fechaPrueba7DiasUtilizada === null);
        const aux2 = modulos.some(item => item.fechaPrueba7DiasUtilizada !== null);
        console.log("aux1: ");
        console.log(aux1);
        console.log("aux2: ");
        console.log(aux2);
        return(
          <div>
            {suscripto ? (
              <div>
                <p className="suscripto-true">Suscripto al plan total</p>
                {fechaMasBaja ? (
                  <p className="card__text_info">Suscripto hasta el: {fechaMasBaja}</p>
                ) : (
                  <p className="card__text_info">Se desuscribirá al siguiente día</p>
                )}
              </div>
            ) : (
              <div>
                <p className="suscripto-false">Desuscripto del plan total</p>
                {fechaMasBaja ? (
                  <p className="card__text_info">La suscripcion ha durado hasta: {fechaMasBaja}</p>
                ) : (
                  <p className="card__text_info">Nunca se ha suscripto</p>
                )}
              </div>
            )}
          </div>
        );
      }
      const moduloEncontrado = modulos.find((item) => item.moduloEnum === busqueda);
      if (moduloEncontrado) {
        return(
          <div>
            {moduloEncontrado.suscripcionActiva ? (
              <div>
                <p className="suscripto-true">Suscripto</p>
                {/*moduloEncontrado.fechaInicioSuscripcion ? (
                  <p className="card__text_info">Posee suscripcion desde: {formatFecha(moduloEncontrado.fechaInicioSuscripcion)}</p>
                ) : (
                  <p className="card__text_info">Posee suscripcion desde: ?</p>
                )*/}
                {moduloEncontrado.fechaMaximaSuscripcion ? (
                  <p className="card__text_info">Suscripto hasta: {formatFecha(moduloEncontrado.fechaMaximaSuscripcion)}</p>
                ) : (
                  <p className="card__text_info">Suscriptohasta: hoy</p>
                )}
              </div>
            ) : (
              <div>
                <p className="suscripto-false">Desuscripto</p>
                {(moduloEncontrado.fechaPrueba7DiasUtilizada) ? (
                  <p className="card__text_info">La suscripción ha durado hasta: {formatFecha(moduloEncontrado.fechaMaximaSuscripcion)}</p>
                ) : (
                  <p className="card__text_info">Nunca se ha suscripto</p>
                )}
              </div>
            )}
            <p className="card__text_info">Trial 7 días utilizado: {moduloEncontrado.fechaPrueba7DiasUtilizada ? "Si, fecha canjeado " + formatFecha(moduloEncontrado.fechaPrueba7DiasUtilizada) : "No"} </p>
          </div>
        );
      } else {
        return "No se ha encontrado precio.";
      }

    } catch (e) {
      return "No se ha encontrado precio.";
    }
  };
  const botonesSuscripcion = (modulo, tiempo) => {
    try{
      const moduloEncontrado = modulos.find((item) => item.moduloEnum === modulo);
      if (moduloEncontrado) {
        return(
          <div>
            {!moduloEncontrado.suscripcionActiva ? (
              <div>
                {moduloEncontrado.fechaPrueba7DiasUtilizada === null ? (
                  <button className="card__btn" onClick={() => chooseSubscription({modulo}, "trial")}>Iniciar prueba gratuita 7 días</button>
                ) : <div></div>}
                <button className="card__btn">Ya ha sido canjeado</button>
              </div>
            ) : (
              <div>
                <button className="card__btn" onClick={() => chooseSubscription("all", "trial")}>Ya ha sido canjeado</button>
                <p className="card__text_info">Vencido en la fecha {formatFecha(moduloEncontrado.fechaMaximaSuscripcion)} </p>
              </div>
            )}
            
            
          </div>
        );
      } else {
        return "No se ha encontrado precio.";
      }

    } catch (e) {
      return "No se ha encontrado precio.";
    }
  };
  const formatFecha = (fechaISO8601) => {
    return moment(fechaISO8601).locale("es").format("DD/MM/YYYY");
  };
  const formatFechaHora = (fechaISO8601) => {
    return moment(fechaISO8601).locale("es").format("DD/MM/YYYY [a las] HH:mm");
  };

  const formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    currencyDisplay: 'code',
  
    // These options are needed to round to whole numbers if that's what you want.
    //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
  });

  const[precioTotalUnMes, setPrecioTotalUnMes] = useState("");
  const[precioTotalUnAnio, setPrecioTotalUnAnio] = useState("");

  const[moduloElegido, setModuloElegido] = useState("");
  const[tiempoElegido, setTiempoElegido] = useState("");
  
  const navigate = useNavigate();
  const chooseSubscription = (moduloClick, tiempoClick) => {
    //setModuloElegido(moduloClick);
    //setTiempoElegido(tiempoClick);
    console.log("modulo: " + moduloClick);
    console.log("tiempo: " + tiempoClick);
    navigate( window.location.pathname + "/pagar", {state:{modulo:moduloClick, tiempo: tiempoClick}})
  }
  const chooseTrial = (moduloClick, tiempoClick) => {
    setModuloElegido(moduloClick);
    setTiempoElegido(tiempoClick);
    console.log("modulo: " + moduloElegido);
    console.log("tiempo: " + tiempoElegido);
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  const cancel = () => {
    setModuloElegido("");
    setTiempoElegido("");
    setMensajeOk("");
    setSubmitted(false);
    setModulos("");
    setMessage("");
  }

  const [mensajeOk, setMensajeOk] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const handleSubmitSuscripcion = (e) => {
    e.preventDefault();
    //modulo, tiempo

    //trial, mes, anio
    if(tiempoElegido==="trial"){
      //Plan total
      if(moduloElegido==="all"){
        setLoading(true);
        ModuloMarketService.activarPrueba7dias().then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      //ACTIVIDAD, etc
      else if(moduloElegido){
        setLoading(true);
        ModuloMarketService.activarPrueba7diasByEnum(moduloElegido).then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      else{
        //Hay tiempo, no hay modulo, no deberia llegar acá.
      }
    }
    else if(tiempoElegido==="mes"){
      //Plan total
      if(moduloElegido==="all"){
        setLoading(true);
        ModuloMarketService.suscripcionPremiumMes().then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      //ACTIVIDAD, etc
      else if(moduloElegido){
        setLoading(true);
        ModuloMarketService.suscripcionBasicMes(moduloElegido).then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      else{
        //Hay tiempo, no hay modulo, no deberia llegar acá.
      }
    }
    else if(tiempoElegido==="anio"){
      //Plan total
      if(moduloElegido==="all"){
        setLoading(true);
        ModuloMarketService.suscripcionPremiumAnio().then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      //ACTIVIDAD, etc
      else if(moduloElegido){
        setLoading(true);
        ModuloMarketService.suscripcionBasicAnio(moduloElegido).then
          (response => {
            setMessage("");
            setLoading(false);
            setSubmitted(true);
            setModulos(response.data);
            console.log(response.data);
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
      }
      else{
        //Hay tiempo, no hay modulo, no deberia llegar acá.
      }
    }
    else{
      //No hay tiempo definido, no deberia llegar acá.
    }
  };

  useEffect(() => {
    cancel();
    setLoading(true);
    ModuloMarketService.getPaidModulos().then
      (response => {
        setLoading(false);
        setModulos(response.data);
        console.log(response.data);
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

    ModuloMarketService.precioSuscripcionPremiumMes().then
      (response => {
        setPrecioTotalUnMes(formatter.format(response.data));
      }
    );
    ModuloMarketService.precioSuscripcionPremiumAnio().then
      (response => {
        setPrecioTotalUnAnio(formatter.format(response.data));
      }
    );
    //window.scrollTo({ top: 0, behavior: "smooth" });
  }, []);

  return (
    <div className="Marketplace">
      {(!moduloElegido && !tiempoElegido) ? (
        <div>
          <br></br><br></br>
          <h1><span className="underlined underline-clip-title">Marketplace</span></h1>
          <br></br>
          <p className="parrafo1">Encuentre lo mejor para su ONG.</p>
          <br></br>
          <h2><span className="underlined underline-clip-subtitle">Plan completo</span></h2>
          <br></br>
          <p className="parrafo1">Encuentre el Plan para su ONG al mejor precio.</p>
          <div className="grid">
            <div className="grid__item">
              <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Snowy Mountains" />
                <div className="card__content">
                  <h1 className="card__header">7 Dias Gratis</h1>
                  
                  {informacionSuscripcion("")}
                  <br></br>
                  <p className="card__text">Prueba nuestro proyecto con sabor <strong>premium</strong> en su ONG por 7 días! </p>
                  {modulos && modulos.every(item => item.fechaPrueba7DiasUtilizada === null) ? (
                    <button className="card__btn" onClick={() => chooseSubscription("all", "trial")}>¡Prueba 7 días gratis!</button>
                  ) : (modulos && modulos.some(item => item.fechaPrueba7DiasUtilizada === null) ? (
                      <button className="card__btn">Trial 7 días ya canjeado<br/>(en algún módulo)</button>
                    ) : (
                      <button className="card__btn">Trial 7 días ya canjeado</button>
                    )
                  )}
                </div>
              </div>
            </div>
            <div className="grid__item">
              <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1485160497022-3e09382fb310?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Desert" />
                <div className="card__content">
                  <h1 className="card__header">Plan total 1 mes</h1>
                  {informacionSuscripcion("")}
                  <br></br>
                  <p className="card__text">Adquiera la versión <strong>premium </strong> por 1 mes</p>
                  <button className="card__btn" onClick={() => chooseSubscription("all", "mes")}>Comprar 1 mes {precioTotalUnMes}<span>&rarr;</span></button>
                </div>
              </div>
            </div>
            <div className="grid__item">
              <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                <div className="card__content">
                  <h1 className="card__header">Plan total 12 meses</h1>
                  {informacionSuscripcion("")}
                  <br></br>
                  <p className="card__text">Adquiera la versión <strong>premium</strong> por 12 meses a precio de 10 meses.</p>
                  <button className="card__btn" onClick={() => chooseSubscription("all", "anio")}>Comprar 12 meses {precioTotalUnAnio} <span>&rarr;</span></button>
                </div>
              </div>
            </div>


            {/*  */}


          </div>

          <br></br><br></br>

          <h2><span className="underlined underline-clip-subtitle">Módulo individual</span></h2>
          <br></br>
          <p className="parrafo1">O sinó, suscribase al módulo que su ONG desea.</p>
          {/*<div className="container mx-auto">*/}
          <div>
            <div className="row">
              <div className="col-lg-4 col-md-12">
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Actividad</h1>
                      {informacionSuscripcion('ACTIVIDAD')}
                      <br></br>
                      <p className="card__text">Guarde las actividades que realice en su ONG con la mejor organización, asignando <strong>beneficiarios</strong> y <strong>profesionales</strong> a cada <strong>actividad</strong>.</p>
                      {buttonTrialSiNoFueCanjeado('ACTIVIDAD')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("ACTIVIDAD", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('ACTIVIDAD')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("ACTIVIDAD", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('ACTIVIDAD')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="col-lg-4 col-md-6">
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Factura</h1>
                      {informacionSuscripcion('FACTURA')}
                      <br></br>
                      <p className="card__text">Con este módulo podrá almacenar <strong>facturas de</strong> sus diferentes <strong>contactos</strong> que haya almacenado en el sistema. Útil para el manejo económico.</p>
                      {buttonTrialSiNoFueCanjeado('FACTURA')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("FACTURA", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('FACTURA')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("FACTURA", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('FACTURA')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="col-lg-4 col-md-6">
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Insumo</h1>
                      {informacionSuscripcion('INSUMO')}
                      <br></br>
                      <p className="card__text">Módulo que almacena <strong>información</strong> de <strong>bienes</strong> de la ONG.</p>
                      {buttonTrialSiNoFueCanjeado('INSUMO')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("INSUMO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('INSUMO')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("INSUMO", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('INSUMO')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-lg-4 col-md-12">
              {/* <div className="col-xs-12 col-sm-12 col-md-6 col-lg-4"> */}
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Prestamo</h1>
                      {informacionSuscripcion('PRESTAMO')}
                      <br></br>
                      <p className="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                      {buttonTrialSiNoFueCanjeado('PRESTAMO')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("PRESTAMO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('PRESTAMO')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("PRESTAMO", "anio")}>Comprar 1 mes por {buscarPrecioUnAnio('PRESTAMO')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="col-lg-4 col-md-6">
              {/* <div className="col-xs-12 col-sm-12 col-md-6 col-lg-4"> */}
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Proyecto</h1>
                      {informacionSuscripcion('PROYECTO')}
                      <br></br>
                      <p className="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                      {buttonTrialSiNoFueCanjeado('PROYECTO')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("PROYECTO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('PROYECTO')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("PROYECTO", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('PROYECTO')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>

              <div className="col-lg-4 col-md-6">
              {/* <div className="col-xs-12 col-sm-12 col-md-6 col-lg-4"> */}
                <div className="grid__item_modulos">
                  <div className="card"><img className="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                    <div className="card__content">
                      <h1 className="card__header">Chat</h1>
                      {informacionSuscripcion('CHAT')}
                      <br></br>
                      <p className="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                      {buttonTrialSiNoFueCanjeado('CHAT')}
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("CHAT", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('CHAT')}<span>&rarr;</span></button>
                      <br></br>
                      <button className="card__btn" onClick={() => chooseSubscription("CHAT", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('CHAT')}<span>&rarr;</span></button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div className="submit-form">
          <div>
            <br></br><br></br>
            <div className = "container">
              <div className = "row">
                <div className = "card col-md-6 offset-md-3 offset-md-3">
                  <div className = "card-body">
                    <div className = "form-group">
                      Hola

                      {/**TODO Al presionar boton submit, abajo del boton esto de message */}
                      {message && (
                        <div className="form-group">
                          <div className="alert alert-danger" role="alert">
                            {message}
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      )}
    </div>
  )
}

/*function MapIntro() {
    //GOOGLE MAPS
    const { isLoaded } = useLoadScript({
        googleMapsApiKey: process.env.NEXT_PUBLIC_GOOGLE_MAPS_KEY,
    });
    console.log("Llegue a maps");
    if(!isLoaded)
    console.log("No loaded");
    else
    console.log("Si loaded");
    if(!isLoaded) return <div>Loading...</div>
    return <Map />

    //FIN CODIGO GOOGLE MAPS
};*/

/*function Map() {
  const center = useMemo(() => ({lat: 44, lng: -80}), []);
    return <GoogleMap zoom={10} center={center} mapContainerClassName="map-container">
      <Marker position={center}/>
    </GoogleMap>
}*/

export default Marketplace;