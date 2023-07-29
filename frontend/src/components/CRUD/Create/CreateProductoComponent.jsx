import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarProductoDefault, ProductoCreateInput } from '../Constants/componente_individual/Producto';
import ProductoService from '../../../services/ProductoService';

const CreateProductoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarProductoDefault}
            DatoUpdateInput = {ProductoCreateInput}
            tipoDatoForImageService = {'producto'}
            Service = {ProductoService}
            urlTablaDato = {'/producto'}
            el_la = {'el'}
            nombreTipoDato = {'producto'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateProductoComponent;