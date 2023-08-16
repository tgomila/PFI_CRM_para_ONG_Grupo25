import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

//Si ya se que no existe lo abstracto en react front, pero lo entiendo de esta manera como dev backend :/
class AbstractBaseService {
  constructor(basePath) {
    this.basePath = basePath +'/';
  }

  getById(dtoId) {
    return axios.get(BACKEND_API_BASE_URL + this.basePath + dtoId, {
      headers: authHeader(),
    });
  }

  getAll() {
    return axios.get(BACKEND_API_BASE_URL + this.basePath + "all", {
      headers: authHeader(),
    });
  }

  create(dto) {
    return axios.post(BACKEND_API_BASE_URL + this.basePath + "alta", dto, {
      headers: authHeader(),
    });
  }

  update(dto) {
    return axios.put(BACKEND_API_BASE_URL + this.basePath, dto, {
      headers: authHeader(),
    });
  }

  delete(dtoId) {
    return axios.delete(BACKEND_API_BASE_URL + this.basePath + dtoId, {
      headers: authHeader(),
    });
  }

  getColumnNames() {
    return axios.get(BACKEND_API_BASE_URL + this.basePath + "nombres_tabla", {
      headers: authHeader(),
    });
  }
}

export default AbstractBaseService;