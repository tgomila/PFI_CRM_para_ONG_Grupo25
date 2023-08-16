import React, { useState, useEffect } from "react";
import modulosService from "../../services/modulosService";
import "../../Styles/Graficos.scss";

function AbstractVistaSinGraficos(ComponenteTabla, moduloKey) {
  return function WrappedComponent() {
    const [visibilidad, setVisibilidad] = useState("");
    const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

    useEffect(() => {
      let modulo = modulosService.getVisibilidadByModulo(moduloKey);
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
            <ComponenteTabla visibilidadInput={visibilidad} />
          </div>
        )}
      </div>
    );
  };
}

export default withVisibilidad;
