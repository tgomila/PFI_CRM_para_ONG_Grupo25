import DonacionService from "../../../services/DonacionService";
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
  DateRangeColumnFilter,
} from"./Tabla_Filters";
import { format } from 'date-fns';

const TablaDonacion = ({visibilidadInput, dataIn}) => {

  const columns = [
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
      Filter: DateRangeColumnFilter,
      filter: dateBetweenFilterFn
    },
    {
      Header: "Donante",
      Cell: ({ row }) => RenderFotoIntegranteRow(row, row.original?.donante, "contacto"),
    },
    {
      Header: "Tipo de donación",
      accessor: "tipoDonacion",
      Filter: SelectColumnFilter,
      filter: 'includes',
      type: "string",
      Cell: ({ value }) => {
        if(value === "INSUMO") {
          return "Insumo";
        } else if(value === "DINERO") {
          return "Dinero";
        } else if(value === "SERVICIO") {
          return "Servicio";
        } else if(value === "OTRO") {
          return "Otro";
        }//else...
        const formattedValue = value
          .toLowerCase()
          .replace(/_/g, " ")
          .replace(/\b\w/g, (match) => match.toUpperCase());
  
        return formattedValue;
      },
    },
    {
      Header: "Descripción",
      accessor: "descripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Valor aproximado de la donación",
      accessor: "valorAproximadoDeLaDonacion",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
      Cell: ({ value }) => (
        <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
      ),
    },
  ];
  
  return(
    <div>
      <TablaGenerica
        columnsIn={columns}
        //dataIn={dataIn ? dataIn : null}
        Service={DonacionService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"DONACION"}
        //tipoDatoParaFoto={"donacion"}
        el_la={"la"}
        nombreTipoDato={"donación"}
      />
    </div>
  );
}

export default TablaDonacion;
