package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Calibri", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        //TOD: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TOD: Generate random string of letters of length n
        String s = "";
        for (int i = 0; i < n; i++) {
            s += CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return s;
    }

    public void drawFrame(String s) {
//        System.out.println("String to draw: " + s);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        //TOD: Take the string and display it in the center of the screen
        StdDraw.text(width/2, height/2, s);
        //TODO: If game is not over, display relevant game information at the top of the screen
        if (!gameOver) {
            int paddingTop = 2;
            int paddingSide = 1;
            int hudHeight = 3;
            StdDraw.textLeft(paddingSide,height - paddingTop, "Round: " + 1);
            StdDraw.textRight(width - paddingSide, height - paddingTop, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.line(0, height - hudHeight, width, height - hudHeight);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TOD: Display each character in letters, making sure to blank the screen between letters
        for (char c: letters.toCharArray()) {
            drawFrame(Character.toString(c));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
            }
            drawFrame("");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (Exception e) {
            }
        }
        gameOver = true;
    }

    public String solicitNCharsInput(int n) {
        drawFrame("Your turn:");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
        }
        drawFrame("");
        //TODO: Read n letters of player input
        String s = "";
        int count = 0;
        while (StdDraw.hasNextKeyTyped() && count <= n) {
            char typedChar = StdDraw.nextKeyTyped();
            s += typedChar;
            count++;
            drawFrame(s);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
            }
        }
        return s;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        int n = 3;
        String s = generateRandomString(n);
//        drawFrame(s);
        //TODO: Establish Engine loop
        gameOver = false;
        round = 1;
        while (!gameOver) {
            flashSequence(s);
            String typedString = solicitNCharsInput(n);
            if (typedString != s) {
                gameOver = true;
            }
        }
        drawFrame("GAME OVER! You made it to round: " + round);
    }

}
