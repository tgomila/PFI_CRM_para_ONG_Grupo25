import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProyectoDefault, ProyectoUpdateInput } from '../Constants/componente_individual/Proyecto';
import ProyectoService from '../../../services/ProyectoService';

const UpdateProyectoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProyectoDefault}
        DatoUpdateInput = {ProyectoUpdateInput}
        tipoDatoForImageService = {null}
        Service = {ProyectoService}
        urlTablaDato = {'/proyecto'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'proyecto'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateProyectoComponent;