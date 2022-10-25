import axios from "axios";
import authHeader from "./auth-header";
const EMPLOYEE_API_BASE_URL = "http://localhost:8080/api/";



class EmployeeService {

    getEmployees(redireccionamiento){
        const direccionURL = EMPLOYEE_API_BASE_URL + redireccionamiento.redireccionamiento + '/all';
        console.log(direccionURL);

        return axios.get(EMPLOYEE_API_BASE_URL + redireccionamiento.redireccionamiento + '/all', { headers: authHeader() });
    }

    createEmployee(redireccionamiento, employee){
        return axios.post(EMPLOYEE_API_BASE_URL + redireccionamiento.redireccionamiento + '/alta', employee , { headers: authHeader() });
    }

    getEmployeeById(employeeId){
        return axios.get(EMPLOYEE_API_BASE_URL + '/' + employeeId);
    }

    updateEmployee(employee, employeeId){
        return axios.put(EMPLOYEE_API_BASE_URL + '/' + employeeId, employee);
    }

    deleteEmployee(employeeId){
        return axios.delete(EMPLOYEE_API_BASE_URL + '/' + employeeId);
    }
}

export default new EmployeeService( )