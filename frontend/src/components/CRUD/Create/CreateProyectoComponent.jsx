import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarProyectoDefault, ProyectoCreateInput } from '../Constants/componente_individual/Proyecto';
import ProyectoService from '../../../services/ProyectoService';

const CreateProyectoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarProyectoDefault}
            DatoUpdateInput = {ProyectoCreateInput}
            tipoDatoForImageService = {null}
            Service = {ProyectoService}
            urlTablaDato = {'/proyecto'}
            isPantallaCompleta = {true}
            el_la = {'el'}
            nombreTipoDato = {'proyecto'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateProyectoComponent;