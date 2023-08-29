import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

/**
 * 
 * @param {string} moduloName ejemplo tiene que ser "CONTACTO", etc. Modulos del backend
 * @returns 
 */
const getVisibilidadByModulo = (moduloName) => {
    /*const modulo = getModulo(moduloName);
    if(modulo.tipoVisibilidad) {
        return modulo.tipoVisibilidad;
    } else {
        return "NO_VISTA";
    }*/

    /*const modulos = JSON.parse(localStorage.getItem("modulos"));
    if(modulos) {
        const contactoModulo = modulos.find((modulo) => modulo.moduloEnum === moduloName);
        if(contactoModulo && contactoModulo.tipoVisibilidad){
            return contactoModulo.tipoVisibilidad;
        }
    }*/

    let rta = axios
    .get(BACKEND_API_BASE_URL + "modulo/moduloname/" + moduloName, { headers: authHeader() })
    .then((response) => {
        if (response.data.tipoVisibilidad) {
            return response.data.tipoVisibilidad;
        }
        else {
            return "NO_VISTA";
        }
    });
    return rta;


};

/**
 * 
 * @param {string} moduloName ejemplo tiene que ser "CONTACTO", etc. Modulos del backend
 * @returns 
 */
const getModulo = (moduloName) => {
    const modulos = JSON.parse(localStorage.getItem("modulos"));
    if(modulos) {
        const contactoModulo = modulos.find((modulo) => modulo.moduloEnum === moduloName);
        if(contactoModulo){
            return contactoModulo;
        }
    }

    let rta = axios
    .get(BACKEND_API_BASE_URL + "modulo/moduloname/" + moduloName, { headers: authHeader() })
    .then((response) => {
        if (response.data) {
            return response.data;
        }
        else {
            return null;
        }
    });
    return rta;


};




/**
 * Incluye NO_VISTA
 * @returns 
 */
const getAll = () => {
    //return  ["Persona", "Beneficiario","Empleado","Colaborador","Consejo Adhonorem","Persona Juridica","Profesional","Donacion","Factura","Users"];
    let link = BACKEND_API_BASE_URL + getBestRoleLink();
    let rta = axios
    //.get(link, { headers: authHeader() }) //V1 cuando backend no leia roles del usuario, se lo ponía a mano.
    .get(BACKEND_API_BASE_URL + "modulo/", { headers: authHeader() })
    .then((response) => {
        if (response.data) {
            if (localStorage.getItem("modulos") !== null){
                localStorage.removeItem("modulos");
            }
            localStorage.setItem("modulos", JSON.stringify(response.data));
            
        }

        return response;
    });
    return rta;


};

/**
 * Exclusivo para la vista tabla de modificar la visibilidad de modulos
 * Incluye NO_VISTA
 * @returns 
 */
const getAllParaModificar = () => {
   let rta = axios
    //.get(link, { headers: authHeader() }) //V1 cuando backend no leia roles del usuario, se lo ponía a mano.
    .get(BACKEND_API_BASE_URL + "modulo/all/simple", { headers: authHeader() })
    .then((response) => {
        return response;
    });
    return rta;
};

const putModificarModuloVisibilidad = (roleEnum, moduloEnum, tipoVisibilidadEnum) => {
    const dto = {
        roleEnum: roleEnum,
        moduloEnum: moduloEnum,
        tipoVisibilidadEnum: tipoVisibilidadEnum
    };
    console.log("dto");
    console.log(dto);

    let rta = axios
    .put(BACKEND_API_BASE_URL + "modulo/modificar", dto, { headers: authHeader() })
    .then((response) => {
        return response.data;
    })
    .catch((error) => {
        throw error;
    });
    return rta;
}

const getBestRoleLink = () => {
    let rol = getBestRole();
    if(rol !== null)
        return "modulo/rolename/" + rol;
    else
        return "modulo/default";
}

//Realmente solo 2 roles lo utilizarán: Profesional y Empleado
//Rol admin también lo utilizará (el jefe)
//El resto de roles son por default
const getBestRole = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        const roles = user.roles;
        
        //Chequear mejor rol
        if(roles.includes('ROLE_ADMIN')){
            return 'ROLE_ADMIN';
        } else if(roles.includes('ROLE_EMPLOYEE')){
            return 'ROLE_EMPLOYEE';
        } else if(roles.includes('ROLE_PROFESIONAL')){
            return 'ROLE_PROFESIONAL';
        } else if(roles.includes('ROLE_USER')){
            return 'ROLE_USER';
        }

    }
    return(null);
}

const getModulos = () => {
    return getAll();

    //Error a partir de abajo, getAll devuelve Promise y modulos devuelve Array
    const user = JSON.parse(localStorage.getItem('user'));
    const modulos = JSON.parse(localStorage.getItem('modulos'));
    if (user && modulos){
        console.log("Entre 1");
        return modulos;
    }
    else{
        console.log("Entre 2");
        return getAll();
    }
};

const modulosService = {
    getVisibilidadByModulo,
    getAll,
    getBestRole,
    getModulos,
    getAllParaModificar,
    putModificarModuloVisibilidad,
  };

export default modulosService;