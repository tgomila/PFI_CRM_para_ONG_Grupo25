import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarVoluntarioDefault } from '../Constants/ConstantsCargarDefault';
import { VoluntarioRead } from '../Constants/ConstantsReadModel';
import VoluntarioService from '../../../services/VoluntarioService';

const ReadVoluntarioComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarVoluntarioDefault}
        DatoUpdateInput = {VoluntarioRead}
        tipoDatoForImageService = {'contacto'}
        Service = {VoluntarioService}
        urlTablaDato = {'/voluntario'}
        el_la = {'el'}
        nombreTipoDato = {'voluntario'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadVoluntarioComponent;