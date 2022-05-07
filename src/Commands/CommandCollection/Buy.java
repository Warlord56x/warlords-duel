package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import Core.Core;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public final class Buy extends Command {

    public Buy() throws CommandException {
        super(2, State.TACTICAL, new Type[] { String.class, Integer.TYPE });
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String unitID = (String) args.get(1);
        int unitSize = (int) args.get(2);
        if (unitSize <= 0) {
            throw new CommandException("invalid unit size");
        }
        for (Unit unit : getHeroUnits()) {
            if (unit.getId().equals(unitID)) {
                throw new CommandException("unit already bought!");
            }
        }
        try {
            getHeroUnits().add(Core.buyUnits(unitID, unitSize, getHero()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        println("Current gold: " + getHero().getStat("gold"));

    }

}
