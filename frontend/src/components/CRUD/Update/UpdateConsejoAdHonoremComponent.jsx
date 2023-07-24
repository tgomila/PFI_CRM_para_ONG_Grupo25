import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarConsejoAdHonoremDefault } from '../Constants/ConstantsCargarDefault';
import { ConsejoAdHonoremUpdateInput } from '../Constants/ConstantsInputModel';
import ConsejoAdHonoremService from '../../../services/ConsejoAdHonoremService';

const UpdateConsejoAdHonoremComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarConsejoAdHonoremDefault}
        DatoUpdateInput = {ConsejoAdHonoremUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {ConsejoAdHonoremService}
        urlTablaDato = {'/consejoadhonorem'}
        el_la = {'el'}
        nombreTipoDato = {'consejo ad honorem'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateConsejoAdHonoremComponent;