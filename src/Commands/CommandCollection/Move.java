package Commands.CommandCollection;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Commands.Command;
import Core.Battlefield;
import Core.State;
import GameExceptions.CommandException;

public class Move extends Command {

    public Move() throws CommandException {
        super(2, State.BATTLE, new Type[] { Integer.TYPE, Integer.TYPE });
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        Battlefield map = getMap();
        int posx = (int) args.get(2);
        int posy = (int) args.get(1);
        try {
            map.moveUnit(posx, posy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
