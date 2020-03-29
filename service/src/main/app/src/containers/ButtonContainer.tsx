import React, { Component } from 'react';
import { Store } from 'redux';

import { createRollAction, createLoadAction } from '../actions/ActionType';
import { default as RestEndpoint } from '../endpoints/RestEndpoint';
import { default as SelectDiceModal } from './SelectDiceModal';
import { Dice } from '../Dice';

import './ButtonContainer.css';

interface ButtonProps {
    store: Store;
    handleAddDice: any;
    handleRemoveDice: any;
    selectedDiceRow: HTMLDivElement | undefined;
}

interface IState {
    showLoadDiceModal: boolean;
    showDeleteDiceModal: boolean;
}

export default class ButtonContainer extends Component<ButtonProps, IState> {

    state: IState = {
        showLoadDiceModal: false,
        showDeleteDiceModal: false
    }

    handleRollDice = () => {
        RestEndpoint.rollDice(this.props.store.getState().diceList).then(newDiceList => {
            this.props.store.dispatch(createRollAction(newDiceList));
        });
    }

    handleLoadDice = (id: number) => {
        RestEndpoint.loadDice(id).then((diceList: Array<Dice>) => {
            this.props.store.dispatch(createLoadAction(diceList));
            this.toggleLoadDiceModal(false);
        });
    }

    handleDeleteDice = (id: number) => {
        RestEndpoint.deleteDice(id).then(() => {
            this.props.store.dispatch(createLoadAction([]));
            this.toggleDeleteDiceModal(false);
        });
    }

    toggleLoadDiceModal = (showState: boolean) => {
        this.setState({ showLoadDiceModal: showState });
    }

    toggleDeleteDiceModal = (showState: boolean) => {
        this.setState({ showDeleteDiceModal: showState });
    }

    render = () => {
        const diceCount: number = this.props.store.getState().diceList.length;

        return (
            <div>
                <div className='buttonContainer'>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={this.props.handleAddDice}>Add dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={this.props.handleRemoveDice}
                            disabled={this.props.selectedDiceRow === undefined}>Remove dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={this.handleRollDice}
                            disabled={diceCount <= 0}>Roll</button>
                    </div>
                </div>

                <SelectDiceModal store={this.props.store}
                    show={this.state.showLoadDiceModal}
                    title='Load dice'
                    okCallback={(id: number) => this.handleLoadDice(id)}
                    cancelCallback={() => this.toggleLoadDiceModal(false)} />
                <SelectDiceModal store={this.props.store}
                    show={this.state.showDeleteDiceModal}
                    title='Delete dice'
                    okCallback={(id: number) => this.handleDeleteDice(id)}
                    cancelCallback={() => this.toggleDeleteDiceModal(false)} />
                <div className='buttonContainer'>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={undefined}
                            disabled={diceCount <= 0}>Save dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={() => this.toggleLoadDiceModal(true)}>Load dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={() => this.toggleDeleteDiceModal(true)}>Delete dice</button>
                    </div>
                </div>
            </div>
        );
    }
}