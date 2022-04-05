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

import Modal from 'react-modal';

import {Grid} from '@mui/material';

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

Modal.setAppElement('#react');

const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));

function Cart() {

    const [foods, setFoods] = useState([]);

    const getData = async () => {
        await fetch("http://localhost:8080/database/getCart").then(res => res.json()).then(res => {
            setFoods(res);
        })
    }

    useEffect(() => {
        getData()
    },[]);

    return (
        <div>
            <TopBar/>
            <OrderMenu foods={foods}/>
            <button type="button" className="btn btn-success">
                Order
            </button>
        </div>
    );
}

/*
<button type="button" className="btn btn-success" onClick={() => {
                                const requestOptions = {
                                    method: 'POST',
                                    headers: { 'Content-Type': 'application/json' },
                                    body: JSON.stringify({

                                        id: food.id,
                                        name: food.name,
                                        description: food.description,
                                        price: food.price,
                                        category: food.category

                                    })
                                };
                                if(logged)
                                    fetch("http://localhost:8080/database/addCart", requestOptions)
                                setShowModal(true)
                                setTimeout(function() { //Start the timer
                                    setShowModal(false)
                                }.bind(this), 1500)
                            }}>
                                Order
                            </button>
 */

function OrderMenu({foods}) {

    return (
        <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
            <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                {foods.map((food, i) => {

                    return (<Grid item xs={12}>
                            <Item> {i}</Item>
                            <Item>{food.name}</Item>
                            <Item>Description: {food.description}</Item>
                            <Item>Costs: {food.price}</Item>
                            <Item>Category: {food.category}</Item>
                        </Grid>

                    )
                })}

            </Grid>
        </Box>
    );
}

export default Cart;