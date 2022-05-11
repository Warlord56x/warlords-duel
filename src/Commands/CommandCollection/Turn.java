package Commands.CommandCollection;

import java.util.ArrayList;

import Commands.Command;
import Core.State;
import GameExceptions.CommandException;

public final class Turn extends Command {

    public Turn() throws CommandException {
        super(0, State.TACTICAL);
    }

    @Override
    protected void doCommand(ArrayList<Object> args) throws CommandException {
        if (!getVersus()) {
            throw new CommandException(
                    "Versus error: versus mode is not enabled! Use the \"versus\" command to enable!");
        }
        setTurn(getTurn() == State.PlAYER1 ? State.PLAYER2 : State.PlAYER1);

    }

}
