package Core;

import java.util.Hashtable;
import java.util.TreeMap;

import Commands.CommandCompiler;
import Commands.CommandCollection.Attack;
import Commands.CommandCollection.Move;
import Commands.CommandCollection.Wait;
import GameExceptions.CommandException;
import GameExceptions.HeroException;
import GameExceptions.ParserException;
import Units.*;

import java.util.Iterator;
import java.util.Random;

public class Hero {

    // Stats attack, defense, magic, wisdom, morale, luck, mana, gold
    private Hashtable<String, Integer> stats = new Hashtable<>();
    private double statCost = 4.8;
    private String name;
    public boolean turn = true;
    public State owner = State.PlAYER1;
    public String[] spells = { "", "", "", "", "" };

    public Hero(String name, int difficulty, int attack, int defense, int magic, int wisdom, int morale,
            int luck, int mana) {
        this.stats.put("attack", attack);
        this.stats.put("defense", defense);
        this.stats.put("magic", magic);
        this.stats.put("wisdom", wisdom);
        this.stats.put("morale", morale);
        this.stats.put("luck", luck);
        this.stats.put("mana", mana + (wisdom * 10));

        switch (difficulty) {
            case 0:
                this.stats.put("gold", 1300);
                break;
            case 1:
                this.stats.put("gold", 1000);
                break;
            default:
                this.stats.put("gold", 700);
                break;
        }

        this.name = name;
    }

    public void addSpell(String spell) throws Exception {
        for (int i = 0; i < spells.length; i++) {
            if (spells[i].equals("")) {
                spells[i] = spell;
                return;
            }
        }
        throw new Exception("Core error: Spells are full.");
    }

    public Hero(String name, int difficulty) {
        this.stats.put("attack", 1);
        this.stats.put("defense", 1);
        this.stats.put("magic", 1);
        this.stats.put("wisdom", 1);
        this.stats.put("morale", 1);
        this.stats.put("luck", 1);
        this.stats.put("mana", 1);

        switch (difficulty) {
            case 0:
                this.stats.put("gold", 1300);
                break;
            case 1:
                this.stats.put("gold", 1000);
                break;
            default:
                this.stats.put("gold", 700);
                break;
        }

        this.name = name;
    }

    public Hero() {
        this.name = "Hero";
        this.stats.put("attack", 1);
        this.stats.put("defense", 1);
        this.stats.put("magic", 1);
        this.stats.put("wisdom", 1);
        this.stats.put("morale", 1);
        this.stats.put("luck", 1);
        this.stats.put("mana", 1);
        this.stats.put("gold", 1300);
    }

    public void action() {
        if (owner == State.AI) {
            try {
                aiDo();
            } catch (Exception e) {
                Core.println(e.getMessage());
            }
        }
        if (owner == State.PlAYER1 || owner == State.PLAYER2) {
            try {
                CommandCompiler.execute(Core.sc.nextLine());
            } catch (CommandException | ParserException e) {
                Core.println(e.getMessage());
            }
        }
    }

    public void damage(Unit unit) {
        unit.takePureDamage(getStat("attack") * 10);
        turn = false;
    }

    public int incStat(String stat, int amount) throws HeroException {
        try {
            Object test = this.stats.get(stat);
            if (test == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new HeroException("Stat does not exists!");
        }
        double tempPrice = this.statCost;
        if (this.stats.get(stat) == 10) {
            throw new HeroException("Stat \"" + stat + "\" already reached the maximum value!");
        }
        if (stat.equals("gold")) {
            throw new HeroException("Stat \"" + stat + "\" cannot be increased!");
        }
        if (this.stats.get(stat) + amount > 10) {
            throw new HeroException("Stat cannot be increased beyond the maximum value!");
        }
        for (int i = 0; i < amount; i++) {
            double interest = (double) Math.ceil(tempPrice) * 0.1;
            tempPrice += interest;
        }
        tempPrice = (double) Math.ceil(tempPrice);
        if ((int) tempPrice > this.getStat("gold")) {
            throw new HeroException(
                    "Price exceeds current budget! Price: " + tempPrice + " budget: " + this.getStat("gold"));
        }
        this.stats.put(stat, this.stats.get(stat) + amount);
        if (stat.equals("wisdom")) {

            setStat("mana", getStat("mana") + amount * 10);
        }
        this.statCost = tempPrice;
        return (int) tempPrice;
    }

    static void aiDo() throws Exception {
        Unit minUnit = aiGetClosestUnit();
        if (Core.map.distanceTo(minUnit, Core.currentUnit) == 1) {
            Attack a = new Attack();
            a.command("attack " + minUnit);
        } else {
            moveAI();
        }
        Wait w = new Wait();
        w.command("wait");
    }

    static void moveAI() throws Exception {
        int min = Integer.MAX_VALUE;
        Unit minUnit = aiGetClosestUnit();
        min = Integer.MAX_VALUE;
        int posx = 1;
        int posy = 1;
        String[][] mapDup = Core.map.getMap();
        Core.map.clearDisplay();
        Core.map.displayMovableTiles(Core.currentUnit);
        for (int i = 0; i < mapDup.length; i++) {
            for (int j = 0; j < mapDup[i].length; j++) {
                if (mapDup[i][j].equals(Core.map.moveTile)) {

                    int distance = Core.map.distanceTo(j, i, minUnit);
                    if (distance < min) {
                        min = distance;
                        posx = j;
                        posy = i;
                    }
                }
            }
        }
        Core.map.clearDisplay();
        Move m = new Move();
        m.command("move " + posx + " " + posy);
    }

    static Unit aiGetClosestUnit() {
        int min = Integer.MAX_VALUE;
        Unit minUnit = null;
        for (Unit unit : Core.map.getPlacedUnits()) {
            if (unit.getOwner() == Core.hero) {
                int distance = Core.map.distanceTo(unit, Core.currentUnit);
                if (distance < min) {
                    min = distance;
                    minUnit = unit;
                }
            }
        }
        return minUnit;
    }

    public void setupAI() {
        owner = State.AI;
        Random rng = Core.rng;
        int randomG = 1;
        int randomF = 1;
        int randomA = 1;
        switch (Core.difficulty) {
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
            Core.enemyHeroUnits.add(Core.enemyHero.buyUnits("g", randomG));
            Core.enemyHeroUnits.add(Core.enemyHero.buyUnits("f", randomF));
            Core.enemyHeroUnits.add(Core.enemyHero.buyUnits("a", randomA));
            Core.enemyHeroUnits.add(Core.enemyHero.buyUnits("k", randomG));
            Core.enemyHeroUnits.add(Core.enemyHero.buyUnits("p", randomG));
            Core.enemyHero.setStat("gold",
                    Core.enemyHero.getStat("gold") - Core.enemyHero.incStat("attack", rng.nextInt(2) + 1));
            Core.enemyHero.setStat("gold",
                    Core.enemyHero.getStat("gold") - Core.enemyHero.incStat("magic", rng.nextInt(2) + 1));
            Core.enemyHero.setStat("gold",
                    Core.enemyHero.getStat("gold") - Core.enemyHero.incStat("wisdom", rng.nextInt(2) + 1));
            for (Unit units : Core.enemyHeroUnits) {
                int posx = rng.nextInt(2) + 11;
                int posy = rng.nextInt(10) + 1;
                if (units.position[0] != -1 || units.position[1] != -1) {
                    continue;
                }
                for (Unit unit : Core.map.getPlacedUnits()) {
                    if (unit.getOwner() != Core.enemyHero) {
                        continue;
                    }
                    if (unit.position[0] == posx || unit.position[1] == posy) {
                        posx = rng.nextInt(2) + 11;
                        posy = rng.nextInt(10) + 1;
                    }
                }
                Core.map.placeUnit(units, posx, posy, "enemy");
            }

        } catch (Exception e) {
            for (Unit units : Core.enemyHeroUnits) {
                int posx = rng.nextInt(2) + 11;
                int posy = rng.nextInt(10) + 1;
                if (units.position[0] != -1 || units.position[1] != -1) {
                    continue;
                }
                for (Unit unit : Core.map.getPlacedUnits()) {
                    if (unit.getOwner() != Core.enemyHero) {
                        continue;
                    }
                    if (unit.position[0] == posx || unit.position[1] == posy) {
                        posx = rng.nextInt(2) + 11;
                        posy = rng.nextInt(10) + 1;
                    }
                }
                try {
                    Core.map.placeUnit(units, posx, posy, "enemy");
                } catch (Exception ex) {
                    Core.println("AI unexpected placement error! Enemy will be having 1 less units than intended!");
                }
            }
        }
    }

    public Unit buyUnits(String id, int size) throws HeroException {
        Unit units = null;
        int price = 0;
        switch (id) {
            case "g":
                units = new Griffin(this, size);
                break;
            case "f":
                units = new Farmer(this, size);
                break;
            case "a":
                units = new Archer(this, size);
                break;
            case "k":
                units = new Knight(this, size);
                break;
            case "p":
                units = new Paladin(this, size);
                break;
            default:
                throw new HeroException("Id does not exists!");
        }
        price = units.getStat("cost") * size;

        if (price > getStat("gold")) {
            throw new HeroException(
                    "Price exceeds current budget! Price: " + price + " budget: " + getStat("gold"));
        }
        if (price == getStat("gold")) {
            Core.println("Warning: Price will deplete the heroes budget! Proceeding anyways!");
        }
        setStat("gold", getStat("gold") - price);
        return units;
    }

    public Hashtable<String, Integer> getStatsDict() {
        return stats;
    }

    public void setStatsDict(Hashtable<String, Integer> stats) {
        this.stats = stats;
    }

    public Iterator<String> getKeys() {
        return this.stats.keySet().iterator();
    }

    public Iterator<String> getKeysSorted() {
        return new TreeMap<String, Integer>(this.stats).keySet().iterator();
    }

    public void setStat(String stat, int value) {
        this.stats.put(stat, value);
    }

    public int getStat(String stat) throws NullPointerException {
        return this.stats.get(stat);
    }

    @Override
    public String toString() {

        String panel = "Name: " + name;

        Iterator<String> keys = getKeysSorted();
        while (keys.hasNext()) {
            String element = keys.next();
            panel += "\n" + element + ": " + stats.get(element);
        }

        return panel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
