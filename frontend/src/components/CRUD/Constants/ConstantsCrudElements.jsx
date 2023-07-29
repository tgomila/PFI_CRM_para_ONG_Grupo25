import React, { useState, useEffect, useRef } from 'react'
import ImageService from '../../../services/ImageService';
import { OverlayTrigger, Tooltip, Image } from "react-bootstrap";

import "../../../Styles/CRUD.scss";

//Este js es para "Create" y "Update" en gran parte.

/**
 * 
 * @param id es numero: 1,2...534... etc
 * @param visibilidad es "EDITAR" para mostrar botones de modificar foto, o "SOLO_VISTA" para no mostrar botones
 * @returns 
 */
const FotoPerfil = ({ id, setFotoSubida, visibilidad, tipoFoto }) => {
    const [foto, setFoto] = useState(null);
    const [isFotoClicked, setIsFotoClicked] = useState(false);
    const [isFotoDefault, setIsFotoDefault] = useState(true);//Solo sirve para mostrar foto chica (default) si no existe foto, o grande (si existe foto).
    const handleFotoClick = () => {
        setIsFotoClicked(!isFotoClicked);
    };
    const fileInputRef = useRef(null);
    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
          setFoto(URL.createObjectURL(file));
          setFotoSubida(URL.createObjectURL(file));
          setIsFotoDefault(false);
          console.log("Se cambió la foto");
          console.log(file);
          console.log(URL.createObjectURL(file));
        }
    };
    useEffect(() => {
        if(id){
            ImageService.getFoto(id, tipoFoto, 'completa').then((res) => {
                setFoto(res);
                setIsFotoDefault(false);
            });
        }
        else{
            setFoto(ImageService.getDefaultImage(tipoFoto));
            setIsFotoDefault(true);
        }
    }, [id]);
    return(
        <div className = "form-group">
            {foto ? (
                <div>
                    {/*<img 
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
                    />*/}
                    
                    <OverlayTrigger
                      placement="right"
                      overlay={
                        <Tooltip>{isFotoClicked ? 'Click para ver foto en tamaño completo' : 'Click para ver foto circular como si estuviera en vista de tabla'}</Tooltip>
                      }
                    >
                        <Image
                        src={foto}
                        alt="Foto de perfil"
                        className={`contacto-img-crud ${isFotoClicked ? 'clicked' : ''}`}
                        style={isFotoClicked ? {
                            borderRadius: '50%',
                            MozBorderRadius: '50%',
                            WebkitBorderRadius: '50%',
                            aspectRatio: '1/1',
                            width: (!id && isFotoDefault) ? '35%' : '100%',
                        } : {
                            width: (!id && isFotoDefault) ? '35%' : '100%',
                        }}
                        onClick={handleFotoClick}
                        />
                    </OverlayTrigger>
                    {visibilidad === 'EDITAR' && (
                    <div style={!id ? { display: "flex", justifyContent: "center" } : {}}>
                        <input
                        type="file"
                        accept="image/*"
                        ref={fileInputRef}
                        style={{ display: "none" }}
                        onChange={handleFileChange}
                        />
                        <button className="btn btn-info" onClick={() => fileInputRef.current.click()}>
                        Agregar/Modificar foto
                        </button>
                    </div>
                    )}
                </div>
            ) : (
                <div>
                    <span className="spinner-border spinner-border-sm"></span>
                    <p>Cargando foto<br />de perfil...</p>
                </div>
            )}
        </div>
    );
};



export {
    FotoPerfil,
}