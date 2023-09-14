import AbstractVista from "./AbstractVista";
import { GraficoVoluntario } from './graficos/GraficoVoluntario'
import TablaVoluntario from "./tables/TablaVoluntario";

const VoluntarioVista = AbstractVista(TablaVoluntario, "VOLUNTARIO", GraficoVoluntario, "voluntarios");

export default VoluntarioVista;