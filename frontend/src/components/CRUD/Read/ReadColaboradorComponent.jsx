import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarColaboradorDefault } from '../Constants/ConstantsCargarDefault';
import { ColaboradorRead } from '../Constants/ConstantsReadModel';
import ColaboradorService from '../../../services/ColaboradorService';

const ReadColaboradorComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarColaboradorDefault}
        DatoUpdateInput = {ColaboradorRead}
        tipoDatoForImageService = {'contacto'}
        Service = {ColaboradorService}
        urlTablaDato = {'/colaborador'}
        el_la = {'el'}
        nombreTipoDato = {'colaborador'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadColaboradorComponent;