package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class Dungeon {

    protected static Random random;
    private static final int BOARDWIDTH = 50;
    private static final int BOARDHEIGHT = 50;
    private final int maxWidth;
    private final int maxHeight;

    private static int roomSize;
    protected static ArrayList<Section> finalCoord;

    protected static boolean clicked = false;

    protected static boolean loaded = false;

    protected static Queue<Character> moveQueue = new Queue<>();

    public Dungeon(long seed) {
        random = new Random(seed);
        maxWidth = random.nextInt(10, 15);
        maxHeight = random.nextInt(10, 15);
        this.finalCoord = new ArrayList<>();
        this.roomSize = 0;

    }

    public ArrayList<Section> mapSplit() {
        for (int i = 0; i < BOARDWIDTH; i += maxWidth) {
            for (int x = 0; x < BOARDHEIGHT; x += maxHeight) {
                if (x + maxHeight >= BOARDHEIGHT && i + maxWidth >= BOARDWIDTH) {
                    Section section = new Section(i, x, BOARDWIDTH - 1, BOARDHEIGHT - 1);
                    finalCoord.add(section);
                } else if (x + maxHeight >= BOARDHEIGHT && i + maxWidth <= BOARDWIDTH) {
                    finalCoord.add(new Section(i, x, i + maxWidth, BOARDHEIGHT - 1));
                } else if (x + maxHeight <= BOARDHEIGHT && i + maxWidth >= BOARDWIDTH) {
                    finalCoord.add(new Section(i, x, BOARDWIDTH - 1, x + maxHeight));
                } else {
                    finalCoord.add(new Section(i, x, i + maxWidth, x + maxHeight));
                }
            }
        }

        return finalCoord;
    }

    public void drawBorder(TETile[][] world) {
        for (Section room: finalCoord) {
            for (int i = room.getTopLeftX(); i < room.getBottomRightX(); i++) {
                world[room.getTopLeftY()][i] = Tileset.FLOWER;
                world[room.getBottomRightY()][i] = Tileset.FLOWER;
            }

            for (int i = room.getTopLeftY(); i < room.getBottomRightY(); i++) {
                world[i][room.getTopLeftX()] = Tileset.FLOWER;
                world[i][room.getBottomRightX()] = Tileset.FLOWER;
            }
        }
    }

    public void createRooms(TETile[][] world) {
        for (Section section : finalCoord) {
            section.room = new Room(world, random, section.getTopLeftY(),
                    section.getBottomRightY(), section.getTopLeftX(), section.getBottomRightX());
            this.roomSize += 1;
        }
    }

    public void makeHallwaysHorizontal(Dungeon dungeon, TETile [][] board) {
        for (int index = 0; index < dungeon.getSections().size() - 1; index++) {
            if (((index + 1) % Hallway.numWidthRooms(dungeon)) == 0) {
                continue;
            } else if (dungeon.getSections().get(index).room.exists()
                    && dungeon.getSections().get(index + 1).room.exists()) {
                createHallwayHorizontal(dungeon.getSections().get(index),
                        dungeon.getSections().get(index + 1), board);
            }
        }
    }

    public void createHallwayHorizontal(Section sect1,
                                        Section sect2, TETile [][] board) {
        boolean turnHallway = false;
        int hallwaySTart = Hallway.roomWidthMiddle(sect1);
        if (sect2.room.getY() == hallwaySTart || hallwaySTart == sect2.room.getY2()) {
            hallwaySTart += 1;
        }
        if (sect2.room.getY() < hallwaySTart && hallwaySTart < sect2.room.getY2()
                || sect2.room.getY2() < hallwaySTart && hallwaySTart < sect2.room.getY()) {
            drawHallwayHorizontal(board, Math.max(sect1.room.getX(), sect1.room.getX2()),
                    Math.min(sect2.room.getX(), sect2.room.getX2()) + 1,
                    Math.min(sect2.room.getY(), sect2.room.getY2()),
                    Math.max(sect2.room.getY(), sect2.room.getY2()), hallwaySTart, turnHallway);
        } else {
            turnHallway = true;
            drawHallwayHorizontal(board, Math.max(sect1.room.getX(), sect1.room.getX2()),
                    Math.min(sect2.room.getX(), sect2.room.getX2()) + 1,
                    Math.min(sect2.room.getY(), sect2.room.getY2()), Math.max(sect2.room.getY(),
                            sect2.room.getY2()), hallwaySTart, turnHallway);
        }
    }
    //start x is section1 max
    //endX is section2 min
    //startY us section2 min
    //lowerY is section2 max
    //endY is turnHallway

    public void drawHallwayHorizontal(TETile [][] board, int startX, int endX,
                                      int startY, int lowerY, int endY, boolean turnHallWay) {
        if (!turnHallWay) {
            for (int i = startX; i < endX; i++) {
                board[i][endY] = Tileset.FLOOR;
                board[i][endY + 1] = Tileset.WALL;
                board[i][endY - 1] = Tileset.WALL;
            }
        }
        if (turnHallWay) {
            for (int i = startX; i <= endX; i++) {
                board[i][endY] = Tileset.FLOOR;
                board[i][endY + 1] = Tileset.WALL;
                board[i][endY - 1] = Tileset.WALL;
            }
            if (endY < startY) { //checks to see if we should turn our hallway upwards
                for (int x = endY; x < startY + 1; x++) {
                    board[endX][x] = Tileset.FLOOR;
                    board[endX + 1][x] = Tileset.WALL;
                    board[endX - 1][x + 1] = Tileset.WALL;
                }
            } else { //checks to see if we should turn our hallways down
                for (int x = endY; x >= lowerY; x--) {
                    board[endX][x] = Tileset.FLOOR;
                    board[endX + 1][x] = Tileset.WALL;
                    board[endX - 1][x - 1] = Tileset.WALL;

                }
            }
        }
    }

    public void makeHallwaysVertical(Dungeon map, TETile [][] board) {
        for (int index = 0; index < map.getSections().size() - 1; index++) {
            if (index + Hallway.numWidthRooms(map) >= map.finalCoord.size()) {
                break;
            } else if (map.getSections().get(index).room.exists()
                    && map.getSections().get(index + Hallway.numWidthRooms(map)).room.exists()) {
                createHallwayVertical(map.getSections().get(index),
                        map.getSections().get(index + Hallway.numWidthRooms(map)), board);
            }
        }
    }

    public void createHallwayVertical(Section sect1, Section sect2, TETile [][] board) {
        boolean turnHallway = false;
        int hallwaySTart = Hallway.roomHeightMiddle(sect1);
        if (sect2.room.getX() == hallwaySTart || hallwaySTart == sect2.room.getX2()) {
            hallwaySTart += 1;
        }
        if (sect2.room.getX() < hallwaySTart && hallwaySTart < sect2.room.getX2()
                || sect2.room.getX2() <= hallwaySTart && hallwaySTart <= sect2.room.getX()) {
            drawHallwayVertical(board, Math.max(sect2.room.getX(), sect2.room.getX2()),
                    Math.min(sect2.room.getX(), sect2.room.getX2()) + 1,
                    Math.max(sect1.room.getY(), sect1.room.getY2()),
                    Math.min(sect2.room.getY(), sect2.room.getY2()), hallwaySTart, turnHallway);
        } else {
            turnHallway = true;
            drawHallwayVertical(board, Math.max(sect2.room.getX(), sect2.room.getX2()),
                    Math.min(sect2.room.getX(), sect2.room.getX2()) + 1,
                    Math.max(sect1.room.getY(),  sect1.room.getY2()),
                    Math.min(sect2.room.getY(), sect2.room.getY2()), hallwaySTart, turnHallway);
        }
    }

    //start x = greater x of section2
    //end x = min x of section2
    //start y = max y of section 1
    //lower y = min y of section 2
    //endY = middle of section 1 height
    public void drawHallwayVertical(TETile [][] board, int startX, int endX,
                                    int startY, int lowerY, int endY, boolean turnHallWay) {
        if (!turnHallWay) {
            for (int y = startY; y <= lowerY; y++) {
                board[endY][y] = Tileset.FLOOR;
                board[endY + 1][y] = Tileset.WALL;
                board[endY - 1][y] = Tileset.WALL;
            }
        }
        if (turnHallWay) {
            for (int y = startY; y <= lowerY + 1; y++) {
                board[endY][y] = Tileset.FLOOR;
                board[endY + 1][y + 1] = Tileset.WALL;
                board[endY - 1][y + 1] = Tileset.WALL;
            }
            if (startX < endY) { //checks to see if we should turn our hallway left
                for (int x = endY; x > startX - 1; x--) {
                    board[x][lowerY + 1] = Tileset.FLOOR;
                    board[x][lowerY + 2] = Tileset.WALL;
                    board[x - 1][lowerY] = Tileset.WALL;
                }
            } else { //checks to see if we should turn our hallways right
                for (int x = endY; x < endX; x++) {
                    board[x][lowerY + 1] = Tileset.FLOOR;
                    board[x][lowerY + 2] = Tileset.WALL;
                    board[x + 1][lowerY] = Tileset.WALL;
                }
            }
        }
    }

    public static void makeHUD(TETile[][] world) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        RunGame.fontSize = 10;
        StdDraw.setPenColor(Color.white);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (StdDraw.isKeyPressed(72)) {
            clicked = !clicked;
        }
        if (!clicked) {
            try {
                StdDraw.text(13, BOARDHEIGHT - 1.5,
                        "GAME HUD: " + "   Tile: " + world[mouseX][mouseY].description()
                                + "    TIME: " + dtf.format(now));
                StdDraw.text(40, BOARDHEIGHT - 1.5,
                        "Name: " + RunGame.avatarName
                                + "      Steps (Click H to Hide): " + Avatar.stepsLeft);
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
                System.out.println("Mouse out of bounds.");
            }
            StdDraw.show();
        }
        if (clicked) {
            try {
                StdDraw.text(13, BOARDHEIGHT - 1.5,
                        "GAME HUD: " + "   Tile: " + world[mouseX][mouseY].description()
                                + "    TIME: " + dtf.format(now));
                StdDraw.text(40, BOARDHEIGHT - 1.5,
                        "Name: " + RunGame.avatarName + "      Steps (Click H to Show): ");
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
                System.out.println("Mouse out of bounds.");
            }
            StdDraw.show();
        }
    }

    public static void main(String [] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(BOARDWIDTH, BOARDHEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[BOARDWIDTH][BOARDHEIGHT];
        for (int x = 0; x < BOARDWIDTH; x += 1) {
            for (int y = 0; y < BOARDHEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        File save = Utils.join(RunGame.CWD, "save1.txt");
        Utils.writeContents(save, args[0]);
        char[] copy = args[0].toCharArray();
        StringBuilder seedString = new StringBuilder();
        StringBuilder movementString = new StringBuilder();
        for (char c : copy) {
            if (Character.isDigit(c)) {
                seedString.append(c);
            } else {
                movementString.append(c);
            }
        }
        long seed = Long.parseLong(seedString.toString());
        Dungeon calvin = new Dungeon(seed);
        ArrayList<Section> coord = calvin.mapSplit();
        calvin.createRooms(world);

        calvin.makeHallwaysVertical(calvin, world);
        calvin.makeHallwaysHorizontal(calvin, world);

        Avatar oscar = new Avatar(world, random);
        Flag hasbulla = new Flag(world, random);
        RunGame.extraStepsCounter = 0;
        Avatar.stepsLeft = 300;
        Encounter.encounterGeneration(world, random);
        char[] movementArray = movementString.toString().toCharArray();
        for (char c: movementArray) {
            moveQueue.enqueue(c);
        }
        while (!RunGame.gameover && Avatar.stepsLeft >= 0) {
            if (!moveQueue.isEmpty()) {
                moveQueue.dequeue();
            }
            if (!loaded) {
                while (!moveQueue.isEmpty()) {
                    oscar.movement(world, random, moveQueue.dequeue());
                }
            }
            loaded = true;
            makeHUD(world);
            ter.renderFrame(world);
            oscar.movement(world, random);
            if (RunGame.extraStepsCounter == 3) {
                Avatar.stepsLeft += 50;
                RunGame.extraStepsCounter = 0;
            }
        }
        if (Avatar.stepsLeft < 0) {
            RunGame.setFont(35);
            StdDraw.clear(Color.black);
            StdDraw.text(BOARDWIDTH / 2, BOARDHEIGHT / 2, "YOU LOST :( ");
            StdDraw.show();
        }
        if (RunGame.gameover) {
            RunGame.setFont(35);
            StdDraw.clear(Color.black);
            StdDraw.text(BOARDWIDTH / 2, BOARDHEIGHT / 2, "YOU JUST WON CONGRATULATIONS");
            StdDraw.show();
        }
    }

    public static int getBoardWidth() {
        return BOARDWIDTH;
    }

    public static int getBoardHeight() {
        return BOARDHEIGHT;
    }

    public int getRoomWidth() {
        return maxWidth;
    }

    public int getRoomHeight() {
        return maxHeight;
    }

    public ArrayList<Section> getSections() {
        return finalCoord;
    }
}
