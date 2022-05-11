package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public class Refund extends Command {

    protected Refund() throws CommandException {
        super(0, State.TACTICAL);
    }

    @Override
    public void doCommand(ArrayList<Object> args) throws CommandException {
        int refund = 0;
        for (Unit unit : getCurrentUnitList()) {
            refund += unit.getStat("cost") * unit.getSize();
            println(unit.getStat("cost") + "" + "" + unit.getSize());
        }
        getHero().setStat("gold", getHero().getStat("gold") + refund);
        println("Current gold: " + getHero().getStat("gold"));
    }

}
