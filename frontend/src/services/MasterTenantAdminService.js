import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;
const SERVICE_URL = BACKEND_API_BASE_URL + "master_tenant/";

class MasterTenantAdminService {

    //Este está de más, no se debería usar
    getAllModulosVisibilidad(){
        return axios.get(SERVICE_URL + 'modulos/visibilidad/all', { headers: authHeader() });
    }

    getAllModulosMarket(){
        return axios.get(BACKEND_API_BASE_URL + 'master_tenant/modulos/market/all', { headers: authHeader() });
    }

    postSumarTiempoMarket(dto){
        console.log("Pase por aqui")
        return axios.post(SERVICE_URL + 'modulos/market/sumar_tiempo', dto, { headers: authHeader() });
    }
    
}

export default new MasterTenantAdminService( )