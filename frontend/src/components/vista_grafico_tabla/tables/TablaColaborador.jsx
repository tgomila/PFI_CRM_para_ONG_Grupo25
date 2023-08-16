import ColaboradorService from "../../../services/ColaboradorService";
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

const TablaColaborador = ({visibilidadInput}) => {

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
      Header: "Área",
      accessor: "area",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Datos bancarios",
      accessor: "datosBancarios",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Fecha de nacimiento",
      accessor: "fechaNacimiento",
      type: "date",
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
        Service={ColaboradorService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"COLABORADOR"}
        el_la={"el"}
        nombreTipoDato={"colaborador"}
      />
    </div>
  );
}

export default TablaColaborador;
