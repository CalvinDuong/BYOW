package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Flag {

    protected static int xLocation;

    protected static int yLocation;

    protected static Section sectionLocation;

    private static Random random;

    public Flag(TETile[][] world, Random seed) {
        random = seed;
        sectionLocation = Dungeon.finalCoord.get(random.nextInt(Dungeon.finalCoord.size() - 1));
        while (!sectionLocation.room.exists()) {
            sectionLocation = Dungeon.finalCoord.get(random.nextInt(Dungeon.finalCoord.size() - 1));
        }
        int sectionMinX = Math.min(sectionLocation.room.getX(), sectionLocation.room.getX2());
        int sectionMaxX = Math.max(sectionLocation.room.getX(), sectionLocation.room.getX2());
        int sectionMinY = Math.min(sectionLocation.room.getY(), sectionLocation.room.getY2());
        int sectionMaxY = Math.max(sectionLocation.room.getY(), sectionLocation.room.getY2());

        xLocation = random.nextInt(sectionMinX + 1, sectionMaxX);
        yLocation = random.nextInt(sectionMinY + 1, sectionMaxY);

        if (xLocation == Avatar.xLocation) {
            xLocation++;
        }
        if (yLocation == Avatar.yLocation) {
            yLocation++;
        }
        world[xLocation][yLocation] = Tileset.SAND;

    }
}
