import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarEmpleadoDefault } from '../Constants/ConstantsCargarDefault';
import { EmpleadoRead } from '../Constants/ConstantsReadModel';
import EmpleadoService from '../../../services/EmpleadoService';

const ReadEmpleadoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarEmpleadoDefault}
        DatoUpdateInput = {EmpleadoRead}
        tipoDatoForImageService = {'contacto'}
        Service = {EmpleadoService}
        urlTablaDato = {'/empleado'}
        el_la = {'el'}
        nombreTipoDato = {'empleado'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadEmpleadoComponent;