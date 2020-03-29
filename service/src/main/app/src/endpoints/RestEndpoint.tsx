import { Component } from 'react';

import { Dice, DiceCollection } from '../Dice';

export default class RestEndpoint extends Component {

    static readonly rootUrl: string = 'http://localhost:8080/';

    /**
     * Roll dice REST request.
     */
    static rollDice = async (request: Array<Dice>) => {

        const requestUrl: string = RestEndpoint.rootUrl + 'roll';
        const requestBody: string = JSON.stringify(request);

        return RestEndpoint.post(requestUrl, requestBody).then(response => response.json());
    }

    /**
     * Send a POST request.
     */
    static post = async (url: string, body: string) => {
        const requestHeaders: HeadersInit = new Headers();
        requestHeaders.append('Accept', 'application/json');
        requestHeaders.append('Content-Length', body.length.toString());
        requestHeaders.append('Content-Type', 'application/json');

        return await fetch(url, {
            method: 'POST',
            headers: requestHeaders,
            mode: 'cors',
            body: body
        });
    }

    /**
     * Get available dice collections from the database.
     */
    static getCollections = async () => {
        const requestUrl: string = RestEndpoint.rootUrl + 'getCollections';
        return RestEndpoint.get(requestUrl).then(response => response.json());
    }

    /**
     * Save a dice collection to the database.
     *
     * @param request the {@link DiceCollection} to save to the database.
     */
    static saveDice = async (request: DiceCollection) => {
        const requestUrl: string = RestEndpoint.rootUrl + 'save';
        const requestBody: string = JSON.stringify(request);

        return RestEndpoint.post(requestUrl, requestBody).then(response => response.json());
    }

    /**
     * Load a dice collection from the database.
     *
     * @param id the ID of the collection to load from the database.
     */
    static loadDice = async (id: number) => {
        const requestUrl: string = RestEndpoint.rootUrl + 'load?id=' + id;
        return RestEndpoint.get(requestUrl).then(response => response.json());
    }

    /**
     * Delete a dice collection from the database.
     *
     * @param id the ID of the collection to delete from the database.
     */
    static deleteDice = async (id: number) => {
        const requestUrl: string = RestEndpoint.rootUrl + 'delete?id=' + id;
        return RestEndpoint.get(requestUrl).then(response => response.json());
    }

    /**
     * Send a GET request.
     */
    static get = async (url: string) => {
        const requestHeaders: HeadersInit = new Headers();
        requestHeaders.append('Accept', 'application/json');

        return await fetch(url, {
            method: 'GET',
            headers: requestHeaders,
            mode: 'cors',
        });
    }
}