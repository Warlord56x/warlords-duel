package Units;

import Core.Hero;
import Core.Core;

public class Knight extends Unit {

    public Knight(Hero h, int size) {
        super("Knight", "K", h, size, 5, 2, 4, 10, 5, 6);
    }

    @Override
    public void specialSkill(Unit unit) {
        if (Core.map.distanceTo(unit, this) == 1) {
            unit.takePureDamage(getStat("maxAttack") * getSize());
        }
    }

}
