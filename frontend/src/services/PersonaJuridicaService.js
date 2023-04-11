import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class PersonaJuridicaService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'personajuridica/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'personajuridica/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'personajuridica/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'personajuridica/' + dtoId, dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'personajuridica/' + dtoId, {headers: authHeader()});
    }

    getEnumTipoPersonaJuridica(){
        return axios.get(BACKEND_API_BASE_URL + 'personajuridica/enum/tipo_persona_puridica', { headers: authHeader() });
    }

    search(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'personajuridica/search/' + dtoId, {headers: authHeader()} );
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'personajuridica/nombres_tabla', { headers: authHeader() });
    }
    
}

export default new PersonaJuridicaService( )