import CreateReadUpdateGenericoConFoto from '../Constants/CreateReadUpdate_Generico';
import { cargarBeneficiarioDefault } from '../Constants/ConstantsCargarDefault';
import { BeneficiarioUpdateInput } from '../Constants/ConstantsInputModel';
import BeneficiarioService from '../../../services/BeneficiarioService';

const UpdateBeneficiarioComponent = () => {
  return (
    <div>
    <CreateReadUpdateGenericoConFoto
        cargarDatosDefault = {cargarBeneficiarioDefault}
        DatoUpdateInput = {BeneficiarioUpdateInput}
        tipoDatoForImageService = {'contacto'}
        Service = {BeneficiarioService}
        urlTablaDato = {'/beneficiario'}
        el_la = {'el'}
        nombreTipoDato = {'beneficiario'}
        typeCRUD={'UPDATE'}
    />
    </div>
  );
};

export default UpdateBeneficiarioComponent;