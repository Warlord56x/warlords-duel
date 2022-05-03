package Units;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import Core.*;
import Core.Hero;

public abstract class Unit implements Comparable<Unit> {
    private final Hashtable<String, Integer> stats = new Hashtable<>();
    private String name;
    private String id;
    private Hero owner;
    private int size;
    protected final int maxSize;
    protected final int maxHpAll;
    protected final int maxHp;
    public boolean backed = true;
    public int[] position = { -1, -1 };

    public Unit(String name, String id, Hero h, int size, int cost, int minAttack, int maxAttack, int health, int speed,
            int init) {
        this.setName(name);
        this.setId(id);
        this.setOwner(h);
        this.setSize(size);
        this.maxSize = size;
        this.maxHp = health;
        this.maxHpAll = health * size;

        this.stats.put("cost", cost);
        this.stats.put("minAttack", minAttack);
        this.stats.put("maxAttack", maxAttack);
        this.stats.put("crit", owner.getStat("luck") * 5);
        this.stats.put("health", health);
        this.stats.put("unitsHealth", health * size);
        this.stats.put("speed", speed);
        this.stats.put("init", init + owner.getStat("morale"));
    }

    public int getSize() {
        return size;
    }

    public void takeDamage(Unit unit, int damage) {
        double protection = (getOwner().getStat("defense") * 5) / 100;
        double total = damage * protection;
        total = damage - total;
        takePureDamage(unit, (int) Math.round(total));
    }

    public void takePureDamage(Unit unit, int damage) {
        if (unit == null) {
            damage /= 2;
        }
        setStat("health", getStat("health") - (getStat("unitsHealth") % damage));
        size = getStat("unitsHealth") / damage;
        setStat("unitsHealth", getStat("unitsHealth") - damage);
        if (getStat("unitsHealth") < 0) {
            Core.map.unitDie(this);
        }
        Core.println(getName() + " - " + getOwner().getName() + " damaged by: " + damage);
        damageBack(unit);
    }

    public void takePureDamage(int damage) {
        setStat("health", getStat("health") - (getStat("unitsHealth") % damage));
        size = getStat("unitsHealth") / damage;
        setStat("unitsHealth", getStat("unitsHealth") - damage);
        if (getStat("unitsHealth") < 0) {
            Core.map.unitDie(this);
        }
        Core.println(getName() + " - " + getOwner().getName() + " damaged by: " + damage);
        damageBack(null);
    }

    protected void damageBack(Unit unit) {
        if (backed) {
            return;
        }
        if (unit == null) {
            return;
        }
        backed = false;
        int damage = (Core.rng.nextInt(getStat("maxAttack")) + getStat("minAttack")) * getSize();
        unit.takeDamage(null, damage);
    }

    public int getAttack() {
        double damage = (Core.rng.nextInt(getStat("maxAttack")) + getStat("minAttack"))
                * getSize();
        double dBonus = (getOwner().getStat("attack") * 10) / 100;
        damage = damage + dBonus;
        return (int) Math.round(damage);
    }

    public void resurrect(int heal) {
        if (getStat("unitsHealth") + heal > maxHpAll) {
            setStat("unitsHealth", maxHpAll);
        }
        while (getStat("health") + heal > maxHp || maxSize == size) {
            heal -= maxHp;
            setStat("health", maxHp);
            setStat("unitsHealth", getStat("health") + heal);
            size++;
        }
        if (getStat("unitsHealth") != maxHpAll) {
            setStat("health", maxHpAll - getStat("unitsHealth"));
        }
    }

    public void fullheal() {
        setStat("health", maxHp);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public abstract void specialSkill(Unit arg);

    public Hero getOwner() {
        return owner;
    }

    public void setOwner(Hero owner) {
        this.owner = owner;
    }

    public String getId() {
        return id.toLowerCase();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hashtable<String, Integer> getStats() {
        return stats;
    }

    public void setStat(String stat, int value) {
        this.stats.put(stat, value);
    }

    public int getStat(String stat) {
        return this.stats.get(stat);
    }

    public Iterator<String> getKeysSorted() {
        return new TreeMap<String, Integer>(this.stats).keySet().iterator();
    }

    @Override
    public String toString() {

        String panel = "Name: " + name;

        Iterator<String> keys = getKeysSorted();
        while (keys.hasNext()) {
            String element = keys.next();
            if (element == "minAttack" || element == "maxAttack") {
                panel += element == "minAttack" ? "\nattack:" + stats.get("minAttack") + " - " + stats.get("maxAttack")
                        : "";
                continue;
            }
            panel += "\n" + element + ": " + stats.get(element);
        }

        return panel;
    }

    @Override
    public int compareTo(Unit o) {
        if (o.getStat("init") > this.getStat("init")) {
            return 1;
        }
        if (o.getStat("init") < this.getStat("init")) {
            return -1;
        }
        if (o.getStat("init") == this.getStat("init")) {
            if (o.getOwner() == Core.hero && this.getOwner() == Core.enemyHero) {
                return 1;
            }
            if (o.getOwner() == Core.enemyHero && this.getOwner() == Core.hero) {
                return -1;
            }
        }
        return 0;
    }

}
