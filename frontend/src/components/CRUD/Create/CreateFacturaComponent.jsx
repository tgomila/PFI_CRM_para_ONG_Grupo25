import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarFacturaDefault, FacturaCreateInput, cargarFacturaItemDefault, FacturaItemCreateInput } from '../Constants/componente_individual/Factura';
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

//Hecho para modal
const CreateFacturaItemComponent = ({ setAgregarItem }) => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarFacturaItemDefault}
            DatoUpdateInput = {FacturaItemCreateInput}
            //tipoDatoForImageService = {null}
            //Service = {FacturaService}
            urlTablaDato = {'/factura'}
            isPantallaCompleta = {false}
            el_la = {'el'}
            nombreTipoDato = {'item de factura'}
            typeCRUD={'CREATE'}
            setAgregarItem={setAgregarItem}
        />
        </div>
    );
};

export {
    CreateFacturaComponent,
    CreateFacturaItemComponent,
};