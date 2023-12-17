package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class Encounter {

    private static Random random;

    private static int _BOARDWIDTH;

    private static int _BOARDHEIGHT;

    protected static int coinScore;


    public static void encounterGeneration(TETile[][] world, Random seed) {
        random = seed;
        int numEncounterRooms = Math.max(random.nextInt(Dungeon.finalCoord.size()), 7);
        for (int index = 0; index < numEncounterRooms; index++) {
            int randomRoom = random.nextInt(Dungeon.finalCoord.size() - 1);
            Section sectionLocation = Dungeon.finalCoord.get(randomRoom);
            while (!sectionLocation.room.exists()) {
                randomRoom = random.nextInt(Dungeon.finalCoord.size() - 1);
                sectionLocation = Dungeon.finalCoord.get(randomRoom);
            }
            int randRoomMinX = Math.min(sectionLocation.room.getX(), sectionLocation.room.getX2());
            int randRoomMaxX = Math.max(sectionLocation.room.getX(), sectionLocation.room.getX2());
            int randRoomMinY = Math.min(sectionLocation.room.getY(), sectionLocation.room.getY2());
            int randRoomMaxY = Math.max(sectionLocation.room.getY(), sectionLocation.room.getY2());
            int encounterRoomX = random.nextInt(randRoomMinX + 1, randRoomMaxX);
            int encounterRoomY = random.nextInt(randRoomMinY + 1, randRoomMaxY);

            world[encounterRoomX][encounterRoomY] = Tileset.WATER;
        }
    }

    public static void encounterRooms(Random seed, int prevX, int prevY, Avatar oscar) {
        random = seed;
        _BOARDWIDTH = Dungeon.getBoardWidth();
        _BOARDHEIGHT = Dungeon.getBoardHeight();
        int numCoins = 10;
        int roomWidthStart = _BOARDWIDTH / 4;
        int roomWidthDistance = _BOARDWIDTH / 2;
        int roomWidthEnd = roomWidthStart + roomWidthDistance;
        int roomHeightStart = 0;
        int roomHeightDistance = _BOARDHEIGHT / 4;
        int roomHeightEnd = roomHeightStart + roomHeightDistance;
        
        RunGame.setFont(20);
        StdDraw.clear(Color.black);
        StdDraw.text(_BOARDWIDTH / 2, _BOARDHEIGHT - 15,
                "YOU FELL INTO A TRAP: DEFUSE ALL THE BOMBS");
        StdDraw.text(_BOARDWIDTH / 2, _BOARDHEIGHT - 25, "DEFUSE 3 BOMBS FOR 50 EXTRA STEPS");
        StdDraw.text(_BOARDWIDTH / 2, _BOARDHEIGHT - 35,
                "CURRENT BOMBS DEFUSED: " + RunGame.extraStepsCounter);
        StdDraw.show();
        StdDraw.pause(1000);

        TERenderer ter = new TERenderer();
        ter.initialize(_BOARDWIDTH, _BOARDHEIGHT);
        TETile[][] encounterRoom = new TETile[_BOARDWIDTH][_BOARDHEIGHT];
        
        for (int x = 0; x < _BOARDWIDTH; x += 1) {
            for (int y = 0; y < _BOARDHEIGHT; y += 1) {
                encounterRoom[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = 0; x < roomWidthDistance; x += 1) {
            for (int y = 0; y < roomHeightDistance; y += 1) {
                encounterRoom[roomWidthStart + x][roomHeightStart + y] = Tileset.FLOOR;
            }
        }

        for (int x = roomWidthStart; x < roomWidthStart + roomWidthDistance; x += 1) {
            encounterRoom[x][roomHeightStart] = Tileset.WALL;
        }
        for (int x = roomWidthStart; x < roomWidthStart + roomWidthDistance; x += 1) {
            encounterRoom[x][roomHeightEnd] = Tileset.WALL;
        }
        for (int y = 0; y < roomHeightDistance + 1; y += 1) {
            encounterRoom[roomWidthStart][y] = Tileset.WALL;
        }
        for (int y = 0; y < roomHeightDistance + 1; y += 1) {
            encounterRoom[roomWidthEnd][y] = Tileset.WALL;
        }

        for (int x = 0; x < numCoins; x++) {
            int randomWidth = random.nextInt(roomWidthStart + 2, roomWidthEnd);
            int randomHeight = random.nextInt(roomHeightStart + 2, roomHeightEnd);
            encounterRoom[randomWidth][randomHeight] = Tileset.MOUNTAIN;
        }

        int xLocation = random.nextInt(roomWidthStart + 1, roomWidthEnd);
        int yLocation = random.nextInt(roomHeightStart + 1, roomHeightEnd);

        encounterRoom[oscar.xLocation][oscar.yLocation] = Tileset.NOTHING;
        encounterRoom[xLocation][yLocation] = Tileset.AVATAR;
        oscar.xLocation = xLocation;
        oscar.yLocation = yLocation;

        while (coinScore != 10) {
            Dungeon.makeHUD(encounterRoom);
            if(StdDraw.isMousePressed()){
                System.out.println(Flag.xLocation);
                System.out.print(Flag.yLocation);
                System.out.println(StdDraw.mouseX() + StdDraw.mouseY());
                if(Math.round(StdDraw.mouseX()) == Math.round(Flag.xLocation)
                        && Math.round(StdDraw.mouseY()) == Math.round(Flag.yLocation)){
                    RunGame.setFont(20);
                    StdDraw.clear(Color.black);
                    StdDraw.text(_BOARDWIDTH / 2, _BOARDHEIGHT / 2, "YOU FOUND THE HIDDEN FLAG");
                    StdDraw.show();
                    StdDraw.pause(2000);
                    RunGame.gameover = true;
                    break;
                }
            }
            if (!Dungeon.loaded) {
                while (!Dungeon.moveQueue.isEmpty()) {
                    oscar.movement(encounterRoom, random, Dungeon.moveQueue.dequeue());
                }
                Dungeon.loaded = true;
                ter.renderFrame(encounterRoom);
            } else {
                oscar.movement(encounterRoom, seed);
            }
            ter.renderFrame(encounterRoom);
        }
        oscar.xLocation = prevX;
        oscar.yLocation = prevY;
        coinScore = 0;
    }
}



