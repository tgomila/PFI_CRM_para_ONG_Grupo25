import React, { useState, useEffect } from "react";
import AuthService from "../services/auth.service";
import {
    FaBars,
    FaHandHoldingMedical,
    FaBuilding,
    FaUserCheck,
    FaUsersCog,
    FaRegCalendarAlt,
    FaClipboardList,
    FaOpencart,
    FaFileInvoiceDollar,
    FaDonate,
    FaTruckLoading,
    FaShareAltSquare,
    FaPalfed,
    FaComments,
    FaRegEye,
    FaUsers,
} from "react-icons/fa";

import {
    AiFillContacts,
} from "react-icons/ai";

import {
    GoPerson,
    GoRocket,
    GoBriefcase,
    GoOrganization,
    GoNote
} from "react-icons/go";


import {
    MdLocalGroceryStore
} from "react-icons/md";

import { NavLink } from 'react-router-dom';

import modulosService from '../services/modulosService';
import { Modal, Button, OverlayTrigger, Tooltip, Image } from "react-bootstrap";
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
    //const [menuItem, setMenuItem] = useState(undefined);
    const [urlLogo, setUrlLogo] = useState(undefined);
    const [tenantName, setTenantName] = useState("");
    const [menuItemService, setMenuItemService] = useState([]);
    const [showModal, setShowModal] = useState(false);
    useEffect(() => {
        // Fetch data
        // Update the document title using the browser API

        console.log("user");
        console.log(user);

        if(user?.dbName === "MasterTenant") {
            let modulos = [
                {
                    order: 1,
                    moduloEnum: "MARKETPLACE",
                    name: 'Marketplace',
                    path: "/master_tenant/market",
                    iconName: "FaClipboardList",
                    tipoVisibilidad: "",
                    priceOneMonth: 0.0,
                    priceOneYear: 0.0,
                },
                {
                    order: 2,
                    moduloEnum: "VISIBILIDAD",
                    name: 'Visibilidad',
                    path: "/master_tenant/visibilidad",
                    iconName: "FaClipboardList",
                    tipoVisibilidad: "",
                    priceOneMonth: 0.0,
                    priceOneYear: 0.0,
                },
                {
                    order: 3,
                    moduloEnum: "SIMULADOR_USUARIOS",
                    name: 'Simular usuario',
                    path: "/master_tenant/tenant",
                    iconName: "FaClipboardList",
                    tipoVisibilidad: "",
                    priceOneMonth: 0.0,
                    priceOneYear: 0.0,
                },
            ];
            setMenuItemService(modulos);
        }
        else if (user) {
    
            //Lista de modulos a mostrar
            let modulos = modulosService.getModulos();
            modulos.then((res) => {
                let menuItemConNoVista = res.data;
                //Si existe actividad, agregar programa de actividades
                const actividadIndex = menuItemConNoVista.findIndex(item => item.name === 'Actividad');
                if(actividadIndex !== -1) {
                    let commonItemsAux = {
                        order: 11,
                        moduloEnum: "PROGRAMA_DE_ACTIVIDADES",
                        name: 'Programa de Actividades',
                        path: "/programadeactividades",
                        iconName: "FaClipboardList",
                        tipoVisibilidad: menuItemConNoVista[actividadIndex].tipoVisibilidad,
                        priceOneMonth: 1.0,
                        priceOneYear: 10.0,
                    };
                    menuItemConNoVista.push(commonItemsAux);
                }
                const filteredMenuItems = menuItemConNoVista.filter(item => item.tipoVisibilidad !== "NO_VISTA" && item.tipoVisibilidad !== "SIN_SUSCRIPCION");
                filteredMenuItems.sort((a, b) => {
                    if (a.order !== b.order) {
                        return a.order - b.order;
                    }
                    return a.name.localeCompare(b.name);
                });
                setMenuItemService(filteredMenuItems);
            });
            setTenantName(user.tenantName);
            
        } else {
            setTenantName("No se ha iniciado sesión");
        }
        setUrlLogo(AuthService.getTenantLogoURL());
        
    }, []);
    
    if (user && user?.dbName !== "MasterTenant") {
        
        //menuItem = mockSERVICIODEMODULOS.map((item) => {
        let menuItemAux = menuItemService.map((item) => {
            //debugger;
            let commonItems = {
                order: item.order,
                path: item.path,
                name: item.name,
                tipoVisibilidad: item.tipoVisibilidad,
                icon: <FaRegEye />
            };
            switch (item.name) {
                case 'Contacto':
                    commonItems['icon'] = <AiFillContacts />;break;
                
                case 'Persona':
                    commonItems['icon'] = <GoPerson />;break;

                case 'Beneficiario':
                    commonItems['icon'] = <GoRocket />;break;

                case 'Voluntario':
                    commonItems['icon'] = <FaHandHoldingMedical />;break;

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

                case 'Users':
                    commonItems['icon'] = <FaUsersCog />;break;

                case 'Actividad':
                    commonItems['icon'] = <FaRegCalendarAlt />;break;

                case 'Programa de Actividades':
                    commonItems['icon'] = <FaClipboardList />;break;

                case 'Producto':
                    commonItems['icon'] = <FaOpencart />;break;

                case 'Factura':
                    commonItems['icon'] = <FaFileInvoiceDollar />;break;

                case 'Donacion':
                    commonItems['icon'] = <FaDonate />;break;

                case 'Insumo':
                    commonItems['icon'] = <FaTruckLoading />;break;

                case 'Prestamo':
                    commonItems['icon'] = <FaShareAltSquare />;break;

                case 'Proyecto':
                    commonItems['icon'] = <FaPalfed />;break;

                case 'Chat':
                    commonItems['icon'] = <FaComments />;break;

                case 'Marketplace':
                    commonItems['icon'] = <MdLocalGroceryStore />;break;

                //case 'Contacto estático':
                //    commonItems['icon'] = <MdLocalGroceryStore />;break;
            }
            return commonItems;

        });
        //Quito chat de la vista, esto es por su había un chat propio
        // menuItemAux = menuItemAux.filter(item => item.name !== 'Chat');
        

        //Agregado extra de item para pruebas

        if(user.roles.includes("ROLE_ADMIN") && user?.dbName !== "MasterTenant") {
            let commonItemsAux = {
                order: 21,
                path: "/modulo_visibilidad",
                name: "Modulo visibilidad",
                tipoVisibilidad: "EDITAR",
                icon: <FaRegEye />
            };
            menuItemAux.push(commonItemsAux);
        }
        //setMenuItem(menuItemAux);
        menuItem = menuItemAux;
    }
    if (user && user?.dbName === "MasterTenant") {
        let menuItemAux = menuItemService.map((item) => {
            //debugger;
            let commonItems = {
                order: item.order,
                path: item.path,
                name: item.name,
                tipoVisibilidad: item.tipoVisibilidad,
                icon: <FaRegEye />
            };
            switch (item.name) {
                case 'Marketplace':
                    commonItems['icon'] = <MdLocalGroceryStore />;break;
                case 'Visibilidad':
                    commonItems['icon'] = <FaRegEye />;break;
                case 'Simular usuario':
                    commonItems['icon'] = <FaUsers />;break;
            }
            return commonItems;
        });
        menuItem = menuItemAux;
    }

    return (
        <div className="miSideBar">
            <div style={{ width: isOpen ? "200px" : "50px" }} className="sidebar" onMouseEnter={() => setIsOpen(true)} onMouseLeave={() => setIsOpen(false)}>
                <div className="top_section">
                    {/* display: isOpen ? "block" : "none" */}
                    <div style={{ marginLeft: isOpen ? "0px" : "-50px" }} className="logoDivSideBar">
                        <OverlayTrigger
                            placement="top"
                            overlay={
                            <Tooltip id="tooltip-top">
                                {tenantName}
                            </Tooltip>
                            }
                        >
                            <Image 
                            src={urlLogo} 
                            alt="profile-img"
                            className="logoSideBar"
                            onClick={() => setShowModal(true)}
                            />
                        </OverlayTrigger>

                        <Modal show={showModal} onHide={() => setShowModal(false)}>
                            <Modal.Header>
                                <Modal.Title>{tenantName}</Modal.Title>
                                    <button className="close" onClick={() => setShowModal(false)}>
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                            </Modal.Header>
                            <Modal.Body>
                                <img src={urlLogo} alt="Logo de ONG" className="contacto-img-modal" />
                            </Modal.Body>
                            <Modal.Footer>
                            <Button variant="secondary" onClick={() => setShowModal(false)}>
                                Cerrar
                            </Button>
                            </Modal.Footer>
                        </Modal>
                    </div>
                    {/*<h1 style={{ display: isOpen ? "block" : "none" }} className="logoText">Logo</h1>*/}
                    <div>
                        {/* display: isOpen ? "block" : "none" */}
                        <h1 style={{ marginLeft: isOpen ? "0px" : "-95px"}} className="logoDoubleText">Menu</h1>
                    </div>
                    
                    {/*anteriormente era 40px*/}
                    <div style={{ marginLeft: isOpen ? "40px" : "9px" } } className="bars">
                        <FaBars onClick={toggle} />
                    </div>
                </div>
                {/* {console.log("menuItem")}
                {console.log(menuItem)} */}
                {menuItem && (
                    menuItem.map((item, index) => (
                        <NavLink to={item.path} key={index} className="link" activeclassname="active">
                            <div className="icon">{item.icon}</div>
                            {/* <div style={{ display: isOpen ? "block" : "none" }} className="link_text">{item.name}</div> */}
                            {/**isOpen ? item.name: "" */}
                            <div style={{ visibility: isOpen ? "visible" : "hidden", opacity: isOpen ? "1": "0" }} className="link_text">{item.name}</div>
                        </NavLink>
                    ))
                )}
            </div>
            <main>{children}</main>
        </div>
        
    );
};

export default Sidebar;