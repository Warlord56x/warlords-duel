package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public final class Exit extends Command {

    public Exit() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    public void doCommand(ArrayList<Object> args) {
        System.out.println("exit...");
        System.exit(0);
    }

}
