import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarInsumoDefault, InsumoRead } from '../Constants/componente_individual/Insumo';
import InsumoService from '../../../services/InsumoService';

const ReadInsumoComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarInsumoDefault}
        DatoUpdateInput = {InsumoRead}
        tipoDatoForImageService = {null}
        dataIn = {dataIn}
        Service = {InsumoService}
        urlTablaDato = {'/insumo'}
        isVentanaEmergente = {isVentanaEmergente}
        isPantallaCompleta = {true}
        el_la = {'el'}
        nombreTipoDato = {'insumo'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadInsumoComponent;