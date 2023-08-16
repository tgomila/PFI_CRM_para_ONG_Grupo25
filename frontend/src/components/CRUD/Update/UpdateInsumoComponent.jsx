import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarInsumoDefault, InsumoUpdateInput } from '../Constants/componente_individual/Insumo';
import InsumoService from '../../../services/InsumoService';

const UpdateInsumoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarInsumoDefault}
        DatoUpdateInput = {InsumoUpdateInput}
        tipoDatoForImageService = {null}
        Service = {InsumoService}
        urlTablaDato = {'/insumo'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'insumo'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateInsumoComponent;