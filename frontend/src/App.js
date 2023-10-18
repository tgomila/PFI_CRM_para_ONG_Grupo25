import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import TablasDinamicas from "./components/vista_grafico_tabla/tables/TablasDinamicas";
// import TablaActividad from "./components/vista_grafico_tabla/tables/TablaActividad_old_dinamico";
// import TablaActividadBeneficiario from "./components/vista_grafico_tabla/tables/TablaActividadBeneficiario_old_dinamico";

import RealizarPago from "./components/RealizarPago";

import ContactoVista from "./components/vista_grafico_tabla/ContactoVista";
import PersonaVista from "./components/vista_grafico_tabla/PersonaVista";
import BeneficiarioVista from "./components/vista_grafico_tabla/BeneficiarioVista";
import EmpleadoVista from "./components/vista_grafico_tabla/EmpleadoVista";
import ProfesionalVista from "./components/vista_grafico_tabla/ProfesionalVista";
import ColaboradorVista from "./components/vista_grafico_tabla/ColaboradorVista";
import ConsejoAdHonoremVista from "./components/vista_grafico_tabla/ConsejoAdHonoremVista";
import VoluntarioVista from "./components/vista_grafico_tabla/VoluntarioVista";
import PersonaJuridicaVista from "./components/vista_grafico_tabla/PersonaJuridicaVista";






import UserVista from "./components/vista_grafico_tabla/UserVista";
import ActividadVista from "./components/vista_grafico_tabla/ActividadVista";
import ProgramaDeActividadesVista from "./components/vista_grafico_tabla/ProgramaDeActividadesVista";
import ProductoVista from "./components/vista_grafico_tabla/ProductoVista";
import DonacionVista from "./components/vista_grafico_tabla/DonacionVista";
import FacturaVista from "./components/vista_grafico_tabla/FacturaVista";
import InsumoVista from "./components/vista_grafico_tabla/InsumoVista";
import PrestamoVista from "./components/vista_grafico_tabla/PrestamoVista";
import ProyectoVista from "./components/vista_grafico_tabla/ProyectoVista";
import ModuloVisibilidadVista from "./components/vista_grafico_tabla/ModuloVisibilidadVista";
import ChatVista from "./components/vista_grafico_tabla/ChatVista";
import AcercaDe from "./components/vista_grafico_tabla/AcercaDe";

//Master admin:
import TenantVista from "./components/vista_grafico_tabla/TenantVista";
import MasterAdminTenantsMarketVista from "./components/vista_grafico_tabla/MasterAdminTenantsMarketVista";
import MasterAdminTenantsVisibilidadVista from "./components/vista_grafico_tabla/MasterAdminTenantsVisibilidadVista";
import CreateSumarTiempoMarketMasterAdmin from "./components/CRUD/Create/CreateSumarTiempoMarketMasterAdmin";

import FooterComponent from "./components/FooterComponent";

//Creates
import CreateContactoComponent from "./components/CRUD/Create/CreateContactoComponent";
import CreatePersonaComponent from "./components/CRUD/Create/CreatePersonaComponent";
import CreatePersonaJuridicaComponent from "./components/CRUD/Create/CreatePersonaJuridicaComponent";
import CreateBeneficiarioComponent from "./components/CRUD/Create/CreateBeneficiarioComponent";
import CreateEmpleadoComponent from "./components/CRUD/Create/CreateEmpleadoComponent";
import CreateProfesionalComponent from "./components/CRUD/Create/CreateProfesionalComponent";
import CreateColaboradorComponent from "./components/CRUD/Create/CreateColaboradorComponent";
import CreateConsejoAdHonoremComponent from "./components/CRUD/Create/CreateConsejoAdHonoremComponent";
import CreateVoluntarioComponent from "./components/CRUD/Create/CreateVoluntarioComponent";
import CreateEmployeeComponent from "./trash-can/CreateEmployeeComponent";//Testing

import CreateUserComponent from "./components/CRUD/Create/CreateUserComponent";
import CreateActividadComponent from "./components/CRUD/Create/CreateActividadComponent";
import CreateProgramaDeActividadesComponent from "./components/CRUD/Create/CreateProgramaDeActividadesComponent";
import CreateProductoComponent from "./components/CRUD/Create/CreateProductoComponent";
import CreateDonacionComponent from "./components/CRUD/Create/CreateDonacionComponent";
import { CreateFacturaComponent } from "./components/CRUD/Create/CreateFacturaComponent";
import CreateInsumoComponent from "./components/CRUD/Create/CreateInsumoComponent";
import CreatePrestamoComponent from "./components/CRUD/Create/CreatePrestamoComponent";
import CreateProyectoComponent from "./components/CRUD/Create/CreateProyectoComponent";
// import CreateChatComponent from "./components/CRUD/Create/CreateChatComponent";

//Read
import ReadContactoComponent from "./components/CRUD/Read/ReadContactoComponent";
import ReadPersonaComponent from "./components/CRUD/Read/ReadPersonaComponent";
import ReadBeneficiarioComponent from "./components/CRUD/Read/ReadBeneficiarioComponent";
import ReadEmpleadoComponent from "./components/CRUD/Read/ReadEmpleadoComponent";
import ReadProfesionalComponent from "./components/CRUD/Read/ReadProfesionalComponent";
import ReadColaboradorComponent from "./components/CRUD/Read/ReadColaboradorComponent";
import ReadConsejoAdHonoremComponent from "./components/CRUD/Read/ReadConsejoAdHonoremComponent";
import ReadVoluntarioComponent from "./components/CRUD/Read/ReadVoluntarioComponent";
import ReadPersonaJuridicaComponent from "./components/CRUD/Read/ReadPersonaJuridicaComponent";

import ReadUserComponent from "./components/CRUD/Read/ReadUserComponent";
import ReadActividadComponent from "./components/CRUD/Read/ReadActividadComponent";
import ReadProgramaDeActividadesComponent from "./components/CRUD/Read/ReadProgramaDeActividadesComponent";
import ReadProductoComponent from "./components/CRUD/Read/ReadProductoComponent";
import ReadDonacionComponent from "./components/CRUD/Read/ReadDonacionComponent";
import ReadFacturaComponent from "./components/CRUD/Read/ReadFacturaComponent";
import ReadInsumoComponent from "./components/CRUD/Read/ReadInsumoComponent";
import ReadPrestamoComponent from "./components/CRUD/Read/ReadPrestamoComponent";
import ReadProyectoComponent from "./components/CRUD/Read/ReadProyectoComponent";
// import ReadChatComponent from "./components/CRUD/Read/ReadChatComponent";

//Updates
import UpdateContactoComponent from "./components/CRUD/Update/UpdateContactoComponent";
import UpdatePersonaJuridicaComponent from "./components/CRUD/Update/UpdatePersonaJuridicaComponent";
import UpdatePersonaComponent from "./components/CRUD/Update/UpdatePersonaComponent";
import UpdateBeneficiarioComponent from "./components/CRUD/Update/UpdateBeneficiarioComponent";
import UpdateEmpleadoComponent from "./components/CRUD/Update/UpdateEmpleadoComponent";
import UpdateProfesionalComponent from "./components/CRUD/Update/UpdateProfesionalComponent";
import UpdateColaboradorComponent from "./components/CRUD/Update/UpdateColaboradorComponent";
import UpdateConsejoAdHonoremComponent from "./components/CRUD/Update/UpdateConsejoAdHonoremComponent";
import UpdateVoluntarioComponent from "./components/CRUD/Update/UpdateVoluntarioComponent";

import UpdateUserComponent from "./components/CRUD/Update/UpdateUserComponent";
import UpdateActividadComponent from "./components/CRUD/Update/UpdateActividadComponent";
import UpdateProgramaDeActividadesComponent from "./components/CRUD/Update/UpdateProgramaDeActividadesComponent";
import UpdateProductoComponent from "./components/CRUD/Update/UpdateProductoComponent";
import UpdateDonacionComponent from "./components/CRUD/Update/UpdateDonacionComponent";
import UpdateFacturaComponent from "./components/CRUD/Update/UpdateFacturaComponent";
import UpdateInsumoComponent from "./components/CRUD/Update/UpdateInsumoComponent";
import UpdatePrestamoComponent from "./components/CRUD/Update/UpdatePrestamoComponent";
import UpdateProyectoComponent from "./components/CRUD/Update/UpdateProyectoComponent";
// import UpdateChatComponent from "./components/CRUD/Update/UpdateChatComponent";

//Updates testing
import UpdateEmployeeComponent from "./components/UpdateEmployeeComponent";
import ViewEmployeeComponent from "./components/ViewEmployeeComponent";

//test
import Testing from "./components/Testing";


