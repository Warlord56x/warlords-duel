package Core;

import java.util.ArrayList;
import java.util.Collections;

import Units.*;

public class Battlefield {
    private String[][] map;
    private ArrayList<Unit> placedUnits = new ArrayList<>();
    private ArrayList<Unit> sortedUnits = new ArrayList<>();
    public final String emptyTile = "[  ]";
    public final String moveTile = "[||]";
    public final int[] wh;

    public Battlefield(int width, int height) {
        this.map = new String[height + 1][width + 1];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                this.map[i][j] = emptyTile;
                if (i == 0 || j == 0 || i == this.map.length - 1 || j == this.map[i].length - 1) {
                    if (i == 0) {
                        this.map[i][j] = String.valueOf(j) + (j < 10 ? "   " : "  ");
                    }
                    if (j == 0) {
                        this.map[i][j] = String.valueOf(i) + "\t";
                    }
                }
            }
        }
        wh = new int[2];
        wh[0] = width;
        wh[1] = height;
    }

    public int distanceTo(Unit unit1, Unit unit2) {
        // Using manhattan distance
        int x1 = unit1.position[0];
        int y1 = unit1.position[1];
        int x2 = unit2.position[0];
        int y2 = unit2.position[1];
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public int distanceTo(int x1, int y1, Unit unit2) {
        // Using manhattan distance
        int x2 = unit2.position[0];
        int y2 = unit2.position[1];
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public int distanceToCh(int x1, int y1, int x2, int y2) {
        // Using chebyshev distance for fireball
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public void fireball(int posx, int posy) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (distanceToCh(posx, posy, x, y) <= 1) {
                    for (Unit unit : placedUnits) {
                        if (unit.position[0] == x && unit.position[1] == y) {
                            unit.takePureDamage(Core.currentUnit.getOwner().getStat("magic") * 20);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String field = "";
        for (String[] col : map) {
            for (int i = 0; i < col.length; i++) {
                field += col[i];
            }
            if (col == map[map.length - 1]) {
                return field;
            }
            field += "\n";
        }
        return field;
    }

    public void placeUnit(Unit unit, int posx, int posy, String turn) throws Exception {

        if (turn.equals("player")) {
            if (posx > 2 || map[posy][posx] != emptyTile) {
                throw new Exception("Map error: invalid position!" + posx + " : " + posy);
            }
        }
        if (turn.equals("enemy")) {
            if (posx < 10 || map[posy][posx] != emptyTile) {
                throw new Exception("Map error: invalid position! " + posx + " : " + posy);
            }
        }

        map[posy][posx] = emptyTile.replace("  ", unit.getOwner().getName().toLowerCase().charAt(0) + unit.getId());
        unit.position[0] = posx;
        unit.position[1] = posy;
        placedUnits.add(unit);
        sortedUnits.add(unit);
        Collections.sort(placedUnits);
        Collections.sort(sortedUnits);
    }

    public Boolean isPlaced(Unit unit) {
        if (placedUnits.contains(unit)) {
            return true;
        }
        return false;
    }

    public void displayMovableTiles(Unit unit) {
        String unitStr = "[" + unit.getOwner().getName().toLowerCase().charAt(0) + unit.getId() + "]";
        String[][] b = new String[wh[1] + 1][wh[0] + 1];
        for (int m = 0; m < unit.getStat("speed"); m++) {

            for (int i = 0; i < wh[1] + 1; i++) {

                for (int j = 0; j < wh[0] + 1; j++) {

                    if (this.map[i][j].equals(unitStr) || this.map[i][j].equals(moveTile)) {

                        if (i + 1 < wh[1] + 1 && this.map[i + 1][j].equals(emptyTile)) {
                            b[i + 1][j] = moveTile;
                        }
                        if (i - 1 >= 1 && this.map[i - 1][j].equals(emptyTile)) {
                            b[i - 1][j] = moveTile;
                        }
                        if (j + 1 < wh[0] + 1 && this.map[i][j + 1].equals(emptyTile)) {
                            b[i][j + 1] = moveTile;
                        }
                        if (j - 1 >= 1 && this.map[i][j - 1].equals(emptyTile)) {
                            b[i][j - 1] = moveTile;
                        }
                    }
                }
            }
            for (int i = 0; i < wh[1] + 1; i++) {
                for (int j = 0; j < wh[0] + 1; j++) {
                    if (b[i][j] == moveTile) {
                        if (map[i][j].equals(emptyTile)) {
                            this.map[i][j] = moveTile;
                        }
                    }
                }
            }
        }
    }

    public void unitDie(Unit unit) {
        sortedUnits.remove(unit);
        placedUnits.remove(unit);
        map[unit.position[1]][unit.position[0]] = emptyTile;
        if (loseCheck()) {
            Core.println("Enemy wins!");
            System.exit(0);
        }
        if (winCheck()) {
            Core.println("Player wins!");
            System.exit(0);
        }
    }

    private boolean loseCheck() {
        boolean lose = true;
        for (Unit unit : placedUnits) {
            if (unit.getOwner() == Core.hero) {
                lose = false;
            }
        }
        return lose;
    }

    private boolean winCheck() {
        boolean win = true;
        for (Unit unit : placedUnits) {
            if (unit.getOwner() == Core.enemyHero) {
                win = false;
            }
        }
        return win;
    }

    public String[][] getMap() {
        return this.map;
    }

    public void clearDisplay() {
        for (int i = 0; i < wh[1] + 1; i++) {
            for (int j = 0; j < wh[0] + 1; j++) {
                if (map[i][j].equals(moveTile)) {
                    map[i][j] = emptyTile;
                }
            }
        }
    }

    public void moveUnit(int posx, int posy) throws Exception {
        Unit unit = Core.currentUnit;

        clearDisplay();
        displayMovableTiles(unit);
        if (map[posy][posx].equals(moveTile)) {
            map[unit.position[1]][unit.position[0]] = emptyTile;
            map[posy][posx] = emptyTile.replace("  ", unit.getOwner().getName().toLowerCase().charAt(0) + unit.getId());
            unit.position[0] = posx;
            unit.position[1] = posy;
            clearDisplay();
            return;
        }
        throw new Exception("Map error: invalid position!" + posx + " : " + posy);
    }

    public ArrayList<Unit> getPlacedUnits() {
        return placedUnits;
    }

    public ArrayList<Unit> getSortedUnits() {
        return sortedUnits;
    }
}
