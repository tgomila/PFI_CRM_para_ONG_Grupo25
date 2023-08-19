import AbstractVista from "./AbstractVista";
import { TablaProgramaDeActividades } from "./tables/TablaProgramaDeActividades";

const ProgramaDeActividadesVista = AbstractVista(TablaProgramaDeActividades, "PROGRAMA_DE_ACTIVIDADES", null);

export default ProgramaDeActividadesVista;
