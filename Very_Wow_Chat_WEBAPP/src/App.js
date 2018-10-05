/* ------------------------------------------------------------------------------
   ------------------------IMPORTS START-----------------------------------------
   ------------------------------------------------------------------------------*/

import React, { Component } from 'react';
import { connect } from 'react-redux';
import Helmet from 'react-helmet';
import { Route, Switch, withRouter } from 'react-router-dom'
import NavBar from './components/nav/Nav';
import NotFound from './routers/notFound';
import Home from './routers/home';
import './App.css';

/* ------------------------------------------------------------------------------
   ------------------------IMPORTS End-------------------------------------------
   ------------------------------------------------------------------------------*/


/* Virknin á App er að það á að sjá um að byrta þau Routes sem verða i notkun
  og þeir Routes sem eru byrt munu sjá um sýna eigin virkni (önnur routes eða Componenta) */   
export default  class App extends Component {

    render() {
      
      return (
        <main className='main'>
          {/* Tiltill á vefsiðuni verður alltaf xxx - (nafn af nemfelagi) */}
          <Helmet defaultTitle='Felag' titleTemplate='%s – Felag' />
          
          {/* Navigationbar Component */}
          <NavBar />
          
          {/* allt innihald siðu er birtur i þessum element 
            það verður athugað slóðina sem notandi er staddur á
            og switch og Router munu sjá um að byrta réttan route */}
          <div className="main__content">
            <Switch location={this.props.location}>
              <Route path="/" exact component={Home} />

              {/* ef ekkert af þessu passaði þá er byrt villu siðu */}
              <Route component={NotFound} />
            </Switch>
          </div>
        </main>
      );
    }
  } 