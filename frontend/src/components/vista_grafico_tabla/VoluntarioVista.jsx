import React, { useState, useEffect } from "react";
import { GraficoVoluntario } from './graficos/GraficoVoluntario'
import TablaVoluntario from "./tables/TablaVoluntario";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function VoluntarioVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('VOLUNTARIO');
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

          <GraficoVoluntario
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaVoluntario
            visibilidadInput={visibilidad}
          />

        </div>

      )}
    </div>
  );
}

export default VoluntarioVista;
