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

function AddFood() {

    const navigate = useNavigate()

    const [breakfast, setBreakfast] = useState(true)
    const [name, setName] = useState(false)
    const [description, setDescription] = useState(false)
    const [price, setPrice] = useState(false)

    const [showModal, setShowModal] = useState(false);

    const toggle = () => {
        setBreakfast(!breakfast)
    }

    useEffect(() => {
    }, []);

    return (
        <div>
            <TopBar/>



            <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
                <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>


                    <Grid item xs={3}>

                        <button type="button" className="btn btn-primary" data-toggle="button" aria-pressed="false" onClick={() => {
                            toggle()
                        }}>
                            {breakfast ? "Breakfast" : "Lunch"}
                        </button>

                    </Grid>

                    <Grid item xs={2}>

                    <input
                            type="text"
                            placeholder="Name of the Food"
                            name="name"
                            onChange={ev => setName(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={2}>

                    <input
                            type="text"
                            placeholder="Description of the Food"
                            name="description"
                            onChange={ev => setDescription(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={2}>

                    <input
                            type="text"
                            placeholder="Price of the Food"
                            name="price"
                            onChange={ev => setPrice(ev.target.value)}
                        />

                    </Grid>

                    <Grid item xs={3}>

                        <button type="button" className="btn btn-success" data-toggle="button" onClick={() => {


                            const requestOptions = {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({

                                    name: name,
                                    description: description,
                                    price: price,
                                    category: (breakfast ? 0 : 1)

                                })
                            };

                            fetch("http://localhost:8080/database/addFood", requestOptions)


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
                                "Successfully added the food!"
                            </h2>
                        </Modal>

                    </Grid>


                </Grid>
            </Box>



        </div>
    );
}

export default AddFood;