import AbstractVista from "./AbstractVista";
import TablaDonacion from "./tables/TablaDonacion";

const DonacionVista = AbstractVista(TablaDonacion, "DONACION", null);

export default DonacionVista;
