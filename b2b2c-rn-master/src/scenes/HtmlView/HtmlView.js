/**
 * Created by Andste on 2019-01-23.
 */
import React, {Component} from 'react';
import {WebView} from 'react-native';
import {connect} from 'react-redux';

class HtmlView extends Component {
    render() {
        return (
            <WebView
                source={{uri: this.url}}
                style={{marginTop: 1}}
            />
        );
    }

    constructor(props) {
        super(props)
        this.url = props.navigation.state.params.url
    }
}

export default connect()(HtmlView)
