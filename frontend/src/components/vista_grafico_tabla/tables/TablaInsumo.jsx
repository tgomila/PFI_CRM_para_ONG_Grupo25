import InsumoService from "../../../services/InsumoService";
import { TablaGenerica } from "./Tabla_Generica";
import { RenderFotoIntegranteRow } from "./Tabla_Variables";
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

const TablaInsumo = ({visibilidadInput, dataIn}) => {

  const columns = [
    {
      Header: "ID",
      accessor: "id",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Tipo",
      accessor: "tipo",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Descripción",
      accessor: "descripcion",
      filter: 'fuzzyText',
      type: "string",
    },
    {
      Header: "Stock actual",
      accessor: "stockActual",
      Filter: NumberRangeColumnFilter,
      filter: 'between',
      type: "number",
    },
    {
      Header: "Fragil",
      accessor: "fragil",
      Cell: ({ row, value }) => //(row.original?.id ? (value ? "✅" : "❌") : "")
        (value === true ? "✅" : value === false ? "❌" : "")
    },
  ];
  
  return(
    <div>
      <TablaGenerica
        columnsIn={columns}
        //dataIn={dataIn ? dataIn : null}
        Service={InsumoService}
        visibilidadInput={visibilidadInput}
        nombreTipoDatoParaModuloVisibilidad={"INSUMO"}
        //tipoDatoParaFoto={"insumo"}
        el_la={"la"}
        nombreTipoDato={"donación"}
      />
    </div>
  );
}

export default TablaInsumo;
