import React, { forwardRef, useEffect, useState, useRef } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
import { columnsPersona } from "./TablaPersona";
import { columnsBeneficiario } from "./TablaBeneficiario";
import { columnsProfesionalParaVistaComoIntegrante } from "./TablaProfesional";
import { RenderMostrarContacto } from "./Tabla_Variables";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { MdPeopleAlt } from "react-icons/md";

const RenderMostrarIntegrantesPersonasRow = (row, integrantesActuales, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural) => {
  return RenderMostrarIntegrantesGenericRow(row, integrantesActuales, columnsPersona, "contacto", el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural);
}

const RenderMostrarIntegrantesBeneficiarioRow = (row, integrantesActuales, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural) => {
  return RenderMostrarIntegrantesGenericRow(row, integrantesActuales, columnsBeneficiario, "contacto", el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural);
}

const RenderMostrarIntegrantesProfesionalRow = (row, integrantesActuales, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural) => {
  return RenderMostrarIntegrantesGenericRow(row, integrantesActuales, columnsProfesionalParaVistaComoIntegrante, "contacto", el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural);
}

const RenderMostrarIntegrantesGenericRow = (row, integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural) => {
  if(!integrantesActuales)
    return(<div/>);//Esto sucede si se agrupa la tabla
  return RenderMostrarIntegrantes(row, integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural);
}

const RenderMostrarIntegrantes = (row, integrantesActuales, columnsIn, tipoDatoParaFoto, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural) => {
  const [showModal, setShowModal] = useState(false);
  const [integrantesActualesAux, setIntegrantesActualesAux] = useState();
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
  }, [integrantesActualesAux])

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };
  
  return(
    <div>
      {numeroDeIntegrantes === 0 && (<label> {"No hay " + nombreTipoIntegrante + " asociad"+ (el_la === "el" ? "o" : "a")}</label>)}
      {/* {numeroDeIntegrantes === 1 && RenderMostrarContacto(integrantesActualesAux[0], "contacto")} */}
      {numeroDeIntegrantes  >= 1 && (
        <div>
          <OverlayTrigger
            placement="top"
            overlay={
              <Tooltip id="tooltip-top">
                Ver {nombreTipoIntegrantePrural}
              </Tooltip>
            }
          >
          <Button
            className="buttonAnimadoAzul"
            onClick={() => handleOpenModal()}
          >
            <span style={{ display: 'flex', alignItems: 'center' }}>
              {tipoDatoParaFoto === 'contacto' && <MdPeopleAlt style={{ marginRight: '5px', fontSize: '20px' }}/>}Ver {nombreTipoIntegrantePrural}
            </span>
          </Button>
          </OverlayTrigger>
        </div>
      )}

      <Modal show={showModal} onHide={handleCloseModal} size="xl">
        <Modal.Header>
          <Modal.Title>{nombreTipoIntegrantePrural.charAt(0).toUpperCase() + nombreTipoIntegrantePrural.slice(1) + " asociad"+ (el_la === "el" ? "o" : "a") + "s"}</Modal.Title>
          <button className="close" onClick={handleCloseModal}>
            <span aria-hidden="true">&times;</span>
          </button>
        </Modal.Header>
        <Modal.Body>
          <div style={{ overflowY: 'auto' }}>
            <TablaGenericaConFoto
              columnsIn={columnsIn}
              //Service={ServiceDeIntegrantes}
              dataIn={integrantesActuales ? integrantesActuales : []}
              visibilidadInput={"SIN_BOTONES"}
              //nombreTipoDatoParaModuloVisibilidad={"CONTACTO"}
              tipoDatoParaFoto={tipoDatoParaFoto}
              el_la={el_la}
              nombreTipoDato={nombreTipoIntegrante}
              style={{ overflowY: 'auto' }}
            />
          </div>
        </Modal.Body>
        <Modal.Footer>
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
}
