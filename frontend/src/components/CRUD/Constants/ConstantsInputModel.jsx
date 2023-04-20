import React from 'react';
import {
    //Contacto
    //IdInput,
    IdShowInput,
    NombreDescripcionForContactoInput,
    NombreDescripcionForPersonaInput,
    CuitInput,
    DomicilioInput,
    EmailInput,
    TelefonoInput,
    //Persona
    NombreInput,
    ApellidoInput,
    DniInput,
    FechaDeNacimientoInput,
    //Beneficiario
    IdOngInput,
    LegajoInput,
    LugarDeNacimientoInput,
    SeRetiraSoloInput,
    CuidadosEspecialesInput,
    EscuelaInput,
    GradoInput,
    TurnoInput
} from './ConstantsInput';

//Agenda
const ContactoCreateInput = ({ contactoSearchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NombreDescripcionForContactoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <CuitInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DomicilioInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TelefonoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ContactoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={"false"} data={data} handleInputChange={handleInputChange} />
                    <NombreDescripcionForContactoInput disabled={"false"} data={data} handleInputChange={handleInputChange} />
                    <CuitInput disabled={"false"} data={data} handleInputChange={handleInputChange} />
                    <DomicilioInput disabled={"false"} data={data} handleInputChange={handleInputChange} />
                    <EmailInput disabled={"false"} data={data} handleInputChange={handleInputChange} />
                    <TelefonoInput disabled={"false"} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}


const PersonaCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NombreInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <ApellidoInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <DniInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <FechaDeNacimientoInput disabled={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <CuitInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <DomicilioInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TelefonoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NombreDescripcionForPersonaInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const PersonaUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={"true"} data={data} handleInputChange={handleInputChange} />
                    <NombreInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <ApellidoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <DniInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <FechaDeNacimientoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <CuitInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <DomicilioInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <EmailInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <TelefonoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <NombreDescripcionForPersonaInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}


const BeneficiarioCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <PersonaCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <IdOngInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <LegajoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <LugarDeNacimientoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <SeRetiraSoloInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <CuidadosEspecialesInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EscuelaInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <GradoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <TurnoInput disabled={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const BeneficiarioUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            <PersonaUpdateInput data={data} handleInputChange={handleInputChange}/>
            {data.id && (
                <div>
                    <IdOngInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <LegajoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <LugarDeNacimientoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <SeRetiraSoloInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <CuidadosEspecialesInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <EscuelaInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <GradoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <TurnoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}



export {
    ContactoCreateInput,
    ContactoUpdateInput,
    PersonaCreateInput,
    PersonaUpdateInput,
    BeneficiarioCreateInput,
    BeneficiarioUpdateInput
}