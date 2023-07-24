import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarVoluntarioDefault } from '../Constants/ConstantsCargarDefault';
import { VoluntarioCreateInput } from "../Constants/ConstantsInputModel";
import VoluntarioService from '../../../services/VoluntarioService';

const CreateVoluntarioComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarVoluntarioDefault}
            DatoUpdateInput = {VoluntarioCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {VoluntarioService}
            urlTablaDato = {'/voluntario'}
            el_la = {'el'}
            nombreTipoDato = {'voluntario'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateVoluntarioComponent;