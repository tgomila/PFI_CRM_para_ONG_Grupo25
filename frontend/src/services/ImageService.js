import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
import defaultImage from './constantsPictures/default.png';
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

const getFotoPerfil = () => {
  return getFotoContacto("1");
}

/**
 * Retorna foto
 * @param {*} dtoId puede ser solo números ejemplo "17" o solo "perfil"
 * @returns 
 */
const getFotoContacto = (dtoId) => {
  const imagenGuardada = localStorage.getItem("foto.contacto."+dtoId);
  const imagenGuardadaFechaCreacion = localStorage.getItem("foto.contacto."+dtoId+".fecha_creacion");
  
  let direccion = "images/perfil";
  let direccionInfo = "images/contacto/info/" + dtoId;
  if(dtoId !== "perfil"){
    console.log("entre a foto 1");
    direccion = "images/contacto/search/"+dtoId;
    direccionInfo = "images/contacto/info/" + dtoId;
  }
  else{
    console.log("entre a foto 2");
    direccion = "images/perfil";
    direccionInfo = "images/contacto/info/" + dtoId;
  }
  if(imagenGuardada !== null && imagenGuardadaFechaCreacion !== null) {
    console.log("entre a foto perfil guardada");
    console.log("Fecha foto guardada: " + imagenGuardadaFechaCreacion);
    let fecha_creacion_ultima_foto_server;
    
    axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() })
      .then((response) => {
        if (response.data.fechaDeCreacion) {
          const fechaFront = imagenGuardadaFechaCreacion;
          const fechaBack = response.data.fechaDeCreacion;
          console.log("Fecha foto front: " + fechaFront);
          console.log("Fecha foto server: " + fechaBack);
          if(fechaBack === fechaFront){
            console.log("son iguales!");
            return imagenGuardada;
          }
          else{
            console.log("no son iguales!");



            console.log(dtoId)
            return axios
              .get(BACKEND_API_BASE_URL + direccion, { 
                headers: authHeader(), 
                responseType: 'blob' 
              })
              .then((response) => {
                console.log("Llegue aca");
                console.log(response);
                if (response.data) {
                  if (localStorage.getItem("foto.contacto."+dtoId) !== null){
                      localStorage.removeItem("foto.contacto."+dtoId);
                  }
                  if (localStorage.getItem("foto.contacto."+dtoId+".fecha_creacion") !== null){
                    localStorage.removeItem("foto.contacto."+dtoId+".fecha_creacion");
                  }
                  
                  const imagenBlob = new Blob([response.data], { type: response.headers['content-type'] });
                  const imagenURL = URL.createObjectURL(imagenBlob);
                  localStorage.setItem("foto.contacto."+dtoId, imagenURL);
          
                  let fecha_creacion;
                  axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() })
                    .then((response) => {
                      if (response.data.fechaDeCreacion) {
                        localStorage.setItem("foto.contacto."+dtoId+".fecha_creacion", response.data.fechaDeCreacion);
                      }
                    })
                    
                    console.log("ImagenURL");
                    console.log(imagenURL);
                  return imagenURL;// response.data;
                }
              })
              .catch(error => {//Asumo not found error.response.status === 404
                console.log("Llegue aca error");
                  if (localStorage.getItem("foto.contacto"+dtoId) !== null){
                    localStorage.removeItem("foto.contacto"+dtoId);
                    localStorage.removeItem("foto.contacto."+dtoId+".fecha_creacion");
                  }
                  return defaultImage;
              });





          }
          //localStorage.setItem("foto.contacto."+dtoId+".fecha_creacion", response.data.fechaDeCreacion);
        }
      });
    //return imagenGuardada;
  }




  console.log(dtoId)
  return axios
    .get(BACKEND_API_BASE_URL + direccion, { 
      headers: authHeader(), 
      responseType: 'blob' 
    })
    .then((response) => {
      console.log("Llegue aca");
      console.log(response);
      if (response.data) {
        if (localStorage.getItem("foto.contacto."+dtoId) !== null){
            localStorage.removeItem("foto.contacto."+dtoId);
        }
        if (localStorage.getItem("foto.contacto."+dtoId+".fecha_creacion") !== null){
          localStorage.removeItem("foto.contacto."+dtoId+".fecha_creacion");
        }
        
        const imagenBlob = new Blob([response.data], { type: response.headers['content-type'] });
        const imagenURL = URL.createObjectURL(imagenBlob);
        localStorage.setItem("foto.contacto."+dtoId, imagenURL);

        let fecha_creacion;
        axios.get(BACKEND_API_BASE_URL + direccionInfo, { headers: authHeader() })
          .then((response) => {
            if (response.data.fechaDeCreacion) {
              localStorage.setItem("foto.contacto."+dtoId+".fecha_creacion", response.data.fechaDeCreacion);
            }
          })
        
        return imagenURL;// response.data;
      }
    })
    .catch(error => {//Asumo not found error.response.status === 404
      console.log("Llegue aca error");
        if (localStorage.getItem("foto.contacto"+dtoId) !== null){
          localStorage.removeItem("foto.contacto"+dtoId);
          localStorage.removeItem("foto.contacto."+dtoId+".fecha_creacion");
        }
        return defaultImage;
    });
};



/*class ImageService {
    
    getFotoPerfil(){
        return axios.get(BACKEND_API_BASE_URL + 'images/perfil', { headers: authHeader(), responseType: 'blob' });
    }
}

export default new ImageService( )*/

const ImageService = {
    getFotoPerfil,
    getFotoContacto,
};

export default ImageService;