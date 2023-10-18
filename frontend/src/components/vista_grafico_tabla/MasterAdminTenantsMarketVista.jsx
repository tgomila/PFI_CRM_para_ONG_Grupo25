import AbstractVista from "./AbstractVista";
import { TablaMasterTenantAdminMarket } from "./tables/TablaMasterTenantAdminMarket";

//Se usa en Master Admin
const MasterAdminTenantsMarketVista = AbstractVista(TablaMasterTenantAdminMarket, null, null, "Marketplace de clientes");

export default MasterAdminTenantsMarketVista;
