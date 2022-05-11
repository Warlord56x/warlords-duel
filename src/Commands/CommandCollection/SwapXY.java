package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public class SwapXY extends Command {

    public SwapXY() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        getMap().swapXY();
    }

}
