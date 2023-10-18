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

const TablaMasterTenantAdminVisibilidad = ({dataIn}) => {

  const [data, setData] = useState(undefined);

  useEffect(() => {
    if (dataIn) {
      setData(dataIn);
      console.log("dataIn");
      console.log(dataIn);
    } else{
      MasterTenantAdminService.getAllModulosVisibilidad().then((res) => {
        //Convierto lista con subitems a lista sin subitems
        const visibilidades = [];
        res.data.forEach(tenant => {
          tenant.rolesModulos.forEach(role => {
              role.items.forEach(item => {
                visibilidades.push({
                      tenantDbName: tenant.tenantDbName,
                      tenantName: tenant.tenantName,
                      role: role.role,
                      roleName: role.roleName,
                      moduloEnum: item.moduloEnum,
                      moduloName: item.moduloName,
                      tipoVisibilidad: item.tipoVisibilidad,
                      tipoVisibilidadName: item.tipoVisibilidadName,
                  });
              });
          });
        });
        //Fin conversión
        setData(visibilidades);
        console.log("visibilidades");
        console.log(visibilidades);
      });
    }
  }, []);

  const columnsTenantsVisibilidad = [
    {
      Header: "Nombre de la ONG",
      accessor: "tenantName",
      Filter: SelectColumnFilter,
      filter: 'equals',
      type: "string",
      Cell: ({ value, row }) => {
        return (
          <div>
            <div style={{ display: "none" }}></div>
            {row.original?.tenantName ? row.original?.tenantName : value}
          </div>
        );
      },
    },
    {
      Header: "Foto",
      Cell: ({ row }) => RenderFotoPerfilForTablaTenant(row.original?.tenantDbName, row.original?.tenantName),
    },
    {
      Header: "Rol",
      accessor: "roleName",
      Filter: SelectColumnFilter,
      filter: 'equals',
      type: "string",
      Cell: ({ value, row }) => {
        return (
          <div>
            <div style={{ display: "none" }}></div>
            {row.original?.roleName ? row.original?.roleName : value}
          </div>
        );
      },
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
    {
      Header: "Tipo de visibilidad",
      accessor: "tipoVisibilidadName",
      Filter: SelectColumnFilter,
      filter: 'equals',
      type: "string",
      Cell: ({ value, row }) => {
        return (
          <div>
            <div style={{ display: "none" }}></div>
            {row.original?.tipoVisibilidadName ? row.original?.tipoVisibilidadName : value}
          </div>
        );
      },
    },
  ];
  
  return(
    <div>
      {data && (
        <TablaGenerica
          columnsIn={columnsTenantsVisibilidad}
          dataIn={data ? data : null}
          //Service={Service}
          //visibilidadInput={"SOLO_VISTA"}
          //nombreTipoDatoParaModuloVisibilidad={"FACTURA"}
          //tipoDatoParaFoto={"factura"}
          el_la={"la"}
          nombreTipoDato={"BD de ONG"}
        />
      )}
      
    </div>
  );
}

export {
  TablaMasterTenantAdminVisibilidad,
};
