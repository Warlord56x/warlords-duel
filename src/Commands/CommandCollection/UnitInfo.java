package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Units.Unit;
import Commands.Command;
import GameExceptions.CommandException;

public final class UnitInfo extends Command {

    public UnitInfo(int params, String commadState, Type[] argTypes) throws CommandException {
        super(params, commadState, argTypes);
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
