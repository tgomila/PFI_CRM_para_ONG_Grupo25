import React, { useState, useEffect } from "react";
import MasterTenantAdminService from "../../../services/MasterTenantAdminService";
import { TablaGenerica } from "./Tabla_Generica";
import { RenderFotoPerfilForTablaTenant, } from "./Tabla_Variables";
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
//import moment from "moment";
import { format } from 'date-fns';

import { RenderMostrarIntegrantesGenericRow, RenderMostrarItemFacturaRow } from "./Tabla_Mostrar_Integrantes_Modal";
import { FaTrashAlt, FaRegEye, FaRegEdit, FaUser, FaArrowRight, FaCloudscale } from "react-icons/fa";

import { useNavigate } from "react-router-dom";

const TablaMasterTenantAdminMarket = ({dataIn}) => {

  const [data, setData] = useState(undefined);

  let navigate = useNavigate();

  useEffect(() => {
    if (dataIn) {
      setData(dataIn);
      console.log("dataIn");
      console.log(dataIn);
    } else{
      MasterTenantAdminService.getAllModulosMarket().then((res) => {
        setData(res.data);
        console.log("res");
        console.log(res);
      });
    }
  }, []);

  const columnsTenantsMarket = [
    {
      Header: "ID",
      accessor: "tenantId",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre de la ONG",
      accessor: "tenantName",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Foto",
      Cell: ({ row }) => RenderFotoPerfilForTablaTenant(row.original?.tenantDbName, row.original?.tenantName),
    },
    {
      Header: "Módulo",
      accessor: "moduloName",
      Filter: SelectColumnFilter,
      filter: 'equals',
      type: "string",
      Cell: ({ value, row }) => {
        return (
          <div>
            <div style={{ display: "none" }}></div>
            {row.original?.moduloName ? row.original?.moduloName : value}
          </div>
        );
      },
    },
    // {
    //   Header: "Es modulo premium",
    //   accessor: "paidModule",
    //   Cell: ({ row, value }) =>
    //     (value === true ? "✅" : value === false ? "❌" : "")
    // },
    {
      Header: "Prueba de 7 días utilizada",
      accessor: "prueba7DiasUtilizada",
      Cell: ({ row, value }) =>
        (row.original?.paidModule ? (value === true ? "✅" : value === false ? "❌" : "") : "")
    },
    {
      Header: "Fecha inicio de suscripción",
      accessor: "fechaInicioSuscripcion",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    {
      Header: "Fecha fin de suscripción",
      accessor: "fechaMaximaSuscripcion",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    {
      Header: "Suscripción activa en sistema",
      accessor: "suscripcionActiva",
      Cell: ({ row, value }) =>
        (row.original?.paidModule ? (value === true ? "✅" : value === false ? "❌" : "") : "")
    },
  ];
  
  return(
    <div>
      {data && (
        <div>
          <div className="row">
            <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create_suma_tiempo")}>
              <span style={{ display: 'flex', alignItems: 'center' }}>
                <FaRegEdit style={{ marginRight: '5px', fontSize: '20px' }}/> Sumar tiempo de suscripción a tenants
              </span>
            </button>
          </div>
          <br></br>
          <TablaGenerica
            columnsIn={columnsTenantsMarket}
            dataIn={data ? data : null}
            //Service={Service}
            //visibilidadInput={"SOLO_VISTA"}
            //nombreTipoDatoParaModuloVisibilidad={"FACTURA"}
            //tipoDatoParaFoto={"factura"}
            el_la={"la"}
            nombreTipoDato={"BD de ONG"}
          />
        </div>
      )}
      
    </div>
  );
}

export {
  TablaMasterTenantAdminMarket,
};
