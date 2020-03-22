import { Reducer } from 'redux';

import { IState } from './IState';
import { LoadAction } from '../actions/ActionType';

/**
 * Load dice reducer.
 */
export const LoadReducer: Reducer<IState, LoadAction> = (oldState: IState | undefined, action: LoadAction) => {
    const newState: IState = Object.assign({}, oldState);

    newState.diceList = action.dice;

    return newState;
}