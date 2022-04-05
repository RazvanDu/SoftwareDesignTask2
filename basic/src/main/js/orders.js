import React, {useEffect} from 'react';
import useState from 'react-usestateref' // see this line
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

function Order() {

    const [orderi, setOrderi, orderiRef] = useState(null);
    const [restaurant, setRestaurant, restaurantRef] = useState(null);
    const [hasOrder, setHasOrder, hasOrderRef] = useState(false);
    const [oldOrders, setOldOrders, oldOrdersRef] = useState(null);
    const [recent, setRecent, recentRef] = useState(true)
    const navigate = useNavigate()

    const toggle = () => {
        setRecent(!recent)
    }

    const getData = async () => {
        await fetch("http://localhost:8080/database/orders/old").then(res => res.json()).then(res => {
            setOldOrders(res);
        })
        let res = await fetch("http://localhost:8080/database/orders/get")
        if(res.status === 200) {
            await fetch("http://localhost:8080/database/orders/get").then(res => res.json()).then(res => {
                setOrderi(res);
            })
            await fetch("http://localhost:8080/database/restaurants/byId/" + orderiRef.current.restaurantID).then(res => res.json()).then(res => {
                setRestaurant(res)
            })
            setHasOrder(true)
        }
    }

    useEffect(() => {
        setInterval(() => {
            getData()
        }, 500);
    },[]);

    const [showModal, setShowModal] = useState(false);

    return (
        <div>
            <TopBar/>

            <button type="button" class="btn btn-primary" data-toggle="button" aria-pressed="false" onClick={() => {toggle()}}>
                {recent ? "RECENT" : "OLD"}
            </button>

            {console.log(hasOrder + " + " + orderi + " + " + restaurant)}

            {recent && hasOrder && <OrderItem orderr={orderi} restaurantName={restaurant.name}/>}
            {!recent && <OldList orders={oldOrders}/>}

        </div>
    );
}

function OldList({orders}) {

    return (
        <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
            <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                {orders.map((orderr, i) => {

                    return (<Grid item xs={12}>
                            <Item>{i}</Item>
                            <OrderItem orderr={orderr} restaurantName={orderr.restaurantName}/>
                        </Grid>

                    )
                })}

            </Grid>
        </Box>
    );
}

function OrderItem({orderr, restaurantName}) {
    return (
        <div>
            <Item>Ordered from: {restaurantName}</Item>
            <Item>Of: {orderr.foodsOrdered}</Item>
            <Item>With the status: <IDToStatus statusOrder={orderr.statusOrder}/></Item>
        </div>
    )
}

function IDToStatus({statusOrder}) {
    return (
        <div>
            {statusOrder === 1 && "PENDING"}
            {statusOrder === 2 && "ACCEPTED"}
            {statusOrder === 3 && "IN DELIVERY"}
            {statusOrder === 4 && "DELIVERED"}
            {statusOrder === 5 && "DECLINED"}
        </div>
    )
}

export default Order;