import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaRead } from '../Constants/ConstantsReadModel';
import PersonaService from '../../../services/PersonaService';

const ReadPersonaComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPersonaDefault}
        DatoUpdateInput = {PersonaRead}
        tipoDatoForImageService = {'contacto'}
        Service = {PersonaService}
        urlTablaDato = {'/personafisica'}
        el_la = {'la'}
        nombreTipoDato = {'persona'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadPersonaComponent;