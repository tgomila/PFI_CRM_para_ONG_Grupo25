import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProfesionalDefault } from '../Constants/ConstantsCargarDefault';
import { ProfesionalUpdateInput } from '../Constants/ConstantsInputModel';
import ProfesionalService from '../../../services/ProfesionalService';

const UpdateProfesionalComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProfesionalDefault}
        DatoUpdateInput = {ProfesionalUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {ProfesionalService}
        urlTablaDato = {'/profesional'}
        el_la = {'el'}
        nombreTipoDato = {'profesional'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateProfesionalComponent;