package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.Core;
import Core.State;
import GameExceptions.CommandException;

public class ToggleVersus extends Command {

    public ToggleVersus() throws CommandException {
        super(0, State.TACTICAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Core.gameWipe();
        setVersus(!getVersus());
        Core.gameSetup();
    }

}
