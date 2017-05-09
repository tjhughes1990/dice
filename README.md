# Dice Roller

This small Java program was designed as a player aid to simulate dice rolls for RPGs.

## Features

* Dice types available:
  * D4
  * D6
  * D8
  * D10
  * D12
  * D20

* Any number of dice up to a maximum of 25 can be rolled at once.

* The sum of the dice rolls is automatically calculated, and displayed to the user.

* The number of "successes" (rolls exceeding a user-defined threshold) are automatically calculated, and displayed to the user. There are also specific options to:
  * Count maximum rolls as two successes.
  * Notify the user of a "botched" roll (rolling a 1, with zero successes).
  
* The `config.txt` file can be used to set the default values on startup. This file must be placed in the same directory as the Dice Roller executable.
