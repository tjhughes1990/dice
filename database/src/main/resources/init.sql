CREATE SCHEMA dice;

CREATE TABLE dice.dice_collection (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE dice.dice (
    id BIGSERIAL PRIMARY KEY,
    collection_id BIGINT NOT NULL,
    min_result INT NOT NULL,
    max_result INT NOT NULL,
    roll_number INT NOT NULL,
    constraint fk_dice_id
        FOREIGN KEY (collection_id)
        REFERENCES dice.dice_collection(id)
        ON DELETE CASCADE
);

-- Add example dice collection
INSERT INTO dice.dice_collection(name)
    VALUES
        ('D6 x3'),
        ('D10 & D12');
INSERT INTO dice.dice(collection_id, min_result, max_result, roll_number)
    VALUES
        (1, 1, 6, 3),
        (2, 1, 10, 1),
        (2, 1, 12, 1);
