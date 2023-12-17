package byow.Core;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Random;

public class RunGame {
    /**
     * The width of the window of this game.
     */
    private int width;
    /**
     * The height of the window of this game.
     */
    private int height;
    /**
     * The current round the user is on.
     */
    private int round;
    /**
     * The Random object used to randomly generate Strings.
     */
    private Random rand;
    /**
     * Whether or not the game is over.
     */

    private boolean seedFound;

    protected static boolean gameover;
    private boolean menu;

    protected static int fontSize;

    protected static int extraStepsCounter;

    protected static String avatarName;

    protected static int ncounter = 0;
    /**
     * Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'.
     */

    public static final File CWD = new File(System.getProperty("user.dir"));

    public RunGame(int widthInput, int heightInput) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = widthInput;
        this.height = heightInput;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public static void setFont(int size) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        fontSize = size;
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, fontSize);
        StdDraw.setFont(fontBig);
    }

    private void newGame() {
        String seed = "N";
        setFont(20);
        StdDraw.text(width / 2, height / 2,
                "Please enter random seed number and then press the 'S' key.");
        StdDraw.show();
        while (!seedFound) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(Color.black);
                StdDraw.text(width / 2, height / 2,
                        "Please enter random seed number and then press the 'S' key.");
                StdDraw.show();
                char character = StdDraw.nextKeyTyped();

                if (character == 'n' || character == 'N') {
                    continue;
                }
                if (character == 's' || character == 'S') {
                    seedFound = true;
                    break;
                }
                seed += character;
                StdDraw.text(width / 2, height / 2 - 5, seed);
                StdDraw.show();
            }
        }
        File save = Utils.join(CWD, "save1.txt");
        Utils.writeContents(save, seed);
        String[] seedArray = new String[] {seed};
        Dungeon.main(seedArray);
    }

    public void loadGame() {
        File nameSave = Utils.join(CWD, "name.txt");
        avatarName = Utils.readContentsAsString(nameSave);
        File save = Utils.join(CWD, "save1.txt");
        String seedString = Utils.readContentsAsString(save);
        String[] seedArray = new String[] {seedString};
        Dungeon.main(seedArray);
    }

    public void startGame() {
        setFont(40);
        menu = true;
        seedFound = false;
        gameover = false;
        StdDraw.text(this.width / 2, this.height - 12, "CS61B: The Game");
        StdDraw.text(this.width / 2, this.height / 2, "New Game (N)");
        StdDraw.text(this.width / 2, this.height / 2.45, "Load Game (L)");
        StdDraw.text(this.width / 2, this.height / 3, "Read Lore (R)");
        StdDraw.text(this.width / 2, this.height / 4, "Quit (Q)");
        StdDraw.show();

        while (menu) {
            if (StdDraw.isMousePressed()) {
                if (StdDraw.mouseX() >= 20 && StdDraw.mouseX() <= 40
                        && StdDraw.mouseY() >= 28 && StdDraw.mouseY() <= 32) {
                    ncounter++;
                    menu = false;
                    avatarName();
                }
                if (StdDraw.mouseX() >= 20 && StdDraw.mouseX() <= 40
                        && StdDraw.mouseY() >= 23 && StdDraw.mouseY() <= 26) {
                    menu = false;
                    loadGame();
                }
                if (StdDraw.mouseX() >= 20 && StdDraw.mouseX() <= 40
                        && StdDraw.mouseY() >= 19 && StdDraw.mouseY() <= 21) {
                    menu = false;
                    readLore();
                }
                if (StdDraw.mouseX() >= 24 && StdDraw.mouseX() <= 35
                        && StdDraw.mouseY() >= 13 && StdDraw.mouseY() <= 16) {
                    menu = false;
                    System.exit(0);
                }
            }
            if (StdDraw.isKeyPressed(78)) {
                menu = false;
                avatarName();
            } else if (StdDraw.isKeyPressed(76)) {
                menu = false;
                loadGame();
            } else if (StdDraw.isKeyPressed(82)) {
                menu = false;
                readLore();
            } else if (StdDraw.isKeyPressed(81)) {
                menu = false;
                System.exit(0);
            }
        }
    }

    public void avatarName() {
        boolean nameInputted = false;
        String name = "";
        setFont(20);
        StdDraw.text(width / 2, height / 2,
                "Please enter your avatar name and press the 'Enter' key.");
        StdDraw.show();
        while (!nameInputted) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(Color.black);
                StdDraw.text(width / 2, height / 2,
                        "Please enter your avatar name and press the 'Enter' key.");
                StdDraw.show();
                char character = StdDraw.nextKeyTyped();
                if (character == 'n' || character == 'N') {
                    ncounter++;
                    if (ncounter == 1) {
                        continue;
                    }
                }
                if (StdDraw.isKeyPressed(10)) {
                    nameInputted = true;
                    break;
                }
                name += character;
                StdDraw.text(width / 2, height / 2 - 5, name);
                StdDraw.show();
            }
        }
        avatarName = name;
        File nameFile = Utils.join(CWD, "name.txt");
        Utils.writeContents(nameFile, name);
        newGame();
    }

    public static void readLore() {
        StdDraw.clear(Color.black);
        setFont(22);
        StdDraw.text(Dungeon.getBoardWidth() / 2 + 3, Dungeon.getBoardHeight() - 1,
                "In a village far away, located in the "
                + "darkest depths of Berkeley");
        StdDraw.text(Dungeon.getBoardWidth() / 2 + 3, Dungeon.getBoardHeight()  - 8,
                "lived a young warrior. He was hired by the "
                + "royal guards to hunt down the evil spirit.");
        StdDraw.text(Dungeon.getBoardWidth() / 2 + 3, Dungeon.getBoardHeight()  - 15, "However,"
                + "our young warrior is unaware of the curse the spirit placed upon him.");
        StdDraw.text(Dungeon.getBoardWidth() / 2 + 3, Dungeon.getBoardHeight() - 22,
                "His legs are cursed and after " + Avatar.stepsLeft + " steps "
                + "his legs will collapse on him. ");
        StdDraw.text(Dungeon.getBoardWidth() / 2 + 4, Dungeon.getBoardHeight() - 29,
                "Will he be able to catch the spirit in time and save his "
                + "little legs, or will he fail and fall ?");
        StdDraw.show();
    }

    public static void main(String[]args) {
        RunGame game = new RunGame(60, 60);
        game.startGame();
    }
}

