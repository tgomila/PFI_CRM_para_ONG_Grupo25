import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarEmpleadoDefault } from '../Constants/ConstantsCargarDefault';
import { EmpleadoUpdateInput } from '../Constants/ConstantsInputModel';
import EmpleadoService from '../../../services/EmpleadoService';

const UpdateEmpleadoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarEmpleadoDefault}
        DatoUpdateInput = {EmpleadoUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {EmpleadoService}
        urlTablaDato = {'/empleado'}
        el_la = {'el'}
        nombreTipoDato = {'empleado'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateEmpleadoComponent;