import React, { Component } from 'react';
import Select, { OptionTypeBase, ValueType } from 'react-select';
import { Store } from 'redux';
import Modal from 'react-bootstrap/Modal';

import { default as RestEndpoint } from '../endpoints/RestEndpoint';
import { DiceCollection } from '../Dice';

interface ISelectDiceModalProps {
    store: Store;
    show: boolean;
    title: string;
    okCallback: any;
    cancelCallback: any;
}

interface IState {
    diceCollectionList: Array<DiceCollection>;
    selected: DiceCollection | undefined;
}

export default class SelectDiceModal extends Component<ISelectDiceModalProps, IState> {

    state: IState = {
        diceCollectionList: [],
        selected: undefined
    }

    getCollections = () => {
        RestEndpoint.getCollections().then((diceCollectionList: Array<DiceCollection>) => {
            const stateIds: Array<number> = this.state.diceCollectionList.map(d => (d as DiceCollection).id);
            diceCollectionList.map(d => d.id).forEach((i: number) => {
                if (!stateIds.includes(i)) {
                    this.setState({ diceCollectionList: diceCollectionList });
                    return;
                }
            });
        });
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
        let showModal: boolean = this.props.show;
        if (showModal) {
            this.getCollections();
        }

        // Create dropdown options.
        const diceCollectionList = this.state.diceCollectionList;
        let options: OptionTypeBase[] = [];
        diceCollectionList.forEach((d: DiceCollection) => {
            options.push({
                value: d,
                label: d.name
            });
        })

        // Get the displayed item.
        let displayObj: OptionTypeBase = { value: undefined, label: 'Select. . .'  };
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
            <Modal show={showModal}>
                <Modal.Header>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <form>
                        <div>
                            <label>Dice Type:</label>
                            <Select className='diceModalSelect'
                                value={displayObj}
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