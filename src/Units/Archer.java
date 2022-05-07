package Units;

import Core.Hero;

public final class Archer extends Unit {

    public Archer(Hero h, int size) {
        super("Archer", "A", h, size, 6, 2, 4, 7, 4, 9);
    }

    @Override
    public void specialSkill(Unit unit) {
        int damage = (Core.Core.rng.nextInt(getStat("maxAttack")) + getStat("minAttack")) * getSize();
        unit.takeDamage(null, damage);
    }
}
