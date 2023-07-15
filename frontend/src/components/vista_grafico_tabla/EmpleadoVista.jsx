import React, { useState, useEffect } from "react";
import { GraficoEmpleado } from './graficos/GraficoEmpleado'
import TablaEmpleado from "./tables/TablaEmpleado";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function EmpleadoVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('EMPLEADO');
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

          <GraficoEmpleado
            visibilidadInput={visibilidad}
          />
          <br/>

          <TablaEmpleado
            visibilidadInput={visibilidad}
          />

        </div>

      )}




    </div>
  );
}

export default EmpleadoVista;
