import React, { forwardRef, useEffect, useState, useRef } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
import { columnsPersona } from "./TablaPersona";
import { columnsBeneficiario } from "./TablaBeneficiario";
import { columnsProfesionalParaVistaComoIntegrante } from "./TablaProfesional";
import { RenderMostrarContacto } from "./Tabla_Variables";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { MdPeopleAlt } from "react-icons/md";

const RenderAgregarItems = ({itemsActuales, columnsIn, tipoDatoParaFoto, el_la, nombreTipoIntegrante, nombreTipoIntegrantePrural}) => {
  const [showModal, setShowModal] = useState(false);
  const [itemsActualesAux, setIntegrantesActualesAux] = useState();
  useEffect(() => {
    setIntegrantesActualesAux(itemsActuales);
  }, []);

  const [numeroDeIntegrantes, setNumeroDeIntegrantes] = useState(null);
  useEffect(() => {
    if (Array.isArray(itemsActualesAux)) {//Es lista
      setNumeroDeIntegrantes(itemsActualesAux.length);
      console.log("itemsActuales.length: " + itemsActualesAux.length);
      itemsActualesAux.forEach((itemsActualesAux) => {
        console.log("Cada uno:");
        console.log(itemsActualesAux)
      });
    } else if (itemsActualesAux) {//Es 1 objeto
      setNumeroDeIntegrantes(1);
      console.log("Es objeto");
      console.log(itemsActualesAux);
    } else {//Esta vacío
      setNumeroDeIntegrantes(0);
      console.log("Es null o es objeto");
      console.log(itemsActualesAux);
    }
  }, [itemsActualesAux])

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };
  
  return(
    <div>
      {numeroDeIntegrantes === 0 && (<label> {"No hay " + nombreTipoIntegrante + " asociad"+ (el_la === "el" ? "o" : "a")}</label>)}
      {/* {numeroDeIntegrantes === 1 && RenderMostrarContacto(itemsActualesAux[0], "contacto")} */}
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
              dataIn={itemsActuales ? itemsActuales : []}
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
  RenderAgregarItemsGenericRow,
}
