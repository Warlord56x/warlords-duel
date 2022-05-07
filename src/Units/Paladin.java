package Units;

import Core.Hero;

public final class Paladin extends Unit {

    public Paladin(Hero h, int size) {
        super("Paladin", "P", h, size, 20, 5, 20, 25, 3, 4);
    }

    @Override
    public void specialSkill(Unit unit) {
        // Heal
        if (this.getStat("health") + 5 >= 25) {
            this.setStat("health", this.getStat("health") + 5);
        } else {
            this.setStat("health", maxHp);
        }
    }

}
