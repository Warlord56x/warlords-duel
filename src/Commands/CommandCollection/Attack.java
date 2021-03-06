package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.*;
import GameExceptions.CommandException;
import Units.Unit;

public final class Attack extends Command {

    public Attack() throws CommandException {
        super(1, State.BATTLE);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Battlefield map = getMap();
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
        if (map.distanceTo(unit, getCurrentUnit()) == 1) {
            unit.takeDamage(getCurrentUnit(), getCurrentUnit().getAttack());
        } else {
            throw new CommandException("Cannot attack foe, it is too far away!");
        }

    }

}
