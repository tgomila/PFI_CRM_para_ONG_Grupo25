import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;


class modulosService {

    getAll(){

        return axios.get(BACKEND_API_BASE_URL + "/modulos" + '/all', { headers: authHeader() });
    }

    
}

export default new modulosService( )