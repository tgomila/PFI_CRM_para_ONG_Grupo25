import React, { useState, useEffect } from "react";
import AuthService from "../services/auth.service";
import {
    FaBars,
    FaBuilding,
    FaUserCheck,
    FaDonate,
    FaFileInvoiceDollar,
    FaUsersCog
} from "react-icons/fa";

import {
    AiFillContacts
} from "react-icons/ai";

import {
    GoPerson,
    GoBriefcase,
    GoRocket,
    GoOrganization,
    GoNote
} from "react-icons/go";


import {
    MdLocalGroceryStore
} from "react-icons/md";

import { NavLink } from 'react-router-dom';

import modulosService from '../services/modulosService';
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;
//DOCUMENTACION ICONOS
// https://react-icons.github.io/react-icons/icons?name=fa
const Sidebar = ({ children }) => {
    const [isOpen, setIsOpen] = useState(false);
    const toggle = () => setIsOpen(!isOpen);
    const [modulos, setModulos] = useState(false);

    const user = AuthService.getCurrentUser();

    let mockSERVICIODEMODULOS = ["Persona", "Beneficiario","Empleado","Colaborador","Consejo Adhonorem","Persona Juridica","Profesional","Donacion","Factura","Users","Marketplace"];


    let menuItem = [];
    let tenantLogoName = "";
    const [currentUser, setCurrentUser] = useState(undefined);
    const [urlLogo, setUrlLogo] = useState(undefined);
    const [menuItemService, setMenuitem] = useState([]);
    useEffect(() => {
        // Fetch data
        // Update the document title using the browser API
    
        //Lista de modulos a mostrar
        let modulos = modulosService.getModulos();
        console.log("Modulos: ");
        console.log(modulos);
        modulos.then((res) => {
            setMenuitem(res.data);
        });

        setUrlLogo("http://ssl.gstatic.com/accounts/ui/avatar_2x.png");//Default
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
            setUrlLogo("http://localhost:8080/logo/" + user.dbName + ".png");
            console.log("El urlLogo es: " + urlLogo);
            //setUrlLogo("http://localhost:8080/logo/tenant2.png");
        }
        else {//Default
            setUrlLogo("http://ssl.gstatic.com/accounts/ui/avatar_2x.png");
        }
        
    }, []);
    console.log("Imprimo menuItemService");
    console.log(menuItemService);
    if (user) {
        
        //menuItem = mockSERVICIODEMODULOS.map((item) => {
        menuItem = menuItemService.map((item) => {
            //debugger;
            let commonItems = {
                order: item.order,
                path: item.path,
                name: item.name,
                tipoVisibilidad: item.tipoVisibilidad
            };
            switch (item.name) {
                case 'Contacto':
                    commonItems['icon'] = <AiFillContacts />;
                    break;
                    /*return {
                        order: item.order,
                        path: item.path,
                        name: item.name,
                        tipoVisibilidad: item.tipoVisibilidad,
                        icon: <AiFillContacts />
                    }
                    break;*/
                
                case 'Persona':
                    commonItems['icon'] = <GoPerson />;break;

                case 'Beneficiario':
                    commonItems['icon'] = <GoRocket />;break;

                case 'Empleado':
                    commonItems['icon'] = <GoBriefcase />;break;

                case 'Colaborador':
                    commonItems['icon'] = <GoOrganization />;break;
                    
                case 'Consejo Adhonorem':
                    commonItems['icon'] = <GoNote />;break;

                case 'Persona Juridica':
                    commonItems['icon'] = <FaBuilding />;break;

                case 'Profesional':
                    commonItems['icon'] = <FaUserCheck />;break;

                case 'Donacion':
                    commonItems['icon'] = <FaDonate />;break;

                case 'Factura':
                    commonItems['icon'] = <FaFileInvoiceDollar />;break;

                case 'Users':
                    commonItems['icon'] = <FaUsersCog />;break;

                case 'Marketplace':
                    commonItems['icon'] = <MdLocalGroceryStore />;break;
                }
                return commonItems;

            }

        );
        

    }
    else {

    }

    return (
        <div className="miSideBar">
            <div style={{ width: isOpen ? "200px" : "50px" }} className="sidebar">
                <div className="top_section">
                    {/* display: isOpen ? "block" : "none" */}
                    <div style={{ marginLeft: isOpen ? "0px" : "-50px" }} className="logoDivSideBar">
                        <img 
                            src={urlLogo}
                            alt="profile-img"
                            className="logoSideBar"
                        />
                    </div>
                    {/*<h1 style={{ display: isOpen ? "block" : "none" }} className="logoText">Logo</h1>*/}
                    <div>
                        {/* display: isOpen ? "block" : "none" */}
                        <h1 style={{ marginLeft: isOpen ? "0px" : "-65px"}} className="logoDoubleText">Ship</h1>
                    </div>
                    
                    {/*anteriormente era 40px*/}
                    <div style={{ marginLeft: isOpen ? "40px" : "9px" } } className="bars">
                        <FaBars onClick={toggle} />
                    </div>
                </div>
                {
                    menuItem.map((item, index) => (
                        <NavLink to={item.path} key={index} className="link" activeclassName="active">
                            <div className="icon">{item.icon}</div>
                            {/* <div style={{ display: isOpen ? "block" : "none" }} className="link_text">{item.name}</div> */}
                            {/**isOpen ? item.name: "" */}
                            <div style={{ visibility: isOpen ? "visible" : "hidden", opacity: isOpen ? "1": "0" }} className="link_text">{item.name}</div>
                        </NavLink>
                    ))
                }
            </div>
            <main>{children}</main>
        </div>
        
    );
};

export default Sidebar;