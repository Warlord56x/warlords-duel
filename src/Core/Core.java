package Core;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Units.*;

public class Core {

    // public static final String[] spells = { "resurrect", "fireball", "fullheal",
    // "stun", "thunderbolt" };
    public static Battlefield map = new Battlefield(12, 10);
    public static Hero enemyHero = new Hero("eHero", 1);
    public static ArrayList<Unit> enemyHeroUnits = new ArrayList<>();
    public static Hero hero = new Hero("Hero", 0);
    public static ArrayList<Unit> heroUnits = new ArrayList<>();
    public static final Scanner sc = new Scanner(System.in);
    public static State state = State.TACTICAL;
    public static State turn = State.PlAYER1;
    public static Random rng = new Random();
    public static Unit currentUnit = null;
    public static Hero currentHero = hero;
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
        println("Copyright (C) 2022  Dániel Török (aka Warlord56x) version: v0.2.5");
        println("Init game...");
        gameSetup();

        while (true) {
            printMapInfo();
            println("Waiting for input...");
            currentHero.action();
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
            enemyHero = new Hero("enemy", difficulty);
            enemyHero.setupAI();
        } else {
            hero = new Hero("hero", difficulty);
            enemyHero = new Hero("enemy", difficulty);
        }
    }

    static void printMapInfo() {
        println(map);
        println("Current turn: " + turnCount);
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
        currentHero = currentUnit.getOwner();
        map.clearDisplay();
        map.displayMovableTiles(currentUnit);
        printOrder();
    }
}