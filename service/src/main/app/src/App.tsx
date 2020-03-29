import * as React from 'react';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import { ReactSVG } from 'react-svg';

import { default as ButtonContainer } from './containers/ButtonContainer';
import { default as DiceListContainer } from './containers/DiceListContainer';
import { default as AddDiceModal } from './containers/AddDiceModal';
import { RootReducer } from './reducers/RootReducer';
import { createRemoveAction } from './actions/ActionType';

import './App.css';

export interface IState {
    selectedDiceRow: HTMLDivElement | undefined;
    showAddDiceModal: boolean;
}

export default class App extends React.Component<{}, IState> {

    readonly store = createStore(RootReducer);

    readonly state: IState = {
        selectedDiceRow: undefined,
        showAddDiceModal: false
    }

    handleRemoveDice = () => {
        if (typeof this.state.selectedDiceRow !== 'undefined') {
            const row: HTMLDivElement = this.state.selectedDiceRow!;
            const id: string | undefined = row.dataset.id;
            if (typeof id === 'string') {
                const index: number = parseInt(id);
                this.setState({ 'selectedDiceRow': undefined });
                row.className = 'diceRow row';

                this.store.dispatch(createRemoveAction(index));
            }
        }
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
                <Provider store={this.store}>
                    <AddDiceModal store={this.store}
                        handleCancel={this.hideAddDiceModal}
                        show={this.state.showAddDiceModal} />
                    <div className='titleBar row'>
                        <ReactSVG className='diceIcon' src='d20-white.svg' beforeInjection={svg => {
                            svg.setAttribute('style', 'width: 50px')
                        }} />
                        <div>Dice Roller</div>
                    </div>

                    <div className='row'>
                        <ButtonContainer store={this.store}
                            handleAddDice={this.showAddDiceModal}
                            handleRemoveDice={this.handleRemoveDice}
                            selectedDiceRow={this.state.selectedDiceRow} />
                        <DiceListContainer
                            selectedDiceRow={this.state.selectedDiceRow}
                            setSelectedDiceRow={this.setSelectedDiceRow} />
                    </div>
                </Provider>
            </div>
        );
    }
}