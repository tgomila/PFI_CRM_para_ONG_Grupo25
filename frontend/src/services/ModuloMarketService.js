import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class ContactoService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/' + dtoId, { headers: authHeader() });
    }
    
    getByModuloEnum(dto){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/find/' + dto, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/all', { headers: authHeader() });
    }

    getPaidModulos(){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/paid_modules', { headers: authHeader() });
    }

    precioSuscripcionPremiumMes(){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/precio/premium/mes', { headers: authHeader() });
    }

    precioSuscripcionPremiumAnio(){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/precio/premium/anio', { headers: authHeader() });
    }

    isPrueba7diasUtilizadaByEnum(dtoEnumName){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/isPrueba7diasUtilizada/' + dtoEnumName, { headers: authHeader() });
    }

    isPrueba7diasUtilizada(){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/isPrueba7diasUtilizada', { headers: authHeader() });
    }

    activarPrueba7diasByEnum(dtoEnumName){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/activarPrueba7dias/' + dtoEnumName, { headers: authHeader() });
    }

    activarPrueba7dias(){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/activarPrueba7dias', { headers: authHeader() });
    }

    suscripcionBasicMes(dtoEnumName){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/mes/' + dtoEnumName, { headers: authHeader() });
    }

    suscripcionBasicAnio(dtoEnumName){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/anio/' + dtoEnumName, { headers: authHeader() });
    }

    suscripcionPremiumMes(){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/premium/mes', { headers: authHeader() });
    }

    suscripcionPremiumAnio(){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/suscripcion/premium/anio', { headers: authHeader() });
    }

    desuscribir(){
        return axios.post(BACKEND_API_BASE_URL + 'modulomarket/premium/desuscribir', { headers: authHeader() });
    }

    poseeAcceso(dtoEnumName){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/modulo/acceso/' + dtoEnumName, { headers: authHeader() });
    }

    poseeSuscripcionActiva(dtoEnumName){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/suscripto/' + dtoEnumName, { headers: authHeader() });
    }

    getModuloMarketVencidosByBoolean(dtoEnumName){
        return axios.get(BACKEND_API_BASE_URL + 'modulomarket/modulo/vencido' + dtoEnumName, { headers: authHeader() });
    }
}

export default new ContactoService( )