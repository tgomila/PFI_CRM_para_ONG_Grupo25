import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import TablasDinamicas from "./components/vista_grafico_tabla/tables/TablasDinamicas";
import TablaActividad from "./components/vista_grafico_tabla/tables/TablaActividad";
import TablaActividadBeneficiario from "./components/vista_grafico_tabla/tables/TablaActividadBeneficiario";

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
import ProductoVista from "./components/vista_grafico_tabla/ProductoVista";

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

import CreateProductoComponent from "./components/CRUD/Create/CreateProductoComponent";

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

import ReadProductoComponent from "./components/CRUD/Read/ReadProductoComponent";

//Updates
import UpdateContactoComponent from "./components/CRUD/Update/UpdatePersonaComponent";
import UpdatePersonaJuridicaComponent from "./components/CRUD/Update/UpdatePersonaJuridicaComponent";
import UpdatePersonaComponent from "./components/CRUD/Update/UpdatePersonaComponent";
import UpdateBeneficiarioComponent from "./components/CRUD/Update/UpdateBeneficiarioComponent";
import UpdateEmpleadoComponent from "./components/CRUD/Update/UpdateEmpleadoComponent";
import UpdateProfesionalComponent from "./components/CRUD/Update/UpdateProfesionalComponent";
import UpdateColaboradorComponent from "./components/CRUD/Update/UpdateColaboradorComponent";
import UpdateConsejoAdHonoremComponent from "./components/CRUD/Update/UpdateConsejoAdHonoremComponent";
import UpdateVoluntarioComponent from "./components/CRUD/Update/UpdateVoluntarioComponent";

import UpdateProductoComponent from "./components/CRUD/Update/UpdateProductoComponent";

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
import Login from "./components/Login";
import Marketplace from "./components/marketplace/Marketplace";
import RealizarPagoComponent from "./components/marketplace/RealizarPagoComponent";
import BoardAdmin from "./components/BoardAdmin";
import EventBus from "./common/EventBus";
import Register from "./components/Register";
import Profile from "./components/Profile";

import { useTable } from "react-table";
import Bienvenido from "./components/Bienvenido";

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
  //const [menuItemService, setMenuitem] = useState([]);
  //useEffect(() => {
      // Fetch data
      // Update the document title using the browser API
  
      //Lista de modulos a mostrar
      //modulosService.getModulos().then((res) => {
      //    setMenuitem(res.data);
      //});

      //Este tiene error
      //let modulos = modulosService.getModulos();
      //if(isPromise(modulos)) {
      //  console.log("Es un promise:");
      //  console.log(modulos);
      //  
      //  modulos.then((res) => {
      //      setMenuitem(res.data);
      //  });
      //}
      //else {//Es un object
      //  console.log("Es un object:");
      //  console.log(modulos);
      //  
      //  modulos.map((res) => {
      //    setMenuitem(res.data);
      //  });
      //}
      
  //}, []);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (user) {
      setCurrentUser(user);
      //setShowModeratorBoard(user.roles.includes("ROLE_MODERATOR"));
      //setShowAdminBoard(user.roles.includes("ROLE_ADMIN"));
    }

    EventBus.on("logout", () => {
      logOut();
    });

    return () => {
      EventBus.remove("logout");
    };
  }, []);

  const logOut = () => {
    AuthService.logout();
    setShowModeratorBoard(false);
    setShowAdminBoard(false);
    setCurrentUser(undefined);
  };


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
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li>
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
            
            <Route path="/users" element={<UserVista />} />
            <Route path="/actividad" element={<TablaActividad redireccionamiento='actividad' />} />
            <Route path="/tablaActividadBeneficiario" element={<TablaActividadBeneficiario redireccionamiento='tablaActividadBeneficiario' />} />
            <Route path="/programadeactividades" element={<TablasDinamicas redireccionamiento='programadeactividades' />} />
            <Route path="/producto" element={<ProductoVista />} />
            <Route path="/donacion" element={<TablasDinamicas redireccionamiento='donacion' />} />
            <Route path="/factura" element={<TablasDinamicas redireccionamiento='factura' />} />
            <Route path="/insumo" element={<TablasDinamicas redireccionamiento='insumo' />} />
            <Route path="/prestamo" element={<TablasDinamicas redireccionamiento='prestamo' />} />
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

            <Route path="/users/create" element={<CreateEmployeeComponent />} />
            <Route path="/actividad/create" element={<CreateEmployeeComponent />} />
            <Route path="/programadeactividades/create" element={<CreateEmployeeComponent />} />
            <Route path="/producto/create" element={<CreateProductoComponent />} />
            <Route path="/donacion/create" element={<CreateEmployeeComponent />} />
            <Route path="/factura/create" element={<CreateEmployeeComponent />} />
            <Route path="/insumo/create" element={<CreateEmployeeComponent />} />
            <Route path="/prestamo/create" element={<CreateEmployeeComponent />} />
            <Route path="/proyecto/create" element={<CreateEmployeeComponent />} />
            <Route path="/chat/create" element={<CreateEmployeeComponent />} />
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

            <Route path="/producto/update" element={<UpdateProductoComponent />} />

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
            
            <Route path="/producto/read" element={<ReadProductoComponent />} />



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
