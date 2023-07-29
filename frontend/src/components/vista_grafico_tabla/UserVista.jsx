import React, { useState, useEffect } from "react";
import TablaUser from "./tables/TablaUser";
import modulosService from "../../services/modulosService";

import "../../Styles/Graficos.scss";

function UserVista() {
  const [visibilidad, setVisibilidad] = useState("");
  const [isVisibilidadReady, setIsVisibilidadReady] = useState(false);

  useEffect(() => {
    let modulo = modulosService.getVisibilidadByModulo('USERS');
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

          <TablaUser
            visibilidadInput={visibilidad}
          />

        </div>

      )}
    </div>
  );
}

export default UserVista;
