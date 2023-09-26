import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarUserDefault, UserRead } from '../Constants/componente_individual/User';
import UserService from '../../../services/UserService';

const ReadUserComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarUserDefault}
        DatoUpdateInput = {UserRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {UserService}
        urlTablaDato = {'/users'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'usuario'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadUserComponent;