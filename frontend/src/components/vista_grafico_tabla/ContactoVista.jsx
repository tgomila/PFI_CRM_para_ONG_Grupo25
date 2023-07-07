import React, { useState, useEffect } from "react";
import GraficoContacto from "./graficos/GraficoContacto";
import TablaContacto from "./tables/TablaContacto";

import "../../Styles/TablasDinamicas.scss";

function ContactoVista() {

  return (
    <div className="Vista">
      <div className="ComponentePrincipal">

        <GraficoContacto />
        <br/>

        <TablaContacto />

      </div>




    </div>
  );
}

export default ContactoVista;
