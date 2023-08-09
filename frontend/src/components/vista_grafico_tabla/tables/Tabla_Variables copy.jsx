import React, { forwardRef, useEffect, useState, useRef } from "react";
//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
import ImageService from "../../../services/ImageService";
import { useNavigate } from 'react-router-dom';
import { FaTrashAlt, FaRegEye, FaRegEdit } from "react-icons/fa";
import CreateReadUpdateGenericoConFoto from "../../CRUD/Constants/CreateReadUpdate_Generico";
import { cargarContactoDefault, cargarPersonaDefault } from "../../CRUD/Constants/ConstantsCargarDefault";
import { ContactoRead, PersonaRead } from "../../CRUD/Constants/ConstantsReadModel";
import ContactoService from "../../../services/ContactoService";
import PersonaService from "../../../services/PersonaService";
import { MdOutlinePersonAdd, MdOutlinePersonOff } from "react-icons/md";

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

//Mismo de arriba pero sin el style cb action
/*const IndeterminateSelectCheckbox = forwardRef(({ indeterminate, isSelected, ...rest }, ref) => {
  const [checkboxState, setCheckboxState] = useState(isSelected);
  const defaultRef = useRef();
  const resolvedRef = ref || defaultRef;

  useEffect(() => {
    resolvedRef.current.indeterminate = indeterminate;
  }, [resolvedRef, indeterminate]);

  return (
    <>
      <div className="cb-integrantes action">
      <label>
        <input type="checkbox" ref={resolvedRef} {...rest} />
        <span>{!isSelected ? 'Quitado' : 'Agregado'}</span>
      </label>
    </div>
    </>
  );
});*/
const IndeterminateSelectCheckbox = forwardRef(({ indeterminate, isSelected, ...rest }, ref) => {
  const defaultRef = useRef();
  const resolvedRef = ref || defaultRef;

  useEffect(() => {
    resolvedRef.current.indeterminate = indeterminate;
  }, [resolvedRef, indeterminate]);

  const useHover = () => {
    const [hovering, setHovering] = useState(false);
    const onHoverProps = {
      onMouseEnter: () => setHovering(true),
      onMouseLeave: () => setHovering(false),
    }
    return [hovering, onHoverProps]
  }
  const [buttonIsHovering, buttonHoverProps] = useHover();

  return (
    <>
      <div className="cb-integrantes action">
        <label {...buttonHoverProps}>
          <input type="checkbox" ref={resolvedRef} {...rest} />
          <span>
            {isSelected ? (
              <>
                {!buttonIsHovering ? <MdOutlinePersonAdd /> : <MdOutlinePersonOff/>} {!buttonIsHovering ? "Agregado" : "Quitar"}
              </>
            ) : (
              <>
                {!buttonIsHovering ? <MdOutlinePersonOff /> : <MdOutlinePersonAdd/>} {!buttonIsHovering ? "Quitado" : "Agregar"}
              </>
            )}
          </span>
        </label>
      </div>
    </>
  );
});


/**
 * 
 * @param {*} row pasale la celda completa
 * @param {*} tipo ejemplo "contacto"
 * @returns 
 */
const RenderFotoPerfilRow = (row, tipoDatoParaFoto) => {
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
  return RenderFotoPerfil(row.original.id, tipoDatoParaFoto, row.original.imagen_tabla, nombreCompleto);
};

