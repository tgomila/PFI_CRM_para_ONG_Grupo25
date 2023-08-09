import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaUpdateInput } from '../Constants/ConstantsInputModel';
import PersonaService from '../../../services/PersonaService';

const UpdatePersonaComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPersonaDefault}
        DatoUpdateInput = {PersonaUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {PersonaService}
        urlTablaDato = {'/personafisica'}
        el_la = {'la'}
        nombreTipoDato = {'persona'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdatePersonaComponent;