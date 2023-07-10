import React, { useState, useEffect } from 'react';
import Chart from 'chart.js/auto';
import { Bar } from 'react-chartjs-2';

const monthNames = [
  "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

const VerticalBarChart = ({ data, el_la, nombreDatoSingular, las_los, nombreDatoPlural }) => {
  const labels = data.map(item => `${monthNames[item.month - 1]} ${item.year}`);
  const counts = data.map(item => item.count);

  const chartData = {
    labels: labels,
    datasets: [
      {
        label: `${nombreDatoPlural} nuev${las_los === 'las' ? 'a' : 'o'}s`,
        data: counts,
        backgroundColor: 'blue',
      },
    ],
  };

  const chartOptions = {
    plugins: {
      tooltip: {
        callbacks: {
          label: context => {
            let texto = '';
            if (las_los && nombreDatoPlural) {
              texto = `Nuev${las_los === 'las' ? 'a' : 'o'}s ${nombreDatoPlural}`;
            } else {
              texto = `Nuevos agregados`;
            }
            texto += `: ${context.parsed.y}`;
            return texto;
          },
        },
      },
      legend: {
        labels: {
          color: 'white',
        },
      },
      title: {
        display: true,
        text: `Fecha de creación de ${nombreDatoPlural}`,
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
 * Componente para mostrar los 5 mejores meses del dato.
 * 
 * Este componente recibe un arreglo de un tipo de dato y muestra una tabla con los
 * meses con mayor cantidad de datos dados de alta. 
 * 
 * @param {Array} data - Datos del dato.
 * @param {string} nombrePlural - Texto recomendado 'contactos'.
 * @returns {JSX.Element} Componente de React que muestra la tabla de los 5 mejores meses.
 */
const Top5meses = ({data}, las_los, nombreDatoPlural) => {
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
              <th scope="col" style={{ color: 'white' }}>{nombreDatoPlural} nuev{las_los === 'las' ? 'a' : 'o'}s</th>
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

const GraficoCreadosUltimoAño = ({showContent, useDataExample, Service, el_la, nombreDatoSingular, las_los, nombreDatoPlural}) => {
  const [data, setData] = useState();
  const [dataBackend, setDataBackend] = useState();

  useEffect(() => {
    if(Service){
      Service.creadosUltimos12meses().then((res) => {
        setDataBackend(res.data);
      });
    }
  }, []);

  useEffect(() => {
    if(useDataExample){
      setData(dataExample);
    }
    else{
      setData(dataBackend);
    }
  }, [useDataExample, dataBackend]);

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

  if(!data) {
    return(
      <div>
        <span className="spinner-border spinner-border-sm"></span>
        <p>Cargando datos para gráfico de {nombreDatoPlural} creados en el último año</p>
      </div>
    );
  }
  
  return (
      <div>
        {(showContent && data) && (
          <div>
            <h2>Gráficos de {nombreDatoPlural} cread{las_los === 'las' ? 'a' : 'o'}s en el último año</h2>
            <div className="row">
              <div className="col-md-8">
                  {(las_los && nombreDatoPlural) ? (
                    <h4>Nuev{las_los === 'las' ? 'a' : 'o'}s {nombreDatoPlural} por mes/año</h4>
                  ) : (
                  <h4>Nuevos agregados por mes/año</h4>
                  )}
                <div style={{ height: '400px', width: '100%' }}>
                  <VerticalBarChart data={data ? data : []} />
                </div>
              </div>
              <div className="col-md-4">
                <Top5meses data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
              </div>
            </div>
          </div>
        )}
      </div>

  );
}

export {
  GraficoCreadosUltimoAño
}
