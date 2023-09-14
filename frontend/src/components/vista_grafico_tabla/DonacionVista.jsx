import AbstractVista from "./AbstractVista";
import { GraficoDonacion } from "./graficos/donacion/GraficoDonacion";
import TablaDonacion from "./tables/TablaDonacion";

const DonacionVista = AbstractVista(TablaDonacion, "DONACION", GraficoDonacion, "donaciones");

export default DonacionVista;
