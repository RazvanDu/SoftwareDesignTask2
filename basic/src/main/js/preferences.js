import React, {useEffect, useState} from 'react';
import App from "./app";
const ReactDOM = require('react-dom');

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

function Preferences() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [items, setItems] = useState([]);

    useEffect(() => {

        fetch("http://localhost:8080/database/users/findByName/uu")
            .then(res => res.json())
            .then(
                (result) => {
                    setIsLoaded(true);
                    console.log("WTF " + result)
                    setItems(result);
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                }
            )}, [])

    return (
        <ul>
            <h2>
                {items.name}
            </h2>
            <h2>
                {items.email}
            </h2>
        </ul>
    );
}

export default Preferences;