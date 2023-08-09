import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProductoDefault, ProductoUpdateInput } from '../Constants/componente_individual/Producto';
import ProductoService from '../../../services/ProductoService';

const UpdateProductoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProductoDefault}
        DatoUpdateInput = {ProductoUpdateInput}
        tipoDatoForImageService = {'producto'}
        Service = {ProductoService}
        urlTablaDato = {'/producto'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'producto'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateProductoComponent;