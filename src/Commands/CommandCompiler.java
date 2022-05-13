package Commands;

import java.lang.reflect.InvocationTargetException;

import GameExceptions.CommandException;
import GameExceptions.ParserException;

public class CommandCompiler {
    static final CommandParser parser = new CommandParser("Commands.CommandCollection");

    public static void execute(String line) throws CommandException, ParserException {
        Class<? extends Command> command = parser.parse(line);
        try {
            if (command != null) {
                command.getMethod("command", String.class).invoke(command.getDeclaredConstructor().newInstance(), line);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | InstantiationException e) {
        }
    }

    /*
     * public static void main(String[] args) throws CommandException,
     * ParserException {
     * execute("move 1 1");
     * }
     */
}
