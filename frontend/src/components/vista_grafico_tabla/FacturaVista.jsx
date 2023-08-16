import AbstractVista from "./AbstractVista";
import { TablaFactura } from "./tables/TablaFactura";

const FacturaVista = AbstractVista(TablaFactura, "FACTURA", null);

export default FacturaVista;
