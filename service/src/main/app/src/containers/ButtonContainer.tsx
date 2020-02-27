import * as React from 'react';

interface ButtonProps {
    diceCount: number;
    handleAddDice: any;
    handleRemoveDice: any;
    handleRollDice: any;
    selectedDiceRowInd?: number | undefined;
}

export default class ButtonContainer extends React.Component<ButtonProps, any> {
    render = () => {
        return (
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
                            onClick={this.props.handleRollDice}
                            disabled={this.props.diceCount <= 0}>Roll</button>
                </div>
            </div>
        );
    }
}