import * as React from 'react';

import './App.css';
import { default as ButtonContainer } from './containers/ButtonContainer';
import { default as DiceListContainer } from './containers/DiceListContainer';
import { Dice } from './Dice';

interface IState {
    diceList: Array<Dice>;
    selectedDiceRow: HTMLDivElement | undefined;
}

export default class App extends React.Component<{}, IState> {

    public readonly state = {
        diceList: [],
        selectedDiceRow: undefined
    }

    handleAddDice = () => {
        let diceList: Array<Dice> = this.state.diceList;
        
        
        // TODO: Get dice info from dialog.
        diceList.push({name: "D6", lowerBound: 1, upperBound: 6, count: 1});
        
        
        this.setState({"diceList": diceList});
    }

    /**
     * Handle removing a dice.
     */
    handleRemoveDice = () => {
        if(typeof this.state.selectedDiceRow !== "undefined") {
            const row: HTMLDivElement = this.state.selectedDiceRow!;
            const id: string | undefined = row.dataset.id;
            if(typeof id === "string") {
                const oldDiceList = this.state.diceList;
                oldDiceList.splice(parseInt(id), 1);
                this.setState({"diceList": oldDiceList, "selectedDiceRow": undefined});
                row.className = "diceRow";
            }
        }
    }

    /**
     * Handle rolling a dice.
     */
    handleRollDice = () => {
        console.log("Dice rolled.");
    }

    setSelectedDiceRow = (row: HTMLDivElement) => {
        this.setState({"selectedDiceRow": row});
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
                            selectedDiceRowInd={this.state.selectedDiceRow} />
                    <DiceListContainer diceList={this.state.diceList}
                            selectedDiceRow={this.state.selectedDiceRow}
                            setSelectedDiceRow={this.setSelectedDiceRow}/>
                </div>
            </div>
        );
    }
}