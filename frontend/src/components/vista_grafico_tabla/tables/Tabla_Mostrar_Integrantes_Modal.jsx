import React, { forwardRef, useEffect, useState, useRef } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
import { columnsPersona } from "./TablaPersona";
import { columnsBeneficiario } from "./TablaBeneficiario";
import { columnsProfesionalParaVistaComoIntegrante } from "./TablaProfesional";
import { columnsActividad } from "./TablaActividad";
import { columnsFacturaItems } from "./TablaFactura";
import { RenderMostrarContacto } from "./Tabla_Variables";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { MdPeopleAlt } from "react-icons/md";

// const RenderMostrarIntegrantesPersonasRow = (row, integrantesActuales, el_la, nombreIntegranteSingular, nombreIntegrantePlural) => {
//   return RenderMostrarIntegrantesGenericRow(row, integrantesActuales, columnsPersona, "contacto", el_la, nombreIntegranteSingular, nombreIntegrantePlural);
// }

//Si copias y pegas este método, no olvides de poner llaves, yo lo olvidé en factura y integrantesActuales se pasaba integrantesActuales, el_la, etc todo junto como un row o array de objetos y arrays
const RenderMostrarIntegrantesPersonasRow = ({integrantesActuales, el_la="la", nombreIntegranteSingular="persona", nombreIntegrantePlural="personas"}) => {
  return RenderMostrarIntegrantesGenericRow({
    integrantesActuales,
    columnsIn: columnsPersona,
    tipoDatoParaFoto: "contacto",
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
  });
}

const RenderMostrarIntegrantesBeneficiarioRow = ({integrantesActuales, el_la="el", nombreIntegranteSingular="profesional", nombreIntegrantePlural="profesionales"}) => {
  return RenderMostrarIntegrantesGenericRow({
    integrantesActuales,
    columnsIn: columnsBeneficiario,
    tipoDatoParaFoto: "contacto",
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
  });
}

const RenderMostrarIntegrantesProfesionalRow = ({integrantesActuales, el_la="el", nombreIntegranteSingular="profesional", nombreIntegrantePlural="profesionales"}) => {
  return RenderMostrarIntegrantesGenericRow({
    integrantesActuales,
    columnsIn: columnsProfesionalParaVistaComoIntegrante,
    tipoDatoParaFoto: "contacto",
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
  });
}

const RenderMostrarActividadesRow = ({integrantesActuales, el_la="la", nombreIntegranteSingular="actividad", nombreIntegrantePlural="actividades"}) => {
  return RenderMostrarIntegrantesGenericRow({
    integrantesActuales,
    columnsIn: columnsActividad(),
    tipoDatoParaFoto: "actividad",
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
  });
}

const RenderMostrarItemFacturaRow = ({integrantesActuales, el_la="el", nombreIntegranteSingular="item", nombreIntegrantePlural="items"}) => {
  console.log("integrantesActuales row");
  console.log(integrantesActuales);
  return RenderMostrarIntegrantesGenericRow({
    integrantesActuales,
    columnsIn: columnsFacturaItems,
    tipoDatoParaFoto: null,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
  });
}

// const RenderMostrarIntegrantesGenericRow = ({integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreIntegranteSingular, nombreIntegrantePlural}) => {
//   if(!integrantesActuales)
//     return(<div/>);//Esto sucede si se agrupa la tabla
//   return RenderMostrarIntegrantesGeneric ({integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreIntegranteSingular, nombreIntegrantePlural});
// }

const RenderMostrarIntegrantesGenericRow = ({integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreIntegranteSingular, nombreIntegrantePlural}) => {
  const [showModal, setShowModal] = useState(false);
  const [integrantesActualesAux, setIntegrantesActualesAux] = useState(null);
  useEffect(() => {
    setIntegrantesActualesAux(integrantesActuales);
  }, []);

  const [numeroDeIntegrantes, setNumeroDeIntegrantes] = useState(null);
  useEffect(() => {
    if (Array.isArray(integrantesActualesAux)) {//Es lista
      setNumeroDeIntegrantes(integrantesActualesAux.length);
      console.log("integrantesActuales.length: " + integrantesActualesAux.length);
      integrantesActualesAux.forEach((integrantesActualesAux) => {
        console.log("Cada uno:");
        console.log(integrantesActualesAux)
      });
    } else if (integrantesActualesAux) {//Es 1 objeto
      setNumeroDeIntegrantes(1);
      console.log("Es objeto");
      console.log(integrantesActualesAux);
    } else {//Esta vacío
      setNumeroDeIntegrantes(0);
      console.log("Es null o es objeto");
      console.log(integrantesActualesAux);
    }
  }, [integrantesActualesAux]);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };
  
  if(!integrantesActuales){
    return(<div/>);
  }
  return(
    <div>
      {numeroDeIntegrantes === 0 && (<label> {"No hay " + nombreIntegranteSingular + " asociad"+ (el_la === "el" ? "o" : "a")}</label>)}
      {/*numeroDeIntegrantes === 1 && RenderMostrarContacto(integrantesActualesAux[0], tipoDatoParaFoto)*/}
      {numeroDeIntegrantes  >= 1 && (
        <div>
          <OverlayTrigger
            placement="top"
            overlay={
              <Tooltip id="tooltip-top">
                Ver {nombreIntegrantePlural}
              </Tooltip>
            }
          >
          <Button
            className="buttonAnimadoAzul"
            onClick={() => handleOpenModal()}
          >
            <span style={{ display: 'flex', alignItems: 'center' }}>
              {tipoDatoParaFoto === 'contacto' && <MdPeopleAlt style={{ marginRight: '5px', fontSize: '20px' }}/>}Ver {nombreIntegrantePlural}
            </span>
          </Button>
          </OverlayTrigger>
        </div>
      )}

      <Modal show={showModal} onHide={handleCloseModal} size="xl" className="modal-custom">
        <Modal.Header className="modal-header-custom">
          <Modal.Title>{nombreIntegrantePlural.charAt(0).toUpperCase() + nombreIntegrantePlural.slice(1) + " asociad"+ (el_la === "el" ? "o" : "a") + "s"}</Modal.Title>
          <button className="close close-button-custom" onClick={handleCloseModal}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body className="modal-body-custom">
          <div style={{ overflowY: 'auto' }}>
            <TablaGenericaConFoto
              columnsIn={columnsIn}
              //Service={ServiceDeIntegrantes}
              dataIn={integrantesActuales ? integrantesActuales : []}
              visibilidadInput={"SIN_BOTONES"}
              //nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
              tipoDatoParaFoto={tipoDatoParaFoto}
              el_la={el_la}
              nombreTipoDato={nombreIntegranteSingular}
              style={{ overflowY: 'auto' }}
            />
          </div>
        </Modal.Body>
        <Modal.Footer className="modal-footer-custom">
          <Button variant="secondary" onClick={handleCloseModal}>
            Cerrar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>

  );
};



export {
  RenderMostrarIntegrantesGenericRow,
  RenderMostrarIntegrantesPersonasRow,
  RenderMostrarIntegrantesBeneficiarioRow,
  RenderMostrarIntegrantesProfesionalRow,

  RenderMostrarActividadesRow,
  RenderMostrarItemFacturaRow,
}
