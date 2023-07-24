import React, { forwardRef, useEffect, useState, useRef } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
import ImageService from "../../../services/ImageService";
import { useNavigate } from 'react-router-dom';
import { FaTrashAlt, FaRegEdit } from "react-icons/fa";

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

/**
 * 
 * @param {*} row pasale la celda completa
 * @param {*} tipo ejemplo "contacto"
 * @returns 
 */
const RenderFotoPerfilRow = (row, tipo) => {
  if(!row.original || !row.original.imagen_tabla)
    return(<div></div>);//Esto sucede si se agrupa la tabla
  const { nombre, apellido, nombreDescripcion } = row.original || {};
  let nombreCompleto = '';
  if (nombre && apellido) {
    nombreCompleto = `${nombre} ${apellido}`;
  } else if (nombre) {
    nombreCompleto = nombre;
  } else if (apellido) {
    nombreCompleto = apellido;
  } else if (nombreDescripcion) {
    nombreCompleto = nombreDescripcion;
  }
  return RenderFotoPerfil(row.original.id, "contacto", row.original.imagen_tabla, nombreCompleto);
};

const RenderFotoPerfil = (id, tipo, imagen, nombrePerfil) => {
  const [showModal, setShowModal] = useState(false);
  const [loadedImage, setLoadedImage] = useState(null);

  useEffect(() => {
    if(id === 1){
      console.log("Mi nombre perfil:");
      console.log(nombrePerfil);
    }
  }, []);

  useEffect(() => {
    if (showModal) {
      console.log("Se realiza consulta de pantalla completa:");
      console.log("id: " + id);
      console.log("tipo: " + tipo);
      ImageService.getFoto(id, tipo, 'completa')
        .then(response => {
          console.log('foto completa');
          console.log(response);
          setLoadedImage(response); // Actualiza el estado con la imagen devuelta por el servicio
          console.log("Fin consulta")
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
                <Image 
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
              {loadedImage ? (
                <a href={loadedImage} target="_blank" rel="noopener noreferrer">
                  <img src={loadedImage} alt="Foto de perfil" className="contacto-img-modal" />
                </a>
              ) : (
                <div>
                  <span className="spinner-border spinner-border-sm"></span>
                  <p>Cargando foto<br />de perfil...</p>
                </div>
              )}
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

/**
 * 
 * @param {string} idInput si es row declararlo como row.original?.id
 * @returns 
 */
const RenderBotonEditar = (idInput, nombreAEditar) => {
  const navigate = useNavigate();
  if(!idInput)
    return(<div/>);//En caso de que se agrupe la tabla, no mostrar
  return (
    <OverlayTrigger
      placement="top"
      overlay={
        <Tooltip id="tooltip-top">
          Editar {nombreAEditar && 'a ' + nombreAEditar + ', '}ID: {idInput}
        </Tooltip>
      }
    >
      <Button
        className="buttonAnimadoVerde"
        onClick={() => navigate( window.location.pathname + "/update", {state:{id:idInput}})}
      >
        {" "}
        <FaRegEdit/>{/**Editar*/}
      </Button>
    </OverlayTrigger>
  );
};

/**
 * 
 * @param {string} idInput si es un row llamarlo así: row.original?.id
 * @param {string} nombreABorrar si es un row llamarlo así: row.original?.nombreDescripcion
 * @param {*} Service dame un service :v
 * @returns 
 */
const RenderBotonBorrar = (idInput, nombreABorrar, Service) => {
//const RenderBotonBorrar = (row, ContactoService) => {
  const [showModal, setShowModal] = useState(false);
  const [loadingErase, setLoadingErase] = useState(false);
  const [deleteMessage, setDeleteMessage] = useState("");
  const [deleteMessageType, setDeleteMessageType] = useState("");

  if(!idInput || !Service)
    return(<div/>);//En caso de que se agrupe la tabla, no mostrar

  const handleOpenModal = () => {
    setDeleteMessage("");
    setDeleteMessageType("");
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    if(deleteMessage || deleteMessageType) {
      setDeleteMessage("");
      setDeleteMessageType("");
      window.location.reload();
    }
  };

  const eliminarRegistro = (id) => {
    if(id != null){

      setLoadingErase(true);
      Service.delete(id).then(
        () => {
          setLoadingErase(false);
          let mensajeAux = "";
          if(nombreABorrar){
            mensajeAux += nombreABorrar + " con ";
          }
          mensajeAux += "ID " + id + " ha sido eliminado correctamente de la base de datos";
          setDeleteMessage(mensajeAux);
          setDeleteMessageType("success");
          //window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setLoadingErase(false);
          setDeleteMessage("Error al eliminar de tipo: " + resMessage);
          setDeleteMessageType("error");
        });
    } else {
      setLoadingErase(false);
    }
  }
  
  return (
    <div>
      <OverlayTrigger
        placement="top"
        overlay={
          <Tooltip id="tooltip-top">
            ¿Seguro desea <strong>borrar</strong> {nombreABorrar && 'a ' + nombreABorrar + ', '}ID: {idInput} ?
          </Tooltip>
        }
      >
      <Button
        className="buttonAnimadoRojo"
        onClick={() => handleOpenModal()}
      >
        <FaTrashAlt/>{/**Borrar*/}
      </Button>
      </OverlayTrigger>
      {/** Este es el cartel que aparece delante "desea borrar?" */}
      <div>
        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            {!deleteMessage ? (
              <Modal.Title>Deseas borrar al ID {idInput ? idInput : ""}?</Modal.Title>
            ) : (
              <Modal.Title>¡Borrado exitoso!</Modal.Title>
            )}
            
          </Modal.Header>

          <Modal.Body>
            {!deleteMessage ? (
              <div>
                {nombreABorrar ? (
                  <p>Seguro desea borrar a {nombreABorrar} con ID {idInput ? idInput : ""}?</p>
                ) : (
                  <p>Seguro desea borrar a ID {idInput ? idInput : ""}?</p>
                )}
              </div>
            ) : (
              <p>{deleteMessage}</p>
            )}
          </Modal.Body>

          <Modal.Footer>
            {!deleteMessage && (
              <div>
                <Button variant="secondary"
                  onClick={() => handleCloseModal()}
                >CERRAR</Button>
                {' '}
                <Button variant="primary"
                  onClick={() => eliminarRegistro(idInput ? idInput : null)}
                >ELIMINAR {' '}
                {loadingErase && (
                  <span className="spinner-border spinner-border-sm"></span>
                )}
                </Button>
              </div>
            )}
            
            {deleteMessage && (
              <Button
                variant={deleteMessageType === "success" ? "success" : "danger"}
                onClick={() => handleCloseModal()}
              >
                CERRAR
              </Button>
            )}
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};





export {
  IndeterminateCheckbox,
  RenderFotoPerfilRow,
  RenderFotoPerfil,
  RenderBotonEditar,
  RenderBotonBorrar,
}
//export default IndeterminateCheckbox;
