import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarDonacionDefault, DonacionCreateInput } from '../Constants/componente_individual/Donacion';
import DonacionService from '../../../services/DonacionService';

const CreateDonacionComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarDonacionDefault}
            DatoUpdateInput = {DonacionCreateInput}
            tipoDatoForImageService = {null}
            Service = {DonacionService}
            urlTablaDato = {'/donacion'}
            isPantallaCompleta = {true}
            el_la = {'la'}
            nombreTipoDato = {'donación'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateDonacionComponent;