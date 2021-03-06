import React, {useEffect, useState} from 'react';
import App from "./app";
import "babel-polyfill";
const ReactDOM = require('react-dom');
import axios from 'axios';
import Navbar from 'react-bootstrap/Navbar'
import { Container, Col, Row, Form } from "react-bootstrap";

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

function TopBar() {

    const [msg, setMsg] = useState("You are not logged in!");
    const [user, setUser] = useState(null);

    useEffect(() => {
        fetchMsg()
    }, []);

    const fetchMsg = () => {
        axios.get('http://localhost:8080/database/loggedUser')
            .then((res) => {
                if(res.data) {
                    setMsg("Hello " + res.data.name + "!");
                    setUser(res.data);
                }
            })
            .catch(err => {});
    }

    const navigate = useNavigate();
    return (
        <Navbar>
            <Container>
                <Navbar.Brand href="/">FoodStore</Navbar.Brand>
                <Navbar.Toggle />
                <Navbar.Collapse className="justify-content-end">
                    <Navbar.Text>
                        {msg}
                    </Navbar.Text>
                    {user == null && <button type="button" class="btn btn-success" onClick={() => navigate('/login')}>Login</button>}
                    {user != null && <button type="button" class="btn btn-danger" onClick={() => navigate('/logout')}>Logout</button>}
                    {user != null && <button type="button" class="btn btn-success" onClick={() => navigate('/cart')}>Cart</button>}
                    {user != null && <button type="button" class="btn btn-success" onClick={() => navigate('/order')}>Order</button>}
                    {user != null && (user.type === 1) && (user.newAdmin === 0) && <button type="button" class="btn btn-info" onClick={() => navigate('/addFood')}>Add Food</button>}
                    {user != null && (user.type === 1) && (user.newAdmin === 0) && <button type="button" class="btn btn-info" onClick={() => navigate('/manageOrders')}>Manage Orders</button>}
                    {user != null && (user.type === 1) && (user.newAdmin === 1) && <button type="button" class="btn btn-info" onClick={() => navigate('/createRestaurant')}>Create Restaurant</button>}
                    {user != null && (user.type === 1) && (user.newAdmin === 0) && <button type="button" class="btn btn-info" onClick={() => {
                        fetch('http://localhost:8080/exportPDF')
                            .then(response => {
                                //const filename = response.headers.get('Content-Disposition').split('filename=')[1];
                                const filename = "menu.pdf"
                                //print("???? + " + filename)
                                response.blob().then(blob => {
                                    let url = window.URL.createObjectURL(blob);
                                    let a = document.createElement('a');
                                    a.href = url;
                                    a.download = filename;
                                    a.click();
                                });
                            })
                    }}>Export PDF</button>}
                    <button type="button" class="btn btn-primary" onClick={() => navigate('/signup')}>Signup</button>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        /*<div>
            {user == null && <button onClick={() => navigate('/login')}>Login</button>}
            {user != null && <button onClick={() => navigate('/logout')}>Logout</button>}
            <button onClick={() => navigate('/signup')}>Signup</button>
            <div>{msg}</div>
        </div>*/
    );
}

export default TopBar;