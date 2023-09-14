import AbstractVista from "./AbstractVista";
import { TablaFactura } from "./tables/TablaFactura";

const FacturaVista = AbstractVista(TablaFactura, "FACTURA", null, "facturas");

export default FacturaVista;
