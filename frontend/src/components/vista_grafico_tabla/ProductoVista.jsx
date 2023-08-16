import AbstractVista from "./AbstractVista";
import TablaProducto from "./tables/TablaProducto";

const ProductoVista = AbstractVista(TablaProducto, "PRODUCTO", null);

export default ProductoVista;
