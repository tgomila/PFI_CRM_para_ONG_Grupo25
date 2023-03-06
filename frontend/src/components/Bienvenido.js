import React from 'react'
import modulosService from '../services/modulosService';


import "../Styles/Bienvenido.scss";

function Bienvenido() {
  modulosService.getAll();
  return (
    <div className='Bienvenido'>
        <h1>Bienvenido a <strong>Cosmos CRM</strong></h1>
    </div>
  )
}

export default Bienvenido