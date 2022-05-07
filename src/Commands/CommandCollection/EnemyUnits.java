package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;
import Units.Unit;

public class EnemyUnits extends Command {

    public EnemyUnits() throws CommandException {
        super(0, "Global");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        String str = "";
        Boolean e = false;
        if (getEnemHeroUnits() != null) {
            if (getEnemHeroUnits().isEmpty()) {
                e = true;
            }
        } else {
            e = true;
        }
        if (e) {
            throw new CommandException("Hero doesn't have any units!");
        }
        for (Unit unit : getEnemHeroUnits()) {
            str += "Name: " + unit.getName() + " size: " + unit.getSize() + "\n";
        }
        println(str);

    }

}
