import ActividadService from "../../../services/ActividadService";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { RenderMostrarIntegrantesPersonasRow } from "./Tabla_Mostrar_Integrantes_Modal";
import { columnsIntegrantesBeneficiarios, columnsIntegrantesProfesionales } from "./Tabla_Variables";
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

  const columns = [
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
      accessor: "fechaHoraDesde",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "yyyy-MM-dd HH:mm:ss");
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn,
    },
    {
      Header: "Fecha y hora hasta",
      accessor: "fechaHoraHasta",
      Cell: ({ value }) => {
        const formattedDate = format(new Date(value), "yyyy-MM-dd HH:mm:ss");
        return value ? <span>{formattedDate}</span> : <></>;
      },
      Filter: DateHourRangeColumnFilter,
      filter: dateBetweenFilterFn,
    },
    ...columnsIntegrantesProfesionales("profesionales"),
    ...columnsIntegrantesBeneficiarios("beneficiarios"),
  ];
  
  return(
    <div>
      <TablaGenericaConFoto
        columnsIn={columns}
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

export default TablaActividad;
