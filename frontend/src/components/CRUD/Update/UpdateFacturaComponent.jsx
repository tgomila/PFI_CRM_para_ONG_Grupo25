import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarFacturaDefault, FacturaUpdateInput } from '../Constants/componente_individual/Factura';
import FacturaService from '../../../services/FacturaService';

const UpdateFacturaComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarFacturaDefault}
        DatoUpdateInput = {FacturaUpdateInput}
        tipoDatoForImageService = {null}
        Service = {FacturaService}
        urlTablaDato = {'/factura'}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'factura'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateFacturaComponent;