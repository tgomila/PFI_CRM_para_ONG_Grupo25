import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarColaboradorDefault } from '../Constants/ConstantsCargarDefault';
import { ColaboradorCreateInput } from "../Constants/ConstantsInputModel";
import ColaboradorService from '../../../services/ColaboradorService';

const CreateColaboradorComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarColaboradorDefault}
            DatoUpdateInput = {ColaboradorCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {ColaboradorService}
            urlTablaDato = {'/colaborador'}
            el_la = {'el'}
            nombreTipoDato = {'colaborador'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateColaboradorComponent;