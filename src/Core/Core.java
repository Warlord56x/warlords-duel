package Core;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Commands.CommandCompiler;
import Units.*;

public class Core {

    // public static final String[] spells = { "resurrect", "fireball", "fullheal",
    // "stun", "thunderbolt" };
    public static Battlefield map = new Battlefield(12, 10);
    public static Hero enemyHero = new Hero("eHero", 1);
    public static ArrayList<Unit> enemyHeroUnits = new ArrayList<>();
    public static Hero hero = new Hero("Hero", 0);
    public static ArrayList<Unit> heroUnits = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    public static State state = State.TACTICAL;
    public static State turn = State.PlAYER1;
    public static Random rng = new Random();
    public static Unit currentUnit = null;
    public static boolean versus = false;
    public static int difficulty = 0;
    public static int turnCount = 0;

    // Shorthand Formatted sysout overrides
    public static void println() {
        System.out.println();
    }

    public static void println(Object str) {
        System.out.println(str);
    }

    public static void game() throws Exception {
        println("Copyright (C) 2022  Dániel Török (aka Warlord56x) version: v0.2.3");
        println("Init game...");
        gameSetup();
        sc = new Scanner(System.in);

        while (true) {
            println(map);
            println("Current turn: " + turnCount);
            println("Waiting for input...");
            try {
                CommandCompiler.execute(sc.nextLine());
                if (state == State.BATTLE) {
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

    public static void setupAI() {
        enemyHero = new Hero("enemy", 1);
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
            enemyHeroUnits.add(enemyHero.buyUnits("g", randomG));
            enemyHeroUnits.add(enemyHero.buyUnits("f", randomF));
            enemyHeroUnits.add(enemyHero.buyUnits("a", randomA));
            enemyHeroUnits.add(enemyHero.buyUnits("k", randomG));
            enemyHeroUnits.add(enemyHero.buyUnits("p", randomG));
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

    public static void gameWipe() {
        map.wipe();
        heroUnits.clear();
        enemyHeroUnits.clear();
    }

    public static void gameSetup() {
        if (!versus) {
            hero = new Hero("hero", difficulty);
            setupAI();
        } else {
            hero = new Hero("hero", difficulty);
            enemyHero = new Hero("enemy", difficulty);
        }
    }

    static void printOrder() {
        String str = "";
        for (Unit unit : map.getSortedUnits()) {
            str += unit.getName() + " - " + unit.getOwner().getName() + " ";
        }
        println(str);
    }

    public static void next() {
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
        if (versus && currentUnit.getOwner() != map.getSortedUnits().get(0).getOwner()) {
            if (turn == State.PlAYER1) {
                turn = State.PLAYER2;
            } else {
                turn = State.PlAYER1;
            }
        }
        currentUnit = map.getSortedUnits().get(0);
        map.clearDisplay();
        map.displayMovableTiles(currentUnit);
        printOrder();
    }
}