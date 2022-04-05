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
import Navbar from "react-bootstrap/Navbar";
import {Container} from "react-bootstrap";

Modal.setAppElement('#react');

const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));

function Dashboard() {

    //const [foods, setFoods] = useState([]);

    const [restaurants, setRestaurants] = useState([]);
    const [search, setSearch] = useState([]);
    const [logged, setLogged] = useState(false);
    const [targetRestaurant, setTargetRestaurant] = useState(-1);

    const getData = async () => {
        await fetch("http://localhost:8080/database/restaurants/all").then(res => res.json()).then(res => {
            setRestaurants(res);
        })
        let res = await fetch("http://localhost:8080/database/isLoggedIn")
        if(res.status === 202) {
            setLogged(true)
        } else
            setLogged(false)
    }

    useEffect(() => {
        getData()
    }, []);

    useEffect(() => {

        setInterval(() => {
            fetch("http://localhost:8080/database/getOrdering").then(res => res.json()).then(res => {
                setTargetRestaurant(res.id)
            })
        }, 500);

    }, []);



    return (
        <div>
            <TopBar />

            <Navbar>
                <Container>
                    <Navbar.Toggle />
                    <Navbar.Collapse className="justify-content-end">
                        <label>
                            Restaurant Name:
                            <input
                                type="text"
                                value={search}
                                placeholder="Enter a name"
                                onChange={e => {
                                    setSearch(e.target.value)
                                    var toSearch = e.target.value
                                    if(toSearch === "")
                                        toSearch = "-1"
                                    fetch("http://localhost:8080/database/restaurants/allFilter/" + toSearch).then(res => res.json()).then(res => {
                                        setRestaurants(res);
                                    })
                                    console.log("XDDD " + toSearch)
                                }}
                            />
                        </label>
                    </Navbar.Collapse>
                </Container>
            </Navbar>



            <Box>
                <Grid container spacing={2}>
                    {restaurants.map((restaurant, j) => {
                        return (<Grid item xs={3}>
                            <Item>{restaurant.name}</Item>
                            <Item>Located in: {restaurant.location}</Item>
                            <Item>You can order food in: {restaurant.delivery}</Item>
                            <Item>
                                <div>
                                    <RestaurantModal restaurant={restaurant} logged={logged} targetRestaurant={targetRestaurant}/>
                                </div>

                            </Item>
                        </Grid>)
                    })}
                </Grid>
            </Box>
        </div>
    );
}

function RestaurantModal({restaurant, logged, targetRestaurant}) {
    const [showModal, setShowModal] = useState(false);

    return (
        <div key={restaurant.id}>


            {(targetRestaurant === -1 || (targetRestaurant === restaurant.id)) &&
            <button type="button" class="btn btn-primary"
                style={{ marginBottom: 10 }}
                onClick={() => {
                    setShowModal(true);
                }}
            >
                View Menu
            </button>}



            <Modal onHide={() => setShowModal(false)} isOpen={showModal}>
                <button type="button" class="btn btn-danger" onClick={() => {setShowModal(false)}}>close</button>
                <h2>
                    {restaurant.name}
                </h2>
                <RestaurantMenu restaurant={restaurant} logged={logged}/>
            </Modal>
        </div>)
}

function RestaurantMenu({restaurant, logged}) {
    return (
        <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
            <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                <br/>
                <h2 style={{  headline: {
                        textAlign: 'center', // <-- the magic
                        fontWeight: 'bold',
                        fontSize: 18,
                        marginTop: 0,
                        width: 200,
                        backgroundColor: 'yellow',
                    }}}>
                    Breakfast:
                </h2>
                {restaurant.foods.filter(food => food.category === 0).map((food, i) => {
                    return (<RestaurantItem food={food} i={i} logged={logged} restaurant={restaurant}/>)
                })}

                <h2 style={{  headline: {
                        textAlign: 'center', // <-- the magic
                        fontWeight: 'bold',
                        fontSize: 18,
                        marginTop: 0,
                        width: 200,
                        backgroundColor: 'yellow',
                    }}}>
                    Lunch:
                </h2>
                {restaurant.foods.filter(food => food.category === 1).map((food, i) => {
                    return (<RestaurantItem food={food} i={i} logged={logged} restaurant={restaurant}/>)
                })}

            </Grid>
        </Box>
    );
}

function RestaurantItem({food, i, logged, restaurant}) {
    const [showModal, setShowModal] = useState(false);
    return (<Grid item xs={12}>
        <Item> {i}</Item>
        <Item>{food.name}</Item>
        <Item>Description: {food.description}</Item>
        <Item>Costs: {food.price}</Item>
        <Item>Category: {food.category}</Item>
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

            const requestOptions2 = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({

                    id: restaurant.id,
                    name: restaurant.name,
                    location: restaurant.location,
                    delivery: restaurant.delivery,
                    food: restaurant.foods

                })
            };
            if(logged)
                fetch("http://localhost:8080/database/addOrdering", requestOptions2)

            setShowModal(true)
            setTimeout(function() { //Start the timer
                setShowModal(false)
            }.bind(this), 1500)
        }}>
            Order
        </button>


        <Modal onHide={() => setShowModal(false)} isOpen={showModal}>
            <button type="button" class="btn btn-danger" onClick={() => {setShowModal(false)}}>close</button>
            <h2>
                {logged && "Successfully added to you cart!"}
                {!logged && "You need to be logged in!"}
            </h2>
        </Modal>


    </Grid>)
}

export default Dashboard;