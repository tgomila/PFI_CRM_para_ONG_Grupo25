import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarUserDefault, UserCreateInput } from '../Constants/componente_individual/User';
import UserService from '../../../services/UserService';

const CreateUserComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarUserDefault}
            DatoUpdateInput = {UserCreateInput}
            tipoDatoForImageService = {null}
            Service = {UserService}
            urlTablaDato = {'/users'}
            isPantallaCompleta = {true}
            el_la = {'el'}
            nombreTipoDato = {'usuario'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateUserComponent;