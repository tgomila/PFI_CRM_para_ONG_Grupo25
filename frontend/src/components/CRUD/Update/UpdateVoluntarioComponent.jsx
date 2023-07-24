import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarVoluntarioDefault } from '../Constants/ConstantsCargarDefault';
import { VoluntarioUpdateInput } from '../Constants/ConstantsInputModel';
import VoluntarioService from '../../../services/VoluntarioService';

const UpdateVoluntarioComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarVoluntarioDefault}
        DatoUpdateInput = {VoluntarioUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {VoluntarioService}
        urlTablaDato = {'/voluntario'}
        el_la = {'el'}
        nombreTipoDato = {'voluntario'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateVoluntarioComponent;