package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;
import Units.Unit;

public final class Special extends Command {

    public Special() throws CommandException {
        super(1, "Game");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Unit unit = null;
        try {
            unit = getUnitById((String) args.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentUnit.getId().equals("a")) {
            currentUnit.specialSkill(unit);
        }

    }

}