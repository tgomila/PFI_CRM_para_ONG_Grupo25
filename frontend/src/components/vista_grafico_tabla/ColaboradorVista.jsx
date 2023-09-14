import AbstractVista from "./AbstractVista";
import { GraficoColaborador } from './graficos/GraficoColaborador'
import TablaColaborador from "./tables/TablaColaborador";

const ColaboradorVista = AbstractVista(TablaColaborador, "COLABORADOR", GraficoColaborador, "colaboradores");

export default ColaboradorVista;