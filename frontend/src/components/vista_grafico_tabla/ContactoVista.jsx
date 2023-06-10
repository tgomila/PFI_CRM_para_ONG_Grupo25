import React, { useState, useEffect } from "react";
import BaseService from "../../services/BaseService";
import TablaContacto from "../tables/TablaContacto";
import ContactoGrafico from "../graficos/ContactoGrafico";

import "../../Styles/TablasDinamicas.scss";

import { useNavigate } from 'react-router-dom';

function ContactoVista() {
  const [data, setData] = useState([]);


  
  const navigate = useNavigate();

  const [employees, setEmployees] = useState([]);

  const actualizarTabla = () => {};

  //Se llama al Endpoint y se trae todo los datos
  const componentDidMount = () => {};



  /*useEffect(() => {
    // Fetch data
    // Update the document title using the browser API

  }, []);*/

  return (
    <div className="Vista">
      <div className="ComponentePrincipal">

        <ContactoGrafico />
        <br/>
        
        {/** Tabla de contactos aquí */}
        <TablaContacto />

      </div>




    </div>
  );
}

export default ContactoVista;
