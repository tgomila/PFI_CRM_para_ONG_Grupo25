import AbstractBaseService from "./AbstractBaseService";

class ProductoService extends AbstractBaseService {
  constructor() {
    super("producto");
  }
}

export default new ProductoService();