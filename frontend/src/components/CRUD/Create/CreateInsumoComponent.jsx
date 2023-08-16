import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarInsumoDefault, InsumoCreateInput } from '../Constants/componente_individual/Insumo';
import InsumoService from '../../../services/InsumoService';

const CreateInsumoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarInsumoDefault}
            DatoUpdateInput = {InsumoCreateInput}
            tipoDatoForImageService = {null}
            Service = {InsumoService}
            urlTablaDato = {'/insumo'}
            isPantallaCompleta = {true}
            el_la = {'el'}
            nombreTipoDato = {'insumo'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateInsumoComponent;