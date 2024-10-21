package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class MemoryUI {
    /**
     * The width of the window of this game.
     */
    private int width;
    /**
     * The height of the window of this game.
     */
    private int height;
    private int round = 1;
    private String mode;

    /**
     * The current round the user is on.
     */
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

    public void drawWelcome() {
        drawText("WELCOME TO OUR MEMORY GAME!");
        StdDraw.pause(2000);
    }

    public void drawText(String text) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, text);
        StdDraw.show();
    }

    public String solicitNCharsInput(int n, String encouragement) {
        drawFrame("Your turn:", n, encouragement);
        StdDraw.pause(2000);
        String s = "";
        int count = 0;
        while (StdDraw.hasNextKeyTyped() && count < n) {
            char typedChar = StdDraw.nextKeyTyped();
            s += typedChar;
            count++;
            drawFrame(s, n, encouragement);
            StdDraw.pause(1000);
        }
        return s;
    }
    private void drawHUD(int round, String mode, String encouragement) {
        int paddingTop = 2;
        int paddingSide = 1;
        StdDraw.textLeft(paddingSide,height - paddingTop, "Round: " + round);
//        StdDraw.text((width / 2.0), height - paddingTop, mode);
        StdDraw.textRight(width - paddingSide, height - paddingTop, encouragement);
        int hudHeight = 3;
        StdDraw.line(0, height - hudHeight, width, height - hudHeight);
    }


    public void drawFrame(String text, int round, String encourage) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2, height / 2, text);
        drawHUD(round, mode, encourage);
        StdDraw.show();
    }

    public void drawRoundWelcome(int round, String encouragement) {
        String text = "Round: " + round;
        drawFrame(text, round, encouragement);
        StdDraw.pause(2000);
    }

    public void flashSequence(String letters, String encouragement) {
        String mode = "Watch";
        for (char c : letters.toCharArray()) {
            drawFrame(Character.toString(c), round, encouragement);
            StdDraw.pause(1000);
            drawFrame("", round, encouragement);
            StdDraw.pause(500);
        }
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
