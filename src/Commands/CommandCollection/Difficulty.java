package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import Core.Core;
import Core.State;
import GameExceptions.CommandException;

public class Difficulty extends Command {

    protected Difficulty() throws CommandException {
        super(1, State.TACTICAL, new Type[] { Integer.TYPE });
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Core.gameWipe();
        setDifficulty((int) args.get(1));
        Core.gameSetup();
    }

}
