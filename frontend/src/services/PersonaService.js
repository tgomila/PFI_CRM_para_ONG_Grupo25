import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class PersonaService {

    getById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/' + dtoId, { headers: authHeader() });
    }

    //devuelve null si persona no existe, pero si existe devuelve persona
    getSiExisteById(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/si_existe/' + dtoId, { headers: authHeader() });
    }
    
    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/all', { headers: authHeader() });
    }

    create(dto){
        return axios.post(BACKEND_API_BASE_URL + 'personafisica/alta', dto, { headers: authHeader() });
    }

    update(dto, dtoId){
        return axios.put(BACKEND_API_BASE_URL + 'personafisica/', dto, {headers: authHeader()} );
    }

    delete(dtoId){
        return axios.delete(BACKEND_API_BASE_URL + 'personafisica/' + dtoId, {headers: authHeader()} );
    }

    search(dtoId){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/search/' + dtoId, {headers: authHeader()} );
    }

    //Graficos
    creadosUltimos12meses(){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/grafico/contar_creados/ultimos_12_meses', { headers: authHeader() });
    }

    categoriaEdades(){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/grafico/contar_categoria_edad', { headers: authHeader() });
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'personafisica/nombres_tabla', { headers: authHeader() });
    }


    
}

export default new PersonaService( )