package Commands.CommandCollection;

import java.util.ArrayList;

import GameExceptions.CommandException;
import Commands.Command;
import Core.Core;
import Core.State;

public class ResetAI extends Command {

    public ResetAI() throws CommandException {
        super(0, State.TACTICAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Core.gameWipe();
        Core.gameSetup();
    }

}
