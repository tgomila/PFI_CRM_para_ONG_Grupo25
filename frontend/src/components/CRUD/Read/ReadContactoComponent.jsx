import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarContactoDefault } from '../Constants/ConstantsCargarDefault';
import { ContactoRead } from '../Constants/ConstantsReadModel';
import ContactoService from '../../../services/ContactoService';

const ReadContactoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarContactoDefault}
        DatoUpdateInput = {ContactoRead}
        tipoDatoForImageService = {'contacto'}
        Service = {ContactoService}
        urlTablaDato = {'/contacto'}
        el_la = {'el'}
        nombreTipoDato = {'contacto'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadContactoComponent;