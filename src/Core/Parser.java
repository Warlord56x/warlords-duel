package Core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class Parser {

    // private static final String unitIds = "fgi";
    private final Hashtable<String, Integer> commands = new Hashtable<>();

    public Parser() {
        commands.put("inc", 2);
        commands.put("buy", 2);
        commands.put("move", 2);
        commands.put("help", 0);
        commands.put("hero", 0);
        commands.put("exit", 0);
        commands.put("units", 0);
        commands.put("myunits", 0);
        commands.put("enemyunits", 0);
        commands.put("unit", 1);
        commands.put("place", 3);
        commands.put("clear", 0);
        commands.put("start", 0);
        commands.put("turn", 0);
        commands.put("refund", 0);
        commands.put("enemy", 0);
        commands.put("wait", 0);
        commands.put("attack", 1);
        commands.put("special", 1);
        commands.put("resurrect", 1);
        commands.put("thunderbolt", 1);
        commands.put("stun", 1);
        commands.put("fullheal", 1);
        commands.put("fireball", 2);
        commands.put("buyspell", 1);
        commands.put("heroAttack", 1);
    }

    public String parse(Scanner line) throws Exception {
        String parsing = line.nextLine().toLowerCase();

        for (String command : commands.keySet()) {
            if (parsing.contains(command)) {
                int args = parsing.split(" ").length - 1;
                if (args != commands.get(command)) {
                    throw new Exception(
                            "Argument error: wrong amount of parameters! Expected: " + commands.get(command) + " got: "
                                    + String.valueOf(args));
                }
                return parsing.toLowerCase();
            }
        }

        throw new Exception("Parse error: \"" + parsing + "\" is not a command!");
    }

    public String commandsTable() {
        StringBuilder str = new StringBuilder();
        for (String element : new TreeMap<>(this.commands).keySet()) {
            str.append(element).append("\t --number of parameters required: ").append(this.commands.get(element)).append("\n");
        }
        return str.toString();
    }

    public Hashtable<String, Integer> getCommands() {
        return commands;
    }

}
