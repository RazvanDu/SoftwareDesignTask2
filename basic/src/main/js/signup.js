import React, { useState } from "react";
import ReactDOM from "react-dom";

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";
import TopBar from "./topbar";
import axios from "axios";
var md5 = require('md5');

/*function getAccount(name){
    return fetch("http://localhost:8080/database/users/findByName/" + name)
        .then(res => res.json())
};*/

function SignUp() {

    const navigate = useNavigate();

    const [errorMessages, setErrorMessages] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [userData, setUserData] = useState(null);

    const renderErrorMessage = (name) =>
        name === errorMessages.name && (
            <div className="error">{errorMessages.message}</div>
        );

    const handleSubmit = async (event) => {
        //Prevent page reload
        event.preventDefault();

        console.log(document.forms[0])

        var {uname, uemail, pass} = document.forms[0];

        pass.value = md5(pass.value);

        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                name: uname.value,
                hash: pass.value, email: uemail.value
            })
        };
        console.log("XX " + uname.value)
        const response = await fetch("http://localhost:8080/database/signupUser", requestOptions)
            .then(res => {
                if(res.status === 200) {
                    setIsSubmitted(true)
                    setErrorMessages({})
                    setTimeout(function () {
                        navigate('/')
                    }, 3000)
                } else {
                    setErrorMessages({name: "invalid", message: "Invalid data"})
                }
            })

        // Compare user info

    };

    const renderForm = (
        <div className="form">
            <form onSubmit={handleSubmit}>
                <div className="input-container">
                    <label>Username </label>
                    <input type="text" name="uname" required />
                    {renderErrorMessage("invalid")}
                </div>
                <div className="input-container">
                    <label>Email </label>
                    <input type="text" name="uemail" required />
                    {renderErrorMessage("invalid")}
                </div>
                <div className="input-container">
                    <label>Password </label>
                    <input type="password" name="pass" required />
                    {renderErrorMessage("invalid")}
                </div>
                <div className="button-container">
                    <input type="submit" />
                </div>
            </form>
        </div>
    );

    return (
        <div>
            <TopBar />
            <div className="login-form">
                <div className="title">Sign Up</div>
                {isSubmitted && <div>You can now use the account to login!</div>}
                {!isSubmitted && renderForm}
            </div>
        </div>
    );
}

export default SignUp;