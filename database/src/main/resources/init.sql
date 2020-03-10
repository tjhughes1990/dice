CREATE SCHEMA dice;

CREATE TABLE dice.dice_collection (
    id bigint PRIMARY KEY,
    name text NOT NULL UNIQUE
);

CREATE TABLE dice.dice (
    id bigint NOT NULL UNIQUE,
    collection_id bigint NOT NULL,
    min_result int NOT NULL,
    max_result int NOT NULL,
    roll_number int NOT NULL
);

-- Add example dice collection
INSERT INTO dice.dice_collection(id, name)
    VALUES
        (1, 'D6 x3'),
        (2, 'D10 & D12');
INSERT INTO dice.dice(id, collection_id, min_result, max_result, roll_number)
    VALUES
        (1, 1, 1, 6, 3),
        (2, 2, 1, 10, 1),
        (3, 2, 1, 12, 1);