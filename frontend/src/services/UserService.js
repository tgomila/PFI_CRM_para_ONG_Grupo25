import AbstractBaseService from "./AbstractBaseService";
import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

class UserService extends AbstractBaseService {
  constructor() {
    super("users");
  }
    
  getAllWithContacto(){
      return axios.get(BACKEND_API_BASE_URL + 'users/list', { headers: authHeader() });
  }
}

export default new UserService();

// class UserService {

//     getById(dtoId){
//         return axios.get(BACKEND_API_BASE_URL + 'users/' + dtoId, { headers: authHeader() });
//     }
    
//     getAll(){
//         return axios.get(BACKEND_API_BASE_URL + 'users/all', { headers: authHeader() });
//     }
    
//     getAllWithContacto(){
//         return axios.get(BACKEND_API_BASE_URL + 'users/list', { headers: authHeader() });
//     }


    
// }

// export default new UserService( )