import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarContactoDefault } from '../Constants/ConstantsCargarDefault';
import { ContactoRead } from '../Constants/ConstantsReadModel';
import ContactoService from '../../../services/ContactoService';

const ReadContactoComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarContactoDefault}
        DatoUpdateInput = {ContactoRead}
        tipoDatoForImageService = {'contacto'}
        dataIn = {dataIn}
        Service = {ContactoService}
        urlTablaDato = {'/contacto'}
        isVentanaEmergente = {isVentanaEmergente}
        el_la = {'el'}
        nombreTipoDato = {'contacto'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadContactoComponent;