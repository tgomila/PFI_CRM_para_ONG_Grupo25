import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
import defaultImage from './constantsPictures/default.png';
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

const getAllContactoTablaFotos = (listaIds, lista) => {
  const newLista = [];
  return axios
    .get(BACKEND_API_BASE_URL + "images/contacto/info", { headers: authHeader() })
    .then((response) => {
      if (response.data) {
        for (const item of lista) {//contacto
          const infoItem = response.data.find(dato => dato.id === item.id);
          let fotoUrl;
          if(infoItem){//Buscar
            fotoUrl = getFotoContactoTabla(item.id);
          }
          else{
            fotoUrl = defaultImage;
          }
          const itemConFoto = { ...item, imagen_tabla: fotoUrl };
          newLista.push(itemConFoto);
        }
        return newLista;
      }
      else{
        for (const item of lista) {//contacto
          const itemConFoto = { ...item, imagen_tabla: defaultImage };
          newLista.push(itemConFoto);
        }
        return newLista;
      }
    })
    .catch(error => {//Asumo not found error.response.status === 404
      for (const item of lista) {//contacto
        const itemConFoto = { ...item, imagen_tabla: defaultImage };
        newLista.push(itemConFoto);
      }
      return newLista;
    });
}

const getFotoPerfil = () => {
  return getFoto(null, "images/perfil");
}

const getFotoContactoTabla = (dtoId) => {
  return getFoto(dtoId, "contacto", "tabla");
}

const getFotoContactoCompleta = (dtoId) => {
  return getFoto(dtoId, "contacto", "completa");
}


/**
 * Retorna foto
 * @param {*} dtoId puede ser solo números ejemplo "17" o solo "perfil"
 * @param {*} tipoFoto "contacto", "producto", etc para backend
 * @param {*} tamanio "tabla" o "completa"
 * @returns 
 */
const getFoto = (dtoId, tipoFoto, tamanio) => {

  //Establezco direccion a contactar a Backend
  let nombreFotoAGuardar;
  let nombreFotoInfoAGuardar;
  let direccion;
  let direccionInfo;
  let imagenGuardada;
  let imagenGuardadaFechaCreacion;
  if(dtoId === "perfil" || tipoFoto === "perfil"){
    //tipoFoto = contacto, tamanio = completo por la fuerza.
    direccion = "images/perfil";
    direccionInfo = "images/perfil/info";
    nombreFotoAGuardar = "foto.perfil";//foto.perfil
    nombreFotoInfoAGuardar = "foto.perfil.fecha_creacion";
    imagenGuardada = localStorage.getItem(nombreFotoAGuardar);
    imagenGuardadaFechaCreacion = localStorage.getItem(nombreFotoAGuardar+".fecha_creacion");
  }
  else if(tamanio === "tabla"){
    direccion = "images/" + tipoFoto + "/search_tabla/"+dtoId;//images/contacto/search_tabla/3
    direccionInfo = "images/" + tipoFoto + "/info/" + dtoId;//images/contacto/info/3
    nombreFotoAGuardar = "foto."+tipoFoto+"_"+tamanio+"."+dtoId;//foto_contacto_tabla.3
    nombreFotoInfoAGuardar = "foto."+tipoFoto+"."+dtoId+".fecha_creacion";//foto.contacto.3.fecha_creacion
    imagenGuardada = localStorage.getItem(nombreFotoAGuardar);
    imagenGuardadaFechaCreacion = localStorage.getItem("foto."+tipoFoto+"."+dtoId+".fecha_creacion");
  } else {//else if(tamanio === "completa"){
    direccion = "images/" + tipoFoto + "/search/"+dtoId;//images/contacto/search/3
    direccionInfo = "images/" + tipoFoto + "/info/" + dtoId;//images/contacto/info/3
    nombreFotoAGuardar = "foto."+tipoFoto+"."+dtoId;//"foto.contacto.3
    nombreFotoInfoAGuardar = "foto."+tipoFoto+"."+dtoId+".fecha_creacion";//foto.contacto.3.fecha_creacion
    imagenGuardada = localStorage.getItem(nombreFotoAGuardar);
    imagenGuardadaFechaCreacion = localStorage.getItem("foto."+tipoFoto+"."+dtoId+".fecha_creacion");
  }

  const removeFotoFromBackend = () => {
    if(dtoId === "perfil" || tipoFoto === "perfil"){//Solo eliminar perfil
      localStorage.removeItem(nombreFotoAGuardar);
    }
    else{//es foto contacto, elimino tamaño "tabla" y "completa"
      localStorage.removeItem("foto."+tipoFoto+"_"+tamanio+"."+dtoId);//foto tabla
      localStorage.removeItem("foto."+tipoFoto+"."+dtoId);//foto completa
    }
    localStorage.removeItem(nombreFotoInfoAGuardar);
    return defaultImage;
  }

  const dataURLtoBlob = (dataURL) => {
    const arr = dataURL.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], { type: mime });
  }

  const getFotoFromBackendFast = () => {
    return axios
      .get(BACKEND_API_BASE_URL + direccion, { 
        headers: authHeader(), 
        responseType: 'blob' 
      })
      .then((response) => {
        if (response.data) {
          const imagenBlob = new Blob([response.data], { type: response.headers['content-type'] });
          const imagenURL = URL.createObjectURL(imagenBlob);

          //localStorage.setItem(nombreFotoAGuardar, imagenURL);
          return imagenURL;// response.data;
        }
      })
      .catch(error => {//Asumo not found error.response.status === 404
        removeFotoFromBackend();
        return defaultImage;
      });
  }
  //return getFotoFromBackendFast();

  const getFotoFromBackend = () => {
    let fechaDeCreacion;
    return axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() })
      .then((response) => {
        //Si existe foto entonces hacer todo
        if (response.data.fechaDeCreacion) {
          //localStorage.setItem(nombreFotoInfoAGuardar, response.data.fechaDeCreacion);
          fechaDeCreacion = response.data.fechaDeCreacion;


          //
          return axios
          .get(BACKEND_API_BASE_URL + direccion, { 
            headers: authHeader(), 
            responseType: 'blob' 
          })
          .then((response) => {
            if (response.data) {
              if(localStorage.getItem(nombreFotoInfoAGuardar) !== fechaDeCreacion){
                //Elimino foto el otro tipo de foto
                removeFotoFromBackend();
                localStorage.setItem(nombreFotoInfoAGuardar, fechaDeCreacion);
              }
              const imagenBlob = new Blob([response.data], { type: response.headers['content-type'] });
              
              const reader = new FileReader();
              reader.onload = function(event) {
                const imagenDataURL = event.target.result;
                localStorage.setItem(nombreFotoAGuardar, imagenDataURL);
              };
              //const imagenURL = URL.createObjectURL(imagenBlob);
              reader.readAsDataURL(imagenBlob);
              const imagenURL = localStorage.getItem(nombreFotoAGuardar);

              //localStorage.setItem(nombreFotoAGuardar, imagenURL);
              return imagenURL;// response.data;
            }
          })
          .catch(error => {//Asumo not found error.response.status === 404
            removeFotoFromBackend();
            return defaultImage;
          });
          //



        }
        else{//Si no existe foto entonces devuelvo default.
          removeFotoFromBackend();
          return defaultImage;
        }
      }
    );

  }

  if(imagenGuardada !== null && imagenGuardadaFechaCreacion !== null) {
    let fecha_creacion_ultima_foto_server;
    
    return axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() })
      .then((response) => {
        if (response.data.fechaDeCreacion) {
          const fechaFront = imagenGuardadaFechaCreacion;
          const fechaBack = response.data.fechaDeCreacion;
          if(fechaBack === fechaFront){
            const imagenBlob = new Blob([imagenGuardada], { type: response.headers['content-type'] });
            const imagenURL = URL.createObjectURL(imagenBlob);
            const imagenURL2 = dataURLtoBlob(imagenGuardada);
            return getFotoFromBackend();
            //return imagenURL2;
          }
          else{
            return getFotoFromBackend();
          }
        }
      });
    //return imagenGuardada;
  }
  else {
    return getFotoFromBackend();
  }
};

const ImageService = {
    getAllContactoTablaFotos,
    getFoto,
    getFotoPerfil,
    getFotoContactoTabla,
    getFotoContactoCompleta,
};

export default ImageService;