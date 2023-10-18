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
import {
  RenderMostrarIntegrantesGenericRow,
  RenderMostrarIntegrantesPersonasRow,
  RenderMostrarIntegrantesBeneficiarioRow,
  RenderMostrarIntegrantesProfesionalRow,
  RenderMostrarActividadesRow,
  RenderMostrarItemFacturaRow,
} from "./Tabla_Mostrar_Integrantes_Modal";
import {
  NumberRangeColumnFilter,
} from"./Tabla_Filters";
import {
  DateHourRangeColumnFilter,
  DateRangeColumnFilter,
  dateBetweenFilterFn,
} from "./Tabla_Filters";
import { format } from 'date-fns';
import * as constantsURL from "../../constants/ConstantsURL";
import defaultNoImage from '../../../services/constantsPictures/defaultNoImage.png';

const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;

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
        <span>Todo</span>
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

const QuitarElementoCheckbox = forwardRef(({ indeterminate, isSelected, ...rest }, ref) => {
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

//Solo se utilizará para login tenant select
const RenderFotoPerfilForTablaTenant = (tenantDbName, nombreTenantFoto) => {
  const [urlLogo, setUrlLogo] = useState(undefined);
  const [showModal, setShowModal] = useState(false);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  if(!nombreTenantFoto){
    nombreTenantFoto = "";
  }
  useEffect(() => {
    setUrlLogo(defaultNoImage);//Default
    if(tenantDbName) {
      setUrlLogo(BACKEND_STATIC_BASE_URL + "logo/" + tenantDbName + ".png");
    }
  }, []);

  return(
    <div>
      {tenantDbName ? (
        <div>
          {nombreTenantFoto ? (
            <div>
              <OverlayTrigger
                placement="top"
                overlay={
                  <Tooltip id="tooltip-top">
                    {nombreTenantFoto}
                  </Tooltip>
                }
              >
                <Image 
                src={urlLogo} 
                alt="Foto de perfil" 
                className="contacto-img-card"
                onClick={handleOpenModal}
                />
              </OverlayTrigger>
            </div>
          ) : (
            <img 
            src={urlLogo} 
            alt="Foto de perfil" 
            className="contacto-img-card"
            onClick={handleOpenModal}
            />

          )}
        
          <Modal show={showModal} onHide={handleCloseModal}>
            <Modal.Header>
              <Modal.Title>{nombreTenantFoto}</Modal.Title>
              <button className="close" onClick={handleCloseModal}>
                <span aria-hidden="true">&times;</span>
              </button>
            </Modal.Header>
            <Modal.Body>
              {urlLogo ? (
                <a href={urlLogo} target="_blank" rel="noopener noreferrer">
                  <img src={urlLogo} alt="Foto de perfil" className="contacto-img-modal" />
                </a>
              ) : (
                <div>
                  <span className="spinner-border spinner-border-sm"></span>
                  <p>No hay foto<br />cargada...</p>
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
          {/* <span className="spinner-border spinner-border-sm"></span>
          <p>No hay foto cargada</p> */}
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
              <Modal.Title>¿Deseas borrar al ID {idInput ? idInput : ""}?</Modal.Title>
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
                  <p>¿Seguro desea borrar a {nombreABorrar} con ID {idInput ? idInput : ""}?</p>
                ) : (
                  <p>¿Seguro desea borrar a ID {idInput ? idInput : ""}?</p>
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
  if(!datoContactoIntegrante)
    return(<div/>);//Esto sucede si se agrupa la tabla
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

//Este column funciona muy bien, pero si agrupas no te muestra el integrante
//Si el "nuevo" render no funciona luego de testear, utilizar este render
// function columnIntegranteConFotoColumn_old(title, property, tipoFoto) {
//   return {
//     Header: title,
//     accessor: (row) => {
//       const data = row?.[property];
//       if (data) {
//         const filteredKeys = Object.keys(data).filter(key => {
//           const value = data[key];
//           return !['imagen', 'imagen_completa'].includes(key) && !Array.isArray(value);
//         });
//         const dataString = filteredKeys.map(key => data[key]).join(' ').toLowerCase();
//         console.log("dataString");
//         console.log(dataString);
//         return dataString;
//       }
//       return null;
//     },
//     Cell: ({ row }) => RenderFotoIntegranteRow(row, row.original?.[property], tipoFoto),
//     //filter: 'fuzzyText',
//     type: "string",
//   };
// }

//Este render funciona muy bien, pero si agrupas no te muestra el integrante
//Si el "nuevo" render no funciona luego de testear, utilizar este render
// const RenderMostrarContacto_old = (datoContactoIntegrante, tipoImagen) => {
//   const [showModal, setShowModal] = useState(false);
//   const [loadedImage, setLoadedImage] = useState(null);
//   const [persona, setPersona] = useState(null);
//   const [loadingSearch, setLoadingSearch] = useState(true);
//   const [nombreModal, setNombreModal] = useState("");

//   useEffect(() => {
//     setNombreModal(getNombreDelDato(datoContactoIntegrante));
//     setPersona(null);
//     setLoadingSearch(true);
//     if(datoContactoIntegrante?.id){
//       PersonaService.search(datoContactoIntegrante.id).then
//         (response => {
//           setLoadingSearch(false);
//           console.log("response CCCCCCCCCCC de " + datoContactoIntegrante?.id);
//           console.log(response);
//           if(response.data){
//             setPersona(response.data);
//             setNombreModal(getNombreDelDato(response.data));
//           }
//         },
//         (error) => {
//           setLoadingSearch(false);
//         }
//       );
//     }

//   }, [datoContactoIntegrante]);

//   useEffect(() => {
//     if(datoContactoIntegrante?.id){
//       if(!datoContactoIntegrante?.imagen_tabla){
//         ImageService.getFoto(datoContactoIntegrante.id, tipoImagen, 'tabla')
//           .then(response => {
//             setLoadedImage(response);
//           })
//           .catch(error => {
//             console.error('Error al obtener la imagen:', error);
//           }
//         );
//       }
//       else{
//         setLoadedImage(datoContactoIntegrante.imagen_tabla);
//       }
//     }
//   }, [datoContactoIntegrante?.id, tipoImagen]);

//   /*useEffect(() => {
//     setNombreModal(getNombreDelDato(datoContactoIntegrante));
//     if(showModal){
//       setPersona(null);
//       setLoadingSearch(true);
//       PersonaService.getById(datoContactoIntegrante.id).then
//         (response => {
//           setPersona(response.data);
//           setNombreModal(getNombreDelDato(response.data));
//           setLoadingSearch(false);
//         },
//         (error) => {
//           setLoadingSearch(false);
//         }
//       );
//     }
//   }, [showModal]);*/

//   useEffect(() => {
//     setNombreModal(getNombreDelDato(datoContactoIntegrante));
//   }, [datoContactoIntegrante]);

//   const handleOpenModal = () => {
//     setShowModal(true);
//   };

//   const handleCloseModal = () => {
//     setShowModal(false);
//   };
  
//   return(
//     <div>
//       {datoContactoIntegrante?.id && (
//         <>
//         {loadedImage ? (
//           <div>
//             {nombreModal ? (
//               <div>
//                 <OverlayTrigger
//                   placement="top"
//                   overlay={
//                     <Tooltip id="tooltip-top">
//                       {nombreModal}
//                     </Tooltip>
//                   }
//                 >
//                   <Image 
//                   src={loadedImage} 
//                   alt="Foto de perfil" 
//                   className="contacto-img-card"
//                   onClick={handleOpenModal}
//                   />
//                 </OverlayTrigger>
//               </div>
//             ) : (
//               <img 
//               src={loadedImage} 
//               alt="Foto del integrante" 
//               className="contacto-img-card"
//               onClick={handleOpenModal}
//               />

//             )}
          
//             <Modal show={showModal} onHide={handleCloseModal}>
//               <Modal.Header>
//                 <Modal.Title>{nombreModal}</Modal.Title>
//                 <button className="close" onClick={handleCloseModal}>
//                   <span aria-hidden="true">&times;</span>
//                 </button>
//               </Modal.Header>
//               <Modal.Body>
//                 <div>
//                   {loadingSearch ? (
//                     <div>
//                       <span className="spinner-border spinner-border-sm"></span>
//                       <p>Cargando datos de contacto o persona...</p>
//                     </div>
//                   ) : (
//                     <div>
//                       <CreateReadUpdateGenericoConFoto
//                         cargarDatosDefault = {persona ? cargarPersonaDefault : cargarContactoDefault}
//                         DatoUpdateInput = {persona ? PersonaRead : ContactoRead}
//                         tipoDatoForImageService = {'contacto'}
//                         dataIn = {persona ? persona : datoContactoIntegrante}
//                         Service = {persona ? PersonaService : ContactoService}
//                         urlTablaDato = {persona ? '/personafisica' : '/contacto'}
//                         isVentanaEmergente = {true}
//                         el_la = {persona ? 'la' : 'el'}
//                         nombreTipoDato = {persona ? 'persona' : 'contacto'}
//                         typeCRUD={'READ'}
//                       />
//                     </div>
//                   )}
//                 </div>
//               </Modal.Body>
//               <Modal.Footer>
//                 <Button variant="secondary" onClick={handleCloseModal}>
//                   Cerrar
//                 </Button>
//               </Modal.Footer>
//             </Modal>
            
//           </div>
//         ) : (
//           <div>
//           <span className="spinner-border spinner-border-sm"></span>
//           <p>Cargando foto<br />de integrante...</p>
//           </div>
//         )}
//         </>
//       )}
//     </div>

//   );
// };


function columnIntegranteConFotoColumn(title, property, tipoFoto) {
  return {
    Header: title,
    accessor: (row) => {
      const data = row?.[property];
      if (data) {
        const filteredKeys = Object.keys(data).filter(key => {
          const value = data[key];
          return !['imagen', 'imagen_completa'].includes(key) && !Array.isArray(value);
        });
        const dataString = filteredKeys.map(key => data[key]).join(' ').toLowerCase();
        console.log("dataString");
        console.log(dataString);
        return dataString;
      }
      return null;
    },
    Cell: ({ row }) => RenderFotoIntegranteRow(
                row, 
                row.original?.[property] || parseInt(row.values?.[title]?.match(/^\d+/)) || null, 
                tipoFoto
              ),
    //filter: 'fuzzyText',
    type: "string",
  };
}

const RenderMostrarContacto = (datoContactoIntegrante, tipoImagen) => {
  const [showModal, setShowModal] = useState(false);
  const [loadedImage, setLoadedImage] = useState(null);
  const [contacto, setContacto] = useState(null);
  const [persona, setPersona] = useState(null);
  const [loadingSearch, setLoadingSearch] = useState(true);
  const [nombreModal, setNombreModal] = useState("");

  const GetIdContacto = () => {
    if(datoContactoIntegrante?.id) {
      return datoContactoIntegrante?.id;
    } else if(typeof datoContactoIntegrante === 'number') {
      return datoContactoIntegrante;
    }
    return null;
  }

  useEffect(() => {
    setNombreModal(getNombreDelDato(datoContactoIntegrante));
    setPersona(null);
    setLoadingSearch(true);
    let idContactoAux = GetIdContacto();//Consigo su id
    if(datoContactoIntegrante?.id && datoContactoIntegrante?.nombre ){
      //No es necesario buscar, ya se ingresó una persona
      setPersona(datoContactoIntegrante);
      setNombreModal(getNombreDelDato(datoContactoIntegrante));
      setLoadingSearch(false);
    }
    else if(idContactoAux && !datoContactoIntegrante?.nombre ){
      //Asigno persona o contacto si existen (al menos con el id deberia existir uno de los dos)

      //Busco si existe persona
      PersonaService.search_persona_contacto(idContactoAux).then
        (response => {
          if(response.data){
            if(response.data.dni) {
              setPersona(response.data);
            }
            else {
              setContacto(response.data);
            }
            setNombreModal(getNombreDelDato(response.data));
          }//Si no hay response.data, no existe contacto con ese id
          setLoadingSearch(false);
        },
        (error) => {
          setLoadingSearch(false);
        }
      );
    }

  }, [datoContactoIntegrante]);

  useEffect(() => {
    let idContactoAux = GetIdContacto();
    if(idContactoAux){
      if(!datoContactoIntegrante?.imagen_tabla){
        ImageService.getFoto(idContactoAux, tipoImagen, 'tabla')
          .then(response => {
            setLoadedImage(response);
          })
          .catch(error => {
            console.error('Error al obtener la imagen:', error);
          }
        );
      }
      else{
        setLoadedImage(datoContactoIntegrante.imagen_tabla);
      }
    }
  }, [datoContactoIntegrante, tipoImagen]);

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
      {(datoContactoIntegrante?.id || GetIdContacto()) && (
        <>
        {loadedImage ? (
          <div>
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
                      {(contacto || persona) ? (
                        <CreateReadUpdateGenericoConFoto
                          cargarDatosDefault = {persona ? cargarPersonaDefault : cargarContactoDefault}
                          DatoUpdateInput = {persona ? PersonaRead : ContactoRead}
                          tipoDatoForImageService = {'contacto'}
                          dataIn = {persona ? persona : contacto}
                          Service = {persona ? PersonaService : ContactoService}
                          urlTablaDato = {persona ? '/personafisica' : '/contacto'}
                          isVentanaEmergente = {true}
                          el_la = {persona ? 'la' : 'el'}
                          nombreTipoDato = {persona ? 'persona' : 'contacto'}
                          typeCRUD={'READ'}
                        />
                      ) : (
                        <>
                        <p>Error al buscar al contacto o persona</p>
                        </>
                      )}
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
          <span className="spinner-border spinner-border-sm"></span>
          <p>Cargando foto<br />de integrante...</p>
          </div>
        )}
        </>
      )}
    </div>

  );
};

function columnsIntegrantesPersonas({property="personas", el_la = "la", nombreIntegranteSingular ="persona", nombreIntegrantePlural="personas"}) {
  return columnsIntegrantesGeneric({
    property,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
    RenderMostrarIntegrantesRow: RenderMostrarIntegrantesPersonasRow,
  });
}

function columnsIntegrantesBeneficiarios({property="beneficiarios", el_la = "el", nombreIntegranteSingular ="beneficiario", nombreIntegrantePlural="beneficiarios"}) {
  return columnsIntegrantesGeneric({
    property,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
    RenderMostrarIntegrantesRow: RenderMostrarIntegrantesBeneficiarioRow,
  });
}

function columnsIntegrantesProfesionales({property="profesionales", el_la = "el", nombreIntegranteSingular ="profesional", nombreIntegrantePlural="profesionales"}) {
  return columnsIntegrantesGeneric({
    property,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
    RenderMostrarIntegrantesRow: RenderMostrarIntegrantesProfesionalRow,
  });
}

function columnsIntegrantesActividades({property="actividades", el_la = "la", nombreIntegranteSingular ="actividad", nombreIntegrantePlural="actividades"}) {
  return columnsIntegrantesGeneric({
    property,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
    RenderMostrarIntegrantesRow: RenderMostrarActividadesRow,
  });
}

function columnsIntegrantesFacturaItems({property="itemsFactura", el_la = "el", nombreIntegranteSingular ="item", nombreIntegrantePlural="items"}) {
  return columnsIntegrantesGeneric({
    property,
    el_la,
    nombreIntegranteSingular,
    nombreIntegrantePlural,
    RenderMostrarIntegrantesRow: RenderMostrarItemFacturaRow,
  });
}


function columnsIntegrantesGeneric({property, el_la="el", nombreIntegranteSingular="integrante", nombreIntegrantePlural="integrantes", RenderMostrarIntegrantesRow}) {
  if(property === null || RenderMostrarIntegrantesRow === null){
    return null;
  }

  return [
    {
      Header: `Número de ${nombreIntegrantePlural}`,
      accessor: (row) => {
        if (row && row[property]) {
          return row[property].length;
        } else {
          return null;
        }
      },
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: `${nombreIntegrantePlural}`,
      accessor: (row) => {
        const dataArray = row?.[property];
        console.log("dataArray");
        console.log(dataArray);
        if (dataArray) {
          const dataArrayString = dataArray
            .map(item => {
              const filteredKeys = Object.keys(item).filter(key => {
                const value = item[key];
                return !['imagen', 'imagen_completa'].includes(key) && !Array.isArray(value);
              });
              return filteredKeys.map(key => item[key]).join(' ').toLowerCase();
            })
            .join(' ');
          return dataArrayString;
        }
        return null;
      },
      Cell: ({ row }) =>{
        console.log("property");
        console.log(property);
        console.log("row.original?.[property]");
        console.log(row);
        return RenderMostrarIntegrantesRow({
        integrantesActuales: row.original?.[property],
        el_la,
        nombreIntegranteSingular,
        nombreIntegrantePlural
      })},
      type: "string",
    },
  ];
}

const columnFechaHora = (headerName="Fecha", accessorName="fecha") => {
  return {
    Header: headerName,
    accessor: accessorName,
    Cell: ({ value }) => {
      const formattedDate = format(new Date(value), "dd/MM/yyyy - HH:mm");
      return value ? <span>{formattedDate}</span> : <></>;
    },
    Filter: DateHourRangeColumnFilter,
    filter: dateBetweenFilterFn,
  };
};

//Diremos que no anda cuando lo metemos en column que no es funcion, porque no puede inicializar este metodo.
const columnFecha = (headerName="Fecha", accessorName="fecha") => {
  return {
    Header: headerName,
    accessor: accessorName,
    Cell: ({ value }) => {
      const formattedDate = format(new Date(value), "dd/MM/yyyy");
      return value ? <span>{formattedDate}</span> : <></>;
    },
    Filter: DateRangeColumnFilter,
    filter: dateBetweenFilterFn,
  };
};





export {
  IndeterminateCheckbox,
  IndeterminateSelectCheckbox,
  RenderFotoPerfilRow,
  RenderFotoPerfil,
  RenderFotoPerfilForTablaTenant,
  RenderFotoIntegranteRow,
  RenderMostrarContacto,
  RenderBotonVer,
  RenderBotonEditar,
  RenderBotonBorrar,
  columnIntegranteConFotoColumn,
  columnsIntegrantesPersonas,
  columnsIntegrantesBeneficiarios,
  columnsIntegrantesProfesionales,
  columnsIntegrantesActividades,
  columnsIntegrantesFacturaItems,
  columnFechaHora,
  columnFecha,
}
