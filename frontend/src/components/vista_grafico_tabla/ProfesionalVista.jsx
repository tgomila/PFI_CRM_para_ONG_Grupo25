import AbstractVista from "./AbstractVista";
import { GraficoProfesional } from './graficos/GraficoProfesional'
import { TablaProfesional } from "./tables/TablaProfesional";

const ProfesionalVista = AbstractVista(TablaProfesional, "PROFESIONAL", GraficoProfesional, "profesionales");

export default ProfesionalVista;