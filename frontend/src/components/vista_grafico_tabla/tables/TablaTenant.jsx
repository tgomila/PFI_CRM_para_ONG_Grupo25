import TenantService from "../../../services/TenantService";
import { TablaGenerica } from "./Tabla_Generica";
import { columnIntegranteConFotoColumn, columnsIntegrantesTenantItems, } from "./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
  dateBetweenFilterFn,
  DateHourRangeColumnFilter,
  emisorFacturaFilterFn,
  CustomTextFilter,
} from"./Tabla_Filters";

import { RenderFotoPerfilForTablaTenant } from "./Tabla_Variables";

const TablaTenant = ({visibilidadInput, dataIn}) => {

  const [data, setData] = useState(undefined);

  useEffect(() => {
    if (dataIn) {
      setData(dataIn);
      console.log("dataIn");
      console.log(dataIn);
    } else{
      TenantService.getAllWithImage().then((res) => {
        setData(res);
        console.log("res");
        console.log(res);
      });
    }
  }, []);





  const columnsTenant = [
    {
      Header: "ID",
      accessor: "tenantClientId",
      type: "number",//sirve para mostrar el icono FaSortNumericDown en tabla generica
    },
    {
      Header: "Foto",
      Cell: ({ row }) => RenderFotoPerfilForTablaTenant(row.original?.imagen, row.original?.tenantName),
    },
    {
      Header: "Número de teléfono",
      accessor: "tenantPhoneNumber",
    },
  ];
  
  return(
    
    <div>
      {dataIn && (
        <TablaGenerica
          columnsIn={columnsTenant}
          dataIn={data}
          //Service={TenantService}
          visibilidadInput={visibilidadInput ? visibilidadInput : "SOLO_VISTA"}
          //nombreTipoDatoParaModuloVisibilidad={"TENANT"}
          //tipoDatoParaFoto={"tenant"}
          el_la={"el"}
          nombreTipoDato={"tenant"}
        />
      )}
    </div>
  );
}

const columnsFacturaItems = [
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
    Header: "Unidades",
    accessor: "unidades",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
  },
  {
    Header: "Precio unitario",
    accessor: "precioUnitario",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
    Cell: ({ value }) => (
      <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
    ),
  },
  {
    Header: "Precio",
    accessor: "precio",
    Filter: NumberRangeColumnFilter,
    filter: 'between',
    type: "number",
    Cell: ({ value }) => (
      <span>{value ? `$${Number(value).toLocaleString("es-AR")}` : ''}</span>
    ),
  },
];

const TenantInput = ({ disabled, data, handleInputChange }) => {
  return(
      <div className = "form-group">
          <br/>
          <ModalSeleccionarIntegrantes
              integrantesActuales = {integrantesActuales}
              columnsIn={columns}
              tipoDatoParaFoto={"contacto"}
              nombreTipoDatoParaModuloVisibilidad={"PROFESIONAL"}
              ServiceDeIntegrantes = {ServiceDeIntegrantes ? ServiceDeIntegrantes : ProfesionalService}
              handleInputChange = {handleInputChange}
              nombreHandleInputChange = {nombreHandleInputChange}
              maxIntegrantesSelected = {maxIntegrantesSelected}
              isEditable = {isEditable}
              el_la = {el_la}
              nombreTipoIntegrante = {nombreTipoIntegrante}
              nombreTipoIntegrantePrural = {nombreTipoIntegrantePrural}
          />
      </div>
  );
};

export {
  columnsFacturaItems,
  TablaTenant,
};
