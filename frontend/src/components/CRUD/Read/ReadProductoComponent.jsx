import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProductoDefault, ProductoRead } from '../Constants/componente_individual/Producto';
import ProductoService from '../../../services/ProductoService';

const ReadProductoComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProductoDefault}
        DatoUpdateInput = {ProductoRead}
        tipoDatoForImageService = {'producto'}
        dataIn = {dataIn}
        Service = {ProductoService}
        urlTablaDato = {'/producto'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'producto'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadProductoComponent;