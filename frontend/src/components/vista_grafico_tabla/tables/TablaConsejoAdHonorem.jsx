import ConsejoAdHonoremService from "../../../services/ConsejoAdHonoremService";
import { TablaGenericaPersona } from "./Tabla_Generica";
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

const TablaConsejoAdHonorem = ({visibilidadInput}) => {

  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",
    },
    {
      Header: "Nombre",
      accessor: "nombre",
      type: "string",
    },
    {
      Header: "Apellido",
      accessor: "apellido",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Función",
      accessor: "funcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Fecha de nacimiento",
      accessor: "fechaNacimiento",
      type: "date",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "dd/MM/yyyy");
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateRangeColumnFilter,
      filter: dateBetweenFilterFn,
    },
    {
      Header: "Edad",
      accessor: "edad",
      type: "number",
      Filter: SliderColumnFilter,
      filter: 'equals',
    },
    {
      Header: "Email",
      accessor: "email",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Cuit",
      accessor: "cuit",
      type: "number",
    },
    {
      Header: "Domicilio",
      accessor: "domicilio",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Telefono",
      accessor: "telefono",
      type: "number",
    },
  ];
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columns}
        Service={ConsejoAdHonoremService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"CONSEJOADHONOREM"}
        el_la={"el"}
        nombreTipoDato={"consejo ad honorem"}
      />
    </div>
  );
}

export default TablaConsejoAdHonorem;
