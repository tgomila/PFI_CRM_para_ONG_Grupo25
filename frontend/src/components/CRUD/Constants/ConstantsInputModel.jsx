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
    //PersonaJuridica
    InternoTelefonoInput,
    TipoPersonaJuridicaInput,
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
    TurnoInput,
    //Trabajador
    DatosBancariosInput,
    //Empleado
    FuncionInput,
    DescripcionEmpleadoInput,
    //Profesional
    ProfesionInput,
    //Colaborador
    AreaInput,
    //Consejo ad honorem
    FuncionConsejoAdHonoremInput,
    //Voluntario
    //...no tiene items independientes.
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

const PersonaJuridicaCreateInput = ({ contactoSearchEncontrado, personaJuridicaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <ContactoCreateInput contactoSearchEncontrado={contactoSearchEncontrado} data={data} handleInputChange={handleInputChange} />
            <InternoTelefonoInput disabled={personaJuridicaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <TipoPersonaJuridicaInput disabled={personaJuridicaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const PersonaJuridicaUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            <ContactoUpdateInput data={data} handleInputChange={handleInputChange}/>
            {data.id && (
                <div>
                    <InternoTelefonoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <TipoPersonaJuridicaInput disabled={""} data={data} handleInputChange={handleInputChange} />
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
            <IdOngInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <LegajoInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <LugarDeNacimientoInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <SeRetiraSoloInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <CuidadosEspecialesInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <EscuelaInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <GradoInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <TurnoInput disabled={""} data={data} handleInputChange={handleInputChange} />
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

//No hay models de este caso
const TrabajadorAbstractCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <PersonaCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <DatosBancariosInput disabled={""} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const TrabajadorAbstractUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            <PersonaUpdateInput data={data} handleInputChange={handleInputChange}/>
            {data.id && (
                <div>
                    <DatosBancariosInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const EmpleadoCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <TrabajadorAbstractCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <FuncionInput disabled={""} data={data} handleInputChange={handleInputChange} />
            <DescripcionEmpleadoInput disabled={""} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const EmpleadoUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <TrabajadorAbstractUpdateInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <FuncionInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <DescripcionEmpleadoInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ProfesionalCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <TrabajadorAbstractCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <ProfesionInput disabled={""} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ProfesionalUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <TrabajadorAbstractUpdateInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <ProfesionInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ColaboradorCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <TrabajadorAbstractCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <AreaInput disabled={""} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ColaboradorUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <TrabajadorAbstractUpdateInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <AreaInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const ConsejoAdHonoremCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <PersonaCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
            <FuncionConsejoAdHonoremInput disabled={""} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const ConsejoAdHonoremUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <PersonaUpdateInput disabled={""} data={data} handleInputChange={handleInputChange} />
                    <FuncionConsejoAdHonoremInput disabled={""} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const VoluntarioCreateInput = ({ contactoSearchEncontrado, personaSearchEncontrada, data, handleInputChange }) => {
    return(
        <div>
            <TrabajadorAbstractCreateInput contactoSearchEncontrado={contactoSearchEncontrado} personaSearchEncontrada={personaSearchEncontrada} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const VoluntarioUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            <PersonaUpdateInput data={data} handleInputChange={handleInputChange}/>
            {data.id && (
                <div>
                </div>
            )}
        </div>
    );
}



export {
    ContactoCreateInput,
    ContactoUpdateInput,
    //Persona jurídica
    PersonaJuridicaCreateInput,
    PersonaJuridicaUpdateInput,
    //Persona
    PersonaCreateInput,
    PersonaUpdateInput,
    //Beneficiario
    BeneficiarioCreateInput,
    BeneficiarioUpdateInput,
    //Empleado
    EmpleadoCreateInput,
    EmpleadoUpdateInput,
    //Profesional
    ProfesionalCreateInput,
    ProfesionalUpdateInput,
    //Colaborador
    ColaboradorCreateInput,
    ColaboradorUpdateInput,
    //ConsejoAdHonorem
    ConsejoAdHonoremCreateInput,
    ConsejoAdHonoremUpdateInput,
    //Voluntario
    VoluntarioCreateInput,
    VoluntarioUpdateInput,
}