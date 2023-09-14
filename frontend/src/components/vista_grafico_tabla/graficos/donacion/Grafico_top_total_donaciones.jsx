import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import { Pie } from "react-chartjs-2";
import { Card } from "react-bootstrap";
import "../../../../Styles/Graficos.scss";
import DonacionService from '../../../../services/DonacionService';

//Este js fue hecho con ayuda de:
// https://codesandbox.io/s/daudsmith-react-chart-j5yls6?file=/src/components/leftNav/leftNav.js
// y adaptado a las necesidades del negocio


const PieChart = ({ data, el_la, nombreDatoSingular, las_los, nombreDatoPlural }) => {
  const labels = data.map(item => getNombreDonanteDeItem(item));
  const cantidades = data.map(item => item.cantidad);
  const totales = data.map(item => item.total);

  const pieData = {
    labels: labels,
    datasets: [
      {
        data: totales,
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
    ],
  };
  
  

  return (
    <div>
      <Card className="graficos-card">
        <h5>Total donado por persona</h5>
        <Pie data={pieData}
        options={{
          plugins: {
            tooltip: {
              callbacks: {
                label: function(context) {
                  var label = context.label || '';
        
                  if (label) {
                    label += ': ';
                  }
                  if (context.parsed !== null) {
                    label += ' $' +context.parsed.toLocaleString();
                  }
                  return label;
                }
              }
            },
            legend: {
              labels: {
                color: "white" // Cambiar el color del texto de filtrado a blanco
              }
            },
          }
        }}/>
      </Card>
    </div>
  );
}

const getNombreDonanteDeItem = (item) => {
  const { nombre, apellido, nombreDescripcion, cuit } = item || {};
  let nombreCompleto = '';//Vacío (solo mostrar "desea borar id: 1" en vez de nombre)
  if (nombre && apellido) {//Beneficiarios, Empleados, etc
    nombreCompleto = `${nombre} ${apellido}`;
  } else if (nombre) {//Una persona sin apellido
    nombreCompleto = nombre;
  } else if (apellido) {//Una persona sin nombre
    nombreCompleto = apellido;
  } else if (nombreDescripcion) {//Contacto
    nombreCompleto = nombreDescripcion;
  } else if (cuit) {//Facturas, insumos, etc
    nombreCompleto = cuit;
  }
  return nombreCompleto;
}

const VerticalBarChart = ({ data, el_la, nombreDatoSingular, las_los, nombreDatoPlural }) => {
  const labels = data.map(item => getNombreDonanteDeItem(item));
  const totales = data.map(item => item.total);

  const chartData = {
    labels: labels,
    datasets: [
      {
        label: `Top donado por ${las_los} ${nombreDatoPlural}`,
        data: totales,
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
            texto = `Total donado`;
            texto += `: $${context.parsed.y.toLocaleString()}`;
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
        text: `Top donadores`,
        color: 'white',
      },
    },
    scales: {
      y: {
        ticks: {
          color: 'white',
          callback: function(value, index, ticks) {
              return `$${value.toLocaleString()}`;
          },
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
const Top5donadores = ({data, las_los, nombreDatoPlural}) => {
  const top5Data = data
    .slice()
    .sort((a, b) => b.total - a.total)
    .slice(0, 5)
    .map(item => ({ donador: getNombreDonanteDeItem(item), cantidad: item.cantidad, total: item.total }));
  
  return (
    <div>
    <Card className="graficos-card">
      <h4>Ordenado top 5 donadores</h4>
      <div className="table-responsive">
        <table className="table">
          <thead>
            <tr>
              <th scope="col" style={{ color: 'white' }}>Donador</th>
              <th scope="col" style={{ color: 'white' }}>Cantidad de {nombreDatoPlural}</th>
              <th scope="col" style={{ color: 'white' }}>Total</th>
            </tr>
          </thead>
          <tbody>
            {top5Data.map((item, index) => (
              <tr key={index}>
                <td>{item.donador}</td>
                <td>{item.cantidad}</td>
                <td>{"$" + item.total.toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      </Card>
    </div>
  );
}

const GraficoTopDonaciones = ({showContent, useDataExample, dataExampleInput, el_la, nombreDatoSingular, las_los, nombreDatoPlural}) => {
  const [data, setData] = useState();
  const [dataBackend, setDataBackend] = useState();
  const [dataExample, setDataExample] = useState();
  const dataExampleAux = [
    { id: 72, nombre: "Julieta", apellido: "Maneiro", cantidad: 7, total: 285000},
    { id: 142, nombre: "Gabriel", apellido: "Saravia", cantidad: 7, total: 225000},
    { id: 39, nombre: "Sabrina", apellido: "Jofre", cantidad: 7, total: 175000},
    { id: 215, nombre: "Analía", apellido: "Bracco", cantidad: 7, total: 160000},
    { id: 45, nombre: "Ignacio", apellido: "Padín", cantidad: 7, total: 155000},
    { id: 89, nombre: "Federica", apellido: "Perez", cantidad: 7, total: 90000},
  ];

  useEffect(() => {
    DonacionService.top_20_donantes_total().then((res) => {
      setDataBackend(res.data);
    });
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
        <p>Cargando datos de top dotal de donaciones</p>
      </div>
    );
  }
  
  return (
      <div>
        {(showContent && data) && (
          <div>
            <Card className="graficos-card-father">
              <h2>Top total de {las_los} {nombreDatoPlural}</h2>
              <div className="row">
                <div className="col-12 col-md-6 col-lg-4">
                  <PieChart data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                </div>
                <div className="col-12 col-md-6 col-lg-8">
                    <div>
                      <VerticalBarChart data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                    </div>
                </div>
                <div className="col-12 col-md-6 col-lg-5">
                  <Top5donadores data={data ? data : []} las_los={las_los} nombreDatoPlural={nombreDatoPlural} />
                </div>
              </div>
            </Card>
          </div>
        )}
      </div>

  );
}

export {
  GraficoTopDonaciones
}
