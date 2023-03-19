import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class ContactoService {
    
    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'contacto/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'contacto/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'contacto/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'contacto/' + dtoId, dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'contacto/' + dtoId, {headers: authHeader()});
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'contacto/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new ContactoService( )