import { Reducer } from 'redux';

import { IState } from './IState';
import { AddAction } from '../actions/ActionType';

/**
 * Add dice reducer.
 */
export const AddReducer: Reducer<IState, AddAction> = (oldState: IState | undefined, action: AddAction) => {
    const newState: IState = Object.assign({}, oldState);

    if(oldState != undefined) {
        newState.diceList = [...oldState.diceList];
        newState.diceList.push(action.dice);
    }

    return newState;
}
