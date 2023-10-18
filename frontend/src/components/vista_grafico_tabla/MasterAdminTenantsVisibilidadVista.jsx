import AbstractVista from "./AbstractVista";
import { TablaMasterTenantAdminVisibilidad } from "./tables/TablaMasterTenantAdminVisibilidad";

//Se usa en Master Admin
const MasterAdminTenantsVisibilidadVista = AbstractVista(TablaMasterTenantAdminVisibilidad, null, null, "Visibilidad de roles de clientes");

export default MasterAdminTenantsVisibilidadVista;
