package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;

public final class Exit extends Command {

    public Exit() throws CommandException {
        super(0, "Global");
    }

    @Override
    public void doCommand(ArrayList<Object> args) {
        System.out.println("exit...");
        System.exit(0);
    }

}
