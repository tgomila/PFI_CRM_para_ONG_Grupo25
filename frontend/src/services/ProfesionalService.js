import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class ProfesionalService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'profesional/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'profesional/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'profesional/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'profesional/', dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'profesional/' + dtoId, {headers: authHeader()});
    }

    search(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'profesional/search/' + dtoId, {headers: authHeader()} );
    }

    //Graficos
    creadosUltimos12meses(){
        return axios.get(BACKEND_API_BASE_URL + 'profesional/grafico/contar_creados/ultimos_12_meses', { headers: authHeader() });
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'profesional/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new ProfesionalService( )