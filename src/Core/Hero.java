package Core;

import java.util.Hashtable;
import java.util.TreeMap;

import Units.Unit;

import java.util.Iterator;

public class Hero {

    // Stats attack, defense, magic, wisdom, morale, luck, mana, gold
    private Hashtable<String, Integer> stats = new Hashtable<>();
    private double statCost = 5.0;
    private String name;
    public boolean turn = true;
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

    public void damage(Unit unit) {
        unit.takePureDamage(getStat("attack") * 10);
        turn = false;
    }

    public int incStat(String stat, int amount) throws Exception {
        double tempPrice = this.statCost;
        if (this.stats.get(stat) == 10) {
            throw new Exception("Argument error: stat \"" + stat + "\" already reached the maximum value!");
        }
        if (stat.equals("gold")) {
            throw new Exception("Argument error: stat \"" + stat + "\" cannot be increased!");
        }
        if (this.stats.get(stat) + amount > 10) {
            throw new Exception("Core error: stat cannot be increased beyond the maximum value!");
        }
        for (int i = 0; i < amount; i++) {
            double interest = (double) Math.ceil(tempPrice) * 0.1;
            tempPrice += interest;
        }
        tempPrice = (double) Math.ceil(tempPrice);
        if ((int) tempPrice > this.getStat("gold")) {
            throw new Exception(
                    "Core error: price exceeds current budget! Price: " + tempPrice + " budget: "
                            + this.getStat("gold"));
        }
        this.stats.put(stat, this.stats.get(stat) + amount);
        if (stat.equals("wisdom")) {

            setStat("mana", getStat("mana") + amount * 10);
        }
        this.statCost = tempPrice;
        return (int) tempPrice;
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

    public int getStat(String stat) {
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
