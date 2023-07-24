import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarConsejoAdHonoremDefault } from '../Constants/ConstantsCargarDefault';
import { ConsejoAdHonoremRead } from '../Constants/ConstantsReadModel';
import ConsejoAdHonoremService from '../../../services/ConsejoAdHonoremService';

const ReadConsejoAdHonoremComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarConsejoAdHonoremDefault}
        DatoUpdateInput = {ConsejoAdHonoremRead}
        tipoDatoForImageService = {'contacto'}
        Service = {ConsejoAdHonoremService}
        urlTablaDato = {'/consejoadhonorem'}
        el_la = {'el'}
        nombreTipoDato = {'consejo ad honorem'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadConsejoAdHonoremComponent;