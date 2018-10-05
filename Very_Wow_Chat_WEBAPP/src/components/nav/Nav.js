import React, { Component } from 'react';

import './Nav.css';
import LiElement from './LiElement';

export default class Nav extends Component {

  render() {
    return (
      <nav className="navigation">
        <LiElement name="Login" link="/"/>
        <LiElement name="SignUp" link="/home"/>
      </nav>
    );
  }
}