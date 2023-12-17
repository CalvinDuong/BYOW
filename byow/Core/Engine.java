package byow.Core;

//import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.util.ArrayList;

public class Engine {
//    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        RunGame game = new RunGame(WIDTH, HEIGHT);
        game.startGame();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        File cwd = new File(System.getProperty("user.dir"));

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        char[] newSeed = input.toCharArray();
        char[] oldSeed = null;
        if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            File seedSave = Utils.join(cwd, "save1.txt");
            String seedString = Utils.readContentsAsString(seedSave);
            oldSeed = seedString.toCharArray();
        }
        ArrayList<Character> copy = new ArrayList<>();
        if (oldSeed != null) {
            for (char value : oldSeed) {
                copy.add(value);
            }
        }
        for (int i = 1; i < newSeed.length; i++) {
            copy.add(newSeed[i]);
        }
        String saveSeed = "";
        StringBuilder seed = new StringBuilder();
        StringBuilder movement = new StringBuilder();
        for (char c : copy) {
            saveSeed += c;
            if (c != 'Q' && c != 'q' && c != ':') {
                if (Character.isDigit(c)) {
                    seed.append(c);
                } else {
                    movement.append(c);
                }
            }
        }
        File save = Utils.join(cwd, "save1.txt");
        Utils.writeContents(save, saveSeed);
        long seedLong = Long.parseLong(seed.toString());
        Dungeon dungeon = new Dungeon(seedLong);
        ArrayList<Section> coord = dungeon.mapSplit();
        dungeon.createRooms(finalWorldFrame);

        dungeon.makeHallwaysHorizontal(dungeon, finalWorldFrame);
        dungeon.makeHallwaysVertical(dungeon, finalWorldFrame);

        Avatar oscar = new Avatar(finalWorldFrame, dungeon.random);
        Flag hasbulla = new Flag(finalWorldFrame, dungeon.random);
        RunGame.extraStepsCounter = 0;
        Avatar.stepsLeft = 300;
        Encounter.encounterGeneration(finalWorldFrame, dungeon.random);
        char[] movementArray = movement.toString().toCharArray();
        for (char c: movementArray) {
            Dungeon.moveQueue.enqueue(c);
        }
        if (!Dungeon.moveQueue.isEmpty()) {
            Dungeon.moveQueue.dequeue();
        }
        if (!Dungeon.loaded) {
            while (!Dungeon.moveQueue.isEmpty()) {
                oscar.movement(finalWorldFrame, Dungeon.random, Dungeon.moveQueue.dequeue());
            }
        }
        Dungeon.loaded = true;
        if (RunGame.extraStepsCounter == 3) {
            Avatar.stepsLeft += 50;
            RunGame.extraStepsCounter = 0;
        }
        return finalWorldFrame;
    }
}

