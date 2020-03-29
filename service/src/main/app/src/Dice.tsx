export type Dice = {
    id: number
    name: string;
    minResult: number;
    maxResult: number;
    rollNumber: number;
    sumResult?: number;
}

export type DiceCollection = {
    id: number;
    name: string;
}