import {
  BrowserRouter,
  Route,
  Routes,
  Navigate,
  NavLink,
  Link,
  Nav,
} from "react-router-dom";
import Error404 from "./components/Error404";
import Sidebar from "./components/Sidebar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import About from "./pages/About.jsx";
import Analytics from "./pages/Analytics.jsx";
import Comment from "./pages/Comment.jsx";
import Product from "./pages/Product.jsx";
import ProductList from "./pages/ProductList.jsx";
import AuthService from "./services/auth.service";
import modulosService from "./services/modulosService";
import Login from "./components/login/Login";
import Marketplace from "./components/marketplace/Marketplace";
import RealizarPagoComponent from "./components/marketplace/RealizarPagoComponent";
import BoardAdmin from "./components/BoardAdmin";
import EventBus from "./common/EventBus";
import Register from "./components/login/Register";
import Profile from "./components/Profile";

import { useTable } from "react-table";
import Bienvenido from "./components/Bienvenido";

import { RenderMostrarContacto } from "./components/vista_grafico_tabla/tables/Tabla_Variables";

import * as constantsURL from "./components/constants/ConstantsURL";
const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;

/*

import { NavigationBar } from './components/NavigationBar';
import { Home } from './Home';
import { About } from './About';
import Sidebar from './components/Sidebar';
*/




