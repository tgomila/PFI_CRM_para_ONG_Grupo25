import React, { useState, useEffect } from "react";
import modulosService from "../../services/modulosService";
import "../../Styles/Graficos.scss";

function AbstractVista(ComponenteTabla, nombreDatoVisibilidad, ComponenteGrafico, tituloNombrePrural) {
  return function WrappedComponent() {
    const [visibilidad, setVisibilidad] = useState("");
    const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);
    const [tituloNombrePruralModificado, setTituloNombrePruralModificado] = useState(null);

    useEffect(() => {
      let modulo = modulosService.getVisibilidadByModulo(nombreDatoVisibilidad);
      modulo.then((response) => {
        if (response) {
          setVisibilidad(response);
          setIsVisibilidadReady(true);
        }
      });
      if(tituloNombrePrural){
        setTituloNombrePruralModificado(tituloNombrePrural.charAt(0).toUpperCase() + tituloNombrePrural.slice(1).toLowerCase());
      }
    }, []);

    return (
      <div className="ComponentePrincipalGraficos">
        {tituloNombrePruralModificado && (
          <div className="Marketplace">
            <h1><span className="underlined underline-clip-title">{tituloNombrePruralModificado}</span></h1>
            <br/><br/>
        </div>
        )}
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