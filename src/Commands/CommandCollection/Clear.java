package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public final class Clear extends Command {

    public Clear() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) {
        clearConsole();
    }

}
