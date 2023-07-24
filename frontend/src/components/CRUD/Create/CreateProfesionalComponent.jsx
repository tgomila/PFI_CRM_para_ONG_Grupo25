import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarProfesionalDefault } from '../Constants/ConstantsCargarDefault';
import { ProfesionalCreateInput } from "../Constants/ConstantsInputModel";
import ProfesionalService from '../../../services/ProfesionalService';

const CreateProfesionalComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarProfesionalDefault}
            DatoUpdateInput = {ProfesionalCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {ProfesionalService}
            urlTablaDato = {'/profesional'}
            el_la = {'el'}
            nombreTipoDato = {'profesional'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateProfesionalComponent;