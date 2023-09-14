import AbstractVista from "./AbstractVista";
import TablaProyecto from "./tables/TablaProyecto";

const ProyectoVista = AbstractVista(TablaProyecto, "PROYECTO", null, "proyectos");

export default ProyectoVista;
