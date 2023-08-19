import VoluntarioService from "../../../services/VoluntarioService";
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
import { columnFecha } from "./Tabla_Variables";

const TablaVoluntario = ({visibilidadInput}) => {

  const columns = [
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
    columnFecha("Fecha de nacimiento", "fechaNacimiento"),
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
        Service={VoluntarioService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"VOLUNTARIO"}
        el_la={"el"}
        nombreTipoDato={"voluntario"}
      />
    </div>
  );
}

export default TablaVoluntario;
