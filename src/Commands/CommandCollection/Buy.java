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
            int price = getCurrentHero().incStat(stat, amount);
            getCurrentHero().setStat("gold", getCurrentHero().getStat("gold") - price);
            println("Current gold: " + getCurrentHero().getStat("gold"));
        } catch (HeroException e) {
            temp = new CommandException(e.getMessage());
            msg = e.getMessage();
        } finally {
            if (msg == "noerror") {
                println(msg);
                return;
            }

            String unitID = (String) args.get(1);

            if (!unitIDs.contains(unitID)) {
                ok = false;
                temp = new CommandException("No unit or stat called: " + unitID + " exists!");
            }

            if (!ok) {
                throw temp;
            }

            for (Unit unit : getCurrentUnitList()) {
                if (unit.getId().equals(unitID)) {
                    try {
                        getCurrentUnitList().add(getCurrentHero().buyUnits(unitID, unit.getSize() + amount));
                        getCurrentUnitList().remove(unit);
                        println("Current gold: " + getCurrentHero().getStat("gold"));
                    } catch (HeroException e) {
                        throw new CommandException(e.getMessage());
                    }
                }
            }
            try {
                getCurrentUnitList().add(getCurrentHero().buyUnits(unitID, amount));
            } catch (HeroException e) {
                throw new CommandException(e.getMessage());
            }
            println("Current gold: " + getCurrentHero().getStat("gold"));
        }
    }

}
