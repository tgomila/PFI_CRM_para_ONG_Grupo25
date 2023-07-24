import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPersonaJuridicaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaJuridicaRead } from '../Constants/ConstantsReadModel';
import PersonaJuridicaService from '../../../services/PersonaJuridicaService';

const ReadPersonaJuridicaComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPersonaJuridicaDefault}
        DatoUpdateInput = {PersonaJuridicaRead}
        tipoDatoForImageService = {'contacto'}
        Service = {PersonaJuridicaService}
        urlTablaDato = {'/personajuridica'}
        el_la = {'el'}
        nombreTipoDato = {'persona jurídica'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadPersonaJuridicaComponent;