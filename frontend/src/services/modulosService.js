import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;



const getAll = () => {
    //return  ["Persona", "Beneficiario","Empleado","Colaborador","Consejo Adhonorem","Persona Juridica","Profesional","Donacion","Factura","Users"];
    console.log("Imprimo rol: " + getBestRoleLink());
    let link = BACKEND_API_BASE_URL + getBestRoleLink();
    let rta = axios
    .get(link, { headers: authHeader() })
    .then((response) => {
        if (response.data) {
            if (localStorage.getItem("modulos") !== null){
                localStorage.removeItem("modulos");
            }
            localStorage.setItem("modulos", JSON.stringify(response.data));
            
        }

        return response;
    });
    console.log("rta: ");
    console.log(rta);
    return rta;


};

const getBestRoleLink = () => {
    let rol = getBestRole();
    if(rol !== null)
        return "modulo/name/" + rol;
    else
        return "modulo/default";
}

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
    getAll,
    getBestRole,
    getModulos,
  };

export default modulosService;