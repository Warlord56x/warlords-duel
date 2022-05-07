package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public final class Wait extends Command {

    public Wait() throws CommandException {
        super(0, State.BATTLE);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        return;
    }

}
