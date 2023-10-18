import React, { useState, useEffect } from 'react';
import { GraficoCreadosUltimoAño } from './Grafico_creadosUltimoAño';
import { GraficoCategoriaEdades } from './Grafico_rango_edad';
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

  const dataExampleEdad = [
    { rangoEdad: "0 a 5 años", etapa: "Primera Infancia", cantidad: 0 },
    { rangoEdad: "6 a 11 años", etapa: "Infancia", cantidad: 57 },
    { rangoEdad: "12 a 18 años", etapa: "Adolescencia", cantidad: 82 },
    { rangoEdad: "19 a 26 años", etapa: "Juventud", cantidad: 19 },
    { rangoEdad: "27 a 59 años", etapa: "Adultez", cantidad: 7 },
    { rangoEdad: "mayor a 60 años", etapa: "Persona mayor", cantidad: 2 },
  ];

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
            <GraficoCategoriaEdades
              showContent={showContent}
              useDataExample={useDataExample}
              visibilidadInput={visibilidadInput}
              dataExampleInput={dataExampleEdad}
              Service={Service}
              el_la={el_la}
              nombreDatoSingular={nombreDatoSingular}
              las_los={las_los}
              nombreDatoPlural={nombreDatoPlural}
            />
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
