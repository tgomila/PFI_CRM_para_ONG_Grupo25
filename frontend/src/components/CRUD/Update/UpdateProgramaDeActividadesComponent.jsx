import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProgramaDeActividadesDefault, ProgramaDeActividadesUpdateInput } from '../Constants/componente_individual/ProgramaDeActividades';
import ProgramaDeActividadesService from '../../../services/ProgramaDeActividadesService';

const UpdateProgramaDeActividadesComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProgramaDeActividadesDefault}
        DatoUpdateInput = {ProgramaDeActividadesUpdateInput}
        tipoDatoForImageService = {null}
        Service = {ProgramaDeActividadesService}
        urlTablaDato = {'/programaDeActividades'}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'programa de actividades'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateProgramaDeActividadesComponent;