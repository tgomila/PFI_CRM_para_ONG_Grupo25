import React, { useState, useEffect } from "react";
import TenantService from "../../services/TenantService";
import { FaWhatsapp, FaTelegram } from 'react-icons/fa';
import { Card } from "react-bootstrap";
import "../../Styles/Graficos.scss";

const AcercaDe = () => {

  return (
    <div className="ComponentePrincipalGraficos">
      <div className="Marketplace">
        <h1><span className="underlined underline-clip-title">Acerca de nosotros</span></h1>
        <br/><br/>
        <Card className="graficos-card-father">
          <h2>Créditos</h2>
          Agradezco a mis padres por invertir en mi carrera, mi tutor y mis profesores de la universidad.
        </Card>
      </div>
    </div>
  );
}

export default AcercaDe;