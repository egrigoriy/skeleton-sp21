package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;


/**
 * Represents the Memory UI
 */
public class MemoryUI {
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
    private int round = 1;
    /**
     * The current mode of this game.
     */
    private Mode mode = Mode.WELCOME;


    public MemoryUI(int width, int height) {
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
    }

    /**
     * Sets the current round of this game.
     * @param round
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Sets the current mode of this game.
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Draws the welcome screen
     */
    public void drawWelcome() {
        drawText("WELCOME TO OUR MEMORY GAME!");
        StdDraw.pause(2000);
    }

    /**
     * Draws just the given text in the middle of the screen in white color
     * @param text
     */

    private void drawText(String text) {
        drawText(text, Color.WHITE);
    }

    /**
     * Draws just the given text in the middle of the screen with given color
     * @param text
     * @param penColor
     */
    private void drawText(String text, Color penColor) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(penColor);
        StdDraw.text(width / 2, height / 2, text);
        StdDraw.show();
    }
    /**
     * Draws a round welcome and encouragement
     * @param encouragement
     */
    public void drawRoundWelcome(String encouragement) {
        String text = "Round: " + round;
        drawFullFrame(text, encouragement);
        StdDraw.pause(2000);
    }

    /**
     * Draws full frame with HUD and text in white
     * @param text
     * @param encourage
     */
    private void drawFullFrame(String text, String encourage) {
        drawFullFrame(text, Color.WHITE, encourage);
    }

    /**
     * Draws full frame with HUD and text in given color
     * @param text
     * @param encourage
     */
    private void drawFullFrame(String text, Color penColor, String encourage) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(penColor);
        StdDraw.text(width / 2, height / 2, text);
        drawHUD(encourage);
        StdDraw.show();
    }
    /**
     * Flash given letters in the middle of the screen and displays a given encouragement in HUD
     * @param letters
     * @param encouragement
     */
    public void flashSequence(String letters, String encouragement) {
        for (char c : letters.toCharArray()) {
            drawFullFrame(Character.toString(c), encouragement);
            StdDraw.pause(1000);
            drawFullFrame("", encouragement);
            StdDraw.pause(500);
        }
    }

    /**
     * Display an invitation message and solicit n input char, which are displayed one by one.
     * The resulted string is returned.
     * @param n
     * @param encouragement
     * @return string
     */
    public String solicitNCharsInput(int n, String encouragement) {
        Color penColor = Color.ORANGE;
        String invitationText = "You turn: ";
        drawFullFrame(invitationText, penColor, encouragement);
        StdDraw.pause(2000);
        String typedString = "";
        int count = 0;
        while (StdDraw.hasNextKeyTyped() && count < n) {
            char typedChar = StdDraw.nextKeyTyped();
            typedString += typedChar;
            count++;
            drawFullFrame(invitationText + typedString, penColor, encouragement);
            StdDraw.pause(1000);
        }
        return typedString;
    }

    /**
     * Draws HUD and given encouragement on it
     * @param encouragement
     */
    private void drawHUD(String encouragement) {
        int paddingTop = 2;
        int paddingSide = 1;
        StdDraw.textLeft(paddingSide,height - paddingTop, "Round: " + round);
        StdDraw.text(width / 2.0, height - paddingTop, String.valueOf(this.mode));
        StdDraw.textRight(width - paddingSide, height - paddingTop, encouragement);
        int hudHeight = 3;
        StdDraw.line(0, height - hudHeight, width, height - hudHeight);
    }


    /**
     * Draws the given final message in case of failure
     * @param message
     */
    public void drawFinalFailureMessage(String message) {
        drawText(message, Color.RED);
    }

    /**
     * Draws the given final message in case of success
     * @param message
     */
    public void drawFinalSuccessMessage(String message) {
        drawText(message, Color.GREEN);
    }
}
