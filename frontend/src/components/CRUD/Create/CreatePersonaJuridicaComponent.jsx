import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarPersonaJuridicaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaJuridicaCreateInput } from "../Constants/ConstantsInputModel";
import PersonaJuridicaService from "../../../services/PersonaJuridicaService";

const CreatePersonaComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarPersonaJuridicaDefault}
            DatoUpdateInput = {PersonaJuridicaCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {PersonaJuridicaService}
            urlTablaDato = {'/personajuridica'}
            el_la = {'la'}
            nombreTipoDato = {'persona jurídica'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreatePersonaComponent;