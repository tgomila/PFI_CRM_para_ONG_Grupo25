import PrestamoService from "../../../services/PrestamoService";
import { TablaGenerica } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn } from "./Tabla_Variables";
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

const TablaPrestamo = ({visibilidadInput, dataIn}) => {

  const columns = [
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
      Header: "Cantidad",
      accessor: "cantidad",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Fecha préstamo inicio",
      accessor: "fechaPrestamoInicio",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    {
      Header: "Fecha préstamo Fin",
      accessor: "fechaPrestamoFin",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    {
      Header: "Ha sido devuelto",
      accessor: "haSidoDevuelto",
      Cell: ({ row, value }) => //(row.original?.id ? (value ? "✅" : "❌") : "")
        (value === true ? "✅" : value === false ? "❌" : "")
    },
    columnIntegranteConFotoColumn("Prestamista", "prestamista", "contacto"),
    columnIntegranteConFotoColumn("Prestatario", "prestatario", "contacto"),
  ];
  
  return(
    <div>
      <TablaGenerica
        columnsIn={columns}
        dataIn={dataIn ? dataIn : null}
        Service={PrestamoService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PRESTAMO"}
        //tipoDatoParaFoto={"prestamo"}
        el_la={"el"}
        nombreTipoDato={"prestamo"}
      />
    </div>
  );
}

export default TablaPrestamo;
