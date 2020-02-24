import React, { Component, ChangeEvent } from 'react';

import Modal from 'react-bootstrap/Modal';
import Select, { OptionTypeBase } from 'react-select';
import './AddDiceModal.css';

interface AddDiceModalProps {
    handleAddDice: any;
    handleCancel: any;
    show: boolean;
}

interface IState {
    type: OptionTypeBase;
    count: number | undefined;
}

const diceTypes: OptionTypeBase[] = [
  { value: {name: "Coin", lowerBound: 0, upperBound: 1}, label: "Coin" },
  { value: {name: "D4", lowerBound: 1, upperBound: 4}, label: "D4" },
  { value: {name: "D6", lowerBound: 1, upperBound: 6}, label: "D6" },
  { value: {name: "D8", lowerBound: 1, upperBound: 8}, label: "D8" },
  { value: {name: "D10", lowerBound: 1, upperBound: 10}, label: "D10" },
  { value: {name: "D12", lowerBound: 1, upperBound: 12}, label: "D12" },
  { value: {name: "D20", lowerBound: 1, upperBound: 20}, label: "D20" }
];

export default class AddDiceModal extends Component<AddDiceModalProps, IState> {

    state = {
        type: diceTypes[2],
        count: 1,
    }

    handleTypeSelect = (e: OptionTypeBase) => {
        this.setState({type: e});
    }

    handleCountChange = (e: ChangeEvent<HTMLInputElement>) => {
        let countValue: string = e.currentTarget.value;

        if(this.validateCount(countValue)) {
            this.setState({"count": parseInt(countValue)});
        } else {
            this.setState({"count": undefined});
        }
    }


    handleAddDice = () => {
        let value = this.state.type.value;
        this.props.handleAddDice({
            "name": value.name,
            "lowerBound": value.lowerBound,
            "upperBound": value.upperBound,
            "count": this.state.count
         });
    }

    validateCount = (text: string) => {
        if(text.length === 0) {
            return false;
        }

        for(let i = 0; i < text.length; i++) {
            let character: number = text.charCodeAt(i);
            if(character < 48 || character > 57) {
                return false;
            }
        }

        let num: number = parseInt(text);
        return !isNaN(num) && num <= 20;
    }

    render = () => {
        return (
            <Modal show={this.props.show}>
                <Modal.Header>
                    <Modal.Title>Add dice</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form>
                        <div>
                            <label>Dice Type:</label>
                            <Select className="diceModalSelect"
                                    value={this.state.type}
                                    onChange={this.handleTypeSelect}
                                    options={diceTypes} />
                        </div>
                        <div style={{margin: "10px 0 0 0"}}>
                            <label>Number:</label>
                            <input className={this.state.count !== undefined ? "diceModalCount": "diceModalCountInvalid"}
                                    type="text"
                                    defaultValue={this.state.count}
                                    onChange={this.handleCountChange} />
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <button type="button"
                            className="btn btn-primary"
                            disabled={this.state.count === undefined}
                            onClick={this.handleAddDice}>OK</button>
                    <button type="button"
                            className="btn btn-primary"
                            onClick={this.props.handleCancel}>Cancel</button>
                </Modal.Footer>
            </Modal>
        );
    }
}