import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class EmpleadoService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'empleado/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'empleado/', dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'empleado/' + dtoId, {headers: authHeader()});
    }

    search(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/search/' + dtoId, {headers: authHeader()} );
    }

    //Graficos
    creadosUltimos12meses(){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/grafico/contar_creados/ultimos_12_meses', { headers: authHeader() });
    }
    
    categoriaEdades(){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/grafico/contar_categoria_edad', { headers: authHeader() });
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'empleado/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new EmpleadoService( )