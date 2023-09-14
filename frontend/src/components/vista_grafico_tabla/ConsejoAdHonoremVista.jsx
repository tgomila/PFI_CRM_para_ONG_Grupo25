import AbstractVista from "./AbstractVista";
import { GraficoConsejoAdHonorem } from './graficos/GraficoConsejoAdHonorem'
import TablaConsejoAdHonorem from "./tables/TablaConsejoAdHonorem";

const ConsejoAdHonoremVista = AbstractVista(TablaConsejoAdHonorem, "CONSEJOADHONOREM", GraficoConsejoAdHonorem, "consejo ad honorem");

export default ConsejoAdHonoremVista;