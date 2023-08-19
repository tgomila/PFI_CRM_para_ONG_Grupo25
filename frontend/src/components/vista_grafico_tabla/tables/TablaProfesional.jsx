import ProfesionalService from "../../../services/ProfesionalService";
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

const TablaProfesional = ({visibilidadInput}) => {

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
      Header: "Profesión",
      accessor: "profesion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Datos Bancarios",
      accessor: "datosBancarios",
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
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columns}
        Service={ProfesionalService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PROFESIONAL"}
        el_la={"el"}
        nombreTipoDato={"profesional"}
      />
    </div>
  );
}

const columnsProfesional = [
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
    Header: "Profesión",
    accessor: "profesion",
    filter: 'fuzzyText',
    type: "string",
  },
  {
    Header: "Datos Bancarios",
    accessor: "datosBancarios",
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

const columnsProfesionalParaVistaComoIntegrante = columnsProfesional.filter(column => 
  column.Header !== "Datos Bancarios"
  && column.Header !== "Fecha de nacimiento"
  && column.Header !== "Edad"
  && column.Header !== "Cuit"
  && column.Header !== "Domicilio"
  && column.Header !== "Telefono"
);


export {
  TablaProfesional,
  columnsProfesionalParaVistaComoIntegrante,
};
