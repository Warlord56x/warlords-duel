package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public final class MyUnits extends Command {

    public MyUnits() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String str = "";
        Boolean e = false;
        if (getHeroUnitList() != null) {
            if (getHeroUnitList().isEmpty()) {
                e = true;
            }
        } else {
            e = true;
        }
        if (e) {
            throw new CommandException("Hero doesn't have any units!");
        }
        for (Unit unit : getHeroUnitList()) {
            str += "Name: " + unit.getName() + " size: " + unit.getSize() + "\n";
        }
        println(str);

    }

}
