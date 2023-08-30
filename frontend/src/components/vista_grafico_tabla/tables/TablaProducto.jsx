import ProductoService from "../../../services/ProductoService";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn } from "./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";
//import moment from "moment";

const TablaProducto = ({visibilidadInput, dataIn}) => {

  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Tipo",
      accessor: "tipo",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Descripción",
      accessor: "descripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Stock actual",
      accessor: "stockActual",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Precio venta",
      accessor: "precioVenta",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
      Cell: ({ value }) => (
        <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
      ),
    },
    {
      Header: "Cantidad fija de compra",
      accessor: "cantFijaCompra",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Cantidad mínima de stock",
      accessor: "cantMinimaStock",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Fragil",
      accessor: "fragil",
      Cell: ({ row, value }) => //(row.original?.id ? (value ? "✅" : "❌") : "")
        (value === true ? "✅" : value === false ? "❌" : "")
    },
    columnIntegranteConFotoColumn("Proveedor", "proveedor", "contacto"),
  ];
  
  return(
    <div>
      <TablaGenericaConFoto
        columnsIn={columns}
        dataIn={dataIn ? dataIn : null}
        Service={ProductoService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PRODUCTO"}
        tipoDatoParaFoto={"producto"}
        el_la={"el"}
        nombreTipoDato={"producto"}
      />
    </div>
  );
}

export default TablaProducto;
