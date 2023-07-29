import React, { useState, useEffect } from "react";
import TablaProducto from "./tables/TablaProducto";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function ProductoVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('PRODUCTO');
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

          <TablaProducto
            visibilidadInput={visibilidad}
          />

        </div>

      )}
    </div>
  );
}

export default ProductoVista;
