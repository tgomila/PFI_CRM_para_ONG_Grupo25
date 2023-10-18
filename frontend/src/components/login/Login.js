import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import TenantService from "../../services/TenantService";

import AuthService from "../../services/auth.service";

import "../../Styles/Login.scss";

import { ModalSeleccionarTenant } from "../CRUD/Constants/componente_individual/Elegir_integrantes";

import { required } from "../CRUD/Constants/ConstantsInput";
import * as constantsURL from "../../components/constants/ConstantsURL";
const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;

//const Login = () => {
function Login() {

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

  console.log("Tenants");
  console.log(tenants);

  let navigate = useNavigate();

  const form = useRef();
  const checkBtn = useRef();

  const [usernameOrEmail, setUsernameOrEmail] = useState("");
  const [password, setPassword] = useState("");
  const [tenantOrClientId, setTenantOrClientId] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const onChangeUsernameOrEmail = (e) => {
    const usernameOrEmail = e.target.value;
    setUsernameOrEmail(usernameOrEmail);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const onChangeTenantOrClientId = (e) => {
    const tenantOrClientId = e.target.value;
    setTenantOrClientId(tenantOrClientId);
  };

  const handleLogin = (e) => {

    //PreventDefault hace que el dato no viaje en el url link expuesto
    e.preventDefault();

    setMessage("");
    setLoading(true);

    form.current.validateAll();

    if (checkBtn.current.context._errors.length === 0) {
      AuthService.login(usernameOrEmail, password, tenantOrClientId).then(
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

  return (
    <div className="Login">
      <div className="login-box">
        <h2>Iniciar Sesión</h2>
        <img
          src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
          alt="profile-img"
          className="profile-img-card"
        />

        <Form onSubmit={handleLogin} ref={form}>
          <div className="user-box">
            <label className="miLabel" htmlFor="usernameOrEmail">
              Nombre de usuario o email:
            </label>
            <Input
              type="text"
              className="miInput"
              name="usernameOrEmail"
              value={usernameOrEmail}
              onChange={onChangeUsernameOrEmail}
              validations={[required]}
            />
          </div>

          <div className="user-box">
            <label className="miLabel" htmlFor="password">
              Contraseña:
            </label>
            <Input
              type="password"
              className="miInput"
              name="password"
              value={password}
              onChange={onChangePassword}
              validations={[required]}
            />
          </div>

          {/* <ModalSeleccionarTenant handleInputChange={onChangeTenantOrClientId} maxIntegrantesSelected = {1}/> */}

          <div className="user-box" style={{ display: 'flex' }}>
            {/* <div className="left-part" style={{ flexBasis: '100%' }}> */}
            <div>
              <label className="miLabel" htmlFor="tenantOrClientId">
                Seleccione ONG:
              </label>

              {/* <ul>
              {tenantsImage.map((tenantItem) => (
                        <>
                        <li className="radio-li">
                          <input
                            //<Input
                            className="radio-input"
                            type="radio"
                            name={"tenantOrClientId"}
                            value={tenantItem.tenantClientId}
                            onChange={onChangeTenantOrClientId}
                            id={tenantItem.tenantClientId}
                            //validations={[required]}
                          />
                          <label for={tenantItem.tenantClientId} className="radio-label">
                            <img src={tenantItem.imageUrl} className="radio-img" />
                          </label>
                        </li>
                        </>
              ))}
              </ul> */}

              <div className="radio-container">
                {tenantsImage.map((tenantItem) => (
                  <div className="radio-item" key={tenantItem.tenantClientId}>
                    <input
                      className="radio-input"
                      type="radio"
                      name={"tenantOrClientId"}
                      value={tenantItem.tenantClientId}
                      onChange={onChangeTenantOrClientId}
                      id={tenantItem.tenantClientId}
                    />
                    <label htmlFor={tenantItem.tenantClientId} className="radio-label">
                      <div className="radio-content">
                        <img src={tenantItem.imageUrl} className="radio-img" alt={tenantItem.tenantName} />
                        <span className="miLabel">{tenantItem.tenantName}</span>
                      </div>
                    </label>
                  </div>
                ))}
                <div className="radio-item" key={0}>
                  <input
                    className="radio-input"
                    type="radio"
                    name={"tenantOrClientId"}
                    value={0}
                    onChange={onChangeTenantOrClientId}
                    id={0}
                  />
                  <label htmlFor={0} className="radio-label">
                    <div className="radio-content">
                      <img src={BACKEND_STATIC_BASE_URL + "logo/logo-CosmosCRM.png"} className="radio-img" alt={"Cosmos Administration"} />
                      <span className="miLabel">{"Cosmos Administration"}</span>
                    </div>
                  </label>
                </div>
              </div>

              {/* <table>
                <tbody>
                  {tenantsImage.map((tenantItem) => (
                    <tr>
                      <td>
                        <li className="radio-li">
                          <input
                            //<Input
                            className="radio-input"
                            type="radio"
                            name={"tenantOrClientId"}
                            value={tenantItem.tenantClientId}
                            onChange={onChangeTenantOrClientId}
                            id={tenantItem.tenantClientId}
                            //validations={[required]}
                          />
                          <label for={tenantItem.tenantClientId} className="radio-label">
                            <img src={tenantItem.imageUrl} className="radio-img" />
                          </label>
                        <label for={tenantItem.tenantClientId} className="miLabel" htmlFor="tenantOrClientId">
                          {tenantItem.tenantName}
                        </label>
                        </li>
                      </td>
                    </tr>
                  ))}
                </tbody>
                
              </table> */}

            </div>
            <div className="right-part" style={{ flexBasis: '48%' }}>
            {/*
                tenantsImage.map((tenantItem) => (
                    <img 
                        src={tenantItem.imageLogo}
                        alt="profile-img"
                        className="logoSideBar"
                        style={{ maxWidth: '100%' }}
                        key={tenantItem.tenantClientId}
                    />
                )

                )
                */}
            </div>
          </div>

          <div className="form-group">
            <button className="miBoton" href="#" disabled={loading}>
              <span></span>
              <span></span>
              <span></span>
              <span></span>
              {loading && (
                <span className="spinner-border spinner-border-sm"></span>
              )}
              INGRESAR
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
    </div>
  );
};

export default Login;
