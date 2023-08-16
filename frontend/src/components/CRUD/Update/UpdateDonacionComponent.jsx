import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarDonacionDefault, DonacionUpdateInput } from '../Constants/componente_individual/Donacion';
import DonacionService from '../../../services/DonacionService';

const UpdateDonacionComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarDonacionDefault}
        DatoUpdateInput = {DonacionUpdateInput}
        tipoDatoForImageService = {null}
        Service = {DonacionService}
        urlTablaDato = {'/donacion'}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'donación'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateDonacionComponent;