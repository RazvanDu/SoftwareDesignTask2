import React, {useEffect, useState} from "react";
import ReactDOM from "react-dom";

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";
import TopBar from "./topbar";
var md5 = require('md5');

function Dashboard() {

    useEffect(() => {
        fetch("http://localhost:8080/logout").then(

            setTimeout(function () {
                navigate('/')
            }, 100)

        );
    },);

    const navigate = useNavigate();
    return (
        <div>
            <TopBar />
            <h2>
                Logging out!
            </h2>
        </div>
    );
}

export default Dashboard;