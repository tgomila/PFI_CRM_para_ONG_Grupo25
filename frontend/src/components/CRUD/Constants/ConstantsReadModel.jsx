import React from 'react';

import {
    //Contacto
    IdRead,
    NombreDescripcionForContactoRead,
    NombreDescripcionForPersonaRead,
    CuitRead,
    DomicilioRead,
    EmailRead,
    TelefonoRead,
    //Persona Jurídica
    InternoTelefonoRead,
    TipoPersonaJuridicaRead,
    //Persona
    NombreRead,
    ApellidoRead,
    DniRead,
    FechaDeNacimientoRead,
    EdadRead,
    //Beneficiario
    IdOngRead,
    LegajoRead,
    LugarDeNacimientoRead,
    SeRetiraSoloRead,
    CuidadosEspecialesRead,
    EscuelaRead,
    GradoRead,
    TurnoRead,
    //Trabajador
    DatosBancariosRead,
    //Empleado
    FuncionEmpleadoRead,
    DescripcionDelEmpleadoRead,
    //Profesional
    ProfesionRead,
    //Colaborador
    AreaRead,
    //ConsejoAdHonorem
    FuncionConsejoAdHonoremRead
    //Voluntario
    //...
} from './ConstantsRead';

//Agenda

const ContactoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreDescripcionForContactoRead data={data} />
                    <EmailRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const PersonaJuridicaRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <ContactoRead data={data} />
                    <InternoTelefonoRead data={data} />
                    <TipoPersonaJuridicaRead data={data} />
                </div>
            )}
        </div>
    );
}

const PersonaRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreRead data={data} />
                    <ApellidoRead data={data} />
                    <NombreDescripcionForPersonaRead data={data} />
                    <DniRead data={data} />
                    <FechaDeNacimientoRead data={data} />
                    <EdadRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <EmailRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const BeneficiarioRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <PersonaRead data={data} />
                    <IdOngRead data={data} />
                    <LegajoRead data={data} />
                    <LugarDeNacimientoRead data={data} />
                    <SeRetiraSoloRead data={data} />
                    <CuidadosEspecialesRead data={data} />
                    <EscuelaRead data={data} />
                    <GradoRead data={data} />
                    <TurnoRead data={data} />
                </div>
            )}
        </div>
    );
}

const EmpleadoRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreRead data={data} />
                    <ApellidoRead data={data} />
                    <FuncionEmpleadoRead data={data} />
                    <DescripcionDelEmpleadoRead data={data} />
                    <NombreDescripcionForPersonaRead data={data} />
                    <DatosBancariosRead data={data} />
                    <DniRead data={data} />
                    <FechaDeNacimientoRead data={data} />
                    <EdadRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <EmailRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const ProfesionalRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreRead data={data} />
                    <ApellidoRead data={data} />
                    <ProfesionRead data={data} />
                    <DatosBancariosRead data={data} />
                    <NombreDescripcionForPersonaRead data={data} />
                    <DniRead data={data} />
                    <FechaDeNacimientoRead data={data} />
                    <EdadRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <EmailRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const ColaboradorRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreRead data={data} />
                    <ApellidoRead data={data} />
                    <AreaRead data={data} />
                    <DatosBancariosRead data={data} />
                    <NombreDescripcionForPersonaRead data={data} />
                    <DniRead data={data} />
                    <FechaDeNacimientoRead data={data} />
                    <EdadRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <EmailRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const ConsejoAdHonoremRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NombreRead data={data} />
                    <ApellidoRead data={data} />
                    <FuncionConsejoAdHonoremRead data={data} />
                    <NombreDescripcionForPersonaRead data={data} />
                    <DniRead data={data} />
                    <FechaDeNacimientoRead data={data} />
                    <EdadRead data={data} />
                    <CuitRead data={data} />
                    <DomicilioRead data={data} />
                    <EmailRead data={data} />
                    <TelefonoRead data={data} />
                </div>
            )}
        </div>
    );
}

const VoluntarioRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <PersonaRead data={data} />
                </div>
            )}
        </div>
    );
}



export {
    ContactoRead,
    PersonaJuridicaRead,
    PersonaRead,
    BeneficiarioRead,
    EmpleadoRead,
    ProfesionalRead,
    ColaboradorRead,
    ConsejoAdHonoremRead,
    VoluntarioRead
}