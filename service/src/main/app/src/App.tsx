import * as React from 'react';

import './App.css';
import { default as ButtonContainer } from './containers/ButtonContainer';
import { default as DiceListContainer } from './containers/DiceListContainer';
import { Dice } from './Dice';

export default class App extends React.Component {

    App = () => {
    }

    state = {
        diceList: [],
        selectedDiceIndex: undefined
    }

    handleAddDice = () => {
        let diceList: Array<Dice> = this.state.diceList;
        
        
        // TODO: Get dice info from dialog.
        diceList.push({name: "D6", lowerBound: 1, upperBound: 6, count: 1});
        
        
        this.setState({"diceList": diceList});
        console.log(this.state.diceList);
    }

    /**
     * Handle removing a dice.
     */
    handleRemoveDice = () => {
        console.log("Dice removed.");
    }

    /**
     * Handle rolling a dice.
     */
    handleRollDice = () => {
        console.log("Dice rolled.");
    }

    render = () => {
        return (
            <div className="App">
                <div className='titleBar'>Dice Roller</div>

                <div className="row">
                    <ButtonContainer diceCount={this.state.diceList.length}
                            handleAddDice={this.handleAddDice}
                            handleRemoveDice={this.handleRemoveDice}
                            handleRollDice={this.handleRollDice}
                            selectedDiceIndex={this.state.selectedDiceIndex} />
                    <DiceListContainer diceList={this.state.diceList}/>
                </div>
            </div>
        );
    }
}