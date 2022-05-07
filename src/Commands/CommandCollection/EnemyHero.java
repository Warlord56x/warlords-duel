package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;

public final class EnemyHero extends Command {

    public EnemyHero() throws CommandException {
        super(0, "Global");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) {
        println(getEnemyHero());
    }

}
