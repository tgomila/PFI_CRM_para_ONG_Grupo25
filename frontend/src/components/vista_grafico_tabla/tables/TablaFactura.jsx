import FacturaService from "../../../services/FacturaService";
import { TablaGenerica } from "./Tabla_Generica";
import { RenderFotoIntegranteRow } from "./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
  dateBetweenFilterFn,
  DateHourRangeColumnFilter,
} from"./Tabla_Filters";
//import moment from "moment";
import { format } from 'date-fns';

import { RenderMostrarIntegrantesGenericRow } from "./Tabla_Mostrar_Integrantes_Modal";

const TablaFactura = ({visibilidadInput, dataIn}) => {
  
  return(
    <div>
      <TablaGenerica
        columnsIn={columnsFactura}
        dataIn={dataIn ? dataIn : null}
        Service={FacturaService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"FACTURA"}
        //tipoDatoParaFoto={"factura"}
        el_la={"la"}
        nombreTipoDato={"factura"}
      />
    </div>
  );
}

const columnsFactura = [
  {
    Header: "ID",
    accessor: "id",
    type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
  },
  {
    Header: "Fecha",
    accessor: "fecha",
    Cell: ({ value }) => {
      const formattedDate = format(new Date(value), "yyyy-MM-dd HH:mm:ss");
      return value ? <span>{formattedDate}</span> : <></>;
    },
    Filter: DateHourRangeColumnFilter,
    filter: dateBetweenFilterFn
  },
  {
    Header: "Cliente",
    accessor: "cliente",
    Cell: ({ row }) => RenderFotoIntegranteRow(row, row.original?.cliente, "contacto"),
  },
  {
    Header: "Emisor factura",
    accessor: "emisorFactura",
    Cell: ({ row }) => RenderFotoIntegranteRow(row, row.original?.emisorFactura, "contacto"),
  },
  {
    Header: "Items",
    Cell: ({ row }) => RenderMostrarIntegrantesGenericRow(row, row.original?.itemsFactura, columnsFacturaItems, null, "el", "item", "items"),
  },
];

const columnsFacturaItems = [
  {
    Header: "ID",
    accessor: "id",
    type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
  },
  {
    Header: "Descripción",
    accessor: "descripcion",
    filter: 'fuzzyText',
    type: "string",
  },
  {
    Header: "Unidades",
    accessor: "unidades",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
  },
  {
    Header: "Precio unitario",
    accessor: "precioUnitario",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
    Cell: ({ value }) => (
      <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
    ),
  },
  {
    Header: "Precio",
    accessor: "precio",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
    Cell: ({ value }) => (
      <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
    ),
  },
];

export {
  columnsFactura,
  columnsFacturaItems,
  TablaFactura,
};
