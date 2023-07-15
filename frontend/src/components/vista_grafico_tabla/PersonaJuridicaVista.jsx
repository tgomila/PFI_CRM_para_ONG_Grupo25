import React, { useState, useEffect } from "react";
import { GraficoPersonaJuridica } from './graficos/GraficoPersonaJuridica'
import TablaPersonaJuridica from "./tables/TablaPersonaJuridica";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function PersonaJuridicaVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('PERSONAJURIDICA');
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

          <GraficoPersonaJuridica
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaPersonaJuridica
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default PersonaJuridicaVista;
