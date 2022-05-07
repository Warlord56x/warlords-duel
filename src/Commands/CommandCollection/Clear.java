package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;

public final class Clear extends Command {

    public Clear() throws CommandException {
        super(0, "Global");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) {
        clearConsole();
    }

}
