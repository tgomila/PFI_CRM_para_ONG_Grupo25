import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ModuloMarketService from "../services/ModuloMarketService";

//import AuthService from "../services/auth.service";

import "../Styles/Marketplace.scss";




function Marketplace() {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [sieteDiasUtilizadoBoolean, setSieteDiasUtilizadoBoolean] = useState(false);
  const [modulos, setModulos] = useState();

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

  const[planElegido, setPlanElegido] = useState("");
  const[moduloElegido, setModuloElegido] = useState("");
  const[tiempoElegido, setTiempoElegido] = useState("");
  const chooseSubscription = (planClick, moduloClick, tiempoClick) => {
    setPlanElegido(planClick);
    setModuloElegido(moduloClick);
    setTiempoElegido(tiempoClick);
    console.log("plan: " + planElegido);
    console.log("modulo: " + moduloElegido);
    console.log("tiempo: " + tiempoElegido);
  }

  useEffect(() => {
    setMessage("");
    setLoading(true);
    ModuloMarketService.isPrueba7diasUtilizada().then
      (response => {
        setLoading(false);
        setSieteDiasUtilizadoBoolean(response.data);
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
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, []);

  return (
    <div class="Marketplace">
      <br></br><br></br>
      <h1><span class="underlined underline-clip-title">Marketplace</span></h1>
      <br></br>
      <p className="parrafo1">Encuentre lo mejor para su ONG.</p>
      <br></br>
      <h2><span class="underlined underline-clip-subtitle">Plan completo</span></h2>
      <br></br>
      <p className="parrafo1">Encuentre el Plan para su ONG al mejor precio.</p>
      <div class="grid">
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Snowy Mountains" />
            <div class="card__content">
              <h1 class="card__header">7 Dias Gratis</h1>
              <p className="precio1">{buscarPrecioUnMes('ACTIVIDAD')}</p>
              <p class="card__text">Prueba nuestro proyecto con sabor <strong>premium</strong> en su ONG por 7 días! </p>
              {sieteDiasUtilizadoBoolean ? (
                <button class="card__btn" onClick={() => chooseSubscription("all", "", "trial")}>Ya ha sido canjeado</button>
              ) : (
                <button class="card__btn">Comprar <span>&rarr;</span>
                </button>
              )}
            </div>
          </div>
        </div>
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1485160497022-3e09382fb310?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2250&amp;q=80"} alt="Desert" />
            <div class="card__content">
              <h1 class="card__header">Plan total 1 mes</h1>
              <p className="precio1">{precioTotalUnMes}</p>
              <p class="card__text">Adquiera la versión <strong>premium </strong> por 1 mes</p>
              <button class="card__btn" onClick={() => chooseSubscription("all", "", "mes")}>Comprar 1 mes {precioTotalUnMes}<span>&rarr;</span></button>
            </div>
          </div>
        </div>
        <div class="grid__item">
          <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
            <div class="card__content">
              <h1 class="card__header">Plan total 12 meses</h1>
              <p className="precio1">{precioTotalUnAnio}</p>
              <p class="card__text">Adquiera la versión <strong>premium</strong> por 12 meses a precio de 10 meses.</p>
              <button class="card__btn" onClick={() => chooseSubscription("all", "", "anio")}>Comprar 12 meses {precioTotalUnAnio} <span>&rarr;</span></button>
            </div>
          </div>
        </div>


        {/*  */}


        </div>

        <br></br><br></br>

        <h2><span class="underlined underline-clip-subtitle">Módulo individual</span></h2>
        <br></br>
        <p className="parrafo1">O sinó, suscribase al módulo que su ONG desea.</p>
        {/*<div class="container mx-auto">*/}
        <div>
          <div class="row">
            <div class="col-lg-4 col-md-12">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Actividad</h1>
                    <p class="card__text">Guarde las actividades que realice en su ONG con la mejor organización, asignando <strong>beneficiarios</strong> y <strong>profesionales</strong> a cada <strong>actividad</strong>.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "ACTIVIDAD", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('ACTIVIDAD')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "ACTIVIDAD", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('ACTIVIDAD')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Factura</h1>
                    <p class="card__text">Con este módulo podrá almacenar <strong>facturas de</strong> sus diferentes <strong>contactos</strong> que haya almacenado en el sistema. Útil para el manejo económico.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "FACTURA", "mes")}>Comprar 1 año por {buscarPrecioUnMes('FACTURA')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "FACTURA", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('FACTURA')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Insumo</h1>
                    <p class="card__text">Módulo que almacena <strong>información</strong> de <strong>bienes</strong> de la ONG.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "INSUMO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('INSUMO')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "INSUMO", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('INSUMO')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-lg-4 col-md-12">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Prestamo</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "PRESTAMO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('PRESTAMO')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "PRESTAMO", "anio")}>Comprar 1 mes por {buscarPrecioUnAnio('PRESTAMO')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Proyecto</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "PROYECTO", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('PROYECTO')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "PROYECTO", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('PROYECTO')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-lg-4 col-md-6">
              <div class="grid__item_modulos">
                <div class="card"><img class="card__img" src={"https://images.unsplash.com/photo-1506318164473-2dfd3ede3623?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=3300&amp;q=80"} alt="Canyons" />
                  <div class="card__content">
                    <h1 class="card__header">Chat</h1>
                    <p class="card__text">Sunsets over the <strong>stunning</strong> Utah Canyonlands, is truly something much more than incredible.</p>
                    <button class="card__btn" onClick={() => chooseSubscription("", "CHAT", "mes")}>Comprar 1 mes por {buscarPrecioUnMes('CHAT')}<span>&rarr;</span></button>
                    <br></br>
                    <button class="card__btn" onClick={() => chooseSubscription("", "CHAT", "anio")}>Comprar 1 año por {buscarPrecioUnAnio('CHAT')}<span>&rarr;</span></button>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>


    </div>
  )
}

export default Marketplace