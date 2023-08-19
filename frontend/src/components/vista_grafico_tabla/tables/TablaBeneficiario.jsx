import BeneficiarioService from "../../../services/BeneficiarioService";
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

const TablaBeneficiario = ({visibilidadInput}) => {
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columnsBeneficiario}
        Service={BeneficiarioService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"BENEFICIARIO"}
        el_la={"el"}
        nombreTipoDato={"beneficiario"}
      />
    </div>
  );
}

const columnsBeneficiario = [
  {
    Header: "ID en sistema",
    accessor: "id",
    type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
  },
  {
    Header: "ID en ONG",
    accessor: "idONG",
    type: "number",
  },
  {
    Header: "Legajo",
    accessor: "legajo",
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
    Header: "Se retira solo",
    accessor: "seRetiraSolo",
    Cell: ({ row, value }) => (row.original?.id ? (value ? "✅" : "❌") : "")
  },
  {
    Header: "Cuidados especiales",
    accessor: "cuidadosEspeciales",
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
  {
    Header: "Lugar de nacimiento",
    accessor: "lugarDeNacimiento",
    type: "string",
  },
  {
    Header: "Escuela",
    accessor: "escuela",
    filter: 'fuzzyText',
    type: "string",
  },
  {
    Header: "Grado",
    accessor: "grado",
    filter: 'fuzzyText',
    type: "string",
  },
  {
    Header: "Turno",
    accessor: "turno",
    Filter: SelectColumnFilter,
    filter: 'includes',
    type: "string",
  },
];

export {
  TablaBeneficiario,
  columnsBeneficiario,
};
