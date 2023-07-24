import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarEmpleadoDefault } from '../Constants/ConstantsCargarDefault';
import { EmpleadoCreateInput } from "../Constants/ConstantsInputModel";
import EmpleadoService from '../../../services/EmpleadoService';

const CreateEmpleadoComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarEmpleadoDefault}
            DatoUpdateInput = {EmpleadoCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {EmpleadoService}
            urlTablaDato = {'/empleado'}
            el_la = {'el'}
            nombreTipoDato = {'empleado'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateEmpleadoComponent;