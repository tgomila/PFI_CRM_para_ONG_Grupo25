import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPrestamoDefault, PrestamoUpdateInput } from '../Constants/componente_individual/Prestamo';
import PrestamoService from '../../../services/PrestamoService';

const UpdatePrestamoComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPrestamoDefault}
        DatoUpdateInput = {PrestamoUpdateInput}
        tipoDatoForImageService = {null}
        Service = {PrestamoService}
        urlTablaDato = {'/prestamo'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'prestamo'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdatePrestamoComponent;