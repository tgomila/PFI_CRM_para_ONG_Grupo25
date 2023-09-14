import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class DonacionService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'donacion/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'donacion/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'donacion/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'donacion/', dto, {headers: authHeader()});
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'donacion/' + dtoId, {headers: authHeader()});
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'donacion/nombres_tabla', { headers: authHeader() });
    }



    //Graficos
    top_20_donantes_total(){
        return axios.get(BACKEND_API_BASE_URL + 'donacion/grafico/top_20_donantes/total', { headers: authHeader() });
    }

    top_20_donantes_cantidad(){
        return axios.get(BACKEND_API_BASE_URL + 'donacion/grafico/top_20_donantes/cantidad', { headers: authHeader() });
    }


    
}

export default new DonacionService( )