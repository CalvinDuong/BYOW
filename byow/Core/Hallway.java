package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway {

    public Hallway() {

    }

    public void makeHallwaysHorizontal(Dungeon dungeon, TETile [][] board) {
        for (int index = 0; index < dungeon.getSections().size() - 1; index++) {
            if (index + 1 % numWidthRooms(dungeon) == 0) {
                continue;
            } else {
                createHallwayHorizontal(dungeon.getSections().get(index),
                        dungeon.getSections().get(index + 1), board);
            }
        }
    }

    public void createHallwayHorizontal(Section section1, Section section2, TETile [][] board) {
        int hallwaySTart = roomWidthMiddle(section1);
        if (section2.room.getY() <= hallwaySTart && hallwaySTart <= section2.room.getY2()) {
            drawHallway(board, section1.room.getX2(), hallwaySTart,
                    section2.room.getX(), hallwaySTart + 2);
        }
    }

    public void drawHallway(TETile [][] board, int startX, int startY, int endX, int endY) {
        for (int i = startX; i < endX; i++) {
            board[i][startY] = Tileset.WALL;
            board[i][endY] = Tileset.WALL;
        }
    }

    //gives us the middle of the room which is where the hallway stems from
    public static int roomWidthMiddle(Section section) {
        return section.room.getWidth() / 2 + section.room.getY();
    }

    //gives us the middle height of the room
    public static int roomHeightMiddle(Section section) {
        return section.room.getHeight() / 2 + section.room.getX();
    }

    public static int numWidthRooms(Dungeon dungeon) {
        if (Dungeon.getBoardHeight() % dungeon.getRoomHeight() != 0) {
            return Dungeon.getBoardHeight() / dungeon.getRoomHeight() + 1;
        } else {
            return Dungeon.getBoardHeight() / dungeon.getRoomHeight();
        }
    }
    public static int numHeightRooms(Dungeon dungeon) {
        if (Dungeon.getBoardWidth() % dungeon.getRoomHeight() != 0) {
            return Dungeon.getBoardHeight() / dungeon.getRoomHeight() + 1;
        } else {
            return Dungeon.getBoardHeight() / dungeon.getRoomHeight();
        }
    }

}
