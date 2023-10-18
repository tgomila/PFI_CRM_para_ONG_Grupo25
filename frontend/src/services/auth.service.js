import axios from "axios";
import * as constantsURL from "../components/constants/ConstantsURL";
import authHeader from "./auth-header";
import React, { useState, useEffect } from "react";
import MasterTenantAdminService from "./MasterTenantAdminService";

//const API_URL = "http://localhost:8080/api/auth/";
const BACKEND_STATIC_BASE_URL = constantsURL.STATIC_BASE_URL;
const API_URL = constantsURL.API_BASE_URL + "auth/";

const register = (name, username, email, password, tenantOrClientId) => {
  return axios.post(API_URL + "signup", {
    name,
    username,
    email,
    password,
    tenantOrClientId,
  });
};

const login = (usernameOrEmail, password, tenantOrClientId) => {
  localStorage.clear();
  return axios
    .post(API_URL + "signin", {
      usernameOrEmail,
      password,
      tenantOrClientId,
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data));
      }
      //setUserPersona();
      return response.data;
    });
};

const MasterTenantAdminlogin = (tenantOrClientId) => {

  if(!tenantOrClientId || tenantOrClientId <= 0){
    return;
  }

  return axios
    .post(API_URL + "master_tenant/signin_as_tenant_admin/" + tenantOrClientId, null, { headers: authHeader(), })
    .then((response) => {
      if (response.data.token) {
        let masterTenantAdmin = getCurrentUser();
        localStorage.clear();
        localStorage.setItem("user", JSON.stringify(response.data));
        localStorage.setItem("masterAdmin", JSON.stringify(masterTenantAdmin));
      }
      return response.data;
    });
};

const adminSimularUserLogin = (userName) => {

  if(!userName){
    return;
  }

  return axios
    .post(API_URL + "admin/signin_as_user/" + userName, null, { headers: authHeader(), })
    .then((response) => {
      if (response.data.token) {
        let adminUser = getCurrentUser();
        let masterTenantAdminUser = getMasterTenantCurrentUser();
        localStorage.clear();
        localStorage.setItem("user", JSON.stringify(response.data));
        localStorage.setItem("admin", JSON.stringify(adminUser));
        if(masterTenantAdminUser) {
          localStorage.setItem("masterAdmin", JSON.stringify(masterTenantAdminUser));
        }
      }
      return response.data;
    });
};

const logout = () => {
  //localStorage.removeItem("user");
  //localStorage.removeItem("modulos");
  localStorage.clear();
};

const logoutUserSimulatorAdmin = () => {
  let admin = getAdminCurrentUser();
  let masterTenantAdminUser = getMasterTenantCurrentUser();
  console.log("masterTenantAdminUser");
  console.log(masterTenantAdminUser);
  localStorage.clear();
  localStorage.setItem("user", JSON.stringify(admin));
  if(masterTenantAdminUser) {
    localStorage.setItem("masterAdmin", JSON.stringify(masterTenantAdminUser));
  }
};

const logoutUserSimulatorMasterTenant = () => {
  let masterTenantAdmin = getMasterTenantCurrentUser();
  localStorage.clear();
  localStorage.setItem("user", JSON.stringify(masterTenantAdmin));
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
  
};

const getAdminCurrentUser = () => {
  return JSON.parse(localStorage.getItem("admin"));
  
};

const getMasterTenantCurrentUser = () => {
  return JSON.parse(localStorage.getItem("masterAdmin"));
  
};

const getTenantLogoURL = () => {
  const user = getCurrentUser();
  if(user?.dbName && user?.dbName !== "MasterTenant")
    return BACKEND_STATIC_BASE_URL + "logo/" + user.dbName + ".png";
  else
    return BACKEND_STATIC_BASE_URL + "logo/" + "logo-CosmosCRM" + ".png";
  //antes este link default "http://ssl.gstatic.com/accounts/ui/avatar_2x.png"
}

const setLogoTenantTab = () => {
  const link = document.querySelector("link[rel~='icon']") || document.createElement('link');
  link.type = 'image/x-icon';
  link.rel = 'shortcut icon';
  link.href = getTenantLogoURL();
  const user = getCurrentUser();
  if(user && user?.dbName === "MasterTenant") {
    link.href = BACKEND_STATIC_BASE_URL + "logo/" + "logo-CosmosCRM" + ".png";
    document.title = user.tenantName;
  } else if (user && user?.dbName && user?.dbName !== "MasterTenant") {
    link.href = BACKEND_STATIC_BASE_URL + "logo/" + user.dbName + ".png";
    document.title = user.tenantName;
  } else {
    link.href = BACKEND_STATIC_BASE_URL + "logo/" + "logo-CosmosCRM" + ".png";
    document.title = "Cosmos CRM";
  }
  document.getElementsByTagName('head')[0].appendChild(link);
}

const AuthService = {
  register,
  login,
  logout,
  logoutUserSimulatorAdmin,
  logoutUserSimulatorMasterTenant,
  getCurrentUser,
  getAdminCurrentUser,
  getMasterTenantCurrentUser,
  MasterTenantAdminlogin,
  adminSimularUserLogin,
  getTenantLogoURL,
  setLogoTenantTab,
};

export default AuthService;
