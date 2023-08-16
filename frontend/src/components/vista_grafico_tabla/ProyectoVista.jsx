import AbstractVista from "./AbstractVista";
import TablaProyecto from "./tables/TablaProyecto";

const ProyectoVista = AbstractVista(TablaProyecto, "PROYECTO", null);

export default ProyectoVista;
