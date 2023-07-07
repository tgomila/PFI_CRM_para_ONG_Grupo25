import React, { useState, useEffect } from 'react';
import Chart from 'chart.js/auto';
import { Bar } from 'react-chartjs-2';
import modulosService from "../../../services/modulosService";
import ContactoService from "../../../services/ContactoService";

const monthNames = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

const VerticalBarChart = ({ data }) => {
  const labels = data.map(item => `${monthNames[item.month - 1]} ${item.year}`);
  const counts = data.map(item => item.count);

  const chartData = {
    labels: labels,
    datasets: [
      {
        label: 'Contactos nuevos',
        data: counts,
        backgroundColor: 'blue',
      },
    ],
  };

  const chartOptions = {
    plugins: {
      tooltip: {
        callbacks: {
          label: context => `Nuevos contactos: ${context.parsed.y}`,
        },
      },
      legend: {
        labels: {
          color: 'white',
        },
      },
      title: {
        display: true,
        text: 'Fecha de creación de contactos',
        color: 'white',
      },
    },
    scales: {
      y: {
        ticks: {
          color: 'white',
        },
        grid: {
          color: '#325d8c',
        },
      },
      x: {
        ticks: {
          color: 'white',
        },
        grid: {
          color: '#325d8c',
        },
      },
    },
  };

  return <Bar data={chartData} options={chartOptions} />;
};

/**
 * @description
 * Componente para mostrar los 5 mejores meses de contacto.
 * 
 * Este componente recibe un arreglo de datos de contacto y muestra una tabla con los
 * meses con mayor cantidad de contactos. 
 * 
 * @param {Array} data - Datos de contacto.
 * @param {string} nombrePlural - Texto recomendado 'contactos'.
 * @returns {JSX.Element} Componente de React que muestra la tabla de los 5 mejores meses.
 */
const Top5meses = ({data}, nombrePrural) => {
  const top5Data = data
    .slice()
    .sort((a, b) => b.count - a.count)
    .slice(0, 5)
    .map(item => ({ month: item.month, year: item.year, count: item.count }));
  
  return (
    <div>
      <h4>Top 5 meses</h4>
      <div className="table-responsive">
        <table className="table">
          <thead>
            <tr>
              <th scope="col" style={{ color: 'white' }}>Mes</th>
              <th scope="col" style={{ color: 'white' }}>Año</th>
              <th scope="col" style={{ color: 'white' }}>Contactos nuevos</th>
            </tr>
          </thead>
          <tbody>
            {top5Data.map((item, index) => (
              <tr key={index}>
                <td>{item.month}</td>
                <td>{item.year}</td>
                <td>{item.count}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

const GraficoContacto = (visibilidadInput) => {

  const [data, setData] = useState();
  const [dataBackend, setDataBackend] = useState();
  const [useDataExample, setUseDataExample] = useState(true);
  const [visibilidad, setVisibilidad] = useState("");

  useEffect(() => {
    if(visibilidadInput === 'EDITAR' || visibilidadInput === 'SOLO_VISTA'){
      setVisibilidad(visibilidadInput)
    }
    if(!visibilidad){
      let modulo = modulosService.getVisibilidadByModulo("CONTACTO");
      modulo.then(async (response) => {
        if(response){
          setVisibilidad(response);
        }
      });
    }

    ContactoService.contactosCreadosUltimos12meses().then((res) => {
      setDataBackend(res.data);
      setUseDataExample(false);
    });
  }, []);

  useEffect(() => {
    if(useDataExample){
      setData(dataExample);
    }
    else{
      setData(dataBackend);
    }
  }, [useDataExample]);

  const dataExample = [
    { month: 1, year: 2022, count: 50 },
    { month: 2, year: 2022, count: 30 },
    { month: 3, year: 2022, count: 15 },
    { month: 4, year: 2022, count: 5 },
    { month: 5, year: 2022, count: 6 },
    { month: 6, year: 2022, count: 15 },
    { month: 7, year: 2022, count: 30 },
    { month: 8, year: 2022, count: 16 },
    { month: 9, year: 2022, count: 7 },
    { month: 10, year: 2022, count: 4 },
    { month: 11, year: 2022, count: 17 },
    { month: 12, year: 2022, count: 43 },
    { month: 1, year: 2023, count: 65 },
    { month: 2, year: 2023, count: 31 },
    { month: 3, year: 2023, count: 15 },
    { month: 4, year: 2023, count: 4 },
    { month: 5, year: 2023, count: 7 },
    { month: 6, year: 2023, count: 14 },
  ];

  const handleToggleData = () => {
    setUseDataExample(prevState => !prevState);
  };
  
  const [showContent, setShowContent] = useState(true);

  const handleToggleContent = () => {
    setShowContent(!showContent);
  };

  return (
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
        <h2>Gráficos</h2>
        <div className="row">
          <div className="col-md-8">
              <h4>Nuevos contactos por mes/año</h4>
            <div style={{ height: '400px', width: '100%' }}>
              <VerticalBarChart data={data ? data : []} />
            </div>
          </div>
          <div className="col-md-4">
            <Top5meses data={data ? data : []} />
          </div>
        </div>
        </div>
      )}
    </div>

  );
};

export default GraficoContacto;
