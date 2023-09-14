import AbstractVista from "./AbstractVista";
import { GraficoBeneficiario } from './graficos/GraficoBeneficiario'
import { TablaBeneficiario } from "./tables/TablaBeneficiario";

const BeneficiarioVista = AbstractVista(TablaBeneficiario, "BENEFICIARIO", GraficoBeneficiario, "beneficiarios");

export default BeneficiarioVista;