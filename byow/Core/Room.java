package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Room {

    private int width, height, x, y, x2, y2;
    private final TETile[][] world;

    private static Random random;

    private boolean exists;

    public Room(TETile[][] map, Random seed, int xMin, int xMax, int yMin, int yMax) {
        this.random = seed;
        this.x = random.nextInt(xMin, xMax);
        this.y = random.nextInt(yMin, yMax);
        this.width = findWidth(random, xMin, xMax);
        this.height = findHeight(random, yMin, yMax);
        this.x2 = this.x + width;
        this.y2 = this.y + height;
        this.exists = true;
        this.world = map;
        if ((this.width != -1) && (this.height != -1)
                && this.y <= Dungeon.getBoardHeight() - 4
                && this.getY2() <= Dungeon.getBoardHeight() - 4) {
            drawRoom();
        } else {
            this.exists = false;
        }
    }


    private int findWidth(Random randomSeed, int xMin, int xMax) {
        int value = 0;
        int farther = Math.max(x - xMin, xMax - x);
        boolean negative = farther == x - xMin;
        if (farther <= 3) {
            value = -1;
        } else {
            value = randomSeed.nextInt(3, farther);
            if (negative) {
                value = -value;
            }
        }
        return value;
    }

    private int findHeight(Random randomSeed, int yMin, int yMax) {
        int value = 0;
        int farther = Math.max(y - yMin, yMax - y);
        boolean negative = farther == y - yMin;
        if (farther <= 3) {
            value = -1;
        } else {
            value = randomSeed.nextInt(3, farther);
            if (negative) {
                value = -value;
            }
        }
        return value;
    }

    private void drawRoom() {
        int xMin = Math.min(x, x2);
        int xMax = Math.max(x, x2);
        int yMin = Math.min(y, y2);
        int yMax = Math.max(y, y2);
        for (int xValue = xMin; xValue <= xMax; xValue++) {
            for (int yValue = yMin; yValue <= yMax; yValue++) {
                world[xValue][yValue] = Tileset.FLOOR;
            }
        }
        for (int xValue = xMin; xValue <= xMax; xValue++) {
            world[xValue][yMin] = Tileset.WALL;
            world[xValue][yMax] = Tileset.WALL;
        }
        for (int yValue = yMin; yValue <= yMax; yValue++) {
            world[xMin][yValue] = Tileset.WALL;
            world[xMax][yValue] = Tileset.WALL;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getX2() {
        return this.x2;
    }

    public int getY2() {
        return this.y2;
    }

    public int getWidth() {
        //return this.width;
        return this.getY2() - this.getY();
    }

    public int getHeight() {
        //return this.height;
        return this.getX2() - this.getX();
    }

    public boolean exists() {
        return this.exists;
    }

}
