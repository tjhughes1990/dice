import { Component } from 'react';

import { Dice } from '../Dice';

export default class RestEndpoint extends Component {
    static rollDice = async (request: Array<Dice>) => {

        const requestBody: string = JSON.stringify(request);

        const requestUrl = 'http://localhost:8080/roll';
        const requestHeaders: HeadersInit = new Headers();
        requestHeaders.append('Accept', 'application/json');
        requestHeaders.append('Content-Length', requestBody.length.toString());
        requestHeaders.append('Content-Type', 'application/json');

        return await fetch(requestUrl, {
            method: 'POST',
            headers: requestHeaders,
            mode: 'cors',
            body: requestBody
        }).then(response => response.json());
    }
}