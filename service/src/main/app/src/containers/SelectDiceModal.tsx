import React, { Component } from 'react';
import Select, { OptionTypeBase, ValueType } from 'react-select';
import Modal from 'react-bootstrap/Modal';

import { DiceCollection } from '../Dice';

import './DiceModal.css';

interface ISelectDiceModalProps {
    show: boolean;
    diceCollectionList: Array<DiceCollection>;
    title: string;
    okCallback: any;
    cancelCallback: any;
}

interface IState {
    selected: DiceCollection | undefined;
}

export default class SelectDiceModal extends Component<ISelectDiceModalProps, IState> {

    state: IState = {
        selected: undefined
    }

    handleSelect = (selected: ValueType<OptionTypeBase> | undefined) => {
        if (selected === undefined) {
            this.setState({ selected: undefined });
        } else {
            this.setState({ selected: ((selected as OptionTypeBase).value as DiceCollection) });
        }
    }

    handleAccept = () => {
        const selected: DiceCollection | undefined = this.state.selected;
        if (selected !== undefined) {
            this.props.okCallback(selected.id);
            this.setState({ selected: undefined });
        }
    }

    handleHide = () => {
        this.props.cancelCallback();
        this.setState({ selected: undefined });
    }

    render = () => {
        // Create dropdown options.
        const diceCollectionList = this.props.diceCollectionList;
        let options: OptionTypeBase[] = [];
        diceCollectionList.forEach((d: DiceCollection) => {
            options.push({
                value: d,
                label: d.name
            });
        });

        // Get the displayed item.
        let displayObj: OptionTypeBase = { value: undefined, label: 'Select. . .' };
        const selected: DiceCollection | undefined = this.state.selected;
        if (selected !== undefined) {
            diceCollectionList.some((d: DiceCollection) => {
                if (d.id === selected.id) {
                    displayObj = { value: d, label: d.name };
                    return true;
                } else {
                    return false;
                }
            });
        }

        return (
            <Modal show={this.props.show}>
                <Modal.Header>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <form>
                        <div>
                            <label>Dice Collection:</label>
                            <Select className='diceModalSelect'
                                value={displayObj}
                                isDisabled={this.props.diceCollectionList.length === 0}
                                onChange={this.handleSelect}
                                options={options} />
                        </div>
                    </form>
                </Modal.Body>

                <Modal.Footer>
                    <button type='button'
                        className='btn btn-primary'
                        disabled={this.state.selected === undefined}
                        onClick={this.handleAccept}>OK</button>
                    <button type='button'
                        className='btn btn-primary'
                        onClick={this.handleHide}>Cancel</button>
                </Modal.Footer>
            </Modal>
        );
    }
}