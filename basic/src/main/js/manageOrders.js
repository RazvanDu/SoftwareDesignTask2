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

import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';

Modal.setAppElement('#react');

const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));

function ManageOrders() {

    const [orders, setOrders, ordersRef] = useState([])
    const navigate = useNavigate()

    const getData = async () => {
        await fetch("http://localhost:8080/database/orders/admin").then(res => res.json()).then(res => {
            setOrders(res);
        }).catch(err => {console.log("?? " + err)})
    }

    useEffect(() => {
        getData()
        setInterval(() => {
            getData()
        }, 500);
    },[]);

    return (
        <div>
            <TopBar/>

            <OldList orders={orders}/>

        </div>
    );
}

function OldList({orders}) {

    const [selectedValue, setSelectedValue] = useState('ANY')

    const handleChange = (event) => {
        setSelectedValue(event.target.value)
    }

    return (

        <div>
            <select value={selectedValue} onChange={handleChange}>
                <option value="ANY">ANY</option>
                <option value="1">PENDING</option>
                <option value="2">ACCEPTED</option>
                <option value="3">IN DELIVERY</option>
            </select>
            <Box style={{maxHeight: '100vh', overflow: 'auto'}} sx={{ flexGrow: 1 }}>
                <Grid style={{maxHeight: '100vh', overflow: 'auto'}} container spacing={2}>

                    {(selectedValue === 'ANY') && orders.map((orderr, i) => {

                        return (<Grid item xs={12}>
                                <Item>{i}</Item>
                                <OrderItem orderr={orderr}/>
                            </Grid>

                        )
                    })}

                    {(selectedValue !== 'ANY') && orders.filter(order =>
                        (order.statusOrder === 1 && selectedValue === '1') ||
                        (order.statusOrder === 2 && selectedValue === '2') ||
                        (order.statusOrder === 3 && selectedValue === '3')
                    ).map((orderr, i) => {

                        return (<Grid item xs={12}>
                                <Item>{i}</Item>
                                <OrderItem orderr={orderr}/>
                            </Grid>

                        )
                    })}

                </Grid>
            </Box>
        </div>
    );
}

function OrderItem({orderr}) {
    return (
        <div>
            <Item>Ordered by: {orderr.userName}</Item>
            <Item>Of: {orderr.foodsOrdered}</Item>
            <Item>With the status: <IDToStatus statusOrder={orderr.statusOrder}/></Item>
            {orderr.statusOrder === 1 && (<Item>


                <button type="button" className="btn btn-danger" data-toggle="button" onClick={() => {


                    const requestOptions = {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            id: orderr.id,
                            restaurantID: orderr.restaurantID,
                            userID: orderr.userID,
                            foodsOrdered: orderr.foodsOrdered,
                            statusOrder: orderr.statusOrder
                        })
                    };

                    fetch("http://localhost:8080/database/cancelOrder", requestOptions)

                }}>
                    Cancel The Order
                </button>
            </Item>)}
            <Item>
                <button type="button" className="btn btn-success" data-toggle="button" onClick={() => {


                    const requestOptions = {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            id: orderr.id,
                            restaurantID: orderr.restaurantID,
                            userID: orderr.userID,
                            foodsOrdered: orderr.foodsOrdered,
                            statusOrder: orderr.statusOrder
                        })
                    };

                    fetch("http://localhost:8080/database/advanceOrder", requestOptions)

                }}>
                    Advance The Order
                </button>
            </Item>
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

export default ManageOrders;