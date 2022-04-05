'use strict';

// tag::vars[]
import Dashboard from "./dashboard";
import Preferences from "./preferences";
import Login from "./login";
import Logout from "./logout";
import Cart from "./cart";
import Signup from "./signup";

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const logout = require('./logout');
const dashboard = require('./dashboard');
const preferences = require('./preferences');
import { Button, Navbar } from 'react-bootstrap'
import {BrowserRouter, Route, Routes} from "react-router-dom";

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
		return (
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Dashboard />}/>
                    <Route path="/login" element={<Login />}/>
                    <Route path="/logout" element={<Logout />}/>
                    <Route path="/signup" element={<Signup />}/>
                    <Route path="/cart" element={<Cart />}/>
                </Routes>
            </BrowserRouter>
		)
	}
}

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)

// end::render[]
