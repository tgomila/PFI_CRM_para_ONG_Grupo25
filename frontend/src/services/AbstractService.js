import axios from "axios";
import authHeader from "./auth-header";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;

//Realmente no tiene utilidad, aún. Fue hecho para el futuro quizás.
const getById = (backendVariable, dtoId) => {
    return axios.get(BACKEND_API_BASE_URL + backendVariable + '/' + dtoId, { headers: authHeader() });
}
    
const getAll = (backendVariable) => {
    return axios.get(BACKEND_API_BASE_URL + backendVariable + '/all', { headers: authHeader() });
}
    
const create = (backendVariable, dto) => {
    return axios.post(BACKEND_API_BASE_URL + backendVariable + '/alta', dto, { headers: authHeader() });
}
    
const update = (backendVariable, dto) => {
    return axios.put(BACKEND_API_BASE_URL + backendVariable + '/', dto, {headers: authHeader()});
}

const deleteItem = (backendVariable, dtoId) => {
    return axios.delete(BACKEND_API_BASE_URL + backendVariable + '/' + dtoId, {headers: authHeader()});
}

const search = (backendVariable, dtoId) => {
    return axios.get(BACKEND_API_BASE_URL + backendVariable + '/search/' + dtoId, {headers: authHeader()} );
}

//Graficos
const creadosUltimos12meses = (backendVariable) => {
    return axios.get(BACKEND_API_BASE_URL + backendVariable + '/grafico/contar_creados/ultimos_12_meses', { headers: authHeader() });
}

//Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
const getColumnNames = (backendVariable) => {
    return axios.get(BACKEND_API_BASE_URL + backendVariable + '/nombres_tabla', { headers: authHeader() });
}

export {
    getById,
    getAll,
    create,
    update,
    deleteItem,
    search,
    creadosUltimos12meses,
    getColumnNames,
}