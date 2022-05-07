package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;

public final class Help extends Command {
    public Help() throws CommandException {
        super(0, "Global");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) {
        println(parser.getCommandsList());

    }
}
