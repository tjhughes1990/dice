import { Action, Reducer } from 'redux';

import { IState } from './IState';
import { ActionType, AddAction, LoadAction, RemoveAction, RollAction } from '../actions/ActionType';
import { AddReducer } from './AddReducer';
import { LoadReducer } from './LoadReducer';
import { RemoveReducer } from './RemoveReducer';
import { RollReducer } from './RollReducer';

const initialState: IState = {
    diceList: []
};

/**
 * Root reducer.
 */
export const RootReducer: Reducer<IState, Action> = (oldState: IState | undefined, action: Action) => {
    if (oldState === undefined) {
        return initialState;
    }

    let newState: IState;
    switch (action.type) {
        case ActionType.ADD_ACTION:
            newState = AddReducer(oldState, action as AddAction);
            break;
        case ActionType.REMOVE_ACTION:
            newState = RemoveReducer(oldState, action as RemoveAction);
            break;
        case ActionType.ROLL_ACTION:
            newState = RollReducer(oldState, action as RollAction);
            break;
        case ActionType.LOAD_ACTION:
            newState = LoadReducer(oldState, action as LoadAction);
            break;
        default:
            newState = Object.assign({}, oldState);
            break;
    }

    return newState;
}
