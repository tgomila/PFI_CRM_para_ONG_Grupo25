import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarConsejoAdHonoremDefault } from '../Constants/ConstantsCargarDefault';
import { ConsejoAdHonoremCreateInput } from "../Constants/ConstantsInputModel";
import ConsejoAdHonoremService from '../../../services/ConsejoAdHonoremService';

const CreateConsejoAdHonoremComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarConsejoAdHonoremDefault}
            DatoUpdateInput = {ConsejoAdHonoremCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {ConsejoAdHonoremService}
            urlTablaDato = {'/consejoadhonorem'}
            el_la = {'el'}
            nombreTipoDato = {'consejo ad honorem'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateConsejoAdHonoremComponent;