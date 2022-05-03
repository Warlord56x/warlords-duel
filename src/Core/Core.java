package Core;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Units.*;

public class Core {

    public static Random rng = new Random();
    private static Parser parser = new Parser();
    private static Scanner sc = new Scanner(System.in);
    public static Hero hero = new Hero("Hero", 0);
    public static Hero enemyHero = new Hero("eHero", 1);
    public static Battlefield map = new Battlefield(12, 10);
    private static ArrayList<Unit> heroUnits = new ArrayList<>();
    private static ArrayList<Unit> enemyHeroUnits = new ArrayList<>();
    public static Unit currentUnit = null;
    private static boolean versus = false;
    private static int turnCount;
    private static String turn = "player";
    private static String state = "Start";
    private static int difficulty = 0;
    public static final String[] spells = { "resurrect", "fireball", "fullheal", "stun", "thunderbolt" };

    // Shorthand Formatted sysout overrides
    static void print() {
        System.out.print("");
    }

    static void print(Object str) {
        System.out.print(str);
    }

    public static void println() {
        System.out.println();
    }

    public static void println(Object str) {
        System.out.println(str);
    }

    static void lnprintln(Object str) {
        System.out.print("\n" + str + "\n");
    }

    public static void main(String[] args) throws Exception {
        println("Init game...");
        println("Difficulty (1/2/3): ");
        int diff = -1;
        String d = "";
        do {
            d = sc.nextLine();
            Pattern pattern = Pattern.compile("[0-9]");
            Matcher match = pattern.matcher(d);
            if (d.length() == 1) {
                if (!match.find()) {
                    continue;
                }
            } else {
                continue;
            }
            diff = Integer.parseInt(d);
            if (diff < 1 || diff > 3) {
                println("Difficulty \"" + diff + "\" does not exists!");
            }
        } while (diff < 1 || diff > 3);
        diff -= 1;
        difficulty = diff;

        println("Versus mode? y/n");
        String answer;
        do {
            answer = sc.nextLine();
        } while (!(answer.contains("n") || answer.contains("y")));
        if (answer.equals("y")) {
            versus = true;
            println("Use the command turn to switch to player2. Difficulty is shared.");
            enemyHero = new Hero("hero2", diff);
        } else {
            versus = false;
            enemyHero = new Hero("enemy", 1);
            setupAI();
        }

        hero = new Hero("hero", diff, 1, 1, 1, 1, 1, 1, 1);
        sc = new Scanner(System.in);

        while (true) {
            println(map);
            println("Current turn: " + turnCount);
            println("Waiting for input...");
            try {
                commandExecute(parser.parse(sc));
                if (state.equals("Game")) {

                    if (!versus) {
                        while (currentUnit.getOwner() == enemyHero) {
                            aiDo();
                        }
                    }
                }
            } catch (Exception e) {
                println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    static void commandExecute(String command) throws Exception {
        command = command.toLowerCase();
        Hero h = new Hero();
        Hero currentHero = hero;
        ArrayList<Unit> currentList = null;
        Unit units[] = { new Griffin(h, 1), new Archer(h, 1), new Farmer(h, 1) };

        if (turn.equals("enemy")) {
            currentList = enemyHeroUnits;
            currentHero = enemyHero;
        }
        if (turn.equals("player")) {
            currentList = heroUnits;
            currentHero = hero;
        }

        if (command.equals("exit")) {
            sc.close();
            System.exit(0);
        }
        if (command.startsWith("help")) {
            println(parser.commandsTable());
        }

        if (command.startsWith("clear")) {
            clearConsole();
        }
        if (command.startsWith("units")) {
            String str = "\n";
            for (Unit unit : units) {
                str += unit.getName() + "\n";
            }
            println(str);
        }
        if (command.startsWith("unit")) {
            String arg1 = command.split(" ")[1];
            for (Unit unit : units) {
                if (unit.getName().equalsIgnoreCase(arg1) || unit.getId().equalsIgnoreCase(arg1)) {
                    lnprintln(unit);
                    return;
                }
            }
            throw new Exception("Argument error: \"" + arg1 + "\"" + " is not a unit ID or name!");
        }

        if (command.startsWith("hero")) {
            lnprintln(currentHero);
        }
        if (command.equals("enemy")) {
            lnprintln(enemyHero);
        }

        if (command.startsWith("myunits")) {
            String str = "";
            Boolean e = false;
            if (currentList != null) {
                if (currentList.isEmpty()) {
                    e = true;
                }
            } else {
                e = true;
            }
            if (e) {
                throw new Exception("Size error: Current hero doesn't have any units!");
            }
            for (Unit unit : currentList) {
                str += "Name: " + unit.getName() + " size: " + unit.getSize() + "\n";
            }
            lnprintln(str);
        }
        if (command.startsWith("enemyunits")) {
            String str = "";
            Boolean e = false;
            if (enemyHeroUnits != null) {
                if (enemyHeroUnits.isEmpty()) {
                    e = true;
                }
            } else {
                e = true;
            }
            if (e) {
                throw new Exception("Size error: Enemy hero doesn't have any units!");
            }
            for (Unit unit : enemyHeroUnits) {
                str += "Name: " + unit.getName() + " size: " + unit.getSize() + "\n";
            }
            lnprintln(str);
        }

        if (state.equals("Game")) {

            if (command.startsWith("move")) {
                int posx = Integer.parseInt(command.split(" ")[1]);
                int posy = Integer.parseInt(command.split(" ")[2]);
                map.moveUnit(posx, posy);
                next();
            }

            if (command.startsWith("heroattack")) {
                if (!currentUnit.getOwner().turn) {
                    throw new Exception("Core error: hero can not attack twice a turn!");
                }
                String arg = command.split(" ")[1];
                Unit unit = getUnitById(arg);
                currentUnit.getOwner().damage(unit);
            }

            if (command.startsWith("stun")) {
                if (currentUnit.getOwner().getStat("mana") < 7) {
                    throw new Exception("Core error: Not enough mana!");
                }
                String arg = command.split(" ")[1];
                Unit unit = getUnitById(arg);
                map.getSortedUnits().remove(unit);
                map.getSortedUnits().add(unit);
                currentUnit.getOwner().setStat("mana", currentUnit.getOwner().getStat("mana") - 7);
            }

            if (command.startsWith("resurrect")) {
                if (currentUnit.getOwner().getStat("mana") < 6) {
                    throw new Exception("Core error: Not enough mana!");
                }
                String arg = command.split(" ")[1];
                Unit unit = getUnitById(arg);
                unit.resurrect(currentUnit.getOwner().getStat("magic") * 50);
                currentUnit.getOwner().setStat("mana", currentUnit.getOwner().getStat("mana") - 6);
            }

            if (command.startsWith("fullheal")) {
                if (currentUnit.getOwner().getStat("mana") < 3) {
                    throw new Exception("Core error: Not enough mana!");
                }
                String arg = command.split(" ")[1];
                Unit unit = getUnitById(arg);
                unit.fullheal();
                currentUnit.getOwner().setStat("mana", currentUnit.getOwner().getStat("mana") - 3);
            }
            if (command.startsWith("thunderbolt")) {
                if (currentUnit.getOwner().getStat("mana") < 5) {
                    throw new Exception("Core error: Not enough mana!");
                }
                String arg = command.split(" ")[1];
                Unit unit = getUnitById(arg);
                unit.takePureDamage(currentUnit.getOwner().getStat("magic") * 30);
                currentUnit.getOwner().setStat("mana", currentUnit.getOwner().getStat("mana") - 5);
            }
            if (command.startsWith("fireball")) {
                if (currentUnit.getOwner().getStat("mana") < 9) {
                    throw new Exception("Core error: Not enough mana!");
                }
                int posx = Integer.parseInt(command.split(" ")[1]);
                int posy = Integer.parseInt(command.split(" ")[2]);
                map.fireball(posx, posy);
                currentUnit.getOwner().setStat("mana", currentUnit.getOwner().getStat("mana") - 9);
            }

            if (command.equals("wait")) {
                next();
            }

            if (command.startsWith("attack")) {
                String unitID = command.split(" ")[1];
                Unit unit = getUnitById(unitID);
                if (unit == null) {
                    throw new Exception("Core error: cannot attack unit, it does not exits!");
                }
                if (map.distanceTo(unit, currentUnit) == 1) {
                    unit.takeDamage(currentUnit, currentUnit.getAttack());
                } else {
                    throw new Exception("Core error: cannot attack foe, it is too far away!");
                }
                next();
            }

            if (command.startsWith("special")) {
                Unit unit = getUnitById(command.split(" ")[1]);
                if (currentUnit.getId().equals("a")) {
                    currentUnit.specialSkill(unit);
                }
            }
        }

        if (state.equals("Start")) {

            if (command.equals("turn")) {
                if (!versus) {
                    throw new Exception("Versus error: versus mode is not enabled! Restart the game to enable!");
                }
                String tempturn = turn;
                if (currentHero == hero) {
                    currentHero = enemyHero;
                    turn = "enemy";
                } else if (currentHero == enemyHero) {
                    currentHero = hero;
                    turn = "player";
                }
                println("turn changed to:" + turn + " from: " + tempturn + "!");
            }

            if (command.startsWith("buyspell")) {
                String arg = command.split(" ")[1];
                for (String string : currentUnit.getOwner().spells) {
                    if (string.equals(arg)) {
                        throw new Exception("Core error: spell already bought!");
                    }
                }
                for (String string : spells) {
                    if (string.equals(arg)) {
                        currentUnit.getOwner().addSpell(arg);
                        return;
                    }
                    throw new Exception("Core error: spell does not exits!");
                }

            }

            if (command.startsWith("inc")) {
                String stat = command.split(" ")[1];
                int amount = Integer.parseInt(command.split(" ")[2]);
                try {
                    currentHero.setStat("gold", currentHero.getStat("gold") - currentHero.incStat(stat, amount));
                } catch (Exception e) {
                    throw e;
                }
                println("Current gold: " + currentHero.getStat("gold"));
            }

            if (command.startsWith("place")) {
                Unit unit = null;
                String unitID = command.split(" ")[1];
                for (Unit unitsIt : currentList) {
                    if (unitsIt.getId().equalsIgnoreCase(unitID)) {
                        unit = unitsIt;
                    }
                }
                if (unit == null) {
                    throw new Exception("Core error: unit does not exists among the current hero's units!");
                }
                int posx = Integer.parseInt(command.split(" ")[2]);
                int posy = Integer.parseInt(command.split(" ")[3]);
                try {
                    map.placeUnit(unit, posx, posy, turn);
                } catch (Exception e) {
                    throw e;
                }
            }

            if (command.equals("start")) {
                boolean cantStart = false;
                if (heroUnits != null) {
                    if (heroUnits.isEmpty()) {
                        cantStart = true;
                    }
                } else {
                    cantStart = true;
                }
                if (map.getPlacedUnits().isEmpty()) {
                    throw new Exception("Core error: There are no units placed by any hero!");
                }
                boolean enemy = false;
                boolean player = false;
                for (Unit unit : map.getPlacedUnits()) {
                    if (unit.getOwner() == enemyHero) {
                        enemy = true;
                    }
                    if (unit.getOwner() == hero) {
                        player = true;
                    }
                }
                if (!player) {
                    throw new Exception("Core error: There are no units placed by the player!");
                }
                if (!enemy) {
                    throw new Exception("Core error: There are no units placed by the enemy!");
                }
                if (cantStart) {
                    throw new Exception("Core error: Can't start the game with no units!");
                }
                println("Game is stating!");
                println();
                for (Unit units2 : map.getPlacedUnits()) {
                    println(units2.getName() + " - " + units2.getOwner().getName());
                }
                if (map.getPlacedUnits().get(0).getOwner() == hero) {
                    turn = "player";
                    lnprintln("Player moves first with: " + map.getPlacedUnits().get(0).getName());
                }
                if (map.getPlacedUnits().get(0).getOwner() == enemyHero) {
                    turn = "enemy";
                    lnprintln("Enemy moves first with: " + map.getPlacedUnits().get(0).getName());
                }
                currentUnit = map.getPlacedUnits().get(0);
                map.displayMovableTiles(currentUnit);
                state = "Game";
            }
            if (command.startsWith("buy")) {
                String unitID = command.split(" ")[1];
                int unitSize = Integer.parseInt(command.split(" ")[2]);
                if (unitSize <= 0) {
                    throw new Exception("Core error: invalid unit size");
                }
                for (Unit unit : currentList) {
                    if (unit.getId().equals(unitID)) {
                        throw new Exception("Core error: unit already bought!");
                    }
                }
                try {
                    currentList.add(buyUnits(unitID, unitSize, currentHero));

                } catch (Exception e) {
                    throw e;
                }
                println("Current gold: " + currentHero.getStat("gold"));
            }

            if (command.equals("refund")) {
                int refund = 0;
                for (Unit unit : currentList) {
                    refund += unit.getStat("cost") * unit.getSize();
                    println(unit.getStat("cost") + "" + "" + unit.getSize());
                }
                currentHero.setStat("gold", currentHero.getStat("gold") + refund);
                println("Current gold: " + currentHero.getStat("gold"));
            }
        }
    }

    static Unit getUnitById(String id) throws Exception {
        for (Unit unit : map.getPlacedUnits()) {
            String unitId = unit.getOwner().getName().charAt(0) + unit.getId();
            if (id.equalsIgnoreCase(unitId)) {
                return unit;
            }
        }
        throw new Exception("Core error: id does not match any unit!");
    }

    static void aiDo() throws Exception {
        Unit minUnit = aiGetClosestUnit();
        if (map.distanceTo(minUnit, currentUnit) == 1) {
            attackAI(minUnit);
        } else {
            moveAI();
        }
        waitAI();
    }

    static void attackAI(Unit unit) {
        unit.takeDamage(currentUnit, currentUnit.getAttack());
    }

    static void waitAI() {
        next();
    }

    static void moveAI() throws Exception {
        int min = Integer.MAX_VALUE;
        Unit minUnit = aiGetClosestUnit();
        min = Integer.MAX_VALUE;
        int posx = 1;
        int posy = 1;
        String[][] mapDup = map.getMap();
        map.clearDisplay();
        map.displayMovableTiles(currentUnit);
        for (int i = 0; i < mapDup.length; i++) {
            for (int j = 0; j < mapDup[i].length; j++) {
                if (mapDup[i][j].equals(map.moveTile)) {

                    int distance = map.distanceTo(j, i, minUnit);
                    if (distance < min) {
                        min = distance;
                        posx = j;
                        posy = i;
                    }
                }
            }
        }
        map.clearDisplay();
        try {
            map.moveUnit(posx, posy);
        } catch (Exception e) {
            println(e);
            map.clearDisplay();
            throw e;
        }
    }

    static Unit aiGetClosestUnit() {
        int min = Integer.MAX_VALUE;
        Unit minUnit = null;
        for (Unit unit : map.getPlacedUnits()) {
            if (unit.getOwner() == hero) {
                int distance = map.distanceTo(unit, currentUnit);
                if (distance < min) {
                    min = distance;
                    minUnit = unit;
                }
            }
        }
        return minUnit;
    }

    static void setupAI() {
        int randomG = 1;
        int randomF = 1;
        int randomA = 1;
        switch (difficulty) {
            case 0:
                randomG = rng.nextInt(1) + 1;
                randomF = rng.nextInt(25) + 1;
                randomA = rng.nextInt(5) + 1;
                break;
            case 1:
                randomG = rng.nextInt(2) + 1;
                randomF = rng.nextInt(20) + 1;
                randomA = rng.nextInt(6) + 1;
                break;
            default:
                randomG = rng.nextInt(3) + 1;
                randomF = rng.nextInt(30) + 1;
                randomA = rng.nextInt(10) + 1;
                break;
        }
        try {
            enemyHeroUnits.add(buyUnits("g", randomG, enemyHero));
            enemyHeroUnits.add(buyUnits("f", randomF, enemyHero));
            enemyHeroUnits.add(buyUnits("a", randomA, enemyHero));
            enemyHeroUnits.add(buyUnits("k", randomG, enemyHero));
            enemyHeroUnits.add(buyUnits("p", randomG, enemyHero));
            enemyHero.setStat("gold", enemyHero.getStat("gold") - enemyHero.incStat("attack", rng.nextInt(2) + 1));
            enemyHero.setStat("gold", enemyHero.getStat("gold") - enemyHero.incStat("magic", rng.nextInt(2) + 1));
            enemyHero.setStat("gold", enemyHero.getStat("gold") - enemyHero.incStat("wisdom", rng.nextInt(2) + 1));
            for (Unit units : enemyHeroUnits) {
                int posx = rng.nextInt(2) + 11;
                int posy = rng.nextInt(10) + 1;
                if (units.position[0] != -1 || units.position[1] != -1) {
                    continue;
                }
                for (Unit unit : map.getPlacedUnits()) {
                    if (unit.getOwner() != enemyHero) {
                        continue;
                    }
                    if (unit.position[0] == posx || unit.position[1] == posy) {
                        posx = rng.nextInt(2) + 11;
                        posy = rng.nextInt(10) + 1;
                    }
                }
                map.placeUnit(units, posx, posy, "enemy");
            }

        } catch (Exception e) {
            for (Unit units : enemyHeroUnits) {
                int posx = rng.nextInt(2) + 11;
                int posy = rng.nextInt(10) + 1;
                if (units.position[0] != -1 || units.position[1] != -1) {
                    continue;
                }
                for (Unit unit : map.getPlacedUnits()) {
                    if (unit.getOwner() != enemyHero) {
                        continue;
                    }
                    if (unit.position[0] == posx || unit.position[1] == posy) {
                        posx = rng.nextInt(2) + 11;
                        posy = rng.nextInt(10) + 1;
                    }
                }
                try {
                    map.placeUnit(units, posx, posy, "enemy");
                } catch (Exception ex) {
                    println("AI unexpected placement error! Enemy will be having 1 less units than intended!");
                }
            }
        }
    }

    static void printOrder() {
        String str = "";
        for (Unit unit : map.getSortedUnits()) {
            str += unit.getName() + " - " + unit.getOwner().getName() + " ";
        }
        println(str);
    }

    static void next() {
        map.getSortedUnits().remove(0);
        if (map.getSortedUnits().isEmpty()) {
            turnCount++;
            for (Unit unit : map.getPlacedUnits()) {
                map.getSortedUnits().add(unit);
            }
            currentUnit = map.getSortedUnits().get(0);
            currentUnit.getOwner().turn = true;
            currentUnit.backed = true;
        }
        currentUnit = map.getSortedUnits().get(0);
        map.clearDisplay();
        map.displayMovableTiles(currentUnit);
        printOrder();
    }

    static Unit buyUnits(String id, int size, Hero h) throws Exception {
        Unit units = null;
        int price = 0;
        boolean e = false;
        switch (id) {
            case "g":
                units = new Griffin(h, size);
                break;
            case "f":
                units = new Farmer(h, size);
                break;
            case "a":
                units = new Archer(h, size);
                break;
            case "k":
                units = new Knight(h, size);
                break;
            case "p":
                units = new Paladin(h, size);
                break;
            default:
                e = true;
                break;
        }
        price = units.getStat("cost") * size;
        if (e) {
            throw new Exception("Argument error: Id does not exists!");
        }

        if (price > h.getStat("gold")) {
            throw new Exception(
                    "Core error: price exceeds current budget! Price: " + price + " budget: " + h.getStat("gold"));
        }
        if (price == h.getStat("gold")) {
            println("Warning: Price will deplete the heroes budget! Proceeding anyways!");
        }
        h.setStat("gold", h.getStat("gold") - price);
        return units;
    }

    static void clearConsole() {
        System.out.print("\033\143");
    }
}
