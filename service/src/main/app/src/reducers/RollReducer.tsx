import { Reducer } from 'redux';

import { IState } from './IState';
import { RollAction } from '../actions/ActionType';

/**
 * Roll dice reducer.
 */
export const RollReducer: Reducer<IState, RollAction> = (oldState: IState | undefined, action: RollAction) => {
    const newState: IState = Object.assign({}, oldState);
    newState.diceList = action.rolls;

    return newState;
}