import AbstractVista from "./AbstractVista";
import TablaInsumo from "./tables/TablaInsumo";

const InsumoVista = AbstractVista(TablaInsumo, "INSUMO", null);

export default InsumoVista;
