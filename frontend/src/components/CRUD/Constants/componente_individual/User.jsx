import React, {useEffect, useState} from 'react'
import Input from "react-validation/build/input";
import Select from "react-validation/build/select";
import { isEmail } from "validator";

import { required, IdInput, IdShowInput } from '../ConstantsInput';
import { IdRead } from '../ConstantsRead';

import { ModalSeleccionarIntegrantesPersona } from './Elegir_integrantes';

import PersonaService from '../../../../services/PersonaService';


const validName = (value) => {
    if (value.length < 1 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          El nombre debe tener entre 3 y 40 caracteres.
        </div>
      );
    }
};
  
const validEmail = (value) => {
    if (!isEmail(value)) {
        return (
        <div className="alert alert-danger" role="alert">
            Este no es un email válido
        </div>
        );
    }
    if (value.length < 1 || value.length > 40) {
        return (
          <div className="alert alert-danger" role="alert">
            El mail debe tener un máximo de 40 caracteres.
          </div>
        );
    }
};
  
const validUsername = (value) => {
    if (value.length < 3 || value.length > 15) {
      return (
        <div className="alert alert-danger" role="alert">
          El username debe tener entre 3 y 15 caracteres.
        </div>
      );
    }
};
  
const validPassword = (value) => {
    if (value.length < 6 || value.length > 20) {
      return (
        <div className="alert alert-danger" role="alert">
          La contraseña debe contener entre 6 y 20 caracteres.
        </div>
      );
    }
};
  
  
//   const validTenantOrClientId = (value) => {
//     if (value.length < 1 || value.length > 20) {
//       return (
//         <div className="alert alert-danger" role="alert">
//           The username must be between 3 and 20 characters.
//         </div>
//       );
//     }
//   };

  

const NameInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Nombre: </label>
            <Input disabled={disabled} placeholder="Name" name="name" type="text" className="form-control" 
                value={data.name} onChange={handleInputChange} validations={[required, validName]}/>
        </div>
    );
};

const NameRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Nombre:
                <br />
            {data.name}</label>
        </div>
    );
};

const UsernameInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Username: </label>
            <Input disabled={disabled} placeholder="Username" name="username" type="text" className="form-control" 
                value={data.username} onChange={handleInputChange} validations={[required, validUsername]}/>
        </div>
    );
};

const UsernameRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Username:
                <br />
            {data.username}</label>
        </div>
    );
};

const RoleMasValuadoRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Rol:
                <br />
            {data.roleMasValuado}</label>
        </div>
    );
};

const EmailInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <label> Email: </label>
            <Input disabled={disabled} placeholder="Email" name="email" type="text" className="form-control" 
                value={data.email} onChange={handleInputChange} validations={[required, validEmail]}/>
        </div>
    );
};

const EmailRead = ({data}) => {
    return(
        <div className = "form-group">
            <label> Email:
                <br />
            {data.email}</label>
        </div>
    );
};

const PasswordInput = ({ disabled, data, handleInputChange }) => {
    const[contraseña1, setContraseña1] = useState('');
    const[contraseña2, setContraseña2] = useState('');

    const handlePasswordChange = (e) => {
        setContraseña1(e.target.value);
    };

    const handleConfirmPasswordChange = (e) => {
        setContraseña2(e.target.value);
    };

    useEffect(() => {

        if (contraseña1 !== '' && contraseña1 === contraseña2) {
            handleInputChange({ target: { name: 'password', value: contraseña2 } });
        }
        else {
            handleInputChange({ target: { name: 'password', value: '' } });
        }
    }, [contraseña1, contraseña2]);

    return (
        <div className="form-group">
            <label> Contraseña: </label>
            {/* <input 
                placeholder="Contraseña" name="password" type="password" className="form-control" 
                onChange={handlePasswordChange} validations={[required, validPassword]}
            />
            <label> Confirmar Contraseña: </label>
            <input 
                placeholder="Confirmar Contraseña" name="confirmPassword" type="password" className="form-control" 
                onChange={handleConfirmPasswordChange} validations={[required, validPassword]}
            /> */}
            <Input disabled={disabled} placeholder="Contraseña" name="password" type="password" className="form-control" 
                onChange={handleInputChange} validations={[required, validPassword]}/>
        </div>
    );
};

const RoleInput = ({ disabled, data, handleInputChange }) => {
    const [selectedOption, setSelectedOption] = useState(data.roles.length > 0 ? data.roles[0] : '');

    const handleChange = (event) => {
        const newValue = event.target.value;
        setSelectedOption(newValue);
        handleInputChange({ target: { name: 'roles', value: [newValue] } });
    };
    
    return(
        <div className = "form-group">
            <label> Rol: </label>
            <select 
            disabled={disabled} 
            name="roles" 
            value={selectedOption} 
            className="form-control" 
            onChange={handleChange}
            //validations={[required]}
            >
                <option value="">Seleccione</option>
                <option value="ROLE_DEFAULT">Rol default indefinido</option>
                <option value="ROLE_USER">Rol usuario</option>
                <option value="ROLE_PROFESIONAL">Rol Profesional</option>
                <option value="ROLE_EMPLOYEE">Rol Empleado</option>
                <option value="ROLE_ADMIN">Rol Administrador</option>
            </select>
        </div>
    );
};

const ContactoInput = ({ disabled, data, handleInputChange }) => {
    return(
        <div className = "form-group">
            <ModalSeleccionarIntegrantesPersona
                integrantesActuales = {data.contacto}
                ServiceDeIntegrantes = {PersonaService}
                handleInputChange = {handleInputChange}
                nombreHandleInputChange = {"contacto"}
                maxIntegrantesSelected = {1}
                isEditable = {!disabled}
                el_la = {"la"}
                nombreTipoIntegrante = {"persona"}
                nombreTipoIntegrantePrural = {"personas"}
            />
        </div>
    );
};

//Sign Up
const cargarUserSinIniciarSesionDefault = {
    name: "",
    username: "",
    email: "",
    password: "",
    tenantOrClientId: "",//Esto que lo agregue el service
}

//Modificar por parte del admin
const cargarUserDefault = {
    id: "",
    name: "",
    username: "",
    roleMasValuado: "",
    email: "",
    contacto: null,
    roles: [],
}

const UserCreateSinIniciarSesionInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NameInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <UsernameInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <PasswordInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            {/* <TenantOrClientIdInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} /> */}
        </div>
    );
}

const UserCreateInput = ({ searchEncontrado, data, handleInputChange }) => {
    return(
        <div>
            <IdShowInput show={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <NameInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <UsernameInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <EmailInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <PasswordInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            {/* <TenantOrClientIdInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} /> */}
            <ContactoInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
            <RoleInput disabled={searchEncontrado} data={data} handleInputChange={handleInputChange} />
        </div>
    );
}

const UserUpdateInput = ({ data, handleInputChange }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdShowInput show={false} data={data} handleInputChange={handleInputChange} />
                    <NameInput data={data} handleInputChange={handleInputChange} />
                    <UsernameInput disabled={true} data={data} handleInputChange={handleInputChange} />
                    <EmailInput disabled={true} data={data} handleInputChange={handleInputChange} />
                    <ContactoInput disabled={false} data={data} handleInputChange={handleInputChange} />
                    <RoleInput disabled={false} data={data} handleInputChange={handleInputChange} />
                </div>
            )}
        </div>
    );
}

const UserRead = ({ data }) => {
    return(
        <div>
            {data.id && (
                <div>
                    <IdRead data={data} />
                    <NameRead data={data} />
                    <UsernameRead data={data} />
                    <RoleMasValuadoRead data={data} />
                    <EmailRead data={data} />
                    <ContactoInput disabled={true} data={data} />
                    <RoleInput disabled={true} data={data} />
                </div>
            )}
        </div>
    );
}



export {
    cargarUserSinIniciarSesionDefault,
    UserCreateSinIniciarSesionInput,
    cargarUserDefault,
    UserCreateInput,
    UserUpdateInput,
    UserRead,
}