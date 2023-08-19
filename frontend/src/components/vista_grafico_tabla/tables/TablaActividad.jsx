import ActividadService from "../../../services/ActividadService";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { RenderMostrarIntegrantesPersonasRow } from "./Tabla_Mostrar_Integrantes_Modal";
import { columnsIntegrantesBeneficiarios, columnsIntegrantesProfesionales, columnFechaHora } from "./Tabla_Variables";
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

const TablaActividad = ({visibilidadInput, dataIn}) => {
  
  return(
    <div>
      <TablaGenericaConFoto
        columnsIn={columnsActividad()}
        dataIn={dataIn ? dataIn : null}
        Service={ActividadService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"ACTIVIDAD"}
        tipoDatoParaFoto={"actividad"}
        el_la={"la"}
        nombreTipoDato={"actividad"}
      />
    </div>
  );
}

const columnsActividad = () => [
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
  // {
  //   Header: "Fecha y hora desde",
  //   accessor: "fechaHoraDesde",
  //   Cell: ({ value }) => {
  //     const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");//"yyyy-MM-dd HH:mm:ss"
  //     return value ? <span>{formattedDate}</span> : <></>;
  //   },
  //   Filter: DateHourRangeColumnFilter,
  //   filter: dateBetweenFilterFn,
  // },
  columnFechaHora("Fecha y hora desde", "fechaHoraDesde"),
  // {
  //   Header: "Fecha y hora hasta",
  //   accessor: "fechaHoraHasta",
  //   Cell: ({ value }) => {
  //     const formattedDate = format(new Date(value), "dd/MM/yyyy HH:mm");
  //     return value ? <span>{formattedDate}</span> : <></>;
  //   },
  //   Filter: DateHourRangeColumnFilter,
  //   filter: dateBetweenFilterFn,
  // },
  columnFechaHora("Fecha y hora hasta", "fechaHoraHasta"),
  ...columnsIntegrantesProfesionales({
    property: "profesionales",
    el_la: "el",
    nombreIntegranteSingular: "profesional",
    nombreIntegrantePlural: "profesionales"
  }),
  ...columnsIntegrantesBeneficiarios({}),
];

export {
  TablaActividad,
  columnsActividad,
};
