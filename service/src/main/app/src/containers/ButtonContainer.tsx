import * as React from 'react';
import { Store } from 'redux';

import { createRollAction } from '../actions/ActionType';
import { default as RestEndpoint } from '../endpoints/RestEndpoint';

import './ButtonContainer.css';

interface ButtonProps {
    store: Store;
    handleAddDice: any;
    handleRemoveDice: any;
    selectedDiceRowInd?: number | undefined;
}

export default class ButtonContainer extends React.Component<ButtonProps, any> {

    handleRollDice = () => {
        RestEndpoint.rollDice(this.props.store.getState().diceList).then(newDiceList => {
            this.props.store.dispatch(createRollAction(newDiceList));
        });
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
                            disabled={this.props.selectedDiceRowInd === undefined}>Remove dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={this.handleRollDice}
                            disabled={diceCount <= 0}>Roll</button>
                    </div>
                </div>
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
                            onClick={undefined}>Load dice</button>
                    </div>
                    <div className='row'>
                        <button type='button'
                            className='btn btn-primary'
                            onClick={undefined}>Delete dice</button>
                    </div>
                </div>
            </div>
        );
    }
}