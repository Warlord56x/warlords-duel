package Commands;

import GameExceptions.ParserException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    private static final List<Class<? extends Command>> commands = new ArrayList<>();
    private static final String commandRegex = "^ *([a-z]|[A-Z])*( +.+)*$";
    private final ParserException notFound = new ParserException("This command does not exists!");
    private static final double initialThreshold = 0.4;

    public CommandParser(String packageName) {
        try {
            loadCommands(packageName);
        } catch (ClassNotFoundException e) {
            System.out.println();
            e.printStackTrace();
        }
    }

    public List<String> getCommandsList() {
        ArrayList<String> commandList = new ArrayList<>();
        for (Class<?> command : commands) {
            commandList.add(command.getSimpleName());
        }
        return commandList;
    }

    /*
     * public static void main(String[] args) throws ParserException {
     * CommandParser instance = new CommandParser();
     * instance.parse("move 1 1");
     * }
     */

    public void loadCommands(@NotNull String path) throws ClassNotFoundException {
        java.net.URL root = Thread.currentThread().getContextClassLoader().getResource(path.replace(".", "/"));

        // Filter .class files.
        assert root != null;
        File[] files = new File(root.getFile()).listFiles((dir, name) -> name.endsWith(".class"));

        // Find classes implementing Command.
        assert files != null;
        for (File file : files) {
            String className = file.getName().replaceAll(".class$", "");
            Class<?> cls = Class.forName(path + "." + className);
            if (Command.class.isAssignableFrom(cls)) {
                commands.add(cls.asSubclass(Command.class));
            }
        }
    }

    public Class<? extends Command> parse(String line) throws ParserException {

        if (!line.matches(commandRegex)) {
            throw notFound;
        }
        line = line.toLowerCase();
        String[] args = line.split(" ");
        String name = args[0];

        for (Class<? extends Command> command : commands) {
            String commandName = command.getSimpleName().toLowerCase();
            if (commandName.equals(name)) {
                return command;
            }
            if (isAbbreviation(commandName, name)) {
                throw new ParserException(
                        "Command: \"" + name + "\" does not exists! Did you mean: \"" + commandName + "\"?");
            }
        }
        throw notFound;
    }

    public static boolean isAbbreviation(String commandName, String testName) {
        char[] testLetters = testName.toLowerCase().toCharArray();
        char[] letters = commandName.toLowerCase().toCharArray();
        int match = 0;

        for (int i = 0; i < letters.length; i++) {
            if (i < testLetters.length) {
                if (letters[i] == (testLetters[i])) {
                    match++;
                }
            }
        }
        return (int) Math.round(testLetters.length * initialThreshold) >= commandName.length() - match;
    }

}
