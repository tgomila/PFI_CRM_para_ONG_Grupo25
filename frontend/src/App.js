import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import TablasDinamicas from "./components/TablasDinamicas";
import FooterComponent from "./components/FooterComponent";
import CreateEmployeeComponent from "./components/CreateEmployeeComponent";
import UpdateEmployeeComponent from "./components/UpdateEmployeeComponent";
import ViewEmployeeComponent from "./components/ViewEmployeeComponent";
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
import Login from "./components/Login";
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
          <Routes>
            <Route path="/" element={<Bienvenido redireccionamiento='beneficiario' />} />
            <Route path="/contacto" element={<TablasDinamicas redireccionamiento='contacto' />} />
            <Route path="/personafisica" element={<TablasDinamicas redireccionamiento='personafisica' />} />
            <Route path="/beneficiario" element={<TablasDinamicas redireccionamiento='beneficiario' />} />
            <Route path="/empleado" element={<TablasDinamicas redireccionamiento='empleado' />} />
            <Route path="/colaborador" element={<TablasDinamicas redireccionamiento='colaborador' />} />
            <Route path="/consejoadhonorem" element={<TablasDinamicas redireccionamiento='consejoadhonorem' />} />
            <Route path="/personajuridica" element={<TablasDinamicas redireccionamiento='personajuridica' />} />
            <Route path="/profesional" element={<TablasDinamicas redireccionamiento='profesional' />} />
            <Route path="/donacion" element={<TablasDinamicas redireccionamiento='donacion' />} />
            <Route path="/factura" element={<TablasDinamicas redireccionamiento='factura' />} />
            <Route path="/users" element={<TablasDinamicas redireccionamiento='users' />} />



            {/* Agregar item*/}
            <Route path="/contacto/add" element={<CreateEmployeeComponent />} />
            <Route path="/personafisica/add" element={<CreateEmployeeComponent />} />
            <Route path="/beneficiario/add" element={<CreateEmployeeComponent />} />
            <Route path="/empleado/add" element={<CreateEmployeeComponent />} />
            <Route path="/colaborador/add" element={<CreateEmployeeComponent />} />
            <Route path="/consejoadhonorem/add" element={<CreateEmployeeComponent />} />
            <Route path="/personajuridica/add" element={<CreateEmployeeComponent />} />
            <Route path="/profesional/add" element={<CreateEmployeeComponent />} />
            <Route path="/donacion/add" element={<CreateEmployeeComponent />} />
            <Route path="/factura/add" element={<CreateEmployeeComponent />} />
            <Route path="/users/add" element={<CreateEmployeeComponent />} />
            



            {/* BARRA DE ARRIBA*/}

            <Route path="/login" element={<Login />} />
            <Route path="/admin" element={<BoardAdmin />} />
            <Route path="/register" element={<Register />} />
            <Route path="/profile" element={<Profile />} />

            
            <Route path="*" element={<Error404 />} />
            
          </Routes>
        </Sidebar>

        <FooterComponent></FooterComponent>
      </div>


        </div>
  );
};

export default App;
