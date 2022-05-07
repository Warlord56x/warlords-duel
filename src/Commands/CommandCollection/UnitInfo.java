package Commands.CommandCollection;

import java.util.ArrayList;

import Units.Unit;
import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public final class UnitInfo extends Command {

    public UnitInfo() throws CommandException {
        super(1, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String arg1 = (String) args.get(1);
        for (Unit unit : units) {
            if (unit.getName().equalsIgnoreCase(arg1) || unit.getId().equalsIgnoreCase(arg1)) {
                println(unit);
                return;
            }
        }
        throw new CommandException("\"" + arg1 + "\"" + " is not a unit ID or name!");
    }

}
