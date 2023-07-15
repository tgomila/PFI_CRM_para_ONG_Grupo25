import React, { useState, useEffect } from "react";
import { GraficoProfesional } from './graficos/GraficoProfesional'
import TablaProfesional from "./tables/TablaProfesional";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function ProfesionalVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('PROFESIONAL');
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

          <GraficoProfesional
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaProfesional
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default ProfesionalVista;
