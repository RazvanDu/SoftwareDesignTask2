import React, { useState } from "react";
import ReactDOM from "react-dom";

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";
import TopBar from "./topbar";
var md5 = require('md5');

/*function getAccount(name){
    return fetch("http://localhost:8080/database/users/findByName/" + name)
        .then(res => res.json())
};*/

function Login() {

    const navigate = useNavigate();

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [userData, setUserData] = useState(null);

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const handleSubmit = (event) => {
        //Prevent page reload
        event.preventDefault();

        var { uname, pass } = document.forms[0];

        pass.value = md5(pass.value);

        //var result = Promise.call(getAccount(uname.value))

        fetch("http://localhost:8080/database/users/findByName/" + uname.value)
            .then(res => res.json())
            .then(res => {
                setUserData(res)
                processAccount(pass, res)
            }).catch((error => {
                setUserData(null)
                processAccount(pass, null)
            }))

        // Compare user info

    };

    const processAccount = (pass, value) => {
        if (value) {
            console.log("lol")
            if (value.hash !== pass.value) {
                // Invalid password
                setErrorMessages({ name: "pass", message: errors.pass });
            } else {
                const requestOptions = {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ title: '...' })
                };
                fetch("http://localhost:8080/database/users/checkAndLogin/" + value.name + "/" + pass.value, requestOptions)
                setIsSubmitted(true);
            }
        } else {
            // Username not found
            setErrorMessages({ name: "uname", message: errors.uname });
        }
    }


    const renderForm = (
        <div className="form">
            <form onSubmit={handleSubmit}>
                <div className="input-container">
                    <label>Username </label>
                    <input type="text" name="uname" required />
                    {renderErrorMessage("uname")}
                </div>
                <div className="input-container">
                    <label>Password </label>
                    <input type="password" name="pass" required />
                    {renderErrorMessage("pass")}
                </div>
                <div className="button-container">
                    <input type="submit" />
                </div>
            </form>
        </div>
    );

    const errors = {
        uname: "invalid username",
        pass: "invalid password"
    };

    return (
        <div>
            <TopBar />
            <div className="login-form">
                <div className="title">Sign In</div>
                {isSubmitted && <div>User is successfully logged in</div>}
                {isSubmitted && <div hidden>setTimeout(function () {navigate('/')}, 3000)</div>}
                {!isSubmitted && renderForm}
            </div>
        </div>
    );
}

export default Login;