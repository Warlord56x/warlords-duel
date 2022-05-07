package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.Battlefield;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public class Start extends Command {

    public Start() throws CommandException {
        super(0, State.TACTICAL);

    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Battlefield map = getMap();
        boolean cantStart = false;
        if (getHeroUnits() != null) {
            if (getHeroUnits().isEmpty()) {
                cantStart = true;
            }
        } else {
            cantStart = true;
        }
        if (map.getPlacedUnits().isEmpty()) {
            throw new CommandException("There are no units placed by any hero!");
        }
        boolean enemy = false;
        boolean player = false;
        for (Unit unit : getMap().getPlacedUnits()) {
            if (unit.getOwner() == getEnemyHero()) {
                enemy = true;
            }
            if (unit.getOwner() == getHero()) {
                player = true;
            }
        }
        if (!player) {
            throw new CommandException("There are no units placed by the player!");
        }
        if (!enemy) {
            throw new CommandException("There are no units placed by the enemy!");
        }
        if (cantStart) {
            throw new CommandException("Can't start the game with no units!");
        }
        println("Game is starting!");
        println();
        for (Unit units2 : map.getPlacedUnits()) {
            println(units2.getName() + " - " + units2.getOwner().getName());
        }
        if (map.getPlacedUnits().get(0).getOwner() == getHero()) {
            println("Player moves first with: " + map.getPlacedUnits().get(0).getName());
        }
        if (map.getPlacedUnits().get(0).getOwner() == getEnemyHero()) {
            println("Enemy moves first with: " + map.getPlacedUnits().get(0).getName());
        }
        setCurrentUnit(map.getPlacedUnits().get(0));
        map.displayMovableTiles(getCurrentUnit());
        setState(State.BATTLE);

    }

}
