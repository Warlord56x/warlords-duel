package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;
import Units.Unit;

public class EnemyUnits extends Command {

    public EnemyUnits() throws CommandException {
        super(0, State.GLOBAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String str = "";
        Boolean e = false;
        if (getEnemyHeroUnitList() != null) {
            if (getEnemyHeroUnitList().isEmpty()) {
                e = true;
            }
        } else {
            e = true;
        }
        if (e) {
            throw new CommandException("Hero doesn't have any units!");
        }
        for (Unit unit : getEnemyHeroUnitList()) {
            str += "Name: " + unit.getName() + " size: " + unit.getSize() + "\n";
        }
        println(str);

    }

}
