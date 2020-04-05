import React, { Component } from 'react';
import { Store } from 'redux';

import { createRollAction, createLoadAction } from '../actions/ActionType';
import { default as RestEndpoint } from '../endpoints/RestEndpoint';
import { default as SaveDiceModal } from './SaveDiceModal';
import { default as SelectDiceModal } from './SelectDiceModal';
import { Dice, DiceCollection } from '../Dice';

import './ButtonContainer.css';

interface ButtonProps {
    store: Store;
    handleAddDice: any;
    handleRemoveDice: any;
    selectedDiceRow: HTMLDivElement | undefined;
}

interface IState {
    isDatabaseAvailable: boolean | undefined;
    showSaveDiceModal: boolean;
    showLoadDiceModal: boolean;
    showDeleteDiceModal: boolean;
    diceCollectionList: Array<DiceCollection>;
}

export default class ButtonContainer extends Component<ButtonProps, IState> {

    state: IState = {
        isDatabaseAvailable: undefined,
        showSaveDiceModal: false,
        showLoadDiceModal: false,
        showDeleteDiceModal: false,
        diceCollectionList: []
    }

    constructor(props: ButtonProps) {
        super(props)
        this.getCollections();
    }

    getCollections = () => {
        RestEndpoint.getCollections().then((json) => {
            const isDatabaseAvailable: boolean = json !== undefined && json.status === undefined;
            
            // Set the state of the database available flag once, in the constructor.
            if(this.state.isDatabaseAvailable === undefined) {
                this.setState({ 'isDatabaseAvailable': isDatabaseAvailable });
            }

            if (!isDatabaseAvailable) {
                return;
            }

            const diceCollectionList: Array<DiceCollection> = json as Array<DiceCollection>;
            const stateIds: Array<number> = this.state.diceCollectionList.map(d => (d as DiceCollection).id);

            if (diceCollectionList.length !== stateIds.length) {
                // If the lengths differ, update list in state.
                this.setState({ diceCollectionList: diceCollectionList });
            } else {
                // If the state does not include all dice collections, update list in state.
                diceCollectionList.map(d => d.id).forEach((i: number) => {
                    if (!stateIds.includes(i)) {
                        this.setState({ diceCollectionList: diceCollectionList });
                        return;
                    }
                });
            }
        });
    }

    handleRollDice = () => {
        RestEndpoint.rollDice(this.props.store.getState().diceList).then(newDiceList => {
            this.props.store.dispatch(createRollAction(newDiceList));
        });
    }

    handleSaveDice = (diceCollection: DiceCollection) => {
        RestEndpoint.saveDice(diceCollection).finally(() => {
            this.getCollections();
            this.toggleSaveDiceModal(false);
        });
    }

    handleLoadDice = (id: number) => {
        RestEndpoint.loadDice(id).then((diceList: Array<Dice>) => {
            this.props.store.dispatch(createLoadAction(diceList));
            this.toggleLoadDiceModal(false);
        });
    }

    handleDeleteDice = (id: number) => {
        RestEndpoint.deleteDice(id).finally(() => {
            this.getCollections();
            this.toggleDeleteDiceModal(false);
        });
    }

    toggleSaveDiceModal = (showState: boolean) => {
        this.setState({ showSaveDiceModal: showState });
    }

    toggleLoadDiceModal = (showState: boolean) => {
        this.setState({ showLoadDiceModal: showState });
    }

    toggleDeleteDiceModal = (showState: boolean) => {
        this.setState({ showDeleteDiceModal: showState });
    }

    renderDatabaseButtons = (isDiceCountNonZero: boolean) => {
        if (this.state.isDatabaseAvailable) {
            return (
                <div>
                    <SaveDiceModal show={this.state.showSaveDiceModal}
                        diceList={this.props.store.getState().diceList}
                        okCallback={this.handleSaveDice}
                        cancelCallback={() => this.toggleSaveDiceModal(false)} />
                    <SelectDiceModal show={this.state.showLoadDiceModal}
                        diceCollectionList={this.state.diceCollectionList}
                        title='Load dice'
                        okCallback={(id: number) => this.handleLoadDice(id)}
                        cancelCallback={() => this.toggleLoadDiceModal(false)} />
                    <SelectDiceModal show={this.state.showDeleteDiceModal}
                        diceCollectionList={this.state.diceCollectionList}
                        title='Delete dice'
                        okCallback={(id: number) => this.handleDeleteDice(id)}
                        cancelCallback={() => this.toggleDeleteDiceModal(false)} />
                    <div className='buttonContainer'>
                        <div className='row'>
                            <button type='button'
                                className='btn btn-primary'
                                onClick={() => this.toggleSaveDiceModal(true)}
                                disabled={isDiceCountNonZero}>Save dice</button>
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
        } else {
            return undefined;
        }
    }

    render = () => {
        const isDiceCountNonZero: boolean = this.props.store.getState().diceList.length <= 0;

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
                            disabled={isDiceCountNonZero}>Roll</button>
                    </div>
                </div>

                {this.renderDatabaseButtons(isDiceCountNonZero)};
            </div>
        );
    }
}