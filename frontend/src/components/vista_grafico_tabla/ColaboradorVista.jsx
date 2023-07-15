import React, { useState, useEffect } from "react";
import { GraficoColaborador } from './graficos/GraficoColaborador'
import TablaColaborador from "./tables/TablaColaborador";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function ColaboradorVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('COLABORADOR');
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

          <GraficoColaborador
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaColaborador
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default ColaboradorVista;
