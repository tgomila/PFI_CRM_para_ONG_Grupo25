import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarFacturaDefault, FacturaRead } from '../Constants/componente_individual/Factura';
import FacturaService from '../../../services/FacturaService';

const ReadFacturaComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarFacturaDefault}
        DatoUpdateInput = {FacturaRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {FacturaService}
        urlTablaDato = {'/factura'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'factura'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadFacturaComponent;