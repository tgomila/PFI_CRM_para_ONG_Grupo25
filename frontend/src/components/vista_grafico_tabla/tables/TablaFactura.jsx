import FacturaService from "../../../services/FacturaService";
import { TablaGenerica } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn, columnsIntegrantesFacturaItems, } from "./Tabla_Variables";
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

const TablaFactura = ({visibilidadInput, dataIn}) => {

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
        const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    columnIntegranteConFotoColumn("Cliente", "cliente", "contacto"),
    columnIntegranteConFotoColumn("Emisor factura", "emisorFactura", "contacto"),
    ...columnsIntegrantesFacturaItems({}),
    /*{
      Header: "Items",
      Cell: ({ row }) => RenderMostrarItemFacturaRow({
        integrantesActuales: row.original?.itemsFactura,
        el_la: "el",
        nombreIntegranteSingular: "item",
        nombreIntegrantePlural: "items",
      }),
    },*/
  ];
  
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
  columnsFacturaItems,
  TablaFactura,
};
