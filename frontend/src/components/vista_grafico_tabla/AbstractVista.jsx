import React, { useState, useEffect } from "react";
import modulosService from "../../services/modulosService";
import "../../Styles/Graficos.scss";

function AbstractVista(ComponenteTabla, nombreDatoVisibilidad, ComponenteGrafico) {
  return function WrappedComponent() {
    const [visibilidad, setVisibilidad] = useState("");
    const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

    useEffect(() => {
      let modulo = modulosService.getVisibilidadByModulo(nombreDatoVisibilidad);
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
            {ComponenteGrafico && (
              <>
                <ComponenteGrafico visibilidadInput={visibilidad} />
                <br />
              </>
            )}

            <ComponenteTabla visibilidadInput={visibilidad} />
          </div>
        )}
      </div>
    );
  };
}

export default AbstractVista;