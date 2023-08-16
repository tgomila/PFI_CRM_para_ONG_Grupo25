import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProyectoDefault, ProyectoRead } from '../Constants/componente_individual/Proyecto';
import ProyectoService from '../../../services/ProyectoService';

const ReadProyectoComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProyectoDefault}
        DatoUpdateInput = {ProyectoRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {ProyectoService}
        urlTablaDato = {'/proyecto'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'proyecto'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadProyectoComponent;