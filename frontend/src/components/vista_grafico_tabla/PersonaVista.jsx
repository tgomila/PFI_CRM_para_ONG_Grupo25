import AbstractVista from "./AbstractVista";
import { GraficoPersona } from './graficos/GraficoPersona'
import { TablaPersona } from "./tables/TablaPersona";

const PersonaVista = AbstractVista(TablaPersona, "PERSONA", GraficoPersona, "personas");

export default PersonaVista;