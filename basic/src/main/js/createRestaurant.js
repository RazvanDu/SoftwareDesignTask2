import React, {useEffect, useState} from 'react';
import App from "./app";
import "babel-polyfill";
const ReactDOM = require('react-dom');
import axios from 'axios';
import TopBar from './topbar'
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';

import Popup from 'reactjs-popup';
import '../../../node_modules/reactjs-popup/dist/index.css';
//import './main.css'
import ReactTooltip from 'react-tooltip';

import Modal from 'react-modal';

import {Grid} from '@mui/material';

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

Modal.setAppElement('#react');

function CreateRestaurant() {

    const navigate = useNavigate()

    const [name, setName] = useState("")
    const [location, setLocation] = useState("")
    const [delivery, setDelivery] = useState("")

    const [showModal, setShowModal] = useState(false);


    useEffect(() => {
    }, []);

    return (
        <div>
            <TopBar/>



            <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
                <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                    <Grid item xs={3}>

                    <input
                            type="text"
                            placeholder="Name of the Restaurant"
                            name="name"
                            onChange={ev => setName(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={3}>

                    <input
                            type="text"
                            placeholder="Location of the Restaurant"
                            name="location"
                            onChange={ev => setLocation(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={3}>

                    <input
                            type="text"
                            placeholder="Delivery Zones Available"
                            name="delivery"
                            onChange={ev => setDelivery(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={3}>

                        <button type="button" className="btn btn-success" data-toggle="button" onClick={() => {

                            const requestOptions = {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({

                                    name: name,
                                    location: location,
                                    delivery: delivery,

                                })
                            };

                            fetch("http://localhost:8080/database/addRestaurant", requestOptions)


                            setShowModal(true)
                            setTimeout(function() { //Start the timer
                                setShowModal(false)
                                navigate('/')
                            }.bind(this), 1500)

                        }}>
                            Add
                        </button>

                        <Modal onHide={() => setShowModal(false)} isOpen={showModal}>
                            <button type="button" class="btn btn-danger" onClick={() => {setShowModal(false)}}>close</button>
                            <h2>
                                "Successfully created the restaurant!"
                            </h2>
                        </Modal>

                    </Grid>


                </Grid>
            </Box>



        </div>
    );
}

export default CreateRestaurant;