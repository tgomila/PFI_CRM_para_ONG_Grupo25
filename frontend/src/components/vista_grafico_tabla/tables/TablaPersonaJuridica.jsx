import PersonaJuridicaService from "../../../services/PersonaJuridicaService";
import { TablaGenericaPersona } from "./Tabla_Generica";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";

const TablaPersonaJuridica = ({visibilidadInput}) => {

  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Nombre/Descripción",
      accessor: "nombreDescripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Tipo de Persona Jurídica",
      accessor: "tipoPersonaJuridica",
      Filter: SelectColumnFilter,
      filter: 'includes',
      type: "string",
      Cell: ({ value }) => {
        if(value === "INSTITUCION") {
          return "Institución";
        } else if(value === "EMPRESA") {
          return "Empresa";
        } else if(value === "ORGANISMO_DEL_ESTADO") {
          return "Organismo del estado";
        } else if(value === "OSC") {
          return "OSC";
        }//else...
        const formattedValue = value
          .toLowerCase()
          .replace(/_/g, " ")
          .replace(/\b\w/g, (match) => match.toUpperCase());
  
        return formattedValue;
      },
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
      Header: "Interno Telefono",
      accessor: "internoTelefono",
      type: "number",
    },
  ];
  
  return(
    <div>
      <TablaGenericaPersona
        columnsIn={columns}
        Service={PersonaJuridicaService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PERSONAJURIDICA"}
        el_la={"la"}
        nombreTipoDato={"persona jurídica"}
      />
    </div>
  );
}

export default TablaPersonaJuridica;
