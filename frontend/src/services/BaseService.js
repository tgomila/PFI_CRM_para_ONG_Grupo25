import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;


class BaseService {

    getById(redireccionamiento, dtoId){
        return axios.get(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/' + dtoId), { headers: authHeader() };
    }
    
    getAll(redireccionamiento){
        return axios.get(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/all', { headers: authHeader() });
    }

    create(redireccionamiento, dto){
        return axios.post(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/alta', dto, { headers: authHeader() });
    }

    update(redireccionamiento, dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/' + dtoId, dto, {headers: authHeader()});
    }

    delete(redireccionamiento, dtoId){
        return axios.delete(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/' + dtoId, {headers: authHeader()});
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(redireccionamiento){
        return axios.get(BACKEND_API_BASE_URL + redireccionamiento.redireccionamiento + '/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new BaseService( )