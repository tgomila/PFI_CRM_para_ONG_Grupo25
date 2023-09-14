import axios from "axios";
import * as constantsURL from "../components/constants/ConstantsURL";
const BACKEND_API_BASE_URL = constantsURL.API_BASE_URL;
const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;


class TenantService {

    getAll(){
        return axios.get(BACKEND_API_BASE_URL + 'tenant/all');
    }

    //Se utiliza para tenant en login
    async getAllWithImage(){
        try {
            const response = await axios.get(BACKEND_API_BASE_URL + 'tenant/all');
            const tenants = response.data;
        
            // Iterar sobre cada tenant y agregar la imagen
            const tenantsConImagen = await Promise.all(tenants.map(async (tenant) => {
                const imagenUrl = BACKEND_STATIC_BASE_URL + 'logo/' + tenant.dbName + '.png';
                // const imagenResponse = await axios.get(imagenUrl, { responseType: 'arraybuffer' });
                // const imagenBase64 = Buffer.from(imagenResponse.data, 'binary').toString('base64');
            
                // return { ...tenant, imagen: `data:image/png;base64,${imagenBase64}` };

                const imagenResponse = await axios.get(imagenUrl, { responseType: 'arraybuffer' });
                const imagenBlob = new Blob([imagenResponse.data], { type: 'image/png' });

                return new Promise((resolve, reject) => {
                    const reader = new FileReader();
                    reader.onload = () => {
                    const imagenBase64 = reader.result;
                    resolve({ ...tenant, imagen: imagenBase64 });
                    };
                    reader.onerror = reject;
                    reader.readAsDataURL(imagenBlob);
                });
            }));
        
            return tenantsConImagen;
        } catch (error) {
            console.error('Error al obtener tenants con imágenes:', error);
            throw error; // Puedes manejar el error como prefieras
        }
    }

    //Esto se usa en caso de tabla dinámica, y asignar nombre real de 'headers' de cada columna de la tabla
    getColumnNames(){
        return axios.get(BACKEND_API_BASE_URL + 'tenant/nombres_tabla');
    }
}

export default new TenantService( )