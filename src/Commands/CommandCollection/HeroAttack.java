package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import GameExceptions.CommandException;
import Units.Unit;

public final class HeroAttack extends Command {

    public HeroAttack() throws CommandException {
        super(1, "_");
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        if (!getCurrentUnit().getOwner().turn) {
            throw new CommandException("Hero can not attack twice a turn!");
        }
        String arg = (String) args.get(1);
        Unit unit = getUnitById(arg);
        getCurrentUnit().getOwner().damage(unit);
    }

}
