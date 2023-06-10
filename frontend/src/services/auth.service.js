import axios from "axios";
import * as constantsURL from "../components/constants/ConstantsURL";

//const API_URL = "http://localhost:8080/api/auth/";
const API_URL = constantsURL.API_BASE_URL + "auth/";

const register = (name, username, email, password, tenantOrClientId) => {
  return axios.post(API_URL + "signup", {
    name,
    username,
    email,
    password,
    tenantOrClientId,
  });
};

const login = (usernameOrEmail, password, tenantOrClientId) => {
  return axios
    .post(API_URL + "signin", {
      usernameOrEmail,
      password,
      tenantOrClientId,
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }

      return response.data;
    });
};

const logout = () => {
  //localStorage.removeItem("user");
  //localStorage.removeItem("modulos");
  localStorage.clear();
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
  
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default AuthService;
