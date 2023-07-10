import BeneficiarioService from "../../../services/BeneficiarioService";
import { TablaGenericaPersona } from "./Tabla_Generica";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";

const TablaBeneficiario = ({visibilidadInput}) => {

  const columns = [
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
      Cell: ({ value }) => (value ? "✅" : "❌")
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
  
  return(
    <div>
      <TablaGenericaPersona
        columns={columns}
        Service={BeneficiarioService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"BENEFICIARIO"}
        el_la={"el"}
        nombreTipoDato={"beneficiario"}
      />
    </div>
  );
}

export default TablaBeneficiario;
