import React, { useState, useRef, useEffect } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import { isEmail } from "validator";
import { cargarUserSinIniciarSesionDefault, UserCreateSinIniciarSesionInput } from "../CRUD/Constants/componente_individual/User";

import { useNavigate } from 'react-router-dom';
import AuthService from "../../services/auth.service";
import TenantService from "../../services/TenantService";


import "./Login.css";
import "../../Styles/CRUD.scss";

// import { required } from "../CRUD/Constants/ConstantsInput";

// const vname = (value) => {
//   if (value.length < 1 || value.length > 40) {
//     return (
//       <div className="alert alert-danger" role="alert">
//         The username must be between 3 and 40 characters.
//       </div>
//     );
//   }
// };

// const validEmail = (value) => {
//   if (!isEmail(value)) {
//     return (
//       <div className="alert alert-danger" role="alert">
//         This is not a valid email.
//       </div>
//     );
//   }
// };

// const vusername = (value) => {
//   if (value.length < 3 || value.length > 20) {
//     return (
//       <div className="alert alert-danger" role="alert">
//         The username must be between 3 and 20 characters.
//       </div>
//     );
//   }
// };

// const vpassword = (value) => {
//   if (value.length < 6 || value.length > 40) {
//     return (
//       <div className="alert alert-danger" role="alert">
//         The password must be between 6 and 40 characters.
//       </div>
//     );
//   }
// };


// const vtenantOrClientId = (value) => {
//   if (value.length < 1 || value.length > 20) {
//     return (
//       <div className="alert alert-danger" role="alert">
//         The username must be between 3 and 20 characters.
//       </div>
//     );
//   }
// };



const Register = () => {

  const [tenants, setTenants] = useState([]);
  const [tenantsImage, setTenantsImage] = useState([]);

  useEffect(() => {
    TenantService.getAll().then((res) => {
      setTenants(res.data);
      const updatedTenantsImage = res.data.map((tenant) => {
        const imageUrl = `http://localhost:8080/logo/${tenant.dbName}.png`;
        return {
          ...tenant,
          imageLogo: imageUrl,
          imageUrl: `http://localhost:8080/logo/${tenant.dbName}.png`,
        };
      });
      setTenantsImage(updatedTenantsImage);
    });

  }, []);

  // const [name, setName] = useState("");
  // const [username, setUsername] = useState("");
  // const [email, setEmail] = useState("");
  // const [password, setPassword] = useState("");
  // const [tenantOrClientId, setTenantOrClientId] = useState("");
  // const [successful, setSuccessful] = useState(false);
  let navigate = useNavigate();
  const form = useRef();
  const checkBtn = useRef();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const [datos, setDatos] = useState(cargarUserSinIniciarSesionDefault);
  const [submitted, setSubmitted] = useState(false);
  const [submittedMessage, setSubmittedMessage] = useState("");


  // const onChangeName = (e) => {
  //   const name = e.target.value;
  //   setName(name);
  // };

  // const onChangeUsername = (e) => {
  //   const username = e.target.value;
  //   setUsername(username);
  // };

  // const onChangeEmail = (e) => {
  //   const email = e.target.value;
  //   setEmail(email);
  // };

  // const onChangePassword = (e) => {
  //   const password = e.target.value;
  //   setPassword(password);
  // };

  // const onChangeTenantOrClientId = (e) => {
  //   const tenantOrClientId = e.target.value;
  //   setTenantOrClientId(tenantOrClientId);
  // };


  const handleSubmit = (e) => {
    e.preventDefault();

    setMessage("");
    setSubmitted(false);
    setLoading(true);

    form.current.validateAll();

    if (checkBtn.current.context._errors.length === 0) {
      AuthService.register(datos.name, datos.username, datos.email, datos.password, datos.tenantOrClientId).then(
        (response) => {
          console.log("response");
          console.log(response);
          setMessage(response.data.message);
          setLoading(true);
          setSubmitted(true);
          setSubmittedMessage("¡Has creado el usuario!");
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setMessage(resMessage);
          setLoading(false);
          setSubmittedMessage("Error al crear el usuario, mensaje de error: " + resMessage);
        }
      );
    }
  };

  const handleLogin = (e) => {

    //PreventDefault hace que el dato no viaje en el url link expuesto
    e.preventDefault();

    setMessage("");
    setLoading(true);

    if (submitted) {
      AuthService.login(datos.username, datos.password, datos.tenantOrClientId).then(
        () => {
          navigate("/");
          window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setLoading(false);
          setMessage(resMessage === "Bad credentials" ? "Usuario o Contraseña incorrecta!" : 
          resMessage === "Request failed with status code 400" ? "Seleccione Tenant!" : resMessage);
        }
      );
    } else {
      setLoading(false);
    }
  };

  // return (
  //   <div className="submit-form">
  //     <div className = "row">
  //       <div className = "card col-md-6 offset-md-3 offset-md-3">
  //         <div className="login-box">
  //           <h2>Registro</h2>
  //           <div className="">
  //             <img
  //               src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
  //               alt="profile-img"
  //               className="profile-img-card"
  //             />

  //             <Form onSubmit={handleRegister} ref={form}>
  //               {!successful && (
  //                 <div>


  //                   <div className="user-box">
  //                     <label className="miLabel"  htmlFor="name">Nombre:</label>
  //                     <Input
  //                       type="text"
  //                       className="miInput"
  //                       name="name"
  //                       value={name}
  //                       onChange={onChangeName}
  //                       validations={[required, vname]}
  //                     />
  //                   </div>


  //                   <div className="user-box">
  //                     <label className="miLabel"  htmlFor="username">Username:</label>
  //                     <Input
  //                       type="text"
  //                       className="miInput"
  //                       name="username"
  //                       value={username}
  //                       onChange={onChangeUsername}
  //                       validations={[required, vusername]}
  //                     />
  //                   </div>

  //                   <div className="user-box">
  //                     <label className="miLabel"  htmlFor="email">Email:</label>
  //                     <Input
  //                       type="text"
  //                       className="miInput"
  //                       name="email"
  //                       value={email}
  //                       onChange={onChangeEmail}
  //                       validations={[required, validEmail]}
  //                     />
  //                   </div>

  //                   <div className="user-box">
  //                     <label className="miLabel"  htmlFor="password">Password:</label>
  //                     <Input
  //                       type="password"
  //                       className="miInput"
  //                       name="password"
  //                       value={password}
  //                       onChange={onChangePassword}
  //                       validations={[required, vpassword]}
  //                     />
  //                   </div>

  //                   <div className="user-box">
  //                     <label className="miLabel"  htmlFor="tenantOrClientId">Tenant o ClientId:</label>
  //                     <Input
  //                       type="text"
  //                       className="miInput"
  //                       name="tenantOrClientId"
  //                       value={tenantOrClientId}
  //                       onChange={onChangeTenantOrClientId}
  //                       validations={[required, vtenantOrClientId]}
  //                     />
  //                   </div>

  //                   <div className="user-box">
  //                     <button className="miBoton">
  //                     <span></span>
  //                       <span></span>
  //                       <span></span>
  //                       <span></span>
                        
  //                       INGRESAR</button>
  //                   </div>
  //                 </div>
  //               )}

  //               {message && (
  //                 <div className="user-box">
  //                   <div
  //                     className={
  //                       successful ? "alert alert-success" : "alert alert-danger"
  //                     }
  //                     role="alert"
  //                   >
  //                     {message}
  //                   </div>
  //                 </div>
  //               )}
  //               <CheckButton style={{ display: "none" }} ref={checkBtn} />
  //             </Form>
  //           </div>
  //         </div>
  //       </div>
  //     </div>
  //   </div>
  // );

  const handleInputChange = event => {
    //Trae literalmente copia de "<Input", pero con value reemplazado con el value que escribió el usuario.
    const { name, value } = event.target;
    setDatos({ ...datos, [name]: value });
  };

  const goLogin = () => {
    navigate("/login");//ejemplo "/personafisica"
    //window.location.reload();
  }

  return (
    <div className="submit-form">
        <div className = "row">
            <div className = "card col-md-6 offset-md-3 offset-md-3">
              {!submitted ? (
                <div className = "card-body">
                  <div className="form-group">
                    <Form onSubmit={handleSubmit} ref={form}>
                      <UserCreateSinIniciarSesionInput
                        data={datos} 
                        handleInputChange={handleInputChange} 
                      />

                      <div className="user-box" style={{ display: 'flex' }}>
                        <div>
                          <label className="miLabel" htmlFor="tenantOrClientId">
                            Seleccione ONG:
                          </label>

                          <div className="radio-container">
                            {tenantsImage.map((tenantItem) => (
                              <div className="radio-item" key={tenantItem.tenantClientId}>
                                <input
                                  className="radio-input"
                                  type="radio"
                                  name={"tenantOrClientId"}
                                  value={tenantItem.tenantClientId}
                                  onChange={handleInputChange}
                                  id={tenantItem.tenantClientId}
                                />
                                <label htmlFor={tenantItem.tenantClientId} className="radio-label">
                                  <div className="radio-content">
                                    <img src={tenantItem.imageUrl} className="radio-img" alt={tenantItem.tenantName} style={{ zIndex: 0 }} />
                                    <span className="miLabel">{tenantItem.tenantName}</span>
                                  </div>
                                </label>
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>
                        
                      <div className="form-group">
                        <button className="btn btn-success" href="#" disabled={loading}>
                          <span></span>
                          <span></span>
                          <span></span>
                          <span></span>
                          {loading && (
                            <span className="spinner-border spinner-border-sm"></span>
                          )}
                          Crear usuario
                        </button>
                      </div>
                        
                      {message && (
                          <div className="form-group">
                              <div className="alert alert-danger" role="alert">
                                  {message}
                              </div>
                          </div>
                      )}
                      <CheckButton style={{ display: "none" }} ref={checkBtn} />
                    </Form>
                  </div>
                  <button className="btn btn-info" onClick={goLogin} style={{marginLeft: "00px"}}>
                    Ir a login
                  </button>
                </div>
            ) : (
              <div className="col-12">
                <h4 className = "col-12">{submittedMessage}</h4>
                <button className="btn btn-success mt-2 mx-2 col-12 col-lg-4" onClick={handleLogin}>
                    Loguearse con cuenta registrada
                </button>
                <button className="btn btn-back mt-2 mb-2 col-12 col-lg-4" onClick={goLogin}>
                  Ir a login
                </button>
              </div>
            )}
          </div>
        </div>
        <br></br><br></br>
    </div>
) ;
};

export default Register;
