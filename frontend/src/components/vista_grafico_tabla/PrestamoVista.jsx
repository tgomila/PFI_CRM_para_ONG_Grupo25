import AbstractVista from "./AbstractVista";
import TablaPrestamo from "./tables/TablaPrestamo";

const PrestamoVista = AbstractVista(TablaPrestamo, "PRESTAMO", null);

export default PrestamoVista;
