import { Reducer } from 'redux';

import { IState } from './IState';
import { RemoveAction } from '../actions/ActionType';

/**
 * Remove dice reducer.
 */
export const RemoveReducer: Reducer<IState, RemoveAction> = (oldState: IState | undefined, action: RemoveAction) => {
    const newState: IState = Object.assign({}, oldState);

    const ind = action.index;
    if (ind >= 0 && ind < newState.diceList.length) {
        newState.diceList.splice(ind, 1);
        
        // Update IDs.
        newState.diceList.forEach((d, i) => {
            d.id = i;
        });
    }

    return newState;
}
