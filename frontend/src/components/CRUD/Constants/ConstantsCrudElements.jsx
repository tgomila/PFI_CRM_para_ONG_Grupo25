import React, { useState, useEffect, useRef } from 'react'
import ImageService from '../../../services/ImageService';

import "../../../Styles/CRUD.scss";

//Este js es para "Create" y "Update" en gran parte.

/**
 * 
 * @param id es numero: 1,2...534... etc
 * @param visibilidad es "EDITAR" para mostrar botones de modificar foto, o "SOLO_VISTA" para no mostrar botones
 * @returns 
 */
const FotoPerfil = ({ id, visibilidad }) => {
    const [foto, setFoto] = useState(null);
    const [isFotoClicked, setIsFotoClicked] = useState(false);
    const handleFotoClick = () => {
        setIsFotoClicked(!isFotoClicked);
    };
    const fileInputRef = useRef(null);
    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
          setFoto(URL.createObjectURL(file));
          console.log("Se cambió la foto");
        }
    };
    useEffect(() => {
        
        ImageService.getFoto(id, "contacto", 'completa').then((res) => {
            setFoto(res);
        });
    }, [id]);
    return(
        <div className = "form-group">
            {foto ? (
                <img 
                src={foto} 
                alt="Foto de perfil" 
                className={`contacto-img-crud ${isFotoClicked ? "clicked" : ""}`}
                style={isFotoClicked ? {
                    borderRadius: "50%",
                    MozBorderRadius: "50%",
                    WebkitBorderRadius: "50%",
                    aspectRatio: "1/1",
                } : {}}
                onClick={handleFotoClick}
                //style={{width: '272px', height: '272px'}}
                />
            ) : (
                <p>Cargando foto<br />de perfil...</p>
            )}
            <div>
              <input
                type="file"
                accept="image/*"
                ref={fileInputRef}
                style={{ display: "none" }}
                onChange={handleFileChange}
              />
              <button onClick={() => fileInputRef.current.click()}>
                Agregar/Modificar foto
              </button>
            </div>
        </div>
    );
};



export {
    //Otros
    FotoPerfil,
}