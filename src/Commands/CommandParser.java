package Commands;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import GameExceptions.ParserException;

public class CommandParser {
    private static List<Class<Command>> commands = new ArrayList<>();
    private static final String commandRegex = "^\s*([a-z]|[A-Z])*(\s+.+)*$";

    public CommandParser() {
        try {
            loadCommands();
        } catch (ClassNotFoundException e) {
            System.out.println("");
            e.printStackTrace();
        }
    }

    public List<String> getCommandsList() {
        ArrayList<String> commandList = new ArrayList<>();
        for (Class<Command> command : commands) {
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

    public void loadCommands() throws ClassNotFoundException {
        String packageName = "Commands.CommandCollection";
        java.net.URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

        // Filter .class files.
        File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });

        // Find classes implementing ICommand.
        for (File file : files) {
            String className = file.getName().replaceAll(".class$", "");
            Class<?> cls = Class.forName(packageName + "." + className);
            if (Command.class.isAssignableFrom(cls)) {
                commands.add((Class<Command>) cls);
            }
        }
    }

    public Class<Command> parse(String line) throws ParserException {

        if (!line.matches(commandRegex)) {
            throw new ParserException("This command does not exists!");
        }
        line = line.toLowerCase();
        String[] args = line.split(" ");
        String name = args[0];

        for (Class<Command> command : commands) {
            if (command.getSimpleName().toLowerCase().equals(name)) {
                return command;
            }
        }
        return null;
    }

}
