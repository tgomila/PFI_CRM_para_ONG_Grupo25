import PersonaService from "../../../services/PersonaService";
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
//import moment from "moment";
import { format } from 'date-fns';

const TablaPersona = ({dataIn, visibilidadInput}) => {
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columnsPersona}
        dataIn={dataIn}
        Service={PersonaService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PERSONA"}
        el_la={"la"}
        nombreTipoDato={"persona"}
      />
    </div>
  );
}

const columnsPersona = [
  {
    Header: "ID",
    accessor: "id",
    type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
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
    Header: "Descripción",
    accessor: "nombreDescripcion",
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

export {
  TablaPersona,
  columnsPersona,
};
