import React, { useState } from 'react';
import AuthService from "../services/auth.service";
import {
    FaBiohazard,
    FaBars,
    FaReact,
    FaRegChartBar,
    FaCommentAlt,
    FaShoppingBag,
    FaThList
}from "react-icons/fa";
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
            path:"/contacto",
            name:"Contacto",
            icon:<FaBiohazard/>
        },
        {
            path:"/personafisica",
            name:"Persona",
            icon:<FaReact/>
        },
        {
            path:"/beneficiario",
            name:"Beneficiario",
            icon:<FaCommentAlt/>
        },
        {
            path:"/empleado",
            name:"Empleado",
            icon:<FaShoppingBag/>
        },
        {
            path:"/colaborador",
            name:"Colaborador",
            icon:<FaShoppingBag/>
        },
        {
            path:"/consejoadhonorem",
            name:"Consejo Adhonorem",
            icon:<FaShoppingBag/>
        },
        {
            path:"/personajuridica",
            name:"Persona Juridica",
            icon:<FaShoppingBag/>
        },
        {
            path:"/profesional",
            name:"Profesional",
            icon:<FaShoppingBag/>
        },
        {
            path:"/donacion",
            name:"Donacion",
            icon:<FaShoppingBag/>
        },
        {
            path:"/factura",
            name:"Factura",
            icon:<FaShoppingBag/>
        },
        {
            path:"/users",
            name:"Users",
            icon:<FaThList/>
        }
    ]
    }
    else if(user.roles.includes("ROLE_MODERATOR")){
        menuItem =[
            {
                path:"/contacto",
                name:"Contacto",
                icon:<FaBiohazard/>
            },
            {
                path:"/personafisica",
                name:"Persona",
                icon:<FaReact/>
            },
            {
                path:"/beneficiario",
                name:"Beneficiario",
                icon:<FaCommentAlt/>
            },
            {
                path:"/empleado",
                name:"Empleado",
                icon:<FaShoppingBag/>
            },
            {
                path:"/colaborador",
                name:"Colaborador",
                icon:<FaShoppingBag/>
            },
            {
                path:"/consejoadhonorem",
                name:"Consejo Adhonorem",
                icon:<FaShoppingBag/>
            },
            {
                path:"/personajuridica",
                name:"Persona Juridica",
                icon:<FaShoppingBag/>
            },
            {
                path:"/profesional",
                name:"Profesional",
                icon:<FaShoppingBag/>
            },
            {
                path:"/donacion",
                name:"Donacion",
                icon:<FaShoppingBag/>
            },
            {
                path:"/factura",
                name:"Factura",
                icon:<FaShoppingBag/>
            }
        ]
    }
    else if(user.roles.includes("ROLE_EMPLOYEE")){
        menuItem =[
            {
                path:"/contacto",
                name:"Contacto",
                icon:<FaBiohazard/>
            },
            {
                path:"/personafisica",
                name:"Persona",
                icon:<FaReact/>
            },
            {
                path:"/beneficiario",
                name:"Beneficiario",
                icon:<FaCommentAlt/>
            },
            {
                path:"/empleado",
                name:"Empleado",
                icon:<FaShoppingBag/>
            },
            {
                path:"/colaborador",
                name:"Colaborador",
                icon:<FaShoppingBag/>
            },
            {
                path:"/consejoadhonorem",
                name:"Consejo Adhonorem",
                icon:<FaShoppingBag/>
            },
            {
                path:"/personajuridica",
                name:"Persona Juridica",
                icon:<FaShoppingBag/>
            },
            {
                path:"/profesional",
                name:"Profesional",
                icon:<FaShoppingBag/>
            }
        ]
    }
    else if(user.roles.includes("ROLE_USER")){
        menuItem =[
            {
                path:"/contacto",
                name:"Contacto",
                icon:<FaBiohazard/>
            },
            {
                path:"/personafisica",
                name:"Persona",
                icon:<FaReact/>
            },
            {
                path:"/beneficiario",
                name:"Beneficiario",
                icon:<FaCommentAlt/>
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