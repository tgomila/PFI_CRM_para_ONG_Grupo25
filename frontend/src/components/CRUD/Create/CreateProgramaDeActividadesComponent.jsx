import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarProgramaDeActividadesDefault, ProgramaDeActividadesCreateInput } from '../Constants/componente_individual/ProgramaDeActividades';
import ProgramaDeActividadesService from '../../../services/ProgramaDeActividadesService';

const CreateProgramaDeActividadesComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarProgramaDeActividadesDefault}
            DatoUpdateInput = {ProgramaDeActividadesCreateInput}
            tipoDatoForImageService = {'programaDeActividades'}
            Service = {ProgramaDeActividadesService}
            urlTablaDato = {'/programaDeActividades'}
            isPantallaCompleta = {true}
            el_la = {'el'}
            nombreTipoDato = {'programa de actividades'}
            typeCRUD={'CREATE'}
        />
        </div>
    );
};

export default CreateProgramaDeActividadesComponent;