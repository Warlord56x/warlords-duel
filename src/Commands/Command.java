package Commands;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import GameExceptions.*;
import Units.*;
import Core.*;

public abstract class Command implements Type {

    protected final int params;
    protected final State commandState;
    protected final CommandParser parser = CommandCompiler.parser;
    protected final Type[] argTypes;
    public static final Class<Command> TYPE = Command.class;

    protected static Random rng = Core.rng;
    /*
     * protected static Battlefield map = Core.map;
     * protected static Unit currentUnit = Core.currentUnit;
     * protected static String state = Core.state;
     * protected static int turnCount = Core.turnCount;
     * protected static int difficulty = Core.difficulty;
     * protected static Hero enemyHero = Core.enemyHero;
     * protected static Hero hero = Core.hero;
     */

    private final Hero dummy = new Hero();
    protected final Unit units[] = { new Griffin(dummy, 1), new Archer(dummy, 1), new Farmer(dummy, 1) };

    protected Command(int params, State commadState, Type... argTypes) throws CommandException {
        this.params = params;
        this.commandState = commadState;
        this.argTypes = argTypes;
        if (params < argTypes.length) {
            throw new CommandException("not enough parameters, expected: " + argTypes.length + " got: " + params + "!");
        }
    }

    protected void println() {
        Core.println();
    }

    protected void println(Object s) {
        Core.println(s);
    }

    public void command(String line) throws ParserException {
        String[] lineS = line.split(" ");
        ArrayList<Object> args = new ArrayList<>();
        int i = 1;
        args.add(lineS[0]);
        for (Type type : argTypes) {
            switch (type.getTypeName()) {
                case "int":
                    args.add(Integer.parseInt(lineS[i]));
                    break;
                case "Double":
                    args.add(Double.parseDouble(lineS[i]));
                    break;
                case "boolean":
                    args.add(Boolean.parseBoolean(lineS[i]));
                    break;
                case "java.lang.String":
                    args.add(lineS[i]);
                    break;
                default:
                    throw new ParserException(
                            "Cannot convert argument " + i + " from  string to " + type.getTypeName());
            }
            i++;
        }

        if (commandState != getState()) {
            throw new ParserException("This command cannot be used in this state, state:" + getState().name()
                    + " expected: " + commandState);
        }

        try {
            doCommand(args);
            if (getState() == State.BATTLE) {
                Core.next();
            }
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }

    protected abstract void doCommand(ArrayList<Object> args) throws CommandException;

    protected static Unit getUnitById(String id) throws CommandException {
        for (Unit unit : getMap().getPlacedUnits()) {
            String unitId = unit.getOwner().getName().charAt(0) + unit.getId();
            if (id.equalsIgnoreCase(unitId)) {
                return unit;
            }
        }
        throw new CommandException("Core error: id does not match any unit!");
    }

    protected static List<String> getCommands() {
        return new ArrayList<>();
    }

    protected static void clearConsole() {
        System.out.print("\033\143");
    }

    @Override
    public String getTypeName() {
        return "Command";
    }

    protected static Battlefield getMap() {
        return Core.map;
    }

    protected static Unit getCurrentUnit() {
        return Core.currentUnit;
    }

    protected static void setCurrentUnit(Unit unit) {
        Core.currentUnit = unit;
    }

    protected static State getState() {
        return Core.state;
    }

    protected static void setState(State state) {
        Core.state = state;
    }

    protected static int getTurnCount() {
        return Core.turnCount;
    }

    protected static int getDifficulty() {
        return Core.difficulty;
    }

    protected static void setDifficulty(int difficulty) {
        Core.difficulty = difficulty;
    }

    protected static Hero getHero() {
        return Core.hero;
    }

    protected static void setHero(Hero hero) {
        Core.hero = hero;
    }

    protected static Hero getEnemyHero() {
        return Core.enemyHero;
    }

    protected static void setEnemyhero(Hero hero) {
        Core.enemyHero = hero;
    }

    protected static ArrayList<Unit> getHeroUnits() {
        return Core.heroUnits;
    }

    protected static ArrayList<Unit> getEnemHeroUnits() {
        return Core.enemyHeroUnits;
    }

    protected static void setVersus(boolean bool) {
        Core.versus = bool;
    }

    protected static boolean getVersus() {
        return Core.versus;
    }
}
