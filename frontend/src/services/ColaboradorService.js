import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class ColaboradorService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'colaborador/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'colaborador/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'colaborador/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'colaborador/' + dtoId, dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'colaborador/' + dtoId, {headers: authHeader()});
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'colaborador/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new ColaboradorService( )