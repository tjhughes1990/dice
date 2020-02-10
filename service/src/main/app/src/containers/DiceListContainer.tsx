import * as React from 'react';
import { Dice } from '../Dice';

interface DiceListProps {
    diceList: Array<Dice>;
}

export default class DiceListContainer extends React.Component<DiceListProps, {}> {
    render = () => {
        let diceListElements: any = []
        this.props.diceList.forEach((d, ind) => {
            diceListElements.push(<div key={ind.toString()}
                                          className="diceRow">
                                      {d.name} X{d.count}
                                  </div>);
        });

        let contents: any = diceListElements.length === 0
                ? <div className="diceRow">No dice added</div>
                : diceListElements;

        return (
            <div className="diceListContainer">
                <div className="title">Dice list</div>
                {contents}
            </div>
        );
    }
}