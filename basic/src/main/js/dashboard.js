import React, {useEffect, useState} from 'react';
import App from "./app";
import "babel-polyfill";
const ReactDOM = require('react-dom');
import axios from 'axios';
import TopBar from './topbar'
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';

import {Grid} from '@mui/material';

import { useNavigate } from "react-router-dom";
import { Navigate } from "react-router-dom";

function Dashboard() {

    const [restaurants, setRestaurants] = useState([]);

    const Item = styled(Paper)(({ theme }) => ({
        backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
        ...theme.typography.body2,
        padding: theme.spacing(1),
        textAlign: 'center',
        color: theme.palette.text.secondary,
    }));

    const getRestaurants = async () => {
        await fetch("http://localhost:8080/database/restaurants/all").then(res => res.json()).then(res => {
            setRestaurants(res);
        })
    }

    useEffect(() => {
        getRestaurants()
    }, []);

    return (
        <div>
            <TopBar />
            <Box sx={{ flexGrow: 1 }}>
                <Grid container spacing={2}>
                    {restaurants.map((restaurant, i) => {
                        console.log("Entered");
                        return (<Grid item xs={3}>
                            <Item>{restaurant.name}</Item>
                            <Item>Located in: {restaurant.location}</Item>
                            <Item>You can order food in: {restaurant.deliveryZones}</Item>
                        </Grid>)
                    })}
                </Grid>
            </Box>
            <h2>Hallo</h2>
        </div>
    );
}

export default Dashboard;