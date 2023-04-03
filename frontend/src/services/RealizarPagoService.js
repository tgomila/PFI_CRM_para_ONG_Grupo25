import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

const redireccionamiento = 'modulomarket/suscripcion/';

class RealizarPagoService {


    postPago( moduloAPagar){
        console.log(BACKEND_API_BASE_URL + redireccionamiento  + moduloAPagar.moduloAPagar)
        console.log(authHeader())
        return axios.post(BACKEND_API_BASE_URL + redireccionamiento  + moduloAPagar.moduloAPagar, { headers: authHeader() });
    }

    
}

export default new RealizarPagoService( )