package Commands;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import GameExceptions.ParserException;

public class CommandParser {
    private static List<Class<? extends Command>> commands = new ArrayList<>();
    private static final String commandRegex = "^\s*([a-z]|[A-Z])*(\s+.+)*$";
    private final ParserException notFound = new ParserException("This command does not exists!");
    private static final double initialThreshold = 0.4;

    public CommandParser(String packageName) {
        try {
            loadCommands(packageName);
        } catch (ClassNotFoundException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public List<String> getCommandsList() {
        ArrayList<String> commandList = new ArrayList<>();
        for (Class<?> command : commands) {
            commandList.add(command.getClass().getSimpleName());
        }
        return commandList;
    }

    /*
     * public static void main(String[] args) throws ParserException {
     * CommandParser instance = new CommandParser();
     * instance.parse("move 1 1");
     * }
     */

    public void loadCommands(String path) throws ClassNotFoundException {
        String packageName = path;
        java.net.URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

        // Filter .class files.
        File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        // Find classes implementing Command.
        for (File file : files) {
            String className = file.getName().replaceAll(".class$", "");
            Class<?> cls = Class.forName(packageName + "." + className);
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
        double matchThreshold = initialThreshold;
        int match = 0;

        for (int i = 0; i < letters.length; i++) {
            if (i < testLetters.length) {
                if (letters[i] == (testLetters[i])) {
                    match++;
                }
            }
        }
        if ((int) Math.round(testLetters.length * matchThreshold) >= (int) commandName.length() - match) {
            return true;
        }
        return false;
    }

}
