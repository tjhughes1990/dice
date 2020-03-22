import { Action, ActionCreator } from 'redux';

import { Dice } from '../Dice';

/**
 * Enum of possible actions.
 */
export enum ActionType {
    ADD_ACTION = 'ADD',
    REMOVE_ACTION = 'REMOVE',
    ROLL_ACTION = 'ROLL',
    LOAD_ACTION = 'LOAD'
}

export interface AddAction extends Action {
    type: ActionType.ADD_ACTION,
    dice: Dice
}

export const createAddAction: ActionCreator<AddAction> = (dice: Dice) => ({
    type: ActionType.ADD_ACTION,
    dice: dice
});

export interface RemoveAction extends Action {
    type: ActionType.REMOVE_ACTION,
    index: number
}

export const createRemoveAction: ActionCreator<RemoveAction> = (ind: number) => ({
    type: ActionType.REMOVE_ACTION,
    index: ind
});

export interface RollAction extends Action {
    type: ActionType.ROLL_ACTION,
    rolls: Array<Dice>
}

export const createRollAction: ActionCreator<RollAction> = (rolls: Array<Dice>) => ({
    type: ActionType.ROLL_ACTION,
    rolls: rolls
});

export interface LoadAction extends Action {
    type: ActionType.LOAD_ACTION,
    dice: Array<Dice>
}

export const createLoadAction: ActionCreator<LoadAction> = (dice: Array<Dice>) => ({
    type: ActionType.LOAD_ACTION,
    dice: dice
});