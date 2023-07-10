import React, { useState, useEffect } from 'react';
import { GraficoCreadosUltimoAño } from './Grafico_creadosUltimoAño';
import BeneficiarioService from "../../../services/BeneficiarioService";

const GraficoBeneficiario = ({visibilidadInput}) => {
  
  //Que cambiar si copias y pegas este código:
  const Service = BeneficiarioService;//Tambien cambiar el "Service" importado
  const el_la = "el";
  const nombreDatoSingular = "beneficiario";
  const las_los = "los";
  const nombreDatoPlural = "beneficiarios";
  //No olvides cambiar luego del return "{showContent && (" agregar o quitar los gráficos que querés
  //   y agregar <br/> al final

  const [useDataExample, setUseDataExample] = useState(false);
  const [visibilidad, setVisibilidad] = useState("NO_VISTA");

  useEffect(() => {
    if(visibilidadInput === "EDITAR"){
      setVisibilidad("EDITAR")
    }
  }, []);

  const handleToggleData = () => {
    setUseDataExample(prevState => !prevState);
  };
  
  const [showContent, setShowContent] = useState(true);
  const handleToggleContent = () => {
    setShowContent(!showContent);
  };

  return (
    <div>
    {visibilidad === 'EDITAR' && (
      <div>
        <div className="row">
          <div className="col-md-12 text-left">
            <button className="btn btn-primary" onClick={handleToggleContent}>
              {showContent ? 'Ocultar gráficos' : 'Mostrar gráficos'}
            </button>
            {showContent && (
              <button className="btn btn-primary ml-2" onClick={handleToggleData}>
                {useDataExample ? 'Cambiar a datos reales en Base de Datos' : 'Cambiar a datos ficticios de ejemplo'}
              </button>
            )}
          </div>
        </div>
        {showContent && (
          <div>
            <GraficoCreadosUltimoAño
              showContent={showContent}
              useDataExample={useDataExample}
              visibilidadInput={visibilidadInput}
              Service={Service}
              el_la={el_la}
              nombreDatoSingular={nombreDatoSingular}
              las_los={las_los}
              nombreDatoPlural={nombreDatoPlural}
            />
            <br/>
          </div>
        )}
      </div>
      )}
    </div>

  );
};

export {
  GraficoBeneficiario
}
