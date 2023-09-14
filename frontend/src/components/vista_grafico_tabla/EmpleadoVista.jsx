import AbstractVista from "./AbstractVista";
import { GraficoEmpleado } from './graficos/GraficoEmpleado'
import TablaEmpleado from "./tables/TablaEmpleado";

const EmpleadoVista = AbstractVista(TablaEmpleado, "EMPLEADO", GraficoEmpleado, "empleados");

export default EmpleadoVista;