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

//DOCUMENTACION ICONOS
// https://react-icons.github.io/react-icons/icons?name=fa
const Sidebar = ({ children }) => {
    const [isOpen, setIsOpen] = useState(false);
    const toggle = () => setIsOpen(!isOpen);
    const [modulos, setModulos] = useState(false);

    const user = AuthService.getCurrentUser();

    let mockSERVICIODEMODULOS = ["Persona", "Beneficiario","Empleado","Colaborador","Consejo Adhonorem","Persona Juridica","Profesional","Donacion","Factura","Users","Marketplace"];


    let menuItem = [];
    useEffect(() => {
        // Fetch data
        // Update the document title using the browser API
    
        //Lista de modulos a mostrar
        modulosService.getAll().then((res) => {
          menuItem = res.data;



        });
    
    
      }, []);
    if (user) {



           
     

            menuItem = mockSERVICIODEMODULOS.map((item) => {
                //debugger;
                switch (item) {
                    case 'Persona':
                        return {
                            path: "/personafisica",
                            name: "Persona",
                            icon: <GoPerson />
                        }
                        break;

                    case 'Beneficiario':
                        return {
                            path: "/beneficiario",
                            name: "Beneficiario",
                            icon: <GoRocket />
                        }
                        break;

                    case 'Empleado':
                        return {
                            path: "/empleado",
                            name: "Empleado",
                            icon: <GoBriefcase />
                        }
                        break;

                    case 'Colaborador':
                        return {
                            path: "/colaborador",
                            name: "Colaborador",
                            icon: <GoOrganization />
                        }
                        break;

                    case 'Consejo Adhonorem':
                        return {
                            path: "/consejoadhonorem",
                            name: "Consejo Adhonorem",
                            icon: <GoNote />
                        }
                        break;


                    case 'Persona Juridica':
                        return {
                            path: "/personajuridica",
                            name: "Persona Juridica",
                            icon: <FaBuilding />
                        }
                        break;


                    case 'Profesional':
                        return {
                            path: "/profesional",
                            name: "Profesional",
                            icon: <FaUserCheck />
                        }
                        break;


                    case 'Donacion':
                        return {
                            path: "/donacion",
                            name: "Donacion",
                            icon: <FaDonate />
                        }
                        break;

                    case 'Factura':
                        return {
                            path: "/factura",
                            name: "Factura",
                            icon: <FaFileInvoiceDollar />
                        }
                        break;


                    case 'Users':
                        return {
                            path: "/users",
                            name: "Users",
                            icon: <FaUsersCog />
                        }
                        break;

                        case 'Marketplace':
                            return {
                                path: "/marketplace",
                                name: "Marketplace",
                                icon: <MdLocalGroceryStore />
                            }
                            break;

                }

            }

            );
        

    }
    else {

    }

    return (
        <div className="miSideBar">
            <div style={{ width: isOpen ? "200px" : "50px" }} className="sidebar">
                <div className="top_section">
                    <h1 style={{ display: isOpen ? "block" : "none" }} className="logo">Logo</h1>
                    <div style={{ marginLeft: isOpen ? "50px" : "0px" }} className="bars">
                        <FaBars onClick={toggle} />
                    </div>
                </div>
                {
                    menuItem.map((item, index) => (
                        <NavLink to={item.path} key={index} className="link" activeclassName="active">
                            <div className="icon">{item.icon}</div>
                            <div style={{ display: isOpen ? "block" : "none" }} className="link_text">{item.name}</div>
                        </NavLink>
                    ))
                }
            </div>
            <main>{children}</main>
        </div>
    );
};

export default Sidebar;