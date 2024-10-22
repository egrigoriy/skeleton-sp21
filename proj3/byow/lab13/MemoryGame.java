package byow.lab13;

import java.util.Random;

enum Mode {
    WELCOME("Welcome"),
    WATCH("Watch"),
    TYPE("Type");

    private final String mode;
    Mode(String mode) {
        this.mode = mode;
    }
}

public class MemoryGame {
    private static final int MAX_ROUNDS = 5;
    private MemoryUI ui;
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

        this.ui = new MemoryUI(width, height);
        rand = new Random(seed);
    }


    /**
     * Returns a string with given length n, where each letter is random.
     * @param n
     * @return a string
     */
    public String generateRandomString(int n) {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += CHARACTERS[rand.nextInt(CHARACTERS.length)];
        }
        return s;
    }

    /**
     * Returns a randomly chosen encouragement
     * @return String
     */
    public String getRandomEncouragement() {
        return ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
    }

    /**
     * Runs main game loop
     */
    public void startGame() {
        gameOver = false;
        round = 0;
        ui.drawWelcome();
        while (!gameOver && round < MAX_ROUNDS) {
            round++;
            ui.setRound(round);
            ui.setMode(Mode.WELCOME);
            ui.drawRoundWelcome(getRandomEncouragement());
            String stringToGuess = generateRandomString(round);
            ui.setMode(Mode.WATCH);
            ui.flashSequence(stringToGuess, getRandomEncouragement());
            ui.setMode(Mode.TYPE);
            String playerString = ui.solicitNCharsInput(round, getRandomEncouragement());
            if (!playerString.equals(stringToGuess)) {
                gameOver = true;
            }
        }
        if (gameOver) {
            ui.drawFinalFailureMessage("GAME OVER! You made it to round: " + round);
        } else {
            ui.drawFinalSuccessMessage("YOU WON!!!");
        }
    }

}
