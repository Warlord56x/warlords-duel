package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;
import GameExceptions.HeroException;
import Units.Unit;

public final class Buy extends Command {

    public Buy() throws CommandException {
        super(2, State.TACTICAL, new Type[] { String.class, Integer.TYPE });
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {

        String msg = "noerror";
        CommandException temp = new CommandException(msg);
        boolean ok = true;

        int amount = (int) args.get(2);
        String stat = (String) args.get(1);
        try {
            getCurrentHero().setStat("gold", getCurrentHero().getStat("gold") - getCurrentHero().incStat(stat, amount));

        } catch (HeroException e) {
            temp = new CommandException(e.getMessage());
            msg = e.getMessage();
        }
        println("Current gold: " + getCurrentHero().getStat("gold"));

        String unitID = (String) args.get(1);

        if (!unitIDs.contains(unitID)) {
            ok = false;
        }

        if (ok || msg != "noerror") {
            throw temp;
        }

        for (Unit unit : getCurrentUnitList()) {
            if (unit.getId().equals(unitID)) {
                try {
                    getCurrentUnitList().add(getCurrentHero().buyUnits(unitID, unit.getSize() + amount));
                    getCurrentUnitList().remove(unit);
                    println("Current gold: " + getCurrentHero().getStat("gold"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            getCurrentUnitList().add(getCurrentHero().buyUnits(unitID, amount));
        } catch (Exception e) {
            e.printStackTrace();
        }
        println("Current gold: " + getCurrentHero().getStat("gold"));
    }

}
