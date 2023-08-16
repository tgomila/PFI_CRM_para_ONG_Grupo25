import React, { useState, useEffect } from "react";
import { GraficoBeneficiario } from './graficos/GraficoBeneficiario'
import { TablaBeneficiario } from "./tables/TablaBeneficiario";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function BeneficiarioVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('BENEFICIARIO');
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

          <GraficoBeneficiario
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaBeneficiario
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default BeneficiarioVista;
