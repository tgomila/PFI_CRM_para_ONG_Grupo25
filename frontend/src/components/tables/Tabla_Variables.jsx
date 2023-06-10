import React, { forwardRef, useEffect, useState, useRef } from "react";
import { Modal, Button, OverlayTrigger, Tooltip } from "react-bootstrap";
import ImageService from "../../services/ImageService";

const IndeterminateCheckbox = forwardRef(({ indeterminate, ...rest }, ref) => {
  const defaultRef = useRef();
  const resolvedRef = ref || defaultRef;

  useEffect(() => {
    resolvedRef.current.indeterminate = indeterminate;
  }, [resolvedRef, indeterminate]);

  return (
    <div className="cb action">
      <label>
        <input type="checkbox" ref={resolvedRef} {...rest} />
        <span>All</span>
      </label>
    </div>
  );
});

const RenderFotoPerfil = (id, tipo, imagen, nombrePerfil) => {
  const [showModal, setShowModal] = useState(false);
  const [loadedImage, setLoadedImage] = useState(null);

  useEffect(() => {
    if (showModal) {
      ImageService.getFoto(id, tipo, 'completa')
        .then(response => {
          console.log('foto completa');
          console.log(response);
          setLoadedImage(response); // Actualiza el estado con la imagen devuelta por el servicio
        })
        .catch(error => {
          console.error('Error al obtener la imagen:', error);
        });
    }
  }, [showModal, id, tipo]);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  if(!nombrePerfil){
    nombrePerfil = "";
  }
  return(
    <div>
      {imagen ? (
        <div>
          {nombrePerfil ? (
            <div>
              <OverlayTrigger
                placement="top"
                overlay={
                  <Tooltip id="tooltip-top">
                    {nombrePerfil}
                  </Tooltip>
                }
              >
                <img 
                src={imagen} 
                alt="Foto de perfil" 
                className="contacto-img-card"
                onClick={handleOpenModal}
                />
              </OverlayTrigger>
            </div>
          ) : (
            <img 
            src={imagen} 
            alt="Foto de perfil" 
            className="contacto-img-card"
            onClick={handleOpenModal}
            />

          )}
        
          <Modal show={showModal} onHide={handleCloseModal}>
            <Modal.Header closeButton>
              <Modal.Title>{nombrePerfil}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <a href={loadedImage} target="_blank" rel="noopener noreferrer">
                <img src={loadedImage} alt="Foto de perfil" className="contacto-img-modal" />
              </a>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleCloseModal}>
                Cerrar
              </Button>
            </Modal.Footer>
          </Modal>
          
        </div>
      ) : (
        <div>
        <span className="spinner-border spinner-border-sm"></span>
        <p>Cargando foto<br />de perfil...</p>
        </div>
      )}
    </div>

  );
};

export {
  IndeterminateCheckbox,
  RenderFotoPerfil,
}
//export default IndeterminateCheckbox;
