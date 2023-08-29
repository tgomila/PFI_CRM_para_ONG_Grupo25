import AbstractVista from "./AbstractVista";
import TablaModuloVisibilidad from "./tables/TablaModuloVisibilidad";

const ModuloVisibilidadVista = AbstractVista(TablaModuloVisibilidad, "MARKETPLACE", null);

export default ModuloVisibilidadVista;
