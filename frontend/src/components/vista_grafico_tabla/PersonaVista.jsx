import React, { useState, useEffect } from "react";
import TablaPersona from "./tables/TablaPersona";

import "../../Styles/TablasDinamicas.scss";

function PersonaVista() {

  return (
    <div className="Vista">
      <div className="ComponentePrincipal">

        <TablaPersona />

      </div>
      
    </div>
  );
}

export default PersonaVista;
