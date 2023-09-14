import React, { useState, useEffect } from 'react';
import { GraficoTopDonaciones } from './Grafico_top_total_donaciones';
import DonacionService from '../../../../services/DonacionService';

const GraficoDonacion = ({visibilidadInput}) => {
  
  //Que cambiar si copias y pegas este código:
  const Service = DonacionService;//Tambien cambiar el "Service" importado
  const el_la = "la";
  const nombreDatoSingular = "donacion";
  const las_los = "las";
  const nombreDatoPlural = "donaciones";
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

  const dataExampleTotal = [
    { id: 72, nombre: "Julieta", apellido: "Maneiro", cantidad: 7, total: 285000},
    { id: 142, nombre: "Gabriel", apellido: "Saravia", cantidad: 7, total: 225000},
    { id: 39, nombre: "Sabrina", apellido: "Jofre", cantidad: 7, total: 175000},
    { id: 215, nombre: "Analía", apellido: "Bracco", cantidad: 7, total: 160000},
    { id: 45, nombre: "Ignacio", apellido: "Padín", cantidad: 7, total: 155000},
    { id: 89, nombre: "Federica", apellido: "Perez", cantidad: 7, total: 90000},
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
            <GraficoTopDonaciones
              showContent={showContent}
              useDataExample={useDataExample}
              visibilidadInput={visibilidadInput}
              dataExampleInput={dataExampleTotal}
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
  GraficoDonacion
}
