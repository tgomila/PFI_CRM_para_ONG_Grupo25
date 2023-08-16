import AbstractBaseService from "./AbstractBaseService";

class FacturaService extends AbstractBaseService {
  constructor() {
    super("factura");
  }
}

export default new FacturaService();