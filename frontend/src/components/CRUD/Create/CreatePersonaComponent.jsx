import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaCreateInput } from "../Constants/ConstantsInputModel";
import PersonaService from '../../../services/PersonaService';

const CreatePersonaComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarPersonaDefault}
            DatoUpdateInput = {PersonaCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {PersonaService}
            urlTablaDato = {'/personafisica'}
            el_la = {'la'}
            nombreTipoDato = {'persona'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreatePersonaComponent;