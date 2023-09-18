import AbstractVista from "./AbstractVista";
import { TablaProgramaDeActividades } from "./tables/TablaProgramaDeActividades";

const ProgramaDeActividadesVista = AbstractVista(TablaProgramaDeActividades, "ACTIVIDAD", null, "programa de actividades");

export default ProgramaDeActividadesVista;