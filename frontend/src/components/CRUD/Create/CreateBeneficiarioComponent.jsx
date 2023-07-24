import CreateReadUpdateGenericoConFoto from "../Constants/CreateReadUpdate_Generico";
import { cargarBeneficiarioDefault } from '../Constants/ConstantsCargarDefault';
import { BeneficiarioCreateInput } from "../Constants/ConstantsInputModel";
import BeneficiarioService from '../../../services/BeneficiarioService';

const CreateBeneficiarioComponent = () => {
    return (
        <div>
        <CreateReadUpdateGenericoConFoto
            cargarDatosDefault = {cargarBeneficiarioDefault}
            DatoUpdateInput = {BeneficiarioCreateInput}
            tipoDatoForImageService = {'contacto'}
            Service = {BeneficiarioService}
            urlTablaDato = {'/beneficiario'}
            el_la = {'el'}
            nombreTipoDato = {'beneficiario'}
            typeCRUD={'CREATE'}
        />
        </div>
      );
};

export default CreateBeneficiarioComponent;