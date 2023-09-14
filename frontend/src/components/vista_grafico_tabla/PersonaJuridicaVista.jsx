import AbstractVista from "./AbstractVista";
import { GraficoPersonaJuridica } from './graficos/GraficoPersonaJuridica'
import TablaPersonaJuridica from "./tables/TablaPersonaJuridica";

const PersonaJuridicaVista = AbstractVista(TablaPersonaJuridica, "PERSONAJURIDICA", GraficoPersonaJuridica, "personas jurídicas");

export default PersonaJuridicaVista;