import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarBeneficiarioDefault } from '../Constants/ConstantsCargarDefault';
import { BeneficiarioRead } from '../Constants/ConstantsReadModel';
import BeneficiarioService from '../../../services/BeneficiarioService';

const ReadBeneficiarioComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarBeneficiarioDefault}
        DatoUpdateInput = {BeneficiarioRead}
        tipoDatoForImageService = {'contacto'}
        Service = {BeneficiarioService}
        urlTablaDato = {'/beneficiario'}
        el_la = {'el'}
        nombreTipoDato = {'beneficiario'}
        typeCRUD={'READ'}
    />
    </div>
  );
};

export default ReadBeneficiarioComponent;