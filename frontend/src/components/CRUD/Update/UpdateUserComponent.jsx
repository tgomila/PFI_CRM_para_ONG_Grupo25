import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarUserDefault, UserUpdateInput } from '../Constants/componente_individual/User';
import UserService from '../../../services/UserService';

const UpdateUserComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarUserDefault}
        DatoUpdateInput = {UserUpdateInput}
        tipoDatoForImageService = {null}
        Service = {UserService}
        urlTablaDato = {'/users'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'usuario'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateUserComponent;