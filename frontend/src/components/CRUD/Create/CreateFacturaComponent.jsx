import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarFacturaDefault, FacturaCreateInput } from '../Constants/componente_individual/Factura';
import FacturaService from '../../../services/FacturaService';

const CreateFacturaComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarFacturaDefault}
            DatoUpdateInput = {FacturaCreateInput}
            tipoDatoForImageService = {null}
            Service = {FacturaService}
            urlTablaDato = {'/factura'}
            isPantallaCompleta = {true}
            el_la = {'la'}
            nombreTipoDato = {'factura'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateFacturaComponent;