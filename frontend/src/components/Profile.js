import React from "react";
import AuthService from "../services/auth.service";

import "../Styles/Profile.scss";

const Profile = () => {
  const currentUser = AuthService.getCurrentUser();

  return (
    <div className="Profile">
      <div className="container">
        <header className="jumbotron">
          <h3>
            <strong>{currentUser.userName}</strong> Profile
          </h3>
        </header>
        <p>
          <strong>Token:</strong> {currentUser.token.substring(0, 20)} ...{" "}
          {currentUser.token.substr(currentUser.token.length - 20)}
        </p>
        <p>
          <strong>tokenType:</strong> {currentUser.tokenType}
        </p>

        {/* 

      <p>
        <strong>Email:</strong> {currentUser.email}
      </p>
      <strong>Authorities:</strong>
      <ul>
        {currentUser.roles &&
          currentUser.roles.map((role, index) => <li key={index}>{role}</li>)}
      </ul>

      */}
      </div>
    </div>
  );
};

export default Profile;