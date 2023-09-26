import ProgramaDeActividadesService from "../../../services/ProgramaDeActividadesService";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { RenderMostrarIntegrantesPersonasRow } from "./Tabla_Mostrar_Integrantes_Modal";
import { columnsIntegrantesActividades } from "./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
  dateBetweenFilterFn,
  DateRangeColumnFilter,
  DateHourRangeColumnFilter,
} from"./Tabla_Filters";
import { format } from 'date-fns';

const TablaProgramaDeActividades = ({visibilidadInput, dataIn}) => {
  
  return(
    <div>
      <TablaGenericaConFoto
        columnsIn={columnsProgramaDeActividades()}
        dataIn={dataIn ? dataIn : null}
        Service={ProgramaDeActividadesService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"ACTIVIDAD"}
        tipoDatoParaFoto={"programaDeActividades"}
        el_la={"el"}
        nombreTipoDato={"programa de actividades"}
      />
    </div>
  );
}

const columnsProgramaDeActividades = () => [
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
    Header: "Fecha y hora desde",
    accessor: "fechaDesde",
    Cell: ({ value }) => {
      const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
      return value ? <span>{formattedDate}</span> : <></>;
    },
    Filter: DateHourRangeColumnFilter,
    filter: dateBetweenFilterFn,
  },
  {
    Header: "Fecha y hora hasta",
    accessor: "fechaHasta",
    Cell: ({ value }) => {
      const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");
      return value ? <span>{formattedDate}</span> : <></>;
    },
    Filter: DateHourRangeColumnFilter,
    filter: dateBetweenFilterFn,
  },
  // ...columnsIntegrantesProfesionales({
  //   property: "profesionales",
  //   el_la: "el",
  //   nombreIntegranteSingular: "profesional",
  //   nombreIntegrantePlural: "profesionales"
  // }),
  // ...columnsIntegrantesBeneficiarios({}),

  // ...columnsIntegrantesActividades({}),
  ...columnsIntegrantesActividades({
    property: "actividades",
    el_la: "la",
    nombreIntegranteSingular: "actividad",
    nombreIntegrantePlural: "actividades"
  }),
];

export {
  TablaProgramaDeActividades,
  columnsProgramaDeActividades,
};
