import ProyectoService from "../../../services/ProyectoService";
import { TablaGenericaConFoto } from "./Tabla_Generica";
import { RenderMostrarIntegrantesPersonasRow } from "./Tabla_Mostrar_Integrantes_Modal";
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

const TablaProyecto = ({visibilidadInput, dataIn}) => {

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
      Header: "Fecha inicio",
      accessor: "fechaInicio",
      type: "date",
      Filter: DateRangeColumnFilter,
      filter: dateBetweenFilterFn,
    },
    {
      Header: "Fecha fin",
      accessor: "fechaFin",
      type: "date",
      Filter: DateRangeColumnFilter,
      filter: dateBetweenFilterFn,
    },
    {
      Header: "Número de Involucrados",
      accessor: "numInvolucrados",
      Cell: ({ row }) => {
        if (row && row.original && row.original.involucrados) {
          return row.original.involucrados.length;
        } else {
          return null;//Puede ser para el caso que se agrupe
        }
      },
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Involucrados",
      Cell: ({ row }) => RenderMostrarIntegrantesPersonasRow(row, row.original?.involucrados, "el", "involucrado", "involucrados"),
    },
  ];
  
  return(
    <div>
      <TablaGenericaConFoto
        columnsIn={columns}
        dataIn={dataIn ? dataIn : null}
        Service={ProyectoService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"PROYECTO"}
        //tipoDatoParaFoto={"proyecto"}
        el_la={"el"}
        nombreTipoDato={"proyecto"}
      />
    </div>
  );
}

export default TablaProyecto;
