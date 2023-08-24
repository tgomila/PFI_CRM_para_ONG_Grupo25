import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarActividadDefault, ActividadUpdateInput } from '../Constants/componente_individual/Actividad';
import ActividadService from '../../../services/ActividadService';

const UpdateActividadComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarActividadDefault}
        DatoUpdateInput = {ActividadUpdateInput}
        tipoDatoForImageService = {'actividad'}
        Service = {ActividadService}
        urlTablaDato = {'/actividad'}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'actividad'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateActividadComponent;