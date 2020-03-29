import React, { Component, ChangeEvent } from 'react';
import Modal from 'react-bootstrap/Modal';

import { Dice } from '../Dice';

import './DiceModal.css';

interface ISaveDiceModalProps {
    show: boolean;
    diceList: Array<Dice>;
    okCallback: any;
    cancelCallback: any;
}

interface IState {
    name: string | undefined;
}

export default class SelectDiceModal extends Component<ISaveDiceModalProps, IState> {

    state: IState = {
        name: undefined
    }

    handleNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        const name: string = e.currentTarget.value;
        if (name.length > 0) {
            this.setState({ name: name });
        } else {
            this.setState({ name: undefined });
        }
    }

    handleSaveDice = () => {
        this.props.okCallback({
            id: null,
            name: this.state.name,
            diceRolls: this.props.diceList
        });
    }

    render = () => {
        return (
            <Modal show={this.props.show}>
                <Modal.Header>
                    <Modal.Title>Save dice</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <form>
                        <div>
                            <label>Collection name:</label>
                            <input className={this.state.name !== undefined ? 'diceModalCount' : 'diceModalCountInvalid'}
                                type='text'
                                defaultValue={this.state.name}
                                onChange={this.handleNameChange} />
                        </div>
                    </form>
                </Modal.Body>

                <Modal.Footer>
                    <button type='button'
                        className='btn btn-primary'
                        disabled={this.state.name === undefined}
                        onClick={this.handleSaveDice}>OK</button>
                    <button type='button'
                        className='btn btn-primary'
                        onClick={this.props.cancelCallback}>Cancel</button>
                </Modal.Footer>
            </Modal>
        );
    }
}
