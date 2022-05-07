package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;
import Units.Unit;

public final class Units extends Command {

    public Units() throws CommandException {
        super(0, "Global");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String str = "\n";
        for (Unit unit : units) {
            str += unit.getName() + "\n";
        }
        println(str);
    }

}
