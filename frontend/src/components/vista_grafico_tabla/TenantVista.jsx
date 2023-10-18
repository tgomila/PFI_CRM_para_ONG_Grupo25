import AbstractVista from "./AbstractVista";
import { TablaTenant } from "./tables/TablaTenant";

//Se usa en Master Admin
const TenantVista = AbstractVista(TablaTenant, null, null, "Simulador de usuarios");

export default TenantVista;
