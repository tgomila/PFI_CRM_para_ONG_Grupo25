import React from 'react';
import logo from './logo.svg';
import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import ListEmployeeComponent from './components/ListEmployeeComponent';
import FooterComponent from './components/FooterComponent';
import CreateEmployeeComponent from './components/CreateEmployeeComponent';
import UpdateEmployeeComponent from './components/UpdateEmployeeComponent';
import ViewEmployeeComponent from './components/ViewEmployeeComponent';
/*

import { NavigationBar } from './components/NavigationBar';
import { Home } from './Home';
import { About } from './About';
import Sidebar from './components/Sidebar';
*/

import {BrowserRouter, Route, Routes, Navigate, NavLink, Link, Nav} from 'react-router-dom'

import Error404 from './components/Error404';

function App() {
  return (
    <div>

<BrowserRouter>

    <Routes>
      <Route path='/' element={<ListEmployeeComponent/>}/>
      
      <Route path='*' element={<Error404/>}/>
      
      
      
      
      
    </Routes>
 

</BrowserRouter>










      {
        /*

<Router>

        <NavigationBar />

        <Sidebar />

                <div className="container">
                    <Switch> 
                          <Route path = "/" exact element = {<ListEmployeeComponent/>}></Route>
                          <Route path = "/employees" component = {ListEmployeeComponent}></Route>
                          <Route path = "/add-employee/:id" component = {CreateEmployeeComponent}></Route>
                          <Route path = "/view-employee/:id" component = {ViewEmployeeComponent}></Route>

                          <Route component={NoMatch} />
                    </Switch>
                </div>
              <FooterComponent />

        </Router>





        */
      }
        
    </div>
    
  );
}

export default App;
