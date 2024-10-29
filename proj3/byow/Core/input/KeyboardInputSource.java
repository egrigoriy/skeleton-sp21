package byow.Core.input;

/**
 * Created by hug.
 */
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    public KeyboardInputSource() {
//        StdDraw.text(0.3, 0.3, "press m to moo, q to quit");
    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
//            if (StdDraw.isMousePressed()) {
//                System.out.println("HAHA");
//                StdDraw.pause(1000);
//                System.out.println(StdDraw.mouseX());
//                System.out.println(StdDraw.mouseY());
//            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
