import React, { useState, useEffect } from "react";
import TenantService from "../../services/TenantService";
import { FaWhatsapp, FaTelegram } from 'react-icons/fa';
import "../../Styles/Graficos.scss";

const ChatVista = () => {
  const [tenant, setTenant] = useState(undefined);
  const [whatsappLink, setWhatsappLink] = useState(undefined);
  useEffect(() => {
    TenantService.getMyTenant().then
      (response => {
        console.log("response")
        console.log(response)
        setTenant(response.data);
        if(response.data.tenantPhoneNumber) {
          let numeroTelefono = response.data.tenantPhoneNumber;
          numeroTelefono = numeroTelefono.replace('+', '');
          setWhatsappLink(`https://wa.me/` + numeroTelefono);
        }
      });
  }, []);

  return (
    <div className="ComponentePrincipalGraficos">
      <div className="Marketplace">
        <h1><span className="underlined underline-clip-title">Chat con ONG</span></h1>
        <br/><br/>
        {whatsappLink && (
          <div>
            <p>Para contactar a la administración por WhatsApp:</p>
            <a
              href={whatsappLink}
              target="_blank"
              rel="noopener noreferrer"
              style={{ fontSize: '2.5em', color: '#25d366' }}
            >
              <FaWhatsapp />
            </a>
            {/* <a
              href={'https://t.me/tomasgomila'}
              target="_blank"
              rel="noopener noreferrer"
              style={{ fontSize: '2em', color: '#0088cc' }}
            >
              <FaTelegram />
            </a> */}
          </div>
        )}
      </div>
    </div>
  );
}

export default ChatVista;