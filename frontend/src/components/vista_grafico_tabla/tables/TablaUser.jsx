import React, { useMemo, useState, useEffect, useRef } from "react";
import UserService from "../../../services/UserService";
import AuthService from "../../../services/auth.service";
import { TablaGenerica } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn } from "./Tabla_Variables";
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonVer,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";
import { Button, OverlayTrigger, Tooltip } from "react-bootstrap";
import { FaTrashAlt, FaRegEye, FaRegEdit, FaUser, FaArrowRight, FaCloudscale, FaUserLock } from "react-icons/fa";

import { useNavigate } from "react-router-dom";

const TablaUser = ({visibilidadInput}) => {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const user = AuthService.getCurrentUser();

  let navigate = useNavigate();

  const columns = useMemo(() => {
    let baseColumns = [
      {
        Header: "ID",
        accessor: "id",
        type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
      },
      columnIntegranteConFotoColumn("Persona asociada", "contacto", "contacto"),
      {
        Header: "Nombre",
        accessor: "name",
        filter: 'fuzzyText',
        type: "string",
      },
      {
        Header: "Nombre de usuario",
        accessor: "username",
        filter: 'fuzzyText',
        type: "string",
      },
      {
        Header: "Email",
        accessor: "email",
        filter: 'fuzzyText',
        type: "string",
      },
      {
        Header: "Rol del usuario",
        accessor: "roleMasValuado",
        filter: 'fuzzyText',
        type: "string",
      },
    ];
    console.log("visibilidad: " + visibilidadInput);
    if(user.roles.includes("ROLE_ADMIN") && user?.dbName !== "MasterTenant") {
      baseColumns.push(
        {
          Header: 'Simular usuario',
          Cell: ({ row }) => {
            return RenderBotonSimularUser(row.original, row.original?.name);
          },
        },
      );
    }
    if ((visibilidadInput === "SOLO_VISTA" || visibilidadInput === "EDITAR")) {
      baseColumns.push(
        {
          Header: 'Ver',
          Cell: ({ row }) => {
            const { nombre, apellido, nombreDescripcion, descripcion } = row.original || {};
            let nombreCompleto = '';//Vacío (solo mostrar "desea borar id: 1" en vez de nombre)
            if (nombre && apellido) {//Beneficiarios, Empleados, etc
              nombreCompleto = `${nombre} ${apellido}`;
            } else if (nombre) {//Una persona sin apellido
              nombreCompleto = nombre;
            } else if (apellido) {//Una persona sin nombre
              nombreCompleto = apellido;
            } else if (nombreDescripcion) {//Contacto
              nombreCompleto = nombreDescripcion;
            } else if (descripcion) {//Facturas, insumos, etc
              nombreCompleto = descripcion;
            }
            return RenderBotonVer(row.original?.id, nombreCompleto);
          },
        }
      );
    }
    if (visibilidadInput === "EDITAR") {
      baseColumns.push(
        {
          Header: 'Editar',
          Cell: ({ row }) => RenderBotonEditar(row.original?.id),
        },
        {
          Header: 'Borrar',
          Cell: ({ row }) => {
            console.log("row")
            console.log(row)
            if(row.original?.username !== 'admin') {
              console.log("Aqui hay username: " + row.original?.username)
              return RenderBotonBorrar(row.original?.id, row.original?.name, UserService);
            }
            else if(row.original?.username === 'admin') {
              return(
                <OverlayTrigger
                  placement="top"
                  overlay={
                    <Tooltip id="tooltip-top">
                      No se permite eliminar a usuario "admin"
                    </Tooltip>
                  }
                >
                  <span role="img" aria-label="icon" style={{ cursor: "pointer", fontSize: "1.4em" }}>
                    <FaUserLock/>
                  </span>
                </OverlayTrigger>
              )
            } else {
              return(<div/>)
            }
          },
        }
      );
    }

  return baseColumns;
  }, [visibilidadInput]);

  const RenderBotonSimularUser = (user, nombreAVer) => {
    const navigate = useNavigate();
    const userName = user?.username;
    const roleMasValuado = user?.roleMasValuado;
    if(!userName)
      return(<div/>);//En caso de que se agrupe la tabla, no mostrar
    return (
      <div>
        {!user?.roles.includes('ROLE_ADMIN') ? (
          <div>
            <OverlayTrigger
              placement="top"
              overlay={
                <Tooltip id="tooltip-top">
                  Simular a {nombreAVer && nombreAVer + ', '}nombre de usuario: {userName} {roleMasValuado && ", rol: " + roleMasValuado}
                </Tooltip>
              }
            >
              <Button
                className="buttonAnimadoVerde"
                onClick={() => handleLoginUserAsAdmin(userName)}
              >
                {" "}
                <FaArrowRight/><FaUser/>
              </Button>
            </OverlayTrigger>
          </div>
        ) : (
          <OverlayTrigger
            placement="top"
            overlay={
              <Tooltip id="tooltip-top">
                No se permite simular usuario con rol administrador
              </Tooltip>
            }
          >
            <span role="img" aria-label="icon" style={{ cursor: "pointer", fontSize: "1.4em" }}>
              <FaUserLock/>
            </span>
          </OverlayTrigger>
        )}
      </div>
    );
  };

  const handleLoginUserAsAdmin = (userName) => {

    if(!userName) {
      return;
    }

    setMessage("");
    setLoading(true);

    AuthService.adminSimularUserLogin(userName).then(
      () => {
        navigate("/");
        window.scrollTo({ top: 0, behavior: "smooth" });
        window.location.reload();
      },
      (error) => {
        console.log("AAA")
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();

        setLoading(false);
        setMessage(resMessage);
      }
    );
  };
  
  return(//{columnsIn, data, visibilidad, Service, el_la, nombreTipoDato}) => {
    <div>
      <TablaGenerica
        columnsIn={columns}
        dataIn={null}
        Service={UserService}
        //visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"USERS"}
        el_la={"el"}
        nombreTipoDato={"usuario"}
      />
    </div>
  );
}

export default TablaUser;
