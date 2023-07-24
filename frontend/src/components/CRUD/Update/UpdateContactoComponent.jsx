import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarContactoDefault } from '../Constants/ConstantsCargarDefault';
import { ContactoUpdateInput } from '../Constants/ConstantsInputModel';
import ContactoService from '../../../services/ContactoService';

const UpdateContactoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarContactoDefault}
        DatoUpdateInput = {ContactoUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {ContactoService}
        urlTablaDato = {'/contacto'}
        el_la = {'el'}
        nombreTipoDato = {'contacto'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateContactoComponent;