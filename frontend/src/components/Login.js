import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import AuthService from "../services/auth.service";

import "./Login.css";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const Login = () => {
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
    e.preventDefault();

    setMessage("");
    setLoading(true);

    form.current.validateAll();

    if (checkBtn.current.context._errors.length === 0) {
      AuthService.login(usernameOrEmail, password, tenantOrClientId).then(
        () => {
          navigate("/profile");
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
          setMessage(resMessage);
        }
      );
    } else {
      setLoading(false);
    }
  };

  return (
    <div class="login-box">
          <h2>Login</h2>
          <img
            src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
            alt="profile-img"
            className="profile-img-card"
          />

          <Form onSubmit={handleLogin} ref={form}>
            <div class="user-box">
                <label className="miLabel"  htmlFor="usernameOrEmail">UsernameOrEmail:</label>
                <Input
                  type="text"
                  className="miInput"
                  name="usernameOrEmail"
                  value={usernameOrEmail}
                  onChange={onChangeUsernameOrEmail}
                  validations={[required]}
                />
            </div>

            <div class="user-box">
                <label className="miLabel"  htmlFor="password">Password:</label>
                <Input
                  type="password"
                  className="miInput"
                  name="password"
                  value={password}
                  onChange={onChangePassword}
                  validations={[required]}
                />
            </div>

            <div class="user-box">
                <label 
                  className="miLabel" htmlFor="tenantOrClientId">Tenant o Client Id:</label>
                <Input
                  type="text"
                  className="miInput"
                  name="tenantOrClientId"
                  value={tenantOrClientId}
                  onChange={onChangeTenantOrClientId}
                  validations={[required]}
                />
            </div>


              <div className="form-group">
                <button 
                className="miBoton"
                  href="#"
                  disabled={loading}
                >
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
  );
};

export default Login;