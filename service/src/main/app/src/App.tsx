import * as React from 'react';

import { ReactSVG } from 'react-svg';
import { default as ButtonContainer } from './containers/ButtonContainer';
import { default as DiceListContainer } from './containers/DiceListContainer';
import { default as AddDiceModal } from './containers/AddDiceModal';
import { Dice } from './Dice';
import { default as RestEndpoint } from './endpoints/RestEndpoint';
import './App.css';

interface IState {
    diceList: Array<Dice>;
    selectedDiceRow: HTMLDivElement | undefined;
    showAddDiceModal: boolean;
}

export default class App extends React.Component<{}, IState> {

    public readonly state = {
        diceList: [],
        selectedDiceRow: undefined,
        showAddDiceModal: false
    }

    handleAddDice = (dice: Dice) => {
        let diceList: Array<Dice> = this.state.diceList;
        diceList.push(dice);
        this.setState({ 'diceList': diceList, 'showAddDiceModal': false });
    }

    handleRemoveDice = () => {
        if (typeof this.state.selectedDiceRow !== 'undefined') {
            const row: HTMLDivElement = this.state.selectedDiceRow!;
            const id: string | undefined = row.dataset.id;
            if (typeof id === 'string') {
                const oldDiceList = this.state.diceList;
                oldDiceList.splice(parseInt(id), 1);
                this.setState({ 'diceList': oldDiceList, 'selectedDiceRow': undefined });
                row.className = 'diceRow row';
            }
        }
    }

    handleRollDice = () => {
        RestEndpoint.rollDice(this.state.diceList).then(newDiceList=> {
            let combinedDiceList = this.state.diceList;
            for (let i = 0; i < combinedDiceList.length; i++) {
                if (newDiceList[i].sumResult) {
                    let dice: Dice = combinedDiceList[i];
                    dice.sumResult = newDiceList[i].sumResult;
                }
            }
            this.setState({ 'diceList': combinedDiceList });
        });
    }

    setSelectedDiceRow = (row: HTMLDivElement) => {
        this.setState({ 'selectedDiceRow': row });
    }

    showAddDiceModal = () => {
        this.setState({ 'showAddDiceModal': true });
    }

    hideAddDiceModal = () => {
        this.setState({ 'showAddDiceModal': false });
    }

    render = () => {
        return (
            <div className='App'>
                <AddDiceModal handleAddDice={this.handleAddDice}
                    handleCancel={this.hideAddDiceModal}
                    show={this.state.showAddDiceModal} />
                <div className='titleBar row'>
                    <ReactSVG className='diceIcon' src='d20-white.svg' beforeInjection={svg => {
                        svg.setAttribute('style', 'width: 50px')
                    }} />
                    <div>Dice Roller</div>
                </div>

                <div className='row'>
                    <ButtonContainer diceCount={this.state.diceList.length}
                        handleAddDice={this.showAddDiceModal}
                        handleRemoveDice={this.handleRemoveDice}
                        handleRollDice={this.handleRollDice}
                        selectedDiceRowInd={this.state.selectedDiceRow} />
                    <DiceListContainer diceList={this.state.diceList}
                        selectedDiceRow={this.state.selectedDiceRow}
                        setSelectedDiceRow={this.setSelectedDiceRow} />
                </div>
            </div>
        );
    }
}