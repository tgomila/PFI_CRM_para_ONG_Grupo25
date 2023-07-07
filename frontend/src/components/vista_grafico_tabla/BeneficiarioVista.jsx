import React, { useState, useEffect } from "react";
import TablaBeneficiario from "./tables/TablaBeneficiario";

import "../../Styles/TablasDinamicas.scss";

function BeneficiarioVista() {

  return (
    <div className="Vista">
      <div className="ComponentePrincipal">

        <TablaBeneficiario />

      </div>
      
    </div>
  );
}

export default BeneficiarioVista;
