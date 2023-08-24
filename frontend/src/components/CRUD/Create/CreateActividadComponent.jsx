import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarActividadDefault, ActividadCreateInput, ActividadItemCreateInput } from '../Constants/componente_individual/Actividad';
import ActividadService from '../../../services/ActividadService';

/**
 * @description
 * Se utilizará para ver ver o agregar/editar integrantes de un CRUD de tipo dato, como una actividad.
 * 
 * @param {boolean} isPantallaCompleta - true si es create clásico, false si es un Modal
 * @param {Object} setAgregarItem - un set clásico que si se agrega item, entonces es un modal
 * @returns {JSX.Element} - Componente de la tabla de integrantes.
 */
const CreateActividadComponent = ({isPantallaCompleta=true, setAgregarItem}) => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarActividadDefault}
            DatoUpdateInput = {setAgregarItem ? ActividadItemCreateInput : ActividadCreateInput}
            tipoDatoForImageService = {!setAgregarItem && 'actividad'}
            Service = {ActividadService}
            urlTablaDato = {'/actividad'}
            isPantallaCompleta = {setAgregarItem ? false : isPantallaCompleta}//Modal agregar item o creacte clasico
            el_la = {'la'}
            nombreTipoDato = {'actividad'}
            typeCRUD={'CREATE'}
            setAgregarItem={setAgregarItem}
        />
        </div>
      );
};

export default CreateActividadComponent;