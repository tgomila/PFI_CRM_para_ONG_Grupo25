import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";

// Esto chequea si el token está expirado en clases superiores, para luego cerrar sesión automáticamente.

const API_URL = constantsURL.API_BASE_URL + "test/";

const getPublicContent = () => {
  return axios.get(API_URL + "all");
};

const getUserBoard = () => {
  return axios.get(API_URL + "user", { headers: authHeader() });
};

const getProfesionalBoard = () => {
  return axios.get(API_URL + "profesional", { headers: authHeader() });
};

const getEmployeeBoard = () => {
  return axios.get(API_URL + "employee", { headers: authHeader() });
};

const getAdminBoard = () => {
  return axios.get(API_URL + "admin", { headers: authHeader() });
};

const UserService = {
  getPublicContent,
  getUserBoard,
  getProfesionalBoard,
  getEmployeeBoard,
  getAdminBoard,
};

export default UserService;
