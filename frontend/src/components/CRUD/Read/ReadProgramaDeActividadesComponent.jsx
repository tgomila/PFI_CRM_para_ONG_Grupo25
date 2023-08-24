import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProgramaDeActividadesDefault, ProgramaDeActividadesRead } from '../Constants/componente_individual/ProgramaDeActividades';
import ProgramaDeActividadesService from '../../../services/ProgramaDeActividadesService';

const ReadProgramaDeActividadesComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProgramaDeActividadesDefault}
        DatoUpdateInput = {ProgramaDeActividadesRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {ProgramaDeActividadesService}
        urlTablaDato = {'/programaDeActividades'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'programa de actividades'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadProgramaDeActividadesComponent;