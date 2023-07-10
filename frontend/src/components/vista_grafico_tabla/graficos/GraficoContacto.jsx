import React, { useState, useEffect } from 'react';
import { GraficoCreadosUltimoAño } from './Grafico_creadosUltimoAño';
import ContactoService from "../../../services/ContactoService";

const GraficoContacto = ({visibilidadInput}) => {
  
  //Que cambiar si copias y pegas este código:
  const Service = ContactoService;//Tambien cambiar el "Service" importado
  const el_la = "el";
  const nombreDatoSingular = "contacto";
  const las_los = "los";
  const nombreDatoPlural = "contactos";
  //No olvides cambiar luego del return "{showContent && (" agregar o quitar los gráficos que querés
  //   y agregar <br/> al final

  //no se usa useState porque devuelve array de 2 variables, uno para la variable y otro para "set"

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
  GraficoContacto
}
