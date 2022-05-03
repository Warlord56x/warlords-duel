# Usage

The starting point is the main method in the Core class,
that is inside the Core Package / Folder.

There are already compiled files in bin

    cd bin
    java Core.Core

To compile (It will compile them next to the .java files)

    javac src/Core/*.java src/Units/*.java

Run self compiled

    cd src
    java Core.Core

VS code usage is possible if the necessary extensions are installed.
Simply open the folder in VS code and use the "run" button.

# Commands

## Global commands

    clear                   // Clear the output
                            // May not work in all environments

    hero                    // Prints the current player's stats

    enemy                   // Prints the current enemy's stats

    help                    // Lists a list of commands

    myunits                 // List of player's units

    enemyunits              // List of enemy's units

    unit <id or name>       // diplay basic unit stats

    exit                    // Terminates the game

## Staring commands

    inc <stat> <amount>     // Increase a hero stat by amount

    buy <id> <amount>       // Buys the given unit according to amount

    buyspell <spellName>    // Buys the appropriate spell

    place <id> <whe> <re>   // Places the unit based on id

    units                   // Prints the units available

    refund                  // Empties current hero's units

    turn                    // Next player / versus only

    start                   // Starts the game

## Game commands

    move <row> <coloumn>    // Moves the unit to the given point

    wait                    // Skip unit

    attack <who>            // Attacks a unit

    special <target>        // Do special skill

    fireball <whe> <re>     // Cast fireball 3*3

    resurrect <target>      // Resurrects target units

    stun <target>           // The target will be the last one to attack

    fullheal <target>       // Heal the target to full hp (1 unit)

    thunderbolt <target>    // Cast thunderbolt

# A few remarks

> the command parsing supports upper, lower, or unique cases such as "bUy G 10".
> Spells work on allies regardles of effect.
>
> The **`inc`** command needs the full name of the hero stat.
>
> Where the word **`id`** is used the units **`id`** must be used (example: buy g 10).
>
> Where the word **`target`** is used it means it needs the name of the unit, on the board (example: special eg (enemy Griffin)).
>
> It is recommended to increase stats before buying units
> the stats from the hero only applies when the unit is bought,
> consider uning refund if you messed up the order.
>
> All commands parse and execute validly ALL TIME,
> this means that you CAN use a starting command,
> and the parsing WILL NOT FAIL if the usage ampunt of arguments are correct.
