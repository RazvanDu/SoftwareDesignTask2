'use strict';

// tag::vars[]
import Dashboard from "./dashboard";
import Preferences from "./preferences";
import Login from "./login";
import Logout from "./logout";
import Cart from "./cart";
import Order from "./orders";
import Signup from "./signup";
import AddFood from "./addFood";

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const logout = require('./logout');
const dashboard = require('./dashboard');
const preferences = require('./preferences');
import { Button, Navbar } from 'react-bootstrap'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {useEffect} from "react";
import useState from 'react-usestateref' // see this line
import axios from "axios";

// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		//this.state = {employees: []};
	    this.state = {authenticated: true}
	}

	/*componentDidMount() { // <2>
		client({method: 'GET', path: '/api/employees'}).done(response => {
			this.setState({employees: response.entity._embedded.employees});
		});
	}*/

	render() { // <3>
		return (<ToFunctional/>)
	}
}

function ToFunctional() {
    const [user, setUser, userRef] = useState(null);

    useEffect(() => {
        fetchMsg()
    });

    const fetchMsg = () => {
        axios.get('http://localhost:8080/database/loggedUser')
            .then((res) => {
                if(res.data) {
                    setUser(res.data);
                }
            });
    }

    return (<BrowserRouter>
        <Routes>
            <Route path="/" element={<Dashboard />}/>
            <Route path="/login" element={<Login />}/>
            <Route path="/logout" element={<Logout />}/>
            <Route path="/signup" element={<Signup />}/>
            {/*user != null && */<Route path="/cart" element={<Cart />}/>}
            {/*user != null && */<Route path="/order" element={<Order />}/>}
            {/*user != null && (userRef.current.type === 1) && */<Route path="/addFood" element={<AddFood />}/>}
        </Routes>
    </BrowserRouter>)
}

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)

// end::render[]
