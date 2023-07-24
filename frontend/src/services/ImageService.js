import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
import defaultImage from './constantsPictures/default.png';
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

//
//
const getAllContactoTablaFotos = async (lista) => {
  return getAllFotos(lista, "contacto", "tabla");
};

/**
 * 
 * @param {Array} lista array de datos de contacto, o producto, etc donde se asignan las fotos.
 * @param {string} tipoFoto "contacto", "producto", etc para backend.
 * @param {string} tamanio "tabla" o "completa".
 * @returns 
 */
const getAllFotos = async (lista, tipoFoto, tamanio) => {
  const newLista = [];
  //console.log("Entro a getAll");
  //console.log("lista:");
  //console.log(lista);
  try {
    const response = await axios.get(BACKEND_API_BASE_URL + "images/" + tipoFoto + "/info", { headers: authHeader() });
    if (response.data) {
      //console.log("Entro a getAll, hay data");
      for (const item of lista) {//Asignar su foto a cada contacto
        //console.log("item:");
        //console.log(item);
        const infoItem = response.data.find(dato => dato.id === item.id);
        let fotoUrl;
        if(infoItem){//Buscar foto que si existe
          //console.log("infoItem:");
          //console.log(infoItem);
          fotoUrl = await getFotoWithInfo(item.id, tipoFoto, tamanio, infoItem);
        }
        else{//No existe foto en bd
          //console.log("no hay item")
          fotoUrl = defaultImage;
          removeFotoFromStorage(item.id, tipoFoto);
        }
        //console.log("fotoUrl:");
        //console.log(fotoUrl);
        const itemConFoto = { ...item, imagen_tabla: fotoUrl };
        newLista.push(itemConFoto);
      }
      return newLista;
    }
    else{//Asumo que no existe ni 1 foto en BD
      for (const item of lista) {//contacto
        const itemConFoto = { ...item, imagen_tabla: defaultImage };
        newLista.push(itemConFoto);
      }
      //console.log("me fui 12");
      return newLista;
    }
  } catch(error) {//Asumo not found error.response.status === 404
    for (const item of lista) {//contacto
      const itemConFoto = { ...item, imagen_tabla: defaultImage };
      newLista.push(itemConFoto);
    }
    return newLista;
  };
}

const getFotoPerfil = () => {
  return getFoto(null, "perfil", "completa");
}

const getFotoContactoTabla = (dtoId) => {
  return getFoto(dtoId, "contacto", "tabla");
}

const getFotoContactoCompleta = (dtoId) => {
  return getFoto(dtoId, "contacto", "completa");
}


/**
 * Retorna foto
 * @param {string} dtoId puede ser solo números ejemplo "17" o solo "perfil"
 * @param {string} tipoFoto "contacto", "producto", etc para backend
 * @param {string} tamanio "tabla" o "completa"
 * @returns 
 */
const getFoto = (dtoId, tipoFoto, tamanio) => {
  return getFotoWithInfo(dtoId, tipoFoto, tamanio, null);
};


/**
 * @description
 * Copia y pega esto:
 * 
 * const auxFotoInfo = getStorageFotoInfo();
 * 
 * let nombreFotoAGuardar = auxFotoInfo.nombreFotoAGuardar;
 * 
 * let nombreFotoInfoAGuardar = auxFotoInfo.nombreFotoInfoAGuardar;
 * 
 * let direccion = auxFotoInfo.direccion;
 * 
 * let direccionInfo = auxFotoInfo.direccionInfo;
 * 
 * let imagenGuardada = auxFotoInfo.imagenGuardada;
 * 
 * let imagenGuardadaFechaCreacion = auxFotoInfo.imagenGuardadaFechaCreacion;
 * 
 * @param {*} dtoId 
 * @param {*} tipoFoto 
 * @param {*} tamanio 
 * @returns 
 */
const getStorageFotoInfo = (dtoId, tipoFoto, tamanio) => {
  
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

  const info = {
    nombreFotoAGuardar,
    nombreFotoInfoAGuardar,
    direccion,
    direccionInfo,
    imagenGuardada,
    imagenGuardadaFechaCreacion,
  }
  return info;
};

/**
 * 
 * @param {string} dtoId ejemplo "27".
 * @param {string} tipoFoto Ejemplo "contacto". Si es perfil -> "perfil"..
 * @returns 
 */
const removeFotoFromStorage = (dtoId, tipoFoto) => {
  if(dtoId === "perfil" || tipoFoto === "perfil"){//Solo eliminar perfil
    const infoPerfil = getStorageFotoInfo("perfil", null, null);
    localStorage.removeItem(infoPerfil.nombreFotoAGuardar);
    localStorage.removeItem(infoPerfil.nombreFotoInfoAGuardar);
  }
  else{//es foto contacto, elimino tamaño "tabla" y "completa"
    const infoFotoTabla = getStorageFotoInfo(dtoId, tipoFoto, "tabla");
    const infoFotoCompleta = getStorageFotoInfo(dtoId, tipoFoto, "completa");
    localStorage.removeItem(infoFotoTabla.nombreFotoAGuardar);//foto tabla
    localStorage.removeItem(infoFotoTabla.nombreFotoInfoAGuardar);//foto info
    localStorage.removeItem(infoFotoCompleta.nombreFotoAGuardar);//foto completa
    localStorage.removeItem(infoFotoCompleta.nombreFotoInfoAGuardar);//foto info
  }
  return defaultImage;
}

/**
 * Retorna foto, fotoInfo NO DEBE SER null o devuelve foto default.
 * @param {string} dtoId puede ser solo números ejemplo "17" o solo "perfil"
 * @param {string} tipoFoto "contacto", "producto", etc para backend
 * @param {string} tamanio "tabla" o "completa"
 * @param {string} fotoInfoDelBackendActual info de foto actual
 * @returns 
 */
const getFotoWithInfo = (dtoId, tipoFoto, tamanio, fotoInfoDelBackendActual) => {

  //Establezco direccion a contactar a Backend
  const auxFotoInfo = getStorageFotoInfo(dtoId, tipoFoto, tamanio);
  let nombreFotoAGuardar = auxFotoInfo.nombreFotoAGuardar;
  let nombreFotoInfoAGuardar = auxFotoInfo.nombreFotoInfoAGuardar;
  let direccion = auxFotoInfo.direccion;
  let direccionInfo = auxFotoInfo.direccionInfo;
  let imagenGuardada = auxFotoInfo.imagenGuardada;
  let imagenGuardadaFechaCreacion = auxFotoInfo.imagenGuardadaFechaCreacion;

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

  const getFotoFromBackend = async () => {
    const response = await axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() });
    return getFotoFromBackendWithInfo(dtoId, tipoFoto, tamanio, response.data);

  }

  let fotito;
  if(fotoInfoDelBackendActual){
    fotito = getFotoFromBackendWithInfo(dtoId, tipoFoto, tamanio, fotoInfoDelBackendActual);
  }
  else{
    fotito = getFotoFromBackend();
    //removeFotoFromStorage(dtoId, tipoFoto);
    //fotito = defaultImage;
  }
  //console.log("me fui");
  return fotito;
};

const getFotoFromBackendWithInfo = (dtoId, tipoFoto, tamanio, fotoInfoBackend) => {
  if(!dtoId){
    console.log("Entre a !dto")
    return defaultImage;
  }
  const fotoInfoFrontend = getStorageFotoInfo(dtoId, tipoFoto, tamanio);
  //Si existe foto entonces hacer todo
  if (fotoInfoBackend.fechaDeCreacion) {
    const fechaFrontend = fotoInfoFrontend.imagenGuardadaFechaCreacion;//Podría estar desactualizado
    const fechaBackend = fotoInfoBackend.fechaDeCreacion;
    if(fechaFrontend === fechaBackend){
      const imagenURL = localStorage.getItem(fotoInfoFrontend.nombreFotoAGuardar);
      if(imagenURL)
        return imagenURL;
    }

    //Consulto foto en backend
    return axios
    .get(BACKEND_API_BASE_URL + fotoInfoFrontend.direccion, {
      headers: authHeader(),
      responseType: 'blob'
    })
    .then((response) => {
      if (response.data) {
        const imagenBlob = new Blob([response.data], { type: response.headers['content-type'] });
        
        return new Promise((resolve) => {//Para que no me devuelva una foto nula, uso "Promise"
          const reader = new FileReader();
          reader.onload = function(event) {
            const imagenDataURL = event.target.result;
            //Elimino foto completa y de tamaño tabla anterior antes de guardar
            removeFotoFromStorage(dtoId, tipoFoto);
            localStorage.setItem(fotoInfoFrontend.nombreFotoInfoAGuardar, fechaBackend);
            localStorage.setItem(fotoInfoFrontend.nombreFotoAGuardar, imagenDataURL);
            resolve(imagenDataURL);
            //Esto se va a ejecutar 2do en el promise
          };
          reader.readAsDataURL(imagenBlob);
          //Esto se va a ejecutar 1ero en el promise
        });
      }
    })
    .catch(error => {//Asumo not found error.response.status === 404
      removeFotoFromStorage(dtoId, tipoFoto);
      return defaultImage;
    });
    //



  }
  else{//Si no existe foto entonces devuelvo default.
    removeFotoFromStorage(dtoId, tipoFoto);//Por si se borró la foto en BD.
    return defaultImage;
  }

};

async function fetchBlob(url) {
  const response = await fetch(url);
  return await response.blob();
}

async function blobToBase64(blob) {
  return new Promise((resolve) => {//Para que no me devuelva una foto nula, uso "Promise"
    const reader = new FileReader();
    reader.onload = function(event) {
      const imagenDataURL = event.target.result;
      resolve(imagenDataURL);
      //Esto se va a ejecutar 2do en el promise
    };
    reader.readAsDataURL(blob);
    //Esto se va a ejecutar 1ero en el promise
  });
}

const uploadImageContacto = async (id, imageUrl) => {
  return uploadImage(id, 'contacto', imageUrl);
}

const uploadImage = async (id, tipoDato, imageUrl) => {
  if(!id || !tipoDato || !imageUrl){
    return null;//error si no recibo nada.
  }
  const url = BACKEND_API_BASE_URL + 'images/' + tipoDato +'/' + id;
  console.log("url:");
  console.log(url);
  console.log("imageUrl:");
  console.log(imageUrl);
  
  const blob = await fetchBlob(imageUrl);
  console.log("blob:");
  console.log(blob);

  // Obtener la extensión del archivo
  const fileExtension = blob.type.split('/')[1]; // Obtiene la parte después de "image/"
  const fileName = `image.${fileExtension}`;
  
  const base64File = await blobToBase64(blob);
  console.log("base64File:");
  console.log(base64File);
  
  const formData = new FormData();
  formData.append('file', blob, fileName);
  console.log("formData:");
  console.log(formData);

  const config = {
    headers: {
      ...authHeader(),
      'Content-Type': 'multipart/form-data',
    },
  };

  console.log("Inicio axios");
  return axios.post(url, formData, config);
};

const ImageService = {
    getAllContactoTablaFotos,
    getAllFotos,
    getFoto,
    getFotoPerfil,
    getFotoContactoTabla,
    getFotoContactoCompleta,
    uploadImage,
    uploadImageContacto,
};

export default ImageService;