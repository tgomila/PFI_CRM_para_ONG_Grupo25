import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarDonacionDefault, DonacionRead } from '../Constants/componente_individual/Donacion';
import DonacionService from '../../../services/DonacionService';

const ReadDonacionComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarDonacionDefault}
        DatoUpdateInput = {DonacionRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {DonacionService}
        urlTablaDato = {'/donacion'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'la'}
        nombreTipoDato = {'donación'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadDonacionComponent;