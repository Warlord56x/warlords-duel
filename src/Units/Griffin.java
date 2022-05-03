package Units;

import Core.Hero;

public class Griffin extends Unit {

    public Griffin(Hero h, int size) {
        super("Griffin", "G", h, size, 15, 5, 10, 30, 7, 15);
    }

    @Override
    protected void damageBack(Unit unit) {
        if (unit == null) {
            return;
        }
        backed = true;
        double damage = (Core.Core.rng.nextInt(getStat("maxAttack")) + getStat("minAttack")) * getSize();
        double dBonus = (getOwner().getStat("attack") * 10) / 100;
        damage = damage + dBonus;
        unit.takeDamage(null, (int) Math.round(damage));
    }

    @Override
    public void specialSkill(Unit arg) {
        Core.Core.println("Passiv special: Infinite back attack!");
    }

}
