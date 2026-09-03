package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public final class Units extends Command {

    public Units() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        StringBuilder str = new StringBuilder("\n");
        for (Unit unit : units) {
            str.append(unit.getName()).append("\n");
        }
        println(str.toString());
    }

}
