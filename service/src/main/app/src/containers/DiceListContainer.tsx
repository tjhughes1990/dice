import React, { Component, MouseEvent } from 'react';

import './DiceListContainer.css';
import { Dice } from '../Dice';

interface DiceListProps {
    diceList: Array<Dice>;
    setSelectedDiceRow: any;
    selectedDiceRow: HTMLDivElement | undefined;
}

export default class DiceListContainer extends Component<DiceListProps, {}> {

    /**
     * Handle clicking a row.
     */
    handleDiceRowClick = (event: MouseEvent<HTMLDivElement>) => {
        const target: HTMLDivElement = event.target as HTMLDivElement;
        const parentRow: HTMLDivElement = target.parentElement as HTMLDivElement;
        if (parentRow !== null && typeof parentRow.dataset.id === 'string') {
            const oldSelectedDiceRow = this.props.selectedDiceRow;
            if (oldSelectedDiceRow !== undefined && oldSelectedDiceRow.dataset.id === parentRow.dataset.id) {
                // Previously selected row was deselected.
                this.props.setSelectedDiceRow(undefined);
                parentRow.className = 'diceRow row';
            } else {
                if (typeof oldSelectedDiceRow !== 'undefined') {
                    oldSelectedDiceRow.className = 'diceRow row';
                }
                parentRow.className = 'diceRowSelected row';

                this.props.setSelectedDiceRow(parentRow);
            }
        }
    }

    render = () => {
        let diceListElements: Array<JSX.Element> = [];
        let sum: number = 0;
        this.props.diceList.forEach((d, ind) => {
            diceListElements.push(<div key={ind.toString()}
                data-id={ind}
                className='diceRow row'
                onClick={this.handleDiceRowClick}>
                <div className='col-3'>{d.name}</div>
                <div className='col-3'>{d.rollNumber}</div>
                <div className='col-3 resultCol'>{d.sumResult ? d.sumResult : undefined}</div>
            </div>);

            if (d.sumResult !== undefined) {
                sum += d.sumResult;
            }
        });

        const isDiceListEmpty: boolean = diceListElements.length === 0;
        const contents: JSX.Element | Array<JSX.Element> = isDiceListEmpty
            ? <div className='diceRowNoSelect'>No dice added</div>
            : diceListElements;

        let totalDiv = undefined;
        const rolled: boolean = this.props.diceList.filter(d => d.sumResult !== undefined).length !== 0;
        if (rolled) {
            totalDiv = <div className='row diceTotalRow'>
                <div className='col-6 diceTotalTitle'>Total:</div>
                <div className='col-3 resultCol'>{sum}</div>
            </div>
        }

        return (
            <div className='diceListContainer'>
                <div className='title'>Dice list</div>
                <div className='diceRowTitle row'>
                    <div className='col-3'>Type</div>
                    <div className='col-3'>Number</div>
                    <div className='col-3 resultCol'>Result</div>
                </div>
                {contents}
                {totalDiv}
            </div>
        );
    }
}