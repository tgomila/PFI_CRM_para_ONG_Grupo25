import React from 'react';



const cardSuscripcion = ({ elements }) => {
  //Hasta 3 elementos por row.
  const Componentes = (elements) => {
    const rows = [];
    for (let i = 0; i < elements.length; i += 3) {
      rows.push(elements.slice(i, i + 3));
    }
    return rows;
  }
  const rows = divideListIntoRows(elements);
  //Listo aquí tengo 3 componentes para cada row

  //render:
  return (
    <>
      {rows.map((row, index) => (
        <div className="row" key={index}>
          {row.map((element, idx) => (
            <div className="col-lg-4 col-md-6" key={idx}>
              <h1 className="card__header">{element}</h1>
            </div>
          ))}
        </div>
      ))}
    </>
  );
}

const informacionSuscripcion = (busqueda) => {
  try{
    if(!busqueda){
      const suscripto = !(modulos && modulos.some(item => item.suscripcionActiva === false));
      const fechaMasBaja = buscarFechaPremiumMasBaja();
      return(
        <div>
          {suscripto ? (
            <div>
              <p className="suscripto-true">Suscripto al plan total</p>
              {fechaMasBaja ? (
                <p className="card__text_info">Suscripto hasta el: {fechaMasBaja}</p>
              ) : (
                <p className="card__text_info">Se desuscribirá al siguiente día</p>
              )}
            </div>
          ) : (
            <div>
              <p className="suscripto-false">Desuscripto del plan total</p>
              {fechaMasBaja ? (
                <p className="card__text_info">La suscripcion ha durado hasta: {fechaMasBaja}</p>
              ) : (
                <p className="card__text_info">Nunca se ha suscripto</p>
              )}
            </div>
          )}
        </div>
      );
    }
    const moduloEncontrado = modulos.find((item) => item.moduloEnum === busqueda);
    if (moduloEncontrado) {
      return(
        <div>
          {moduloEncontrado.suscripcionActiva ? (
            <div>
              <p className="suscripto-true">Suscripto</p>
              {/*moduloEncontrado.fechaInicioSuscripcion ? (
                <p className="card__text_info">Posee suscripcion desde: {formatFecha(moduloEncontrado.fechaInicioSuscripcion)}</p>
              ) : (
                <p className="card__text_info">Posee suscripcion desde: ?</p>
              )*/}
              {moduloEncontrado.fechaMaximaSuscripcion ? (
                <p className="card__text_info">Suscripto hasta: {formatFecha(moduloEncontrado.fechaMaximaSuscripcion)}</p>
              ) : (
                <p className="card__text_info">Suscriptohasta: hoy</p>
              )}
            </div>
          ) : (
            <div>
              <p className="suscripto-false">Desuscripto</p>
              {(moduloEncontrado.fechaPrueba7DiasUtilizada) ? (
                <p className="card__text_info">La suscripción ha durado hasta: {formatFecha(moduloEncontrado.fechaMaximaSuscripcion)}</p>
              ) : (
                <p className="card__text_info">Nunca se ha suscripto</p>
              )}
            </div>
          )}
          <p className="card__text_info">Trial 7 días utilizado: {moduloEncontrado.fechaPrueba7DiasUtilizada ? "Si, canjeado el día " + formatFecha(moduloEncontrado.fechaPrueba7DiasUtilizada) : "No"} </p>
        </div>
      );
    } else {
      return "No se ha encontrado precio.";
    }

  } catch (e) {
    return "No se ha encontrado precio.";
  }
};

export {
  Componentes,
}