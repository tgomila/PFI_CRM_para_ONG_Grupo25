import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import { Pie } from "react-chartjs-2";
import { Card } from "react-bootstrap";
import "../../../Styles/Graficos.scss";

//Este js fue hecho con ayuda de:
// https://codesandbox.io/s/daudsmith-react-chart-j5yls6?file=/src/components/leftNav/leftNav.js
// y adaptado a las necesidades del negocio


const PieChart = ({ data, el_la, nombreDatoSingular, las_los, nombreDatoPlural }) => {
  const labels = data.map(item => item.rangoEdad);
  const etapas = data.map(item => item.etapa);
  const cantidades = data.map(item => item.cantidad);

  const pieData = {
    labels: labels,
    datasets: [
      {
        data: cantidades,
        backgroundColor: [
          "#FF8C00", // Naranja oscuro
          "#003366", // Azul oscuro
          "#00CED1", // Celeste oscuro
          "#006400", // Verde oscuro
          "#8B008B", // Violeta oscuro
          "#FF1493", // Rosa oscuro
          // Agrega más colores aquí según tus preferencias
        ],
        hoverBackgroundColor: [
          "#FF6600", // Naranja oscuro más oscuro
          "#001A33", // Azul oscuro más oscuro
          "#008B8D", // Celeste oscuro más oscuro
          "#004400", // Verde oscuro más oscuro
          "#800080", // Violeta oscuro más oscuro
          "#FF007F", // Rosa oscuro más oscuro
          // Agrega más colores aquí según tus preferencias
        ]
      }
    ]
  };
  
  

  return (
    <div>
      <Card className="graficos-card">
        <h5>Categoría de edades</h5>
        <Pie data={pieData}
        options={{
          plugins: {
            legend: {
              labels: {
                color: "white" // Cambiar el color del texto de filtrado a blanco
              }
            }
          }
        }}/>
      </Card>
    </div>
  );
}

const VerticalBarChart = ({ data, el_la, nombreDatoSingular, las_los, nombreDatoPlural }) => {
  const labels = data.map(item => item.rangoEdad);
  const etapas = data.map(item => item.etapa);
  const cantidades = data.map(item => item.cantidad);

  const chartData = {
    labels: labels,
    datasets: [
      {
        label: `Categoría de edad de ${las_los} ${nombreDatoPlural}`,
        data: cantidades,
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

  return (
    <div>
    <Card className="graficos-card">
      <Bar data={chartData} options={chartOptions} />
    </Card>
    </div>
    
  );
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
const Top6edades = ({data, las_los, nombreDatoPlural}) => {
  const top5Data = data
    .slice()
    .sort((a, b) => b.cantidad - a.cantidad)
    .slice(0, 6)
    .map(item => ({ rangoEdad: item.rangoEdad, etapa: item.etapa, cantidad: item.cantidad }));
  
  return (
    <div>
    <Card className="graficos-card">
      <h4>Ordenado top 6 edades</h4>
      <div className="table-responsive">
        <table className="table">
          <thead>
            <tr>
              <th scope="col" style={{ color: 'white' }}>Rango de edad</th>
              <th scope="col" style={{ color: 'white' }}>Etapa</th>
              <th scope="col" style={{ color: 'white' }}>Cantidad de {nombreDatoPlural}</th>
            </tr>
          </thead>
          <tbody>
            {top5Data.map((item, index) => (
              <tr key={index}>
                <td>{item.rangoEdad}</td>
                <td>{item.etapa}</td>
                <td>{item.cantidad}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      </Card>
    </div>
  );
}

const GraficoCategoriaEdades = ({showContent, useDataExample, dataExampleInput, Service, el_la, nombreDatoSingular, las_los, nombreDatoPlural}) => {
  const [data, setData] = useState();
  const [dataBackend, setDataBackend] = useState();
  const [dataExample, setDataExample] = useState();
  const dataExampleAux = [
    { rangoEdad: "0 a 5 años", etapa: "Primera Infancia", cantidad: 17 },
    { rangoEdad: "6 a 11 años", etapa: "Infancia", cantidad: 47 },
    { rangoEdad: "12 a 18 años", etapa: "Adolescencia", cantidad: 30 },
    { rangoEdad: "19 a 26 años", etapa: "Juventud", cantidad: 15 },
    { rangoEdad: "27 a 59 años", etapa: "Adultez", cantidad: 4 },
    { rangoEdad: "mayor a 60 años", etapa: "Persona mayor", cantidad: 2 },
  ];

  useEffect(() => {
    if(Service){
      Service.categoriaEdades().then((res) => {
        setDataBackend(res.data);
      });
    }
    if(!dataExampleInput){
      setDataExample(dataExampleAux);
    } else{
      setDataExample(dataExampleInput);
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


  if(!data) {
    return(
      <div>
        <span className="spinner-border spinner-border-sm"></span>
        <p>Cargando datos de categoría de edades de {nombreDatoPlural}</p>
      </div>
    );
  }
  
  return (
      <div>
        {(showContent && data) && (
          <div>
            <Card className="graficos-card-father">
              <h2>Categoría de edades de {las_los} {nombreDatoPlural}</h2>
              <div className="row">
                <div className="col-12 col-md-6 col-lg-4">
                  <PieChart data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                </div>
                <div className="col-12 col-md-6 col-lg-8">
                      {/*(las_los && nombreDatoPlural) ? (
                        <h4>Categoría de edades de {las_los} {nombreDatoPlural}</h4>
                      ) : (
                      <h4>Categoría de edades</h4>
                      )*/}
                    {/*<div style={{ height: '400px', width: '100%' }}>*/}
                    <div>
                      <VerticalBarChart data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                    </div>
                </div>
                <div className="col-12 col-md-6 col-lg-5">
                  <Top6edades data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                </div>
              </div>
            </Card>
          </div>
        )}
      </div>

  );
}

export {
  GraficoCategoriaEdades
}
