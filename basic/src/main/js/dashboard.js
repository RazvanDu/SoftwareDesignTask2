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

function Dashboard() {

    const [restaurants, setRestaurants] = useState([]);
    //const [foods, setFoods] = useState([]);

    const getData = async () => {
        await fetch("http://localhost:8080/database/restaurants/all").then(res => res.json()).then(res => {
            setRestaurants(res);
        })
        /*await fetch("http://localhost:8080/database/foods/all").then(res => res.json()).then(res => {
            setFoods(res);
        })*/
    }

    useEffect(() => {
        getData()
    }, []);

    return (
        <div>
            <TopBar />
            <Box>
                <Grid container spacing={2}>
                    {restaurants.map((restaurant, j) => {
                        console.log("Entered");
                        return (<Grid item xs={3}>
                            <Item>{restaurant.name}</Item>
                            <Item>Located in: {restaurant.location}</Item>
                            <Item>You can order food in: {restaurant.delivery}</Item>
                            <Item>
                                <div>
                                    <RestaurantModal restaurant={restaurant}/>
                                </div>

                            </Item>
                        </Grid>)
                    })}
                </Grid>
            </Box>
        </div>
    );
}

function RestaurantModal(restaurant) {
    const [showModal, setShowModal] = useState(false);

    const customStyles = {
        content: {
            top: "50%",
            left: "50%",
            right: "auto",
            bottom: "auto",
            marginRight: "-50%",
            transform: "translate(-50%, -50%)"
        }
    };

    return (
        <div key={restaurant.restaurant.id}>
            <button type="button" class="btn btn-primary"
                style={{ marginBottom: 10 }}
                onClick={() => {
                    setShowModal(true);
                }}
            >
                View Menu
            </button>
            <Modal onHide={() => setShowModal(false)} isOpen={showModal}>
                <button type="button" class="btn btn-danger" onClick={() => {setShowModal(false)}}>close</button>
                <h2>
                    {restaurant.restaurant.name}
                </h2>
                <RestaurantMenu restaurant={restaurant.restaurant}/>
            </Modal>
        </div>)
}

function RestaurantMenu(restaurant) {
    return (
        <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
            <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                {restaurant.restaurant.foods.map((food, i) => {

                    return (<Grid item xs={12}>
                            <Item> {i}</Item>
                            <Item>{food.name}</Item>
                            <Item>Description: {food.description}</Item>
                            <Item>Costs: {food.price}</Item>
                            <Item>Category: {food.category}</Item>
                            <button type="button" className="btn btn-success">
                                Order
                            </button>
                        </Grid>

                    )
                })}

            </Grid>
        </Box>
    );
}

export default Dashboard;