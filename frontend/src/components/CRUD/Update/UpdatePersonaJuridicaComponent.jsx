import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPersonaJuridicaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaJuridicaUpdateInput } from '../Constants/ConstantsInputModel';
import PersonaJuridicaService from '../../../services/PersonaJuridicaService';

const UpdatePersonaJuridicaComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPersonaJuridicaDefault}
        DatoUpdateInput = {PersonaJuridicaUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {PersonaJuridicaService}
        urlTablaDato = {'/personajuridica'}
        el_la = {'la'}
        nombreTipoDato = {'persona jurídica'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdatePersonaJuridicaComponent;