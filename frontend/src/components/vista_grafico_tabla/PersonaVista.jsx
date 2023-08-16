import React, { useState, useEffect } from "react";
import { GraficoPersona } from './graficos/GraficoPersona'
import { TablaPersona } from "./tables/TablaPersona";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function PersonaVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('PERSONA');
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

          <GraficoPersona
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaPersona
            visibilidadInput={visibilidad}
          />

        </div>

      )}
    </div>
  );
}

export default PersonaVista;
