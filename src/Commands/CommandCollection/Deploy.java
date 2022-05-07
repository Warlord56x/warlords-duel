package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import Core.Battlefield;
import GameExceptions.CommandException;
import Units.Unit;

public final class Deploy extends Command {

    public Deploy() throws CommandException {
        super(3, "Tactical", new Type[] { String.class, Integer.TYPE, Integer.TYPE });
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Battlefield map = getMap();
        Unit unit = null;
        String unitID = (String) args.get(1);
        for (Unit unitsIt : getHeroUnits()) {
            if (unitsIt.getId().equalsIgnoreCase(unitID)) {
                unit = unitsIt;
            }
        }
        if (unit == null) {
            throw new CommandException("unit does not exists among the current hero's units!");
        }
        int posx = (int) args.get(2);
        int posy = (int) args.get(3);
        try {
            map.placeUnit(unit, posx, posy, "player");
        } catch (Exception e) {
            println(e.getMessage());
        }

    }

}
