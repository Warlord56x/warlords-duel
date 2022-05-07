# Usage

The starting point is the main method in the Main class,
that is inside the bin folder with already compiled files.

    cd bin
    java Main

To compile (It will compile them next to the .java files)

    javac src/Core/*.java src/Commands/*.java src/Commands/CommandCollection/*.java src/Units/*.java src/GameExeptions/*.java

Run self compiled

    cd src
    java Main

VS code usage is possible if the necessary extensions are installed.
Simply open the folder in VS code and use the "run" button.

# Commands

## Global commands

    clear                   // Clears the output
                            // May not work in all environments

    hero                    // Prints the current player's stats

    enemyinfo               // Prints the current enemy's stats

    help                    // Lists a list of commands

    myunits                 // Lists the player's units

    enemyunits              // Lists the enemy's units

    unit <id or name>       // diplays basic unit stats

    exit                    // Terminates the game

## Tactical phase commands

    inc <stat> <amount>     // Increases a hero stat by amount

    buy <id> <amount>       // Buys the given unit according to amount

    buyspell <spellName>    // Buys the appropriate spell

    deploy <id> <whe> <re>  // Deploys the unit based on id to the given point

    units                   // Prints the units available

    refund                  // Empties current hero's units

    turn                    // Next player / versus only

    start                   // Starts the game

## Battle phase commands

    move <whe> <re>         // Moves the unit to the given point

    wait                    // Skip unit

    attack <target>         // Attacks a unit

    heroattack <target>     // Attacks a unit with the hero

    special <target>        // Do special skill

    fireball <whe> <re>     // Casts fireball 3*3

    resurrect <target>      // Resurrects target units

    stun <target>           // The target will be the last one to attack

    fullheal <target>       // Heals the target to full hp (1 unit)

    thunderbolt <target>    // Casts thunderbolt

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
> consider using refund if you messed up the order.
>
> All commands parse and execute validly ALL TIME,
> this means that you CAN use a tactical phase command in the battle phase,
> and the parsing WILL NOT FAIL if the usage amount of arguments are correct.
