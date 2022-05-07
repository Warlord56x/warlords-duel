package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;
import Units.Unit;

public final class Attack extends Command {

    public Attack(int params, String commadState, Type[] argTypes) throws CommandException {
        super(params, commadState, argTypes);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String unitID = (String) args.get(1);
        Unit unit = null;
        try {
            unit = getUnitById(unitID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (unit == null) {
            throw new CommandException("Cannot attack unit, it does not exits!");
        }
        if (map.distanceTo(unit, currentUnit) == 1) {
            unit.takeDamage(currentUnit, currentUnit.getAttack());
        } else {
            throw new CommandException("Cannot attack foe, it is too far away!");
        }

    }

}