const RenderFotoPerfil = (id, tipo, imagen, nombrePerfil) => {
  const [showModal, setShowModal] = useState(false);
  const [loadedImage, setLoadedImage] = useState(null);

  useEffect(() => {
    if (showModal) {
      ImageService.getFoto(id, tipo, 'completa')
        .then(response => {
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
            <Modal.Header>
              <Modal.Title>{nombrePerfil}</Modal.Title>
              <button className="close" onClick={handleCloseModal}>
                <span aria-hidden="true">&times;</span>
              </button>
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
const RenderBotonVer = (idInput, nombreAVer) => {
  const navigate = useNavigate();
  if(!idInput)
    return(<div/>);//En caso de que se agrupe la tabla, no mostrar
  return (
    <OverlayTrigger
      placement="top"
      overlay={
        <Tooltip id="tooltip-top">
          Ver {nombreAVer && 'a ' + nombreAVer + ', '}ID: {idInput}
        </Tooltip>
      }
    >
      <Button
        className="buttonAnimadoVerde"
        onClick={() => navigate( window.location.pathname + "/read", {state:{id:idInput}})}
      >
        {" "}
        <FaRegEye/>{/**Ver*/}
      </Button>
    </OverlayTrigger>
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
          <Modal.Header>
            {!deleteMessage ? (
              <Modal.Title>Deseas borrar al ID {idInput ? idInput : ""}?</Modal.Title>
            ) : (
              <Modal.Title>¡Borrado exitoso!</Modal.Title>
            )}
            <button className="close" onClick={handleCloseModal}>
              <span aria-hidden="true">&times;</span>
            </button>
            
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

const RenderFotoIntegranteRow = (row, datoContactoIntegrante, tipoIntegrante) => {
  if(!row.original)
    return(<div></div>);//Esto sucede si se agrupa la tabla
  /*const { nombre, apellido, nombreDescripcion } = row.original || {};
  let nombreCompleto = '';
  if (nombre && apellido) {
    nombreCompleto = `${nombre} ${apellido}`;
  } else if (nombre) {
    nombreCompleto = nombre;
  } else if (apellido) {
    nombreCompleto = apellido;
  } else if (nombreDescripcion) {
    nombreCompleto = nombreDescripcion;
  }*/
  return RenderMostrarContacto(datoContactoIntegrante, "contacto");
};

const getNombreDelDato = (dato) => {
  const { nombre, apellido, nombreDescripcion } = dato || {};
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
  return nombreCompleto;
}

const RenderMostrarContacto = (datoContactoIntegrante, tipoImagen) => {
  const [showModal, setShowModal] = useState(false);
  const [loadedImage, setLoadedImage] = useState(null);
  const [persona, setPersona] = useState(null);
  const [loadingSearch, setLoadingSearch] = useState(true);
  const [nombreModal, setNombreModal] = useState("");
  useEffect(() => {
    console.log("datoContactoIntegrante");
    console.log(datoContactoIntegrante);
    console.log("tipoImagen");
    console.log(tipoImagen);
    
    if(datoContactoIntegrante?.id && !datoContactoIntegrante?.imagen_tabla){
      {console.log("Hola12")}
      ImageService.getFoto(datoContactoIntegrante.id, tipoImagen, 'tabla')
        .then(response => {
          {console.log("response")};
          {console.log(response)};
          setLoadedImage(response); // Actualiza el estado con la imagen devuelta por el servicio
          {console.log("Hola23")}
        })
        .catch(error => {
          console.error('Error al obtener la imagen:', error);
        }
      );
    }
  }, [showModal, datoContactoIntegrante?.id, tipoImagen]);

  useEffect(() => {
    setNombreModal(getNombreDelDato(datoContactoIntegrante));
    if(showModal){
      setPersona(null);
      setLoadingSearch(true);
      PersonaService.getById(datoContactoIntegrante.id).then
        (response => {
          setPersona(response.data);
          setNombreModal(getNombreDelDato(response.data));
          setLoadingSearch(false);
        },
        (error) => {
          setLoadingSearch(false);
        }
      );
    }
  }, [showModal]);

  useEffect(() => {
    setNombreModal(getNombreDelDato(datoContactoIntegrante));
  }, [datoContactoIntegrante]);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };
  
  return(
    <div>
      {console.log("datoContactoIntegrante")}
      {console.log(datoContactoIntegrante)}
      {datoContactoIntegrante?.id && (
        <>
        {console.log("loadedImage")}
        {console.log(loadedImage)}
        {loadedImage ? (
          <div>
            {console.log("Hay foto")}
            {nombreModal ? (
              <div>
                <OverlayTrigger
                  placement="top"
                  overlay={
                    <Tooltip id="tooltip-top">
                      {nombreModal}
                    </Tooltip>
                  }
                >
                  <Image 
                  src={loadedImage} 
                  alt="Foto de perfil" 
                  className="contacto-img-card"
                  onClick={handleOpenModal}
                  />
                </OverlayTrigger>
              </div>
            ) : (
              <img 
              src={loadedImage} 
              alt="Foto del integrante" 
              className="contacto-img-card"
              onClick={handleOpenModal}
              />

            )}
          
            <Modal show={showModal} onHide={handleCloseModal}>
              <Modal.Header>
                <Modal.Title>{nombreModal}</Modal.Title>
                <button className="close" onClick={handleCloseModal}>
                  <span aria-hidden="true">&times;</span>
                </button>
              </Modal.Header>
              <Modal.Body>
                <div>
                  {loadingSearch ? (
                    <div>
                      <span className="spinner-border spinner-border-sm"></span>
                      <p>Cargando datos de contacto o persona...</p>
                    </div>
                  ) : (
                    <div>
                      <CreateReadUpdateGenericoConFoto
                        cargarDatosDefault = {persona ? cargarPersonaDefault : cargarContactoDefault}
                        DatoUpdateInput = {persona ? PersonaRead : ContactoRead}
                        tipoDatoForImageService = {'contacto'}
                        dataIn = {persona ? persona : datoContactoIntegrante}
                        Service = {persona ? PersonaService : ContactoService}
                        urlTablaDato = {persona ? '/personafisica' : '/contacto'}
                        isVentanaEmergente = {true}
                        el_la = {persona ? 'la' : 'el'}
                        nombreTipoDato = {persona ? 'persona' : 'contacto'}
                        typeCRUD={'READ'}
                      />
                    </div>
                  )}
                </div>
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
          {console.log("No hay loadedImage")}
          <span className="spinner-border spinner-border-sm"></span>
          <p>Cargando foto<br />de integrante...</p>
          {console.log("No hay loadedImage")}
          </div>
        )}
        </>
      )}
    </div>

  );
};



export {
  IndeterminateCheckbox,
  IndeterminateSelectCheckbox,
  RenderFotoPerfilRow,
  RenderFotoPerfil,
  RenderFotoIntegranteRow,
  RenderMostrarContacto,
  RenderBotonVer,
  RenderBotonEditar,
  RenderBotonBorrar,
}