const App = () => {





  const [showModeratorBoard, setShowModeratorBoard] = useState(false);
  const [showAdminBoard, setShowAdminBoard] = useState(false);
  const [currentUser, setCurrentUser] = useState(undefined);
  const [masterAdminUser, setMasterAdminUser] = useState(undefined);
  const [masterTenantAdminUser, setMasterTenantUserAdminUser] = useState(undefined);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    AuthService.setLogoTenantTab();
    if (user) {
      setCurrentUser(user);
      //setShowModeratorBoard(user.roles.includes("ROLE_MODERATOR"));
      //setShowAdminBoard(user.roles.includes("ROLE_ADMIN"));
    }
    const masterTenantAdminUserAux = AuthService.getMasterTenantCurrentUser();
    if (masterTenantAdminUserAux) {
      setMasterTenantUserAdminUser(masterTenantAdminUserAux);
    }
    const masterAdminUserAux = AuthService.getAdminCurrentUser();
    if (masterAdminUserAux) {
      setMasterAdminUser(masterAdminUserAux);
    }

    EventBus.on("logout", () => {
      logOut();
    });

    return () => {
      EventBus.remove("logout");
    };
  }, []);

  const logoutUserSimulatorAdmin = () => {
    AuthService.logoutUserSimulatorAdmin();
  };

  const logoutUserSimulatorMasterTenant = () => {
    AuthService.logoutUserSimulatorMasterTenant();
  };

  const logOut = () => {
    AuthService.logout();
    setShowModeratorBoard(false);
    setShowAdminBoard(false);
    setCurrentUser(undefined);
  };

  // const myProfile = () => {
  //   if(!currentUser)
  //     return <></>
  //   return RenderMostrarContacto(currentUser?.persona ? currentUser?.persona : currentUser?.contacto, 'perfil');
  // }


  return (


    <div className="principal">


      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <Link to={"/"} className="navbar-brand">
          COSMOS
        </Link>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/"} className="nav-link">

            </Link>
          </li>

          {showModeratorBoard && (
            <li className="nav-item">
              <Link to={"/mod"} className="nav-link">
                Tabla Moderador
              </Link>
            </li>
          )}

          {showAdminBoard && (
            <li className="nav-item">
              <Link to={"/admin"} className="nav-link">
              Tabla Admin
              </Link>
            </li>
          )}

          {currentUser && (
            <li className="nav-item">
              <Link to={"/"} className="nav-link">
              Bienvenido
              </Link>
            </li>
          )}
        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            {masterAdminUser && (
              <li className="nav-item">
                <a href="/users" className="nav-link" onClick={logoutUserSimulatorAdmin}>
                  Dejar simulación de usuario
                </a>
              </li>
            )}
            {!masterAdminUser && masterTenantAdminUser && (
              <li className="nav-item">
                <a href="/master_tenant/tenant" className="nav-link" onClick={logoutUserSimulatorMasterTenant}>
                  Dejar simulación de usuario
                </a>
              </li>
            )}
            {/* <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li> */}
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={logOut}>
                Cerrar Sesión
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Iniciar Sesión
              </Link>
            </li>

            <li className="nav-item">
              <Link to={"/register"} className="nav-link">
                Registrarse
              </Link>
            </li>
          </div>
        )}
      </nav>

      <div className="sidebarYRoutes">
        <Sidebar style={{ marginLeft: "0px" }}>
          <br/><br/>
          {/**<br></br><br></br> */}
          <Routes>

            {/* Lista dinámica */}
            {/*<Route path="/personafisica" element={<TablasDinamicas redireccionamiento='personafisica' />} />*/}

            {/* Lista estática con gráficos */}
            <Route path="/contacto" element={<ContactoVista/>} />
            <Route path="/personafisica" element={<PersonaVista/>} />
            <Route path="/beneficiario" element={<BeneficiarioVista/>} />
            <Route path="/voluntario" element={<VoluntarioVista/>} />
            <Route path="/empleado" element={<EmpleadoVista/>} />
            <Route path="/colaborador" element={<ColaboradorVista/>} />
            <Route path="/consejoadhonorem" element={<ConsejoAdHonoremVista/>} />
            <Route path="/personajuridica" element={<PersonaJuridicaVista/>} />
            <Route path="/profesional" element={<ProfesionalVista/>} />


            {/* <Route path="/actividad" element={<TablaActividad redireccionamiento='actividad' />} /> */}
            {/* <Route path="/tablaActividadBeneficiario" element={<TablaActividadBeneficiario redireccionamiento='tablaActividadBeneficiario' />} /> */}
            {/* <Route path="/programadeactividades" element={<TablasDinamicas redireccionamiento='programadeactividades' />} /> */}
            {/* <Route path="/factura" element={<TablasDinamicas redireccionamiento='factura' />} /> */}


            <Route path="/users" element={<UserVista />} />
            <Route path="/actividad" element={<ActividadVista />} />
            <Route path="/programadeactividades" element={<ProgramaDeActividadesVista />} />
            <Route path="/producto" element={<ProductoVista />} />
            <Route path="/donacion" element={<DonacionVista />} />
            <Route path="/factura" element={<FacturaVista />} />
            <Route path="/insumo" element={<InsumoVista />} />
            <Route path="/prestamo" element={<PrestamoVista/>} />
            <Route path="/proyecto" element={<ProyectoVista/>} />
            <Route path="/modulo_visibilidad" element={<ModuloVisibilidadVista/>} />
            <Route path="/chat" element={<ChatVista/>} />
            <Route path="/acerca_de" element={<AcercaDe/>} />
            
            <Route path="/master_tenant/tenant" element={<TenantVista/>} />
            <Route path="/master_tenant/market" element={<MasterAdminTenantsMarketVista/>} />
            <Route path="/master_tenant/market/create_suma_tiempo" element={<CreateSumarTiempoMarketMasterAdmin/>} />
            <Route path="/master_tenant/visibilidad" element={<MasterAdminTenantsVisibilidadVista/>} />

            <Route path="/proyecto" element={<TablasDinamicas redireccionamiento='proyecto' />} />
            <Route path="/chat" element={<TablasDinamicas redireccionamiento='chat' />} />
            <Route path="/marketplace" element={<Marketplace  />} />
            <Route path="/marketplace/pagar" element={<RealizarPagoComponent  />} />
            <Route path="/Testing" element={<Testing  />} />



            {/* Create item */}
            <Route path="/contacto/create" element={<CreateContactoComponent />} />
            <Route path="/personafisica/create" element={<CreatePersonaComponent />} />
            <Route path="/beneficiario/create" element={<CreateBeneficiarioComponent />} />
            <Route path="/empleado/create" element={<CreateEmpleadoComponent />} />
            <Route path="/profesional/create" element={<CreateProfesionalComponent />} />
            <Route path="/colaborador/create" element={<CreateColaboradorComponent />} />
            <Route path="/consejoadhonorem/create" element={<CreateConsejoAdHonoremComponent />} />
            <Route path="/voluntario/create" element={<CreateVoluntarioComponent />} />
            <Route path="/personajuridica/create" element={<CreatePersonaJuridicaComponent />} />

            <Route path="/users/create" element={<CreateUserComponent />} />
            <Route path="/actividad/create" element={<CreateActividadComponent />} />
            <Route path="/programadeactividades/create" element={<CreateProgramaDeActividadesComponent />} />
            <Route path="/producto/create" element={<CreateProductoComponent />} />
            <Route path="/donacion/create" element={<CreateDonacionComponent />} />
            <Route path="/factura/create" element={<CreateFacturaComponent />} />
            <Route path="/insumo/create" element={<CreateInsumoComponent />} />
            <Route path="/prestamo/create" element={<CreatePrestamoComponent />} />
            <Route path="/proyecto/create" element={<CreateProyectoComponent />} />
            {/* <Route path="/chat/create" element={<CreateChatComponent />} /> */}
            <Route path="/realizarpago/mes/actividad" element={<RealizarPago   moduloAPagar='mes/ACTIVIDAD' />} />
            <Route path="/realizarpago/año/actividad" element={<RealizarPago  moduloAPagar='ACTIVIDAD' tiempoContratado='anio'/>} />
            <Route path="/realizarpago/mes/factura" element={<RealizarPago moduloAPagar='FACTURA' tiempoContratado='mes'/>} />
            <Route path="/realizarpago/año/factura" element={<RealizarPago moduloAPagar='FACTURA' tiempoContratado='anio'/>} />



            {/* Update item */}
            <Route path="/contacto/update" element={<UpdateContactoComponent />} />
            <Route path="/personafisica/update" element={<UpdatePersonaComponent />} />
            <Route path="/beneficiario/update" element={<UpdateBeneficiarioComponent />} />
            <Route path="/empleado/update" element={<UpdateEmpleadoComponent />} />
            <Route path="/profesional/update" element={<UpdateProfesionalComponent />} />
            <Route path="/colaborador/update" element={<UpdateColaboradorComponent />} />
            <Route path="/consejoadhonorem/update" element={<UpdateConsejoAdHonoremComponent />} />
            <Route path="/voluntario/update" element={<UpdateVoluntarioComponent />} />
            <Route path="/personajuridica/update" element={<UpdatePersonaJuridicaComponent />} />

            <Route path="/users/update" element={<UpdateUserComponent />} />
            <Route path="/actividad/update" element={<UpdateActividadComponent />} />
            <Route path="/programadeactividades/update" element={<UpdateProgramaDeActividadesComponent />} />
            <Route path="/producto/update" element={<UpdateProductoComponent />} />
            <Route path="/donacion/update" element={<UpdateDonacionComponent />} />
            <Route path="/factura/update" element={<UpdateFacturaComponent />} />
            <Route path="/insumo/update" element={<UpdateInsumoComponent />} />
            <Route path="/prestamo/update" element={<UpdatePrestamoComponent />} />
            <Route path="/proyecto/update" element={<UpdateProyectoComponent />} />
            {/* <Route path="/chat/update" element={<UpdateChatComponent />} /> */}

            {/* Read item */}
            <Route path="/contacto/read" element={<ReadContactoComponent />} />
            <Route path="/personafisica/read" element={<ReadPersonaComponent />} />
            <Route path="/beneficiario/read" element={<ReadBeneficiarioComponent />} />
            <Route path="/empleado/read" element={<ReadEmpleadoComponent />} />
            <Route path="/profesional/read" element={<ReadProfesionalComponent />} />
            <Route path="/colaborador/read" element={<ReadColaboradorComponent />} />
            <Route path="/consejoadhonorem/read" element={<ReadConsejoAdHonoremComponent />} />
            <Route path="/voluntario/read" element={<ReadVoluntarioComponent />} />
            <Route path="/personajuridica/read" element={<ReadPersonaJuridicaComponent />} />

            <Route path="/users/read" element={<ReadUserComponent />} />
            <Route path="/actividad/read" element={<ReadActividadComponent />} />
            <Route path="/programadeactividades/read" element={<ReadProgramaDeActividadesComponent />} />
            <Route path="/producto/read" element={<ReadProductoComponent />} />
            <Route path="/donacion/read" element={<ReadDonacionComponent />} />
            <Route path="/factura/read" element={<ReadFacturaComponent />} />
            <Route path="/insumo/read" element={<ReadInsumoComponent />} />
            <Route path="/prestamo/read" element={<ReadPrestamoComponent />} />
            <Route path="/proyecto/read" element={<ReadProyectoComponent />} />
            {/* <Route path="/chat/read" element={<ReadChatComponent />} /> */}



            {/* BARRA DE ARRIBA */}
            <Route path="/" element={<Bienvenido redireccionamiento='beneficiario' />} />
            <Route path="/login" element={<Login />} />
            <Route path="/admin" element={<BoardAdmin />} />
            <Route path="/register" element={<Register />} />
            <Route path="/profile" element={<Profile />} />


            <Route path="*" element={<Error404 />} />

          </Routes>
          <br/><br/>
        </Sidebar>

        <FooterComponent></FooterComponent>
      </div>


    </div>
  );
};

function isPromise(p) {
  if (typeof p === 'object' && typeof p.then === 'function') {
    return true;
  }

  return false;
}

export default App;
