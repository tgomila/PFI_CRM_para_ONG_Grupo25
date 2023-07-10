//No sirve, porque palabras como acción o avión no se puede saber si es 'la' o 'el' de forma automática.
//Sin incluir la palabra completa.

const determinarPalabraPlural = (palabra) => {
  const ultimaLetra = palabra.slice(-1);
  const ultimasDosLetras = palabra.slice(-2);

  // Reglas para formar el plural en español
  if (ultimasDosLetras === 'ón') {
    return palabra.slice(0, -2) + 'ones'; // e.g. "lección" -> "lecciones"
  } else if (ultimaLetra === 's') {
    return palabra; // La palabra ya está en plural
  } else if (ultimaLetra === 'z') {
    return palabra.slice(0, -1) + 'ces'; // e.g. "luz" -> "luces"
  } else if (ultimasDosLetras === 'ad') {
    return palabra.slice(0, -2) + 'ades'; // e.g. "ciudad" -> "ciudades"
  } else {
    return palabra + 's'; // Agregar "s" al final por defecto
  }
}

//evitar acéntos
const determinarArticuloSingular = (palabra) => {
  const primerLetra = palabra.charAt(0).toLowerCase();
  const ultimaLetra = palabra.slice(-1).toLowerCase();
  const ultimasDosLetras = palabra.slice(-2).toLowerCase();
  const anteUltimaLetra = ultimasDosLetras.charAt(0);

  // Reglas para determinar el artículo en español
  if (primerLetra === 'a' || primerLetra === 'e' || primerLetra === 'i' || primerLetra === 'o' || primerLetra === 'u') {
    if(ultimaLetra === 'e' || ultimaLetra === 'i' || ultimaLetra === 'o' || ultimaLetra === 'u') {
      return 'el';//Empleado, usuario
    }
    if(ultimaLetra === 'a') {
      return 'la';//entrevista, actividad
    }
  }
  //avión, acción... bueno ya con esto descarto que se pueda automatizar a menos que se ponga a mano.
  if(ultimaLetra === 'a') {
    return 'la';//pastilla
  }
  if (ultimaLetra === 'e' || ultimaLetra === 'i' || ultimaLetra === 'o' || ultimaLetra === 'u') {
    return 'el';//pase, paty, cosmético, menú
  }
  if (ultimasDosLetras === 'ón' || ultimasDosLetras === 'on') {
    return 'la'; // e.g. "lección" -> "lecciones"
  }
  if (anteUltimaLetra === 'a' || anteUltimaLetra === 'u') {
    return 'la'; // paz, salud
  }
  if (anteUltimaLetra === 'e' || anteUltimaLetra === 'i' || anteUltimaLetra === 'o') {
    return 'el'; // pez, budín, jamón
  }
  if (ultimasDosLetras === 'ad') {
    return palabra.slice(0, -2) + 'ades'; // e.g. "ciudad" -> "ciudades"
  } else {
    return palabra + 's'; // Agregar "s" al final por defecto
  }
}

const determinarArticuloPlural = (palabra) => {
  const articuloSingular = determinarArticuloSingular(palabra);
  if(articuloSingular === 'la') {
    return 'las';
  } else{
    return 'los'
  }
}

//Ejemplo adj 'nuevo' sust 'computadora' devuelve 'nueva'
const obtenerAdjetivoConcordante = (adj, sust) => {
  const ultimaLetra = sust.charAt(sust.length - 1).toLowerCase();
  const articuloSingularSust = determinarArticuloSingular(sust); //Me dice si es masculino o femenino

  // Determinar el género del sustantivo
  if (ultimaLetra === 'a') {
    return adj + 'a'; // Femenino singular
  } else {
    return adj; // Masculino singular o cualquier otro caso
  }
}

// Uso de la función
const adjetivo = 'nuevo';
const sustantivo1 = 'computadora';
const sustantivo2 = 'automovil';

const adjetivoConcordante1 = obtenerAdjetivoConcordante(adjetivo, sustantivo1);
const adjetivoConcordante2 = obtenerAdjetivoConcordante(adjetivo, sustantivo2);

console.log(adjetivoConcordante1); // "nueva"
console.log(adjetivoConcordante2); // "nuevo"


