import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarPrestamoDefault, PrestamoCreateInput } from '../Constants/componente_individual/Prestamo';
import PrestamoService from '../../../services/PrestamoService';

const CreatePrestamoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarPrestamoDefault}
            DatoUpdateInput = {PrestamoCreateInput}
            tipoDatoForImageService = {null}
            Service = {PrestamoService}
            urlTablaDato = {'/prestamo'}
            isPantallaCompleta = {true}
            el_la = {'el'}
            nombreTipoDato = {'prestamo'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreatePrestamoComponent;