import React, { Component, MouseEvent} from 'react';
import { Dice } from '../Dice';

interface DiceListProps {
    diceList: Array<Dice>;
    setSelectedDiceRow: any;
    selectedDiceRow: HTMLDivElement | undefined;
}

export default class DiceListContainer extends Component<DiceListProps, {}> {

    constructor(props: any) {
        super(props);
    }

    /**
     * Handle clicking a row.
     */
    handleDiceRowClick = (event: MouseEvent<HTMLDivElement>) => {
        const target = event.target as HTMLDivElement;
        if(typeof target.dataset.id === "string") {
            const oldSelectedDiceRow = this.props.selectedDiceRow;
            if(oldSelectedDiceRow !== undefined && oldSelectedDiceRow.dataset.id === target.dataset.id) {
                // Previously selected row was deselected.
                this.props.setSelectedDiceRow(undefined);
                target.className = "diceRow";
            } else {
                const ind: number = parseInt(target.dataset.id);
                if(typeof oldSelectedDiceRow !== "undefined") {
                    oldSelectedDiceRow.className = "diceRow";
                }
                target.className = "diceRowSelected";

                this.props.setSelectedDiceRow(target);
            }
        }
    }

    render = () => {
        let diceListElements: Array<JSX.Element> = [];
        this.props.diceList.forEach((d, ind) => {
            diceListElements.push(<div key={ind.toString()}
                                          data-id={ind}
                                          className="diceRow"
                                          onClick={this.handleDiceRowClick}>
                                      {d.name} X{d.count}
                                  </div>);
        });

        let contents: JSX.Element | Array<JSX.Element> = diceListElements.length === 0
                ? <div className="diceRowNoSelect">No dice added</div>
                : diceListElements;

        return (
            <div className="diceListContainer">
                <div className="title">Dice list</div>
                {contents}
            </div>
        );
    }
}