import axios from "axios";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;


class TenantService {

    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'tenant/all');
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'tenant/nombres_tabla');
    }
}

export default new TenantService( )