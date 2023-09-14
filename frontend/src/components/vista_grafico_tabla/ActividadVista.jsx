import AbstractVista from "./AbstractVista";
import { TablaActividad } from "./tables/TablaActividad";

const ActividadVista = AbstractVista(TablaActividad, "ACTIVIDAD", null, "actividades");

export default ActividadVista;