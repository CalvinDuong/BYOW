package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.util.Random;

public class Avatar {

    protected static int xLocation;

    protected static int yLocation;

    protected static Section sectionLocation;

    private static Random random;

    protected static int stepsLeft = 300;

    protected static boolean previousKeyQuit;

    public Avatar(TETile[][] world, Random seed) {
        random = seed;
        sectionLocation = Dungeon.finalCoord.get(random.nextInt(Dungeon.finalCoord.size()));
        while (!sectionLocation.room.exists()) {
            sectionLocation = Dungeon.finalCoord.get(random.nextInt(Dungeon.finalCoord.size() - 1));
        }
        int sectionMinX = Math.min(sectionLocation.room.getX(), sectionLocation.room.getX2());
        int sectionMaxX = Math.max(sectionLocation.room.getX(), sectionLocation.room.getX2());
        int sectionMinY = Math.min(sectionLocation.room.getY(), sectionLocation.room.getY2());
        int sectionMaxY = Math.max(sectionLocation.room.getY(), sectionLocation.room.getY2());

        this.xLocation = random.nextInt(sectionMinX + 1, sectionMaxX);
        this.yLocation = random.nextInt(sectionMinY + 1, sectionMaxY);
        world[xLocation][yLocation] = Tileset.AVATAR;
    }

    public void moveUp(TETile[][] world, Random seed) {
        if (xLocation == Flag.xLocation && yLocation + 1 == Flag.yLocation) {
            RunGame.gameover = true;
            return;
        }
        if (world[xLocation][yLocation + 1].description.equals("WATER")) {
            RunGame.extraStepsCounter++;
            Encounter.encounterRooms(seed, xLocation, yLocation, this);
            world[xLocation][yLocation + 1] = Tileset.FLOOR;
        }
        if (world[xLocation][yLocation + 1].description.equals("COIN")) {
            world[xLocation][yLocation + 1] = Tileset.FLOOR;
            Encounter.coinScore++;
        }
        if (world[xLocation][yLocation + 1].description.equals("WALKWAY")) {
            world[xLocation][yLocation] = Tileset.FLOOR;
            world[xLocation][yLocation + 1] = Tileset.AVATAR;
            Avatar.yLocation = yLocation + 1;
        }
        stepsLeft--;
    }

    public void moveDown(TETile[][] world, Random seed) {
        if (xLocation == Flag.xLocation && yLocation - 1 == Flag.yLocation) {
            RunGame.gameover = true;
            return;
        }
        if (world[xLocation][yLocation - 1].description.equals("WATER")) {
            RunGame.extraStepsCounter++;
            Encounter.encounterRooms(seed, xLocation, yLocation, this);
            world[xLocation][yLocation - 1] = Tileset.FLOOR;
        }
        if (world[xLocation][yLocation - 1].description.equals("COIN")) {
            world[xLocation][yLocation - 1] = Tileset.FLOOR;
            Encounter.coinScore++;
        }
        if (world[xLocation][yLocation - 1].description.equals("WALKWAY")) {
            world[xLocation][yLocation] = Tileset.FLOOR;
            world[xLocation][yLocation - 1] = Tileset.AVATAR;
            Avatar.yLocation = yLocation - 1;
        }
        stepsLeft--;
    }

    public void moveLeft(TETile[][] world, Random seed) {
        if (xLocation - 1 == Flag.xLocation && yLocation == Flag.yLocation) {
            RunGame.gameover = true;
            return;
        }
        if (world[xLocation - 1][yLocation].description.equals("WATER")) {
            RunGame.extraStepsCounter++;
            Encounter.encounterRooms(seed, xLocation, yLocation, this);
            world[xLocation - 1][yLocation] = Tileset.FLOOR;
        }
        if (world[xLocation - 1][yLocation].description.equals("COIN")) {
            world[xLocation - 1][yLocation] = Tileset.FLOOR;
            Encounter.coinScore++;
        }
        if (world[xLocation - 1][yLocation].description.equals("WALKWAY")) {
            world[xLocation][yLocation] = Tileset.FLOOR;
            world[xLocation - 1][yLocation] = Tileset.AVATAR;
            Avatar.xLocation = xLocation - 1;
        }
        stepsLeft--;

    }

    public void moveRight(TETile[][] world, Random seed) {
        if (xLocation + 1 == Flag.xLocation && yLocation == Flag.yLocation) {
            RunGame.gameover = true;
            return;
        }
        if (world[xLocation + 1][yLocation].description.equals("WATER")) {
            RunGame.extraStepsCounter++;
            Encounter.encounterRooms(seed, xLocation, yLocation, this);
            world[xLocation + 1][yLocation] = Tileset.FLOOR;
        }
        if (world[xLocation + 1][yLocation].description.equals("COIN")) {
            world[xLocation + 1][yLocation] = Tileset.FLOOR;
            Encounter.coinScore++;
        }
        if (world[xLocation + 1][yLocation].description.equals("WALKWAY")) {
            world[xLocation][yLocation] = Tileset.FLOOR;
            world[xLocation + 1][yLocation] = Tileset.AVATAR;
            Avatar.xLocation = xLocation + 1;
        }
        stepsLeft--;
    }

    public void movement(TETile[][] world, Random seed) {
        File save = Utils.join(RunGame.CWD, "save1.txt");
        if (StdDraw.hasNextKeyTyped()) {
            char character = StdDraw.nextKeyTyped();
            if (character == 'q' || character == 'Q') {
                if (previousKeyQuit) {
                    System.exit(0);
                }
            }
            if (character == ':') {
                previousKeyQuit = true;
            } else {
                previousKeyQuit = false;
            }
            if (character == 'w' || character == 'W') {
                String text = Utils.readContentsAsString(save) + "w";
                Utils.writeContents(save, text);
                moveUp(world, seed);
            }
            if (character == 'a' || character == 'A') {
                String text = Utils.readContentsAsString(save) + "a";
                Utils.writeContents(save, text);
                moveLeft(world, seed);
            }
            if (character == 's' || character == 'S') {
                String text = Utils.readContentsAsString(save) + "s";
                Utils.writeContents(save, text);
                moveDown(world, seed);
            }
            if (character == 'd' || character == 'D') {
                String text = Utils.readContentsAsString(save) + "d";
                Utils.writeContents(save, text);
                moveRight(world, seed);
            }
        }
    }

    public void movement(TETile[][] world, Random seed, char character) {
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'q' || key == 'Q') {
                if (previousKeyQuit) {
                    System.exit(0);
                }
            }
            if (character == ':') {
                previousKeyQuit = true;
            } else {
                previousKeyQuit = false;
            }
        }
        if (character == 'w' || character == 'W') {
            moveUp(world, seed);
        }
        if (character == 'a' || character == 'A') {
            moveLeft(world, seed);
        }
        if (character == 's' || character == 'S') {
            moveDown(world, seed);
        }
        if (character == 'd' || character == 'D') {
            moveRight(world, seed);
        }
    }
}
