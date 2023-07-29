import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaRead } from '../Constants/ConstantsReadModel';
import PersonaService from '../../../services/PersonaService';

const ReadPersonaComponent = ({dataIn, isVentanaEmergente}) => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarPersonaDefault}
        DatoUpdateInput = {PersonaRead}
        tipoDatoForImageService = {'contacto'}
        dataIn = {dataIn}
        Service = {PersonaService}
        urlTablaDato = {'/personafisica'}
        isVentanaEmergente = {isVentanaEmergente}
        el_la = {'la'}
        nombreTipoDato = {'persona'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadPersonaComponent;