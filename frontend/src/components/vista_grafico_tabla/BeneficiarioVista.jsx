import React, { useState, useEffect } from "react";
import { GraficoBeneficiario } from './graficos/GraficoBeneficiario'
import TablaBeneficiario from "./tables/TablaBeneficiario";
import modulosService from "../../services/modulosService";

import "../../Styles/TablasDinamicas.scss";

function BeneficiarioVista() {
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
    <div className="Vista">
      {isVisibilidadReady && (
        <div className="ComponentePrincipal">

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
