package Units;

import Core.Hero;

public class Farmer extends Unit {

    public Farmer(Hero h, int size) {
        super("Farmer", "F", h, size, 2, 1, 1, 3, 4, 8);
    }

    @Override
    public void specialSkill(Unit arg) {
        Core.Core.println("No skill!");
    }

}
