import React, { useState } from 'react';
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
}from "react-icons/ai";

import {
    GoPerson,
    GoBriefcase,
    GoRocket,
    GoOrganization,
    GoNote
}from "react-icons/go";

import { NavLink } from 'react-router-dom';

//DOCUMENTACION ICONOS
// https://react-icons.github.io/react-icons/icons?name=fa
const Sidebar = ({children}) => {
    const[isOpen ,setIsOpen] = useState(false);
    const toggle = () => setIsOpen (!isOpen);


    const user = AuthService.getCurrentUser();

    let menuItem =[];

    if(user){

    if (user.roles.includes("ROLE_ADMIN")) {
        menuItem =[
        
        {
            path:"/personafisica",
            name:"Persona",
            icon:<GoPerson/>
        },
        {
            path:"/beneficiario",
            name:"Beneficiario",
            icon:<GoRocket/>
        },
        {
            path:"/empleado",
            name:"Empleado",
            icon:<GoBriefcase/>
        },
        {
            path:"/colaborador",
            name:"Colaborador",
            icon:<GoOrganization/>
        },
        {
            path:"/consejoadhonorem",
            name:"Consejo Adhonorem",
            icon:<GoNote/>
        },
        {
            path:"/personajuridica",
            name:"Persona Juridica",
            icon:<FaBuilding/>
        },
        {
            path:"/profesional",
            name:"Profesional",
            icon:<FaUserCheck/>
        },
        {
            path:"/donacion",
            name:"Donacion",
            icon:<FaDonate/>
        },
        {
            path:"/factura",
            name:"Factura",
            icon:<FaFileInvoiceDollar/>
        },
        {
            path:"/users",
            name:"Users",
            icon:<FaUsersCog/>
        }
    ];

    const aux = {
        path:"/contacto",
        name:"Contacto",
        icon:<AiFillContacts/>
    };
    menuItem.push(aux);

    }
    else if(user.roles.includes("ROLE_PROFESIONAL")){
        menuItem =[
        {
            path:"/contacto",
            name:"Contacto",
            icon:<AiFillContacts/>
        },
        {
            path:"/personafisica",
            name:"Persona",
            icon:<GoPerson/>
        },
            {
                path:"/beneficiario",
                name:"Beneficiario",
                icon:<GoRocket/>
            },
            {
                path:"/empleado",
                name:"Empleado",
                icon:<GoBriefcase/>
            },
            {
                path:"/colaborador",
                name:"Colaborador",
                icon:<GoOrganization/>
            },
            {
                path:"/consejoadhonorem",
                name:"Consejo Adhonorem",
                icon:<GoNote/>
            },
            {
                path:"/personajuridica",
                name:"Persona Juridica",
                icon:<FaBuilding/>
            },
            {
                path:"/profesional",
                name:"Profesional",
                icon:<FaUserCheck/>
            },
            {
                path:"/donacion",
                name:"Donacion",
                icon:<FaDonate/>
            },
            {
                path:"/factura",
                name:"Factura",
                icon:<FaFileInvoiceDollar/>
            }
        ]
    }
    else if(user.roles.includes("ROLE_EMPLOYEE")){
        menuItem =[
        {
            path:"/contacto",
            name:"Contacto",
            icon:<AiFillContacts/>
        },
        {
            path:"/personafisica",
            name:"Persona",
            icon:<GoPerson/>
        },
            {
                path:"/beneficiario",
                name:"Beneficiario",
                icon:<GoRocket/>
            },
            {
                path:"/empleado",
                name:"Empleado",
                icon:<GoBriefcase/>
            },
            {
                path:"/colaborador",
                name:"Colaborador",
                icon:<GoOrganization/>
            },
            {
                path:"/consejoadhonorem",
                name:"Consejo Adhonorem",
                icon:<GoNote/>
            },
            {
                path:"/personajuridica",
                name:"Persona Juridica",
                icon:<FaBuilding/>
            },
            {
                path:"/profesional",
                name:"Profesional",
                icon:<FaUserCheck/>
            }
        ]
    }
    else if(user.roles.includes("ROLE_USER")){
        menuItem =[
        {
            path:"/contacto",
            name:"Contacto",
            icon:<AiFillContacts/>
        },
        {
            path:"/personafisica",
            name:"Persona",
            icon:<GoPerson/>
        },
        {
            path:"/beneficiario",
            name:"Beneficiario",
            icon:<GoRocket/>
        }
    ]
    }
}
else{

}
    
    return (
        <div className="miSideBar">
           <div style={{width: isOpen ? "200px" : "50px"}} className="sidebar">
               <div className="top_section">
                   <h1 style={{display: isOpen ? "block" : "none"}} className="logo">Logo</h1>
                   <div style={{marginLeft: isOpen ? "50px" : "0px"}} className="bars">
                       <FaBars onClick={toggle}/>
                   </div>
               </div>
               {
                   menuItem.map((item, index)=>(
                       <NavLink to={item.path} key={index} className="link" activeclassName="active">
                           <div className="icon">{item.icon}</div>
                           <div style={{display: isOpen ? "block" : "none"}} className="link_text">{item.name}</div>
                       </NavLink>
                   ))
               }
           </div>
           <main>{children}</main>
        </div>
    );
};

export default Sidebar;