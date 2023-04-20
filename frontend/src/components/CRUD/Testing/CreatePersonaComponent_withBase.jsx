import React from 'react'
import PersonaService from '../../../services/PersonaService';
import { cargarPersonaDefault } from '../Constants/ConstantsCargarDefault';
import { PersonaCreateInput } from '../Constants/ConstantsInputModel';
import CreateBaseComponent from './CreateBaseComponent';



function CreatePersonaComponent() {
    
    return (
        <CreateBaseComponent cargarBaseDefault={cargarPersonaDefault} BaseCreateInput={PersonaCreateInput} Service={PersonaService} el_la="la" nombreBase="personaaa" direccionTable="/personafisica" />
    );
};

export default CreatePersonaComponent;