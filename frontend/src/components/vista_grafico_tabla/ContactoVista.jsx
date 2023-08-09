import React, { useState, useEffect } from "react";
import { GraficoContacto } from './graficos/GraficoContacto'
import TablaContacto from "./tables/TablaContacto";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function ContactoVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('CONTACTO');
    modulo.then((response) => {
      if (response) {
        setVisibilidad(response);
        setIsVisibilidadReady(true);
      }
    });
  }, []);

  return (
    <div className="ComponentePrincipalGraficos">
      {isVisibilidadReady && (
        <div>

          <GraficoContacto
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaContacto
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default ContactoVista;