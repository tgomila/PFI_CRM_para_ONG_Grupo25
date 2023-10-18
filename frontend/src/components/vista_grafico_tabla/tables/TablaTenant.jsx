import React, { useState, useEffect } from "react";
import TenantService from "../../../services/TenantService";
import AuthService from "../../../services/auth.service";
import MasterTenantAdminService from "../../../services/MasterTenantAdminService";
import { TablaGenerica } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn, columnsIntegrantesTenantItems, } from "./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
  dateBetweenFilterFn,
  DateHourRangeColumnFilter,
  emisorFacturaFilterFn,
  CustomTextFilter,
} from"./Tabla_Filters";

import { RenderFotoPerfilForTablaTenant } from "./Tabla_Variables";
import { Button, OverlayTrigger, Tooltip } from "react-bootstrap";
import { FaTrashAlt, FaRegEye, FaRegEdit, FaUser, FaArrowRight, FaCloudscale } from "react-icons/fa";

import { useNavigate } from "react-router-dom";

const TablaTenant = ({dataIn}) => {

  const [data, setData] = useState(undefined);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  let navigate = useNavigate();

  useEffect(() => {
    if (dataIn) {
      setData(dataIn);
      console.log("dataIn");
      console.log(dataIn);
    } else{
      TenantService.getAllWithImage().then((res) => {
        setData(res);
        console.log("res");
        console.log(res);
      });
    }
  }, []);

  const RenderBotonSimularTenantAdmin = (tenantClientId, nombreAVer) => {
    const navigate = useNavigate();
    if(!tenantClientId)
      return(<div/>);//En caso de que se agrupe la tabla, no mostrar
    return (
      <OverlayTrigger
        placement="top"
        overlay={
          <Tooltip id="tooltip-top">
            Simular {nombreAVer && 'a administrador de ' + nombreAVer + ', '}Tenant ID: {tenantClientId}
          </Tooltip>
        }
      >
        <Button
          className="buttonAnimadoVerde"
          onClick={() => handleLoginTenantAdmin(tenantClientId)}
        >
          {" "}
          <FaArrowRight/><FaUser/>
        </Button>
      </OverlayTrigger>
    );
  };

  const handleLoginTenantAdmin = (tenantOrClientId) => {

    if(!tenantOrClientId) {
      return;
    }

    setMessage("");
    setLoading(true);

    AuthService.MasterTenantAdminlogin(tenantOrClientId).then(
      () => {
        navigate("/users");
        window.scrollTo({ top: 0, behavior: "smooth" });
        window.location.reload();
      },
      (error) => {
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



  const columnsTenant = [
    {
      Header: "ID",
      accessor: "tenantClientId",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Foto",
      //Cell: ({ row }) => RenderFotoPerfilForTablaTenant(row.original?.imagen, row.original?.tenantName),
      Cell: ({ row }) => RenderFotoPerfilForTablaTenant(row.original?.dbName, row.original?.tenantName),
    },
    {
      Header: "Nombre de base de datos",
      accessor: "dbName",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Nombre de la ONG",
      accessor: "tenantName",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Número de teléfono",
      accessor: "tenantPhoneNumber",
    },
    {
      Header: 'Simular administrador',
      Cell: ({ row }) => {
        return RenderBotonSimularTenantAdmin(row.original?.tenantClientId, row.original?.tenantName);
      },
    },
  ];
  
  return(
    
    <div>
      {data && (
        <TablaGenerica
          columnsIn={columnsTenant}
          dataIn={data}
          //Service={TenantService}
          visibilidadInput={""}
          //nombreTipoDatoParaModuloVisibilidad={"TENANT"}
          //tipoDatoParaFoto={"tenant"}
          el_la={"el"}
          nombreTipoDato={"tenant"}
        />
      )}
    </div>
  );
}

export {
  TablaTenant,
};
