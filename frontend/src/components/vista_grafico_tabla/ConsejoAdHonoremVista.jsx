import React, { useState, useEffect } from "react";
import { GraficoConsejoAdHonorem } from './graficos/GraficoConsejoAdHonorem'
import TablaConsejoAdHonorem from "./tables/TablaConsejoAdHonorem";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function ConsejoAdHonoremVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('CONSEJOADHONOREM');
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

          <GraficoConsejoAdHonorem
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaConsejoAdHonorem
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default ConsejoAdHonoremVista;
