/* ------------------------------------------------------------------------------
   ------------------------IMPORTS START-----------------------------------------
   ------------------------------------------------------------------------------*/

import React from 'react';
import ReactDOM from 'react-dom';
import { Router } from 'react-router-dom';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';

/* ------------------------------------------------------------------------------
   ------------------------IMPORTS End-------------------------------------------
   ------------------------------------------------------------------------------*/

import thunk from 'redux-thunk';
//import rootReducer from './reducers';  MUNA Að Uncommenta
import App from './App';
import history from './history';
import './index.css';

/* verkefni sett upp til að styðja async actions í redux ef þurfum */
const store = createStore(
  //rootReducer,      MUNA Að Uncommenta
  applyMiddleware(thunk)
);

/* Provider gerir það þannig að allt i þvi hefur aðgang af store 
  sem er basicly allt i okkar tilfelli  við notum lika history til 
  að rederecta i actions sem er mjög þægilegt ef notkun á þvi þarfst i frammtiðini */
ReactDOM.render(
  <Provider store={store} >
    <Router history={history}>
      <App />
    </Router>
  </Provider>, document.getElementById('root'));