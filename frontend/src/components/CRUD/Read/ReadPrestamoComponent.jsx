import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPrestamoDefault, PrestamoRead } from '../Constants/componente_individual/Prestamo';
import PrestamoService from '../../../services/PrestamoService';

const ReadPrestamoComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPrestamoDefault}
        DatoUpdateInput = {PrestamoRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {PrestamoService}
        urlTablaDato = {'/prestamo'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'prestamo'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadPrestamoComponent;