import AbstractBaseService from "./AbstractBaseService";

class ProyectoService extends AbstractBaseService {
  constructor() {
    super("proyecto");
  }
}

export default new ProyectoService();