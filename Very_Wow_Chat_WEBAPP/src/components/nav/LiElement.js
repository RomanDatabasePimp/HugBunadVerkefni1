import React, { Component } from 'react';
import { Link } from 'react-router-dom';

export default class LiElement extends Component {

  render() {
    return (
      <li className="navigation__element">
        <Link to={this.props.link}>
          {this.props.name}
        </Link>
      </li>
    )
  }
}