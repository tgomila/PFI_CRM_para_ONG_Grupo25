import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarActividadDefault, ActividadRead } from '../Constants/componente_individual/Actividad';
import ActividadService from '../../../services/ActividadService';

const ReadActividadComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarActividadDefault}
        DatoUpdateInput = {ActividadRead}
        tipoDatoForImageService = {'actividad'}
        dataIn = {dataIn}
        Service = {ActividadService}
        urlTablaDato = {'/actividad'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'actividad'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadActividadComponent;