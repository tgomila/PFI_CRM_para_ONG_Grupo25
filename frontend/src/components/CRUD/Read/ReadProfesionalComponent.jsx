import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarProfesionalDefault } from '../Constants/ConstantsCargarDefault';
import { ProfesionalRead } from '../Constants/ConstantsReadModel';
import ProfesionalService from '../../../services/ProfesionalService';

const ReadProfesionalComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarProfesionalDefault}
        DatoUpdateInput = {ProfesionalRead}
        tipoDatoForImageService = {'contacto'}
        Service = {ProfesionalService}
        urlTablaDato = {'/profesional'}
        el_la = {'el'}
        nombreTipoDato = {'profesional'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadProfesionalComponent;