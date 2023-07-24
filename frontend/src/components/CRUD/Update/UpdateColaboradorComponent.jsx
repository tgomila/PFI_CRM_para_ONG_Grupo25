import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarColaboradorDefault } from '../Constants/ConstantsCargarDefault';
import { ColaboradorUpdateInput } from '../Constants/ConstantsInputModel';
import ColaboradorService from '../../../services/ColaboradorService';

const UpdateColaboradorComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarColaboradorDefault}
        DatoUpdateInput = {ColaboradorUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {ColaboradorService}
        urlTablaDato = {'/colaborador'}
        el_la = {'el'}
        nombreTipoDato = {'colaborador'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateColaboradorComponent;