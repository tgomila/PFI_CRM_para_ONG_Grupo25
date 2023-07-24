import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarContactoDefault } from '../Constants/ConstantsCargarDefault';
import { ContactoCreateInput } from "../Constants/ConstantsInputModel";
import ContactoService from '../../../services/ContactoService';

const CreateContactoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarContactoDefault}
            DatoUpdateInput = {ContactoCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {ContactoService}
            urlTablaDato = {'/contacto'}
            el_la = {'el'}
            nombreTipoDato = {'contacto'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateContactoComponent;